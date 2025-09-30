import Models.Employee;
import Models.Professional;
import Services.ScoringService;
import Views.View;
import Views.EmployeeView;
import Views.ProfessionalView;
import Views.CreditView;
import Views.AnalyticsView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main extends View {

    public static void main(String[] args) {
        Employee.getAll().forEach(ScoringService::updateClientScore);
        Professional.getAll().forEach(ScoringService::updateClientScore);

        showHeader("JavaMicroCredit - Credit Scoring System");
        println("Welcome to the Automated Credit Scoring System");

        while (true) {
            String[] mainMenuOptions = {
                    "Client Management",
                    "Credit Scoring & Decision",
                    "Payment History Management",
                    "Analytics & Reports",
                    "Exit System"
            };

            int choice = showMenuAndGetChoice("Main Menu", mainMenuOptions);

            switch (choice) {
                case 1:
                    handleClientManagement();
                    break;
                case 2:
                    CreditView.showMenu();
                    break;
                case 3:
                    showWarning("ba9i");
                    pauseBeforeMenu();
                    break;
                case 4:
                    AnalyticsView.showMenu();
                    break;
                case 5:
                    showSuccess("Thank you for using JavaMicroCredit System!");
                    System.exit(0);
                    break;
                default:
                    showError("Invalid choice. Please try again.");
            }
        }
    }

    private static void handleClientManagement() {
        while (true) {
            String[] clientMenuOptions = {
                    "Manage Employees",
                    "Manage Professionals",
                    "Back to Main Menu"
            };

            int choice = showMenuAndGetChoice("Client Management", clientMenuOptions);

            switch (choice) {
                case 1:
                    EmployeeView.showMenu();
                    break;
                case 2:
                    ProfessionalView.showMenu();
                    break;
                case 3:
                    return;
                default:
                    showError("Invalid choice. Please try again.");
            }
        }
    }

    private static void executeScheduledInstallementsUpdater() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);



        // Schedule the task to run every 1 hour
        scheduler.scheduleAtFixedRate(() -> {

            //

        }, 0, 1, TimeUnit.HOURS);
    }

}