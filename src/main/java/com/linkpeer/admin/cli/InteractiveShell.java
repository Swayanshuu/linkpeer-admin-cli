package com.linkpeer.admin.cli;

import com.linkpeer.admin.cli.commands.TopLevelCommand;
import com.linkpeer.admin.service.AuthService;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.reader.EndOfFileException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

@Component
public class InteractiveShell implements CommandLineRunner {

    private final TopLevelCommand topLevelCommand;
    private final AuthService authService;
    private final CommandLine.IFactory factory;

    public InteractiveShell(TopLevelCommand topLevelCommand, AuthService authService, CommandLine.IFactory factory) {
        this.topLevelCommand = topLevelCommand;
        this.authService = authService;
        this.factory = factory;
    }

    public static LineReader reader;

    @Override
    public void run(String... args) throws Exception {
        // Register shutdown hook to clean up session when process exits or terminal closes
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                authService.logout();
            } catch (Exception ignored) {
            }
        }));

        // Always clear any session on startup so authentication is required every run
        authService.logout();

        CommandLine cmd = new CommandLine(topLevelCommand, factory);
        
        System.out.println("\n\n" +
                "  _      _       _    _____               \n" +
                " | |    (_)     | |  |  __ \\              \n" +
                " | |     _ _ __ | | _| |__) |__  ___ _ __ \n" +
                " | |    | | '_ \\| |/ /  ___/ _ \\/ _ \\ '__|\n" +
                " | |____| | | | |   <| |  |  __/  __/ |   \n" +
                " |______|_|_| |_|_|\\_\\|_|   \\___|\\___|_|   \n" +
                "                                          \n" +
                "          ADMINISTRATION CLI              \n\n");

        try (Terminal terminal = TerminalBuilder.builder().system(true).build()) {
            reader = LineReaderBuilder.builder()
                    .terminal(terminal)
                    .build();

            // Force login credentials prompt immediately on CLI startup
            while (!authService.isAuthenticated()) {
                System.out.println("\u001B[33m🔐 Authentication Required. Please log in.\u001B[0m");
                try {
                    String email = reader.readLine("Email: ");
                    if (email == null || email.trim().isEmpty()) {
                        continue;
                    }
                    String password = reader.readLine("Password: ", '*');
                    if (password == null || password.trim().isEmpty()) {
                        continue;
                    }
                    if (authService.login(email.trim(), password)) {
                        System.out.println("\u001B[32m✓ Login successful!\u001B[0m\n");
                        break;
                    } else {
                        System.out.println("\u001B[31m✗ Invalid email or password. Please try again.\u001B[0m\n");
                    }
                } catch (UserInterruptException | EndOfFileException e) {
                    System.out.println("Exiting...");
                    authService.logout();
                    System.exit(0);
                }
            }

            // Execute root command once to display welcome message
            cmd.execute();

            String prompt = "linkpeer> ";
            while (true) {
                try {
                    String line = reader.readLine(prompt);
                    if (line == null || line.trim().isEmpty()) {
                        continue;
                    }
                    if ("exit".equalsIgnoreCase(line.trim()) || "quit".equalsIgnoreCase(line.trim())) {
                        break;
                    }
                    
                    String[] words = line.trim().split("\\s+");
                    cmd.execute(words);

                } catch (UserInterruptException | EndOfFileException e) {
                    break; // Ctrl+C or Ctrl+D
                }
            }
        } finally {
            // Automatically clear session when exiting interactive shell loop
            authService.logout();
        }
        
        System.out.println("Goodbye!");
        System.exit(0);
    }
}
