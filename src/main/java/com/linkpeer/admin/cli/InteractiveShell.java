package com.linkpeer.admin.cli;

import com.linkpeer.admin.cli.commands.TopLevelCommand;
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
    private final CommandLine.IFactory factory;

    public InteractiveShell(TopLevelCommand topLevelCommand, CommandLine.IFactory factory) {
        this.topLevelCommand = topLevelCommand;
        this.factory = factory;
    }

    public static LineReader reader;

    @Override
    public void run(String... args) throws Exception {
        CommandLine cmd = new CommandLine(topLevelCommand, factory);
        
        System.out.println("\n\n" +
                "  _      _       _    _____               \n" +
                " | |    (_)     | |  |  __ \\              \n" +
                " | |     _ _ __ | | _| |__) |__  ___ _ __ \n" +
                " | |    | | '_ \\| |/ /  ___/ _ \\/ _ \\ '__|\n" +
                " | |____| | | | |   <| |  |  __/  __/ |   \n" +
                " |______|_|_| |_|_|\\_\\_|   \\___|\\___|_|   \n" +
                "                                          \n" +
                "          ADMINISTRATION CLI              \n\n");
        
        // Execute the root command once to display welcome message
        cmd.execute();

        try (Terminal terminal = TerminalBuilder.builder().system(true).build()) {
            reader = LineReaderBuilder.builder()
                    .terminal(terminal)
                    .build();

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
        }
        System.out.println("Goodbye!");
        System.exit(0);
    }
}
