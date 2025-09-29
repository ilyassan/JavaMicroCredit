package Views;

import Enums.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;

public abstract class View {
    private static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void println(String string) {
        System.out.println(string);
    }

    public static void print(String string) {
        System.out.print(string);
    }

    public static int getInt(String prompt) {
        while (true) {
            try {
                print(prompt);
                int input = scanner.nextInt();
                scanner.nextLine();
                return input;
            } catch (InputMismatchException e) {
                scanner.nextLine();
                println("Invalid input. Please enter a valid number.");
            }
        }
    }

    public static int getInt(String prompt, int min, int max) {
        while (true) {
            int value = getInt(prompt);
            if (value >= min && value <= max) {
                return value;
            }
            println("Please enter a number between " + min + " and " + max + ".");
        }
    }

    public static double getDouble(String prompt) {
        while (true) {
            try {
                print(prompt);
                double input = scanner.nextDouble();
                scanner.nextLine();
                return input;
            } catch (InputMismatchException e) {
                scanner.nextLine();
                println("Invalid input. Please enter a valid number.");
            }
        }
    }

    public static double getDouble(String prompt, double min, double max) {
        while (true) {
            double value = getDouble(prompt);
            if (value >= min && value <= max) {
                return value;
            }
            println("Please enter a number between " + min + " and " + max + ".");
        }
    }

    public static double getPositiveDouble(String prompt) {
        while (true) {
            double value = getDouble(prompt);
            if (value > 0) {
                return value;
            }
            println("Please enter a positive number.");
        }
    }

    public static String getString(String prompt) {
        print(prompt);
        return scanner.nextLine().trim();
    }

    public static String getNonEmptyString(String prompt) {
        while (true) {
            String input = getString(prompt);
            if (!input.isEmpty()) {
                return input;
            }
            println("This field cannot be empty. Please try again.");
        }
    }

    public static LocalDate getDate(String prompt) {
        while (true) {
            String dateStr = getString(prompt + " (format: yyyy-MM-dd): ");
            try {
                return LocalDate.parse(dateStr, DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                println("Invalid date format. Please use yyyy-MM-dd format (e.g., 2023-12-25).");
            }
        }
    }

    public static LocalDate getDateInRange(String prompt, LocalDate minDate, LocalDate maxDate) {
        while (true) {
            LocalDate date = getDate(prompt);
            if (date.isAfter(minDate.minusDays(1)) && date.isBefore(maxDate.plusDays(1))) {
                return date;
            }
            println("Date must be between " + minDate.format(DATE_FORMATTER) +
                   " and " + maxDate.format(DATE_FORMATTER) + ".");
        }
    }

    public static boolean getYesNo(String prompt) {
        while (true) {
            String input = getString(prompt + " (y/n): ").toLowerCase();
            if (input.equals("y") || input.equals("yes")) {
                return true;
            } else if (input.equals("n") || input.equals("no")) {
                return false;
            }
            println("Please enter 'y' for yes or 'n' for no.");
        }
    }

    public static void pauseBeforeMenu() {
        println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public static void showError(String message) {
        println("ERROR: " + message);
    }

    public static void showSuccess(String message) {
        println("SUCCESS: " + message);
    }

    public static void showWarning(String message) {
        println("WARNING: " + message);
    }

    public static void showSeparator() {
        println("==========================================");
    }

    public static void showHeader(String title) {
        showSeparator();
        println("           " + title.toUpperCase());
        showSeparator();
    }

    public static int showMenuAndGetChoice(String title, String[] options) {
        showHeader(title);

        for (int i = 0; i < options.length; i++) {
            println((i + 1) + ". " + options[i]);
        }

        return getInt("\nEnter your choice: ", 1, options.length);
    }

    public static <T extends Enum<T>> T getEnum(String prompt, Class<T> enumClass) {
        T[] enumConstants = enumClass.getEnumConstants();

        while (true) {
            println("\n" + prompt);
            for (int i = 0; i < enumConstants.length; i++) {
                println((i + 1) + ". " + enumConstants[i]);
            }

            int choice = getInt("Select option (1-" + enumConstants.length + "): ", 1, enumConstants.length);
            return enumConstants[choice - 1];
        }
    }

    public static FamilyStatus getFamilyStatus(String prompt) {
        return getEnum(prompt, FamilyStatus.class);
    }

    public static ContractType getContractType(String prompt) {
        return getEnum(prompt, ContractType.class);
    }

    public static SectorType getSectorType(String prompt) {
        return getEnum(prompt, SectorType.class);
    }

    public static CreditType getCreditType(String prompt) {
        return getEnum(prompt, CreditType.class);
    }

    public static DecisionEnum getDecisionEnum(String prompt) {
        return getEnum(prompt, DecisionEnum.class);
    }

    public static PaymentStatusEnum getPaymentStatusEnum(String prompt) {
        return getEnum(prompt, PaymentStatusEnum.class);
    }

    public static LocalDate parseDate(String dateStr) {
        try {
            return LocalDate.parse(dateStr, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static String getStringInput() {
        return scanner.nextLine();
    }

    public static int getIntInput() {
        try {
            int input = scanner.nextInt();
            scanner.nextLine();
            return input;
        } catch (InputMismatchException e) {
            scanner.nextLine();
            println("Invalid input. Please enter a whole number.");
            return -1;
        }
    }

    public static double getDoubleInput() {
        try {
            double input = scanner.nextDouble();
            scanner.nextLine();
            return input;
        } catch (InputMismatchException e) {
            scanner.nextLine();
            println("Invalid input. Please enter a number.");
            return -1;
        }
    }
}