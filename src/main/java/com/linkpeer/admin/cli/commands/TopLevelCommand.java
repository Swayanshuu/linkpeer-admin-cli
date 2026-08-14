package com.linkpeer.admin.cli.commands;

import com.linkpeer.admin.service.AuthService;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;

@Component
@Command(name = "linkpeer-admin", 
         mixinStandardHelpOptions = true, 
         version = "1.0.0",
         description = "@|bold,blue LinkPeer Admin CLI - Administration Tool|@",
         headerHeading = "@|bold,green Usage:|@%n",
         synopsisHeading = "%n@|bold,green Synopsis:|@%n",
         descriptionHeading = "%n@|bold,green Description:|@%n",
         parameterListHeading = "%n@|bold,green Parameters:|@%n",
         optionListHeading = "%n@|bold,green Options:|@%n",
         commandListHeading = "%n@|bold,green Commands:|@%n",
         subcommands = {
             AuthCommand.class,
             UsersCommand.class,
             FacultyCommand.class,
             PostsCommand.class,
             CommentsCommand.class,
             SubsCommand.class,
             PaymentsCommand.class,
             AnalyticsCommand.class,
             ExportCommand.class,
             NoticesCommand.class,
             BroadcastsCommand.class,
             NotificationsCommand.class
         })
public class TopLevelCommand implements Runnable {

    private final AuthService authService;
    
    public TopLevelCommand(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void run() {
        System.out.println("Welcome to LinkPeer Admin CLI.");
        if (authService.isAuthenticated()) {
            System.out.println("Currently logged in as: " + authService.whoami().getAdminEmail());
        } else {
            System.out.println("Please login to continue using: login or auth login");
        }
        System.out.println("Type 'help' for a list of commands.");
    }

    @Command(name = "login", description = "Login to the admin CLI")
    public void login() {
        if (com.linkpeer.admin.cli.InteractiveShell.reader == null) {
            System.out.println("\u001B[31m✗ Interactive shell not initialized\u001B[0m");
            return;
        }
        
        try {
            String email = com.linkpeer.admin.cli.InteractiveShell.reader.readLine("Email: ");
            if (email == null || email.trim().isEmpty()) {
                System.out.println("\u001B[31m✗ Email is required\u001B[0m");
                return;
            }
            
            // Mask password input with '*'
            String password = com.linkpeer.admin.cli.InteractiveShell.reader.readLine("Password: ", '*');
            if (password == null || password.trim().isEmpty()) {
                System.out.println("\u001B[31m✗ Password is required\u001B[0m");
                return;
            }

            if (authService.login(email.trim(), password)) {
                System.out.println("\u001B[32m✓ Login successful\u001B[0m");
            } else {
                System.out.println("\u001B[31m✗ Invalid email or password\u001B[0m");
            }
        } catch (org.jline.reader.UserInterruptException e) {
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
