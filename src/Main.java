import Views.View;
import Views.EmployeeView;
import Views.ProfessionalView;
import Views.CreditView;

public class Main extends View {

    public static void main(String[] args) {
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
                    showWarning("ba9i");
                    pauseBeforeMenu();
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
}