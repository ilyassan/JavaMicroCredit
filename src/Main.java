import Views.View;

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
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
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
}