package Views;

import Models.Person;
import Models.Employee;
import Models.Professional;
import Services.AnalyticsService;
import Services.ScoreRecalculationService;
import java.util.List;

public class AnalyticsView extends View {

    public static void showMenu() {
        while (true) {
            String[] analyticsMenuOptions = {
                    "Sort Clients",
                    "Search Mortgage Eligible Clients",
                    "At-Risk Clients Report",
                    "Employment Type Distribution",
                    "Marketing Campaign Targeting",
                    "Recalculate All Scores (Utility)",
                    "Back to Main Menu"
            };

            int choice = showMenuAndGetChoice("Analytics & Reports", analyticsMenuOptions);

            switch (choice) {
                case 1:
                    sortClients();
                    break;
                case 2:
                    showWarning("Coming soon...");
                    pauseBeforeMenu();
                    break;
                case 3:
                    showWarning("Coming soon...");
                    pauseBeforeMenu();
                    break;
                case 4:
                    showWarning("Coming soon...");
                    pauseBeforeMenu();
                    break;
                case 5:
                    showWarning("Coming soon...");
                    pauseBeforeMenu();
                    break;
                case 6:
                    recalculateAllScores();
                    break;
                case 7:
                    return;
                default:
                    showError("Invalid choice. Please try again.");
            }
        }
    }

    private static void sortClients() {
        while (true) {
            String[] sortOptions = {
                    "Sort by Score (Descending)",
                    "Sort by Score (Ascending)",
                    "Sort by Income (Descending)",
                    "Sort by Income (Ascending)",
                    "Sort by Relationship Seniority (Oldest First)",
                    "Sort by Relationship Seniority (Newest First)",
                    "Back to Analytics Menu"
            };

            int choice = showMenuAndGetChoice("Sort Clients", sortOptions);

            List<Person> sortedClients = null;

            switch (choice) {
                case 1:
                    sortedClients = AnalyticsService.sortByScore(true);
                    break;
                case 2:
                    sortedClients = AnalyticsService.sortByScore(false);
                    break;
                case 3:
                    sortedClients = AnalyticsService.sortByIncome(true);
                    break;
                case 4:
                    sortedClients = AnalyticsService.sortByIncome(false);
                    break;
                case 5:
                    sortedClients = AnalyticsService.sortByRelationshipSeniority(false);
                    break;
                case 6:
                    sortedClients = AnalyticsService.sortByRelationshipSeniority(true);
                    break;
                case 7:
                    return;
                default:
                    showError("Invalid choice. Please try again.");
                    continue;
            }

            if (sortedClients != null) {
                displaySortedClients(sortedClients);
            }
        }
    }

    private static void displaySortedClients(List<Person> clients) {
        showHeader("Sorted Clients (" + clients.size() + " total)");

        if (clients.isEmpty()) {
            showInfo("No clients found.");
            pauseBeforeMenu();
            return;
        }

        for (int i = 0; i < clients.size(); i++) {
            Person person = clients.get(i);
            String clientType = person instanceof Employee ? "Employee" : "Professional";
            double income = 0;

            if (person instanceof Employee) {
                income = ((Employee) person).getSalary();
            } else if (person instanceof Professional) {
                income = ((Professional) person).getIncome();
            }

            println((i + 1) + ". " + person.getFirstName() + " " + person.getLastName() +
                   " | Type: " + clientType +
                   " | Score: " + (person.getScore() != null ? person.getScore() : "N/A") +
                   " | Income: " + income + " DH" +
                   " | Member Since: " + (person.getCreatedAt() != null ? person.getCreatedAt().toLocalDate() : "N/A"));
        }

        pauseBeforeMenu();
    }

    private static void recalculateAllScores() {
        showHeader("Recalculate All Client Scores");

        println("This will recalculate scores for all existing clients based on:");
        println("- Professional Stability");
        println("- Financial Capacity");
        println("- Payment History");
        println("- Client Relationship");
        println("- Complementary Criteria");
        println("");

        if (getYesNo("Are you sure you want to recalculate all scores?")) {
            try {
                ScoreRecalculationService.recalculateAllScores();
                showSuccess("All client scores have been recalculated successfully!");
            } catch (Exception e) {
                showError("Error recalculating scores: " + e.getMessage());
            }
        } else {
            println("Operation cancelled.");
        }

        pauseBeforeMenu();
    }

    private static void showInfo(String message) {
        println("INFO: " + message);
    }
}