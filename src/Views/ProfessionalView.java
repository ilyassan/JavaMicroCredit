package Views;

import Models.Employee;
import Models.Professional;
import Enums.FamilyStatus;
import Enums.SectorType;
import java.time.LocalDate;
import java.util.List;

public class ProfessionalView extends View {

    public static void showMenu() {
        while (true) {
            String[] professionalMenuOptions = {
                    "Create Professional",
                    "Search Professional by ID",
                    "Modify Professional",
                    "Delete Professional",
                    "List All Professionals",
                    "Back to Client Management"
            };

            int choice = showMenuAndGetChoice("Professional Management", professionalMenuOptions);

            switch (choice) {
                case 1:
                    createProfessional();
                    break;
                case 2:
                    searchProfessionalById();
                    break;
                case 3:
                    modifyProfessional();
                    break;
                case 4:
                    deleteProfessional();
                    break;
                case 5:
                    listAllProfessionals();
                    break;
                case 6:
                    return;
                default:
                    showError("Invalid choice. Please try again.");
            }
        }
    }

    private static void createProfessional() {
        showHeader("Create New Professional");

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
            boolean investment = getYesNo("Does the professional have investments?");
            boolean placement = getYesNo("Does the professional have placements?");

            int childrenCount = getInt("Enter number of children: ", 0, 20);

            // Family Status
            FamilyStatus familyStatus = getFamilyStatus("Select family status:");

            // Professional Information
            double income = getPositiveDouble("Enter monthly income (DH): ");

            String taxRegistrationNumber = getNonEmptyString("Enter tax registration number: ");

            SectorType businessSector = getSectorType("Select business sector:");

            String activity = getString("Enter activity/profession (optional): ");
            if (activity.isEmpty()) {
                activity = null;
            }

            // Create the professional
            Professional professional = Professional.create(
                firstName, lastName, dateOfBirth, city,
                investment, placement, childrenCount, familyStatus,
                income, taxRegistrationNumber, businessSector, activity
            );

            showSuccess("Professional created successfully!");
            showProfessionalDetails(professional);

        } catch (Exception e) {
            showError("Failed to create professional: " + e.getMessage());
        }

        pauseBeforeMenu();
    }

    private static Professional selectProfessionalById() {
        try {
            int professionalId = getInt("Enter professional ID: ", 1, Integer.MAX_VALUE);

            return Professional.findById(professionalId);
        }
        catch (Exception e) {
            showError("Failed to select professional ID: " + e.getMessage());
            return null;
        }
    }

    private static void searchProfessionalById() {
        showHeader("Search Employee by ID");

        Professional professional = selectProfessionalById();
        if (professional == null) {
            showError("Professional not found.");
            pauseBeforeMenu();
            return;
        }

        showProfessionalDetails(professional);
        pauseBeforeMenu();
    }

    private static void modifyProfessional() {
        showHeader("Modify Professional");
        showWarning("Feature will be implemented in next phase");
        pauseBeforeMenu();
    }

    private static void deleteProfessional() {
        showHeader("Delete Porfessional");

        try {
            Professional professional = selectProfessionalById();

            if (professional == null){
                showError("Professional not found.");
                pauseBeforeMenu();
                return;
            };

            // Confirm deletion
            boolean confirm = getYesNo("Are you sure you want to delete this professional?");
            if (!confirm) {
                showWarning("Deletion cancelled.");
                pauseBeforeMenu();
                return;
            }

            boolean deleted = Professional.delete(professional.getId());

            if (deleted) {
                showSuccess("Professional deleted successfully!");
            } else {
                showError("Failed to delete professional. Please try again.");
            }

        } catch (Exception e) {
            showError("Error during deletion: " + e.getMessage());
        }

        pauseBeforeMenu();
    }

    private static void listAllProfessionals() {
        showHeader("List All Professionals");
        List<Professional> professionals = Professional.getAll();

        for (Professional professional : professionals) {
            showProfessionalDetails(professional);
        }

        pauseBeforeMenu();
    }

    private static void showProfessionalDetails(Professional professional) {
        showSeparator();
        println("Professional Details:");
        println("ID: " + professional.getId());
        println("Name: " + professional.getFirstName() + " " + professional.getLastName());
        println("Date of Birth: " + professional.getDateOfBirth());
        if (professional.getCity() != null) {
            println("City: " + professional.getCity());
        }
        println("Investment: " + (professional.getInvestment() ? "Yes" : "No"));
        println("Placement: " + (professional.getPlacement() ? "Yes" : "No"));
        println("Children Count: " + professional.getChildrenCount());
        println("Family Status: " + professional.getFamilyStatus());
        println("Income: " + professional.getIncome() + " DH");
        println("Tax Registration Number: " + professional.getTaxRegistrationNumber());
        println("Business Sector: " + professional.getBusinessSector());
        if (professional.getActivity() != null) {
            println("Activity: " + professional.getActivity());
        }
        println("Created At: " + professional.getCreatedAt());
        showSeparator();
    }
}