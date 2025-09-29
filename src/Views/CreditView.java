package Views;

import Enums.CreditType;
import Models.Credit;
import Models.Employee;
import Models.Professional;
import Services.CreditService;

public class CreditView extends View {

    public static void showMenu() {
        while (true) {
            String[] creditMenuOptions = {
                    "Create Credit Request",
                    "View Credit Details",
                    "List All Credits",
                    "Back to Main Menu"
            };

            int choice = showMenuAndGetChoice("Credit Scoring & Decision", creditMenuOptions);

            switch (choice) {
                case 1:
                    createCreditRequest();
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
                    return;
                default:
                    showError("Invalid choice. Please try again.");
            }
        }
    }

    private static void createCreditRequest() {
        showHeader("Create Credit Request");

        try {
            // Choose client type
            String[] clientTypeOptions = {
                    "Employee",
                    "Professional",
                    "Cancel"
            };

            int clientTypeChoice = showMenuAndGetChoice("Select Client Type", clientTypeOptions);

            if (clientTypeChoice == 3) {
                return;
            }

            Integer employeeId = null;
            Integer professionalId = null;

            // Get client ID and verify existence
            if (clientTypeChoice == 1) {
                employeeId = getInt("Enter Employee ID: ", 1, Integer.MAX_VALUE);
                Employee employee = Employee.findById(employeeId);

                if (employee == null) {
                    showError("Employee not found with ID: " + employeeId);
                    pauseBeforeMenu();
                    return;
                }

                println("Client: " + employee.getFirstName() + " " + employee.getLastName());
                println("Salary: " + employee.getSalary() + " DH");
            } else {
                professionalId = getInt("Enter Professional ID: ", 1, Integer.MAX_VALUE);
                Professional professional = Professional.findById(professionalId);

                if (professional == null) {
                    showError("Professional not found with ID: " + professionalId);
                    pauseBeforeMenu();
                    return;
                }

                println("Client: " + professional.getFirstName() + " " + professional.getLastName());
                println("Income: " + professional.getIncome() + " DH");
            }

            // Credit Type
            CreditType creditType = getCreditType("Select credit type:");

            // Credit Details
            double requestedAmount = getPositiveDouble("Enter requested amount (DH): ");
            double interestRate = getDouble("Enter annual interest rate (%): ", 0, 20);
            int durationInMonths = getInt("Enter duration in months: ", 1, 360);

            // Confirmation
            showHeader("Credit Request Summary");
            println("Client Type: " + (employeeId != null ? "Employee" : "Professional"));
            println("Client ID: " + (employeeId != null ? employeeId : professionalId));
            println("Credit Type: " + creditType);
            println("Requested Amount: " + requestedAmount + " DH");
            println("Interest Rate: " + interestRate + "%");
            println("Duration: " + durationInMonths + " months");
            println("");

            if (!getYesNo("Confirm credit request creation?")) {
                println("Credit request cancelled.");
                pauseBeforeMenu();
                return;
            }

            // Create credit with installments using service
            Credit credit = CreditService.createCreditWithInstallments(
                employeeId,
                professionalId,
                requestedAmount,
                interestRate,
                durationInMonths,
                creditType
            );

            showSuccess("Credit request created successfully!");
            println("Credit ID: " + credit.getId());
            println("Decision: " + credit.getDecision());
            println("Approved Amount: " + credit.getApprovedAmount() + " DH");
            pauseBeforeMenu();

        } catch (Exception e) {
            showError("Error creating credit request: " + e.getMessage());
            e.printStackTrace();
            pauseBeforeMenu();
        }
    }

}