package com.linkpeer.admin.cli.commands;

import com.linkpeer.admin.service.AuthService;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

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
    private final AuthCommand authCommand;
    
    public TopLevelCommand(AuthService authService, AuthCommand authCommand) {
        this.authService = authService;
        this.authCommand = authCommand;
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
    public void login(@Parameters(index = "0", arity = "0..1", description = "Admin email") String email,
                      @Parameters(index = "1", arity = "0..1", description = "Admin password") String password) {
        authCommand.login(email, password);
    }

    @Command(name = "logout", description = "Logout from the admin CLI")
    public void logout() {
        authCommand.logout();
    }

    @Command(name = "whoami", description = "Show current logged in admin user")
    public void whoami() {
        authCommand.whoami();
    }
}
