import Models.Employee;
import Models.Professional;
import Models.Installement;
import Models.PaymentRecord;
import Enums.PaymentStatusEnum;
import Repositories.EmployeeRepository;
import Repositories.ProfessionalRepository;
import Repositories.InstallementRepository;
import Repositories.PaymentRecordRepository;
import Services.ScoringService;
import Services.PaymentService;
import Views.View;
import Views.EmployeeView;
import Views.ProfessionalView;
import Views.CreditView;
import Views.AnalyticsView;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main extends View {

    public static void main(String[] args) {
        EmployeeRepository.getAll().forEach(ScoringService::updateClientScore);
        ProfessionalRepository.getAll().forEach(ScoringService::updateClientScore);

        executeScheduledScoreCalculator();
        executeScheduledInstallementsUpdater();

        showHeader("JavaMicroCredit - Credit Scoring System");
        println("Welcome to the Automated Credit Scoring System");

        while (true) {
            String[] mainMenuOptions = {
                    "Client Management",
                    "Credit Scoring & Decision",
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
                    AnalyticsView.showMenu();
                    break;
                case 4:
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

    private static void executeScheduledInstallementsUpdater() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        scheduler.scheduleAtFixedRate(() -> {
            try {
                updateOverdueInstallments();
            } catch (Exception e) {
                System.err.println("Error updating overdue installments: " + e.getMessage());
            }
        }, 0, 1, TimeUnit.HOURS);
    }

    private static void executeScheduledScoreCalculator() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        scheduler.scheduleAtFixedRate(() -> {
            EmployeeRepository.getAll().forEach(ScoringService::updateClientScore);
            ProfessionalRepository.getAll().forEach(ScoringService::updateClientScore);
        }, 0, 1, TimeUnit.MINUTES);
    }

    private static void updateOverdueInstallments() {
        List<Installement> allInstallments = InstallementRepository.getAll();
        LocalDate today = LocalDate.now();

        allInstallments.stream()
            .filter(inst -> {
                PaymentRecord lastPayment = PaymentRecordRepository.getLatestByInstallementId(inst.getId());
                return lastPayment == null;
            })
            .filter(inst -> {
                return !inst.getDueDate().isAfter(today);
            })
            .forEach(inst -> {
                PaymentStatusEnum status = PaymentService.getInstallementStatus(inst);

                if (status == PaymentStatusEnum.LATE || status == PaymentStatusEnum.UNPAID_UNSETTLED) {
                    PaymentRecordRepository.create(inst.getId(), status);
                }
            });
    }

}