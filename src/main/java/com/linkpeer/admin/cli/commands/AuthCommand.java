package com.linkpeer.admin.cli.commands;

import com.linkpeer.admin.cli.InteractiveShell;
import com.linkpeer.admin.service.AuthService;
import org.jline.reader.UserInterruptException;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Component
@Command(name = "auth", description = "Authentication commands")
public class AuthCommand {

    private final AuthService authService;

    public AuthCommand(AuthService authService) {
        this.authService = authService;
    }

    @Command(name = "login", description = "Login to the admin CLI")
    public void login(@Parameters(index = "0", arity = "0..1", description = "Admin email") String email,
                      @Parameters(index = "1", arity = "0..1", description = "Admin password") String password) {
        try {
            if (email == null || email.trim().isEmpty()) {
                if (InteractiveShell.reader != null) {
                    email = InteractiveShell.reader.readLine("Email: ");
                }
            }
            if (email == null || email.trim().isEmpty()) {
                System.out.println("\u001B[31m✗ Email is required\u001B[0m");
                return;
            }

            if (password == null || password.trim().isEmpty()) {
                if (InteractiveShell.reader != null) {
                    password = InteractiveShell.reader.readLine("Password: ", '*');
                } else if (System.console() != null) {
                    char[] passChars = System.console().readPassword("Password: ");
                    if (passChars != null) {
                        password = new String(passChars);
                    }
                }
            }
            if (password == null || password.trim().isEmpty()) {
                System.out.println("\u001B[31m✗ Password is required\u001B[0m");
                return;
            }

            if (authService.login(email.trim(), password)) {
                System.out.println("\u001B[32m✓ Login successful\u001B[0m");
            } else {
                System.out.println("\u001B[31m✗ Invalid email or password\u001B[0m");
            }
        } catch (UserInterruptException e) {
            System.out.println("\n\u001B[33m- Login cancelled\u001B[0m");
        }
    }

    @Command(name = "logout", description = "Logout from the admin CLI")
    public void logout() {
        authService.logout();
        System.out.println("\u001B[32m✓ Logged out successfully\u001B[0m");
    }

    @Command(name = "whoami", description = "Show current logged in admin user")
    public void whoami() {
        AuthService.SessionInfo session = authService.whoami();
        if (session != null) {
            System.out.println("Logged in as: " + session.getAdminEmail());
            System.out.println("Login time: " + session.getLoginTimestamp());
        } else {
            System.out.println("Not logged in.");
        }
    }
}
