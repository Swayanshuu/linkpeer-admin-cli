package com.linkpeer.admin.cli.commands;

import com.linkpeer.admin.service.AuthService;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Component
@Command(name = "auth", aliases = {"login", "logout", "whoami"}, description = "Authentication commands")
public class AuthCommand {

    private final AuthService authService;

    public AuthCommand(AuthService authService) {
        this.authService = authService;
    }

    @Command(name = "login", description = "Login to the admin CLI")
    public void login(@Parameters(index = "0", description = "Admin email", interactive = true, arity = "0..1", prompt = "Email: ") String email,
                      @Parameters(index = "1", description = "Admin password", interactive = true, arity = "0..1", prompt = "Password: ") String password) {
        if (authService.login(email, password)) {
            System.out.println("\u001B[32m✓ Login successful\u001B[0m");
        } else {
            System.out.println("\u001B[31m✗ Invalid email or password\u001B[0m");
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
