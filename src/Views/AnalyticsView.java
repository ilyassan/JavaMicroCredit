package Views;

import Models.Person;
import Models.Employee;
import Models.Professional;
import Services.AnalyticsService;
import java.util.List;
import java.util.Map;

public class AnalyticsView extends View {

    public static void showMenu() {
        while (true) {
            String[] analyticsMenuOptions = {
                    "Sort Clients",
                    "Search Mortgage Eligible Clients",
                    "At-Risk Clients Report (Top 10)",
                    "Employment Type Distribution",
                    "Back to Main Menu"
            };

            int choice = showMenuAndGetChoice("Analytics & Reports", analyticsMenuOptions);

            switch (choice) {
                case 1:
                    sortClients();
                    break;
                case 2:
                    searchMortgageEligibleClients();
                    break;
                case 3:
                    showAtRiskClients();
                    break;
                case 4:
                    showEmploymentTypeDistribution();
                    break;
                case 5:
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

    private static void searchMortgageEligibleClients() {
        showHeader("Mortgage Eligible Clients");
        println("Criteria: Age 25-50, Income >4000 DH, CDI Contract, Score >70, Married\n");

        try {
            List<Employee> eligibleClients = AnalyticsService.findMortgageEligibleClients();

            if (eligibleClients.isEmpty()) {
                showInfo("No clients meet the mortgage eligibility criteria.");
                pauseBeforeMenu();
                return;
            }

            println("Found " + eligibleClients.size() + " eligible client(s):\n");

            for (int i = 0; i < eligibleClients.size(); i++) {
                Employee emp = eligibleClients.get(i);
                println((i + 1) + ". " + emp.getFirstName() + " " + emp.getLastName());
                println("   ID: " + emp.getId());
                println("   Age: " + java.time.temporal.ChronoUnit.YEARS.between(emp.getDateOfBirth(), java.time.LocalDate.now()) + " years");
                println("   Salary: " + emp.getSalary() + " DH");
                println("   Contract: " + emp.getContractType() + " - " + emp.getEmploymentSector());
                println("   Score: " + emp.getScore());
                println("   Family Status: " + emp.getFamilyStatus());
                println("");
            }
        } catch (Exception e) {
            showError("Error searching for eligible clients: " + e.getMessage());
        }

        pauseBeforeMenu();
    }

    private static void showAtRiskClients() {
        showHeader("At-Risk Clients Report (Top 10)");
        println("Criteria: Score <60, Recent incidents (<6 months)\n");

        try {
            List<Person> atRiskClients = AnalyticsService.findAtRiskClients();

            if (atRiskClients.isEmpty()) {
                showInfo("No at-risk clients found.");
                pauseBeforeMenu();
                return;
            }

            println("Found " + atRiskClients.size() + " at-risk client(s):\n");

            for (int i = 0; i < atRiskClients.size(); i++) {
                Person person = atRiskClients.get(i);
                String clientType = person instanceof Employee ? "Employee" : "Professional";
                double income = 0;

                if (person instanceof Employee) {
                    income = ((Employee) person).getSalary();
                } else if (person instanceof Professional) {
                    income = ((Professional) person).getIncome();
                }

                println((i + 1) + ". " + person.getFirstName() + " " + person.getLastName());
                println("   ID: " + person.getId() + " | Type: " + clientType);
                println("   Score: " + person.getScore() + " (HIGH RISK)");
                println("   Income: " + income + " DH");
                println("   Member Since: " + (person.getCreatedAt() != null ? person.getCreatedAt().toLocalDate() : "N/A"));
                println("");
            }

            showWarning("These clients require immediate follow-up and support.");
        } catch (Exception e) {
            showError("Error generating at-risk report: " + e.getMessage());
        }

        pauseBeforeMenu();
    }

    private static void showEmploymentTypeDistribution() {
        showHeader("Employment Type Distribution");

        try {
            Map<String, AnalyticsService.EmploymentTypeStats> distribution =
                AnalyticsService.getEmploymentTypeDistribution();

            if (distribution.isEmpty()) {
                showInfo("No employment data available.");
                pauseBeforeMenu();
                return;
            }

            println("Employment Type Statistics:\n");

            distribution.forEach((type, stats) -> {
                println("═══ " + type + " ═══");
                println("  Total Clients: " + stats.getCount());
                println("  Average Score: " + String.format("%.2f", stats.getAvgScore()));
                println("  Average Income: " + String.format("%.2f", stats.getAvgIncome()) + " DH");
                println("  Approval Rate: " + String.format("%.2f", stats.getApprovalRate()) + "%");
                println("");
            });
        } catch (Exception e) {
            showError("Error generating distribution report: " + e.getMessage());
        }

        pauseBeforeMenu();
    }

    private static void showInfo(String message) {
        println("INFO: " + message);
    }
}