package Views;

import Enums.PaymentStatusEnum;
import Models.Employee;
import Models.Installement;
import Models.PaymentRecord;
import Models.Credit;
import Enums.ContractType;
import Enums.FamilyStatus;
import Enums.SectorType;
import Repositories.EmployeeRepository;
import Repositories.CreditRepository;
import Repositories.InstallementRepository;
import Repositories.PaymentRecordRepository;
import Services.PaymentService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeeView extends View {

    public static void showMenu() {
        while (true) {
            String[] employeeMenuOptions = {
                    "Create Employee",
                    "Search Employee by ID",
                    "Modify Employee",
                    "Delete Employee",
                    "List All Employees",
                    "View Employee Credits",
                    "Pay Installement",
                    "Back to Client Management"
            };

            int choice = showMenuAndGetChoice("Employee Management", employeeMenuOptions);

            switch (choice) {
                case 1:
                    createEmployee();
                    break;
                case 2:
                    searchEmployeeById();
                    break;
                case 3:
                    modifyEmployee();
                    break;
                case 4:
                    deleteEmployee();
                    break;
                case 5:
                    listAllEmployees();
                    break;
                case 6:
                    viewEmployeeCredits();
                    break;
                case 7:
                    payInstallement();
                    break;
                case 8:
                    return;
                default:
                    showError("Invalid choice. Please try again.");
            }
        }
    }

    private static void createEmployee() {
        showHeader("Create New Employee");

        try {
            // Personal Information
            String firstName = getNonEmptyString("Enter first name: ");
            String lastName = getNonEmptyString("Enter last name: ");

            LocalDate dateOfBirth = getDateInRange(
                "Enter date of birth",
                LocalDate.now().minusYears(70),
                LocalDate.now().minusYears(18)
            );

            String city = getString("Enter city (optional): ");
            if (city.isEmpty()) {
                city = null;
            }

            // Financial Information
            boolean investment = getYesNo("Does the employee have investments?");
            boolean placement = getYesNo("Does the employee have placements?");

            int childrenCount = getInt("Enter number of children: ", 0, 20);

            // Family Status
            FamilyStatus familyStatus = getFamilyStatus("Select family status:");

            // Employment Information
            double salary = getPositiveDouble("Enter monthly salary (DH): ");
            int monthsInWork = getInt("Enter months in current work: ", 0, 600);

            String position = getString("Enter position (optional): ");
            if (position.isEmpty()) {
                position = null;
            }

            ContractType contractType = getContractType("Select contract type:");
            SectorType employmentSector = getSectorType("Select employment sector:");

            // Create the employee
            Employee employee = EmployeeRepository.create(
                firstName, lastName, dateOfBirth, city,
                investment, placement, childrenCount, familyStatus,
                salary, monthsInWork, position, contractType, employmentSector
            );

            showSuccess("Employee created successfully!");
            showEmployeeDetails(employee);

        } catch (Exception e) {
            showError("Failed to create employee: " + e.getMessage());
        }

        pauseBeforeMenu();
    }

    private static Employee selectEmployeeById() {
        try {
            int employeeId = getInt("Enter Employee ID: ", 1, Integer.MAX_VALUE);

            return EmployeeRepository.findById(employeeId);
        }
        catch (Exception e) {
            showError("Failed to select employee ID: " + e.getMessage());
            return null;
        }
    }

    private static void searchEmployeeById() {
        showHeader("Search Employee by ID");

        Employee employee = selectEmployeeById();
        if (employee == null) {
            showError("Employee not found.");
            pauseBeforeMenu();
            return;
        }

        showEmployeeDetails(employee);
        pauseBeforeMenu();
    }

    private static void modifyEmployee() {
        showHeader("Modify Employee");

        try {
            Employee employee = selectEmployeeById();
            if (employee == null) {
                showError("Employee not found.");
                pauseBeforeMenu();
                return;
            }

            showEmployeeDetails(employee);
            boolean confirm = getYesNo("Do you want to modify this employee?");
            if (!confirm) {
                showWarning("Modification cancelled.");
                pauseBeforeMenu();
                return;
            }

            // Personal Information
            String firstName = getNonEmptyString("Enter first name (" + employee.getFirstName() + "): ");
            if (firstName.isEmpty()) firstName = employee.getFirstName();

            String lastName = getNonEmptyString("Enter last name (" + employee.getLastName() + "): ");
            if (lastName.isEmpty()) lastName = employee.getLastName();

            LocalDate dateOfBirth = getDateInRange(
                    "Enter date of birth (" + employee.getDateOfBirth() + ")",
                    LocalDate.now().minusYears(70),
                    LocalDate.now().minusYears(18)
            );

            String city = getString("Enter city (current: " + (employee.getCity() != null ? employee.getCity() : "None") + ") (optional): ");
            if (city.isEmpty()) city = employee.getCity();

            // Financial Information
            boolean investment = getYesNo("Does the employee have investments? (current: " + (employee.getInvestment() ? "Yes" : "No") + ")");
            boolean placement = getYesNo("Does the employee have placements? (current: " + (employee.getPlacement() ? "Yes" : "No") + ")");

            int childrenCount = getInt("Enter number of children (current: " + employee.getChildrenCount() + "): ", 0, 20);

            // Family Status
            FamilyStatus familyStatus = getFamilyStatus("Select family status (current: " + employee.getFamilyStatus() + "):");

            // Employment Information
            double salary = getPositiveDouble("Enter monthly salary (DH) (current: " + employee.getSalary() + "): ");
            int monthsInWork = getInt("Enter months in current work (current: " + employee.getMonthsInWork() + "): ", 0, 600);

            String position = getString("Enter position (current: " + (employee.getPosition() != null ? employee.getPosition() : "None") + ") (optional): ");
            if (position.isEmpty()) position = employee.getPosition();

            ContractType contractType = getContractType("Select contract type (current: " + employee.getContractType() + "):");
            SectorType employmentSector = getSectorType("Select employment sector (current: " + employee.getEmploymentSector() + "):");

            // Update employee
            employee.setFirstName(firstName);
            employee.setLastName(lastName);
            employee.setDateOfBirth(dateOfBirth);
            employee.setCity(city);
            employee.setInvestment(investment);
            employee.setPlacement(placement);
            employee.setChildrenCount(childrenCount);
            employee.setFamilyStatus(familyStatus);
            employee.setSalary(salary);
            employee.setMonthsInWork(monthsInWork);
            employee.setPosition(position);
            employee.setContractType(contractType);
            employee.setEmploymentSector(employmentSector);
            employee.setUpdatedAt(java.time.LocalDateTime.now());

            boolean updated = EmployeeRepository.update(employee);

            if (updated) {
                showSuccess("Employee updated successfully!");
                showEmployeeDetails(employee);
            } else {
                showError("Failed to update employee. Please try again.");
            }

        } catch (Exception e) {
            showError("Error during modification: " + e.getMessage());
        }

        pauseBeforeMenu();
    }

    private static void deleteEmployee() {
        showHeader("Delete Employee");

        try {
            Employee employee = selectEmployeeById();

            if (employee == null){
                showError("Employee not found.");
                pauseBeforeMenu();
                return;
            };

            // Confirm deletion
            boolean confirm = getYesNo("Are you sure you want to delete this employee?");
            if (!confirm) {
                showWarning("Deletion cancelled.");
                pauseBeforeMenu();
                return;
            }

            boolean deleted = EmployeeRepository.delete(employee.getId());

            if (deleted) {
                showSuccess("Employee deleted successfully!");
            } else {
                showError("Failed to delete employee. Please try again.");
            }

        } catch (Exception e) {
            showError("Error during deletion: " + e.getMessage());
        }

        pauseBeforeMenu();
    }

    private static void listAllEmployees() {
        showHeader("List All Employees");
        List<Employee> employees = EmployeeRepository.getAll();

        for (Employee employee : employees) {
            showEmployeeDetails(employee);
        }
        pauseBeforeMenu();
    }

    private static void showEmployeeDetails(Employee employee) {
        showSeparator();
        println("Employee Details:");
        println("ID: " + employee.getId());
        println("Name: " + employee.getFirstName() + " " + employee.getLastName());
        println("Date of Birth: " + employee.getDateOfBirth());
        if (employee.getCity() != null) {
            println("City: " + employee.getCity());
        }
        println("Investment: " + (employee.getInvestment() ? "Yes" : "No"));
        println("Placement: " + (employee.getPlacement() ? "Yes" : "No"));
        println("Children Count: " + employee.getChildrenCount());
        println("Family Status: " + employee.getFamilyStatus());
        println("Salary: " + employee.getSalary() + " DH");
        println("Months in Work: " + employee.getMonthsInWork());
        if (employee.getPosition() != null) {
            println("Position: " + employee.getPosition());
        }
        println("Contract Type: " + employee.getContractType());
        println("Employment Sector: " + employee.getEmploymentSector());
        println("Score: " + (employee.getScore() != null ? employee.getScore() : "Not calculated"));
        println("Created At: " + employee.getCreatedAt());
        showSeparator();
    }

    private static void payInstallement() {
        showHeader("Pay Installement");

        int employeeId = getInt("Enter Employee ID: ", 1, Integer.MAX_VALUE);
        Employee employee = EmployeeRepository.findById(employeeId);

        if (employee == null) {
            showError("Employee not found with ID: " + employeeId);
            pauseBeforeMenu();
            return;
        }

        List<Credit> credits = CreditRepository.getCreditsByEmployeeId(employeeId);

        if (credits.isEmpty()) {
            showInfo("No credits found for this employee.");
            pauseBeforeMenu();
            return;
        }

        println("Employee: " + employee.getFirstName() + " " + employee.getLastName());
        println("\nCredits:\n");

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

        List<Installement> installements = InstallementRepository.getInstallementsByCreditId(selectedCredit.getId());

        if (installements.isEmpty()) {
            showInfo("No installements due for this credit yet.");
            pauseBeforeMenu();
            return;
        }

        println("\nInstallements for Credit ID " + selectedCredit.getId() + ":\n");

        List<Installement> unpaidInstallements = new ArrayList<>();
        for (int i = 0; i < installements.size(); i++) {
            Installement inst = installements.get(i);
            PaymentRecord lastPayment = PaymentRecordRepository.getLatestByInstallementId(inst.getId());

            String paymentStatus;
            if (lastPayment != null) {
                paymentStatus = lastPayment.getStatus().toString();
            } else {
                PaymentStatusEnum status = PaymentService.getInstallementStatus(inst);
                paymentStatus = status != null ? status.toString() : "NOT PAID";
            }

            println((i + 1) + ". Installement ID: " + inst.getId() +
                   " | Due Date: " + inst.getDueDate() +
                   " | Amount: " + inst.getAmount() + " DH" +
                   " | Status: " + paymentStatus);

            if (lastPayment == null) {
                unpaidInstallements.add(inst);
            }
        }

        if (unpaidInstallements.isEmpty()) {
            showInfo("All due installements have been paid.");
            pauseBeforeMenu();
            return;
        }

        println("");
        int installementIndex = getInt("Enter installement number to pay (1-" + installements.size() + "): ", 1, installements.size());
        Installement selectedInstallement = installements.get(installementIndex - 1);

        PaymentRecord existingPayment = PaymentRecordRepository.getLatestByInstallementId(selectedInstallement.getId());
        if (existingPayment != null) {
            showError("This installement has already been paid.");
            pauseBeforeMenu();
            return;
        }

        if (getYesNo("Confirm payment of " + selectedInstallement.getAmount() + " DH for installement due on " + selectedInstallement.getDueDate() + "?")) {
            try {
                PaymentRecord payment = PaymentService.recordPayment(selectedInstallement.getId());
                showSuccess("Payment recorded successfully!");
                println("Payment Status: " + payment.getStatus());
                println("Payment Date: " + payment.getCreatedAt());
            } catch (Exception e) {
                showError("Error recording payment: " + e.getMessage());
            }
        } else {
            println("Payment cancelled.");
        }

        pauseBeforeMenu();
    }

    private static void viewEmployeeCredits() {
        showHeader("View Employee Credits");

        int employeeId = getInt("Enter Employee ID: ", 1, Integer.MAX_VALUE);
        Employee employee = EmployeeRepository.findById(employeeId);

        if (employee == null) {
            showError("Employee not found with ID: " + employeeId);
            pauseBeforeMenu();
            return;
        }

        List<Credit> credits = CreditRepository.getCreditsByEmployeeId(employeeId);

        if (credits.isEmpty()) {
            showInfo("No credits found for this employee.");
            pauseBeforeMenu();
            return;
        }

        println("Employee: " + employee.getFirstName() + " " + employee.getLastName());
        println("Total Credits: " + credits.size());
        println("");

        for (int i = 0; i < credits.size(); i++) {
            Credit credit = credits.get(i);
            println("--- Credit #" + (i + 1) + " ---");
            println("Credit ID: " + credit.getId());
            println("Credit Type: " + credit.getCreditType());
            println("Credit Date: " + credit.getCreditDate());
            println("Requested Amount: " + credit.getRequestedAmount() + " DH");
            println("Approved Amount: " + credit.getApprovedAmount() + " DH");
            println("Interest Rate: " + credit.getInterestRate() + "%");
            println("Duration: " + credit.getDurationInMonths() + " months");
            println("Decision: " + credit.getDecision());
            println("");
        }

        pauseBeforeMenu();
    }

    private static void showInfo(String message) {
        println("INFO: " + message);
    }
}