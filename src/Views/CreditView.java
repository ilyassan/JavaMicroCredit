package Views;

import Enums.CreditType;
import Enums.DecisionEnum;
import Models.*;
import Repositories.EmployeeRepository;
import Repositories.ProfessionalRepository;
import Repositories.CreditRepository;
import Repositories.InstallementRepository;
import Services.CreditService;
import java.util.List;

public class CreditView extends View {

    public static void showMenu() {
        while (true) {
            String[] creditMenuOptions = {
                    "Create Credit Request",
                    "Review Pending Credits",
                    "View Credit Details",
                    "Back to Main Menu"
            };

            int choice = showMenuAndGetChoice("Credit Scoring & Decision", creditMenuOptions);

            switch (choice) {
                case 1:
                    createCreditRequest();
                    break;
                case 2:
                    reviewPendingCredits();
                    break;
                case 3:
                    showSpeceficCreditDetails();
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
                Employee employee = EmployeeRepository.findById(employeeId);

                if (employee == null) {
                    showError("Employee not found with ID: " + employeeId);
                    pauseBeforeMenu();
                    return;
                }

                println("Client: " + employee.getFirstName() + " " + employee.getLastName());
                println("Salary: " + employee.getSalary() + " DH");
                println("Score: " + employee.getScore());
            } else {
                professionalId = getInt("Enter Professional ID: ", 1, Integer.MAX_VALUE);
                Professional professional = ProfessionalRepository.findById(professionalId);

                if (professional == null) {
                    showError("Professional not found with ID: " + professionalId);
                    pauseBeforeMenu();
                    return;
                }

                println("Client: " + professional.getFirstName() + " " + professional.getLastName());
                println("Income: " + professional.getIncome() + " DH");
                println("Score: " + professional.getScore());
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

    private static void reviewPendingCredits() {
        showHeader("Credits Pending Manual Review");

        List<Credit> pendingCredits = CreditRepository.findByDecision(DecisionEnum.MANUAL_REVIEW);

        if (pendingCredits.isEmpty()) {
            showInfo("No credits pending manual review.");
            pauseBeforeMenu();
            return;
        }

        println("Found " + pendingCredits.size() + " credit(s) pending review:\n");

        for (int i = 0; i < pendingCredits.size(); i++) {
            Credit credit = pendingCredits.get(i);
            println("--- Credit #" + (i + 1) + " ---");
            println("Credit ID: " + credit.getId());
            println("Credit Type: " + credit.getCreditType());
            println("Credit Date: " + credit.getCreditDate());
            println("Requested Amount: " + credit.getRequestedAmount() + " DH");
            println("Interest Rate: " + credit.getInterestRate() + "%");
            println("Duration: " + credit.getDurationInMonths() + " months");

            if (credit.getEmployeeId() != null) {
                Employee employee = EmployeeRepository.findById(credit.getEmployeeId());
                if (employee != null) {
                    println("Client: " + employee.getFirstName() + " " + employee.getLastName() + " (Employee)");
                    println("Salary: " + employee.getSalary() + " DH");
                    println("Score: " + employee.getScore());
                }
            } else if (credit.getProfessionalId() != null) {
                Professional professional = ProfessionalRepository.findById(credit.getProfessionalId());
                if (professional != null) {
                    println("Client: " + professional.getFirstName() + " " + professional.getLastName() + " (Professional)");
                    println("Income: " + professional.getIncome() + " DH");
                    println("Score: " + professional.getScore());
                }
            }
            println("");
        }

        if (getYesNo("Do you want to review a credit?")) {
            int creditIndex = getInt("Enter credit number to review (1-" + pendingCredits.size() + "): ", 1, pendingCredits.size());
            reviewSingleCredit(pendingCredits.get(creditIndex - 1));
        }
    }

    private static void reviewSingleCredit(Credit credit) {
        showHeader("Review Credit ID: " + credit.getId());

        println("Requested Amount: " + credit.getRequestedAmount() + " DH");
        println("Interest Rate: " + credit.getInterestRate() + "%");
        println("Duration: " + credit.getDurationInMonths() + " months");
        println("");

        String[] reviewOptions = {
                "Approve with requested amount",
                "Approve with different amount",
                "Reject",
                "Cancel"
        };

        int choice = showMenuAndGetChoice("Review Decision", reviewOptions);

        DecisionEnum newDecision;
        Double approvedAmount;

        switch (choice) {
            case 1:
                newDecision = DecisionEnum.IMMEDIATE_APPROVAL;
                approvedAmount = credit.getRequestedAmount();
                break;
            case 2:
                approvedAmount = getPositiveDouble("Enter approved amount (DH): ");
                newDecision = DecisionEnum.IMMEDIATE_APPROVAL;
                break;
            case 3:
                newDecision = DecisionEnum.AUTOMATIC_REJECTION;
                approvedAmount = 0.0;
                break;
            case 4:
                return;
            default:
                showError("Invalid choice.");
                return;
        }

        boolean updated = CreditRepository.updateDecisionAndAmount(credit.getId(), newDecision, approvedAmount);

        if (updated) {
            if (newDecision == DecisionEnum.IMMEDIATE_APPROVAL) {
                credit.setDecision(newDecision);
                credit.setApprovedAmount(approvedAmount);
                List<Installement> installements = InstallementRepository.generateInstallements(credit);
                showSuccess("Credit approved! " + installements.size() + " installments generated.");
            } else {
                showSuccess("Credit rejected.");
            }
        } else {
            showError("Failed to update credit decision.");
        }

        pauseBeforeMenu();
    }

    private static void showSpeceficCreditDetails() {
        println("\nCredits:\n");

        List<Credit> credits = CreditRepository.getAll();

        for (int i = 0; i < credits.size(); i++) {
            Credit credit = credits.get(i);
            println((i + 1) + ". Credit ID: " + credit.getId() +
                    " | Type: " + credit.getCreditType() +
                    " | Amount: " + credit.getApprovedAmount() + " DH" +
                    " | Status: " + credit.getDecision());
        }

        println("");
        int creditIndex = getInt("Enter credit number (1-" + credits.size() + "): ", 1, credits.size());
        Credit selectedCredit = credits.get(creditIndex - 1);
        showCreditDetails(selectedCredit);
    }

    private static void showCreditDetails(Credit credit) {
        showHeader("Credit Details - ID: " + credit.getId());

        // Display basic credit information
        println("Credit Type: " + credit.getCreditType());
        println("Credit Date: " + credit.getCreditDate());
        println("Requested Amount: " + credit.getRequestedAmount() + " DH");
        println("Approved Amount: " + credit.getApprovedAmount() + " DH");
        println("Interest Rate: " + credit.getInterestRate() + "%");
        println("Duration: " + credit.getDurationInMonths() + " months");
        println("Decision: " + credit.getDecision());
        println("");

        Person person = null;

        // Display client information
        if (credit.getEmployeeId() != null) {
            person = EmployeeRepository.findById(credit.getEmployeeId());
        } else if (credit.getProfessionalId() != null) {
            person = ProfessionalRepository.findById(credit.getProfessionalId());
        }

        println("Client Type: " + (person instanceof Employee ? "Employee" : "Professional"));
        println("Client: " + person.getFirstName() + " " + person.getLastName());
        println("Salary: " + (person instanceof  Employee ? ((Employee) person).getSalary() : ((Professional) person).getIncome()) + " DH");
        println("Score: " + person.getScore());

        println("");

        // Display installments if approved
        if (credit.getDecision() == DecisionEnum.IMMEDIATE_APPROVAL) {
            List<Installement> installments = InstallementRepository.getInstallementsByCreditId(credit.getId());
            if (installments != null && !installments.isEmpty()) {
                println("Total Installments: " + installments.size());
            } else {
                showInfo("No installments found for this credit.");
            }
        } else {
            showInfo("No installments available (Credit not approved).");
        }
    }

    private static void showInfo(String message) {
        println("INFO: " + message);
    }

}