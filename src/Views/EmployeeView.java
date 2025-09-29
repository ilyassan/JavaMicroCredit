package Views;

import Models.Employee;
import Enums.ContractType;
import Enums.FamilyStatus;
import Enums.SectorType;
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
            Employee employee = Employee.create(
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

    private static void searchEmployeeById() {
        showHeader("Search Employee by ID");
        showWarning("Ba9i MA DRTHACH");
        pauseBeforeMenu();
    }

    private static void modifyEmployee() {
        showHeader("Modify Employee");
        showWarning("Ba9i MA DRTHACH");
        pauseBeforeMenu();
    }

    private static void deleteEmployee() {
        showHeader("Delete Employee");
        showWarning("Ba9i MA DRTHACH");
        pauseBeforeMenu();
    }

    private static void listAllEmployees() {
        showHeader("List All Employees");
        List<Employee> employees = Employee.getAll();

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
        println("Created At: " + employee.getCreatedAt());
        showSeparator();
    }
}