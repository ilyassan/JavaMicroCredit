package Services;

import Enums.PaymentStatusEnum;
import Models.Installement;
import Models.PaymentRecord;
import Models.Credit;
import Models.Employee;
import Models.Professional;
import Models.Person;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class PaymentService {

    public static PaymentRecord recordPayment(Integer installementId) {
        Installement installement = Installement.findById(installementId);

        if (installement == null) {
            throw new RuntimeException("Installement not found");
        }

        LocalDate today = LocalDate.now();
        LocalDate dueDate = installement.getDueDate();

        PaymentStatusEnum status = determinePaymentStatus(dueDate, today);

        PaymentRecord paymentRecord = PaymentRecord.create(installementId, status);

        Credit credit = Credit.findById(installement.getCreditId());
        if (credit != null) {
            Person person = null;
            if (credit.getEmployeeId() != null) {
                person = Employee.findById(credit.getEmployeeId());
            } else if (credit.getProfessionalId() != null) {
                person = Professional.findById(credit.getProfessionalId());
            }

            if (person != null) {
                double newScore = ScoringService.calculateScore(person);
                person.setScore(newScore);

                if (person instanceof Employee) {
                    Employee.update((Employee) person);
                } else if (person instanceof Professional) {
                    Professional.update((Professional) person);
                }
            }
        }

        return paymentRecord;
    }

    public static PaymentStatusEnum determinePaymentStatus(LocalDate dueDate, LocalDate paymentDate) {
        long daysDifference = ChronoUnit.DAYS.between(dueDate, paymentDate);

        if (daysDifference <= 0) {
            return PaymentStatusEnum.ON_TIME;
        } else if (daysDifference >= 1 && daysDifference <= 4) {
            return PaymentStatusEnum.ON_TIME;
        } else if (daysDifference >= 5 && daysDifference <= 30) {
            return PaymentStatusEnum.PAID_LATE;
        } else if (daysDifference > 30) {
            return PaymentStatusEnum.UNPAID_SETTLED;
        }

        return PaymentStatusEnum.ON_TIME;
    }

    public static PaymentStatusEnum getInstallementStatus(Installement installement) {
        PaymentRecord lastPayment = PaymentRecord.getLatestByInstallementId(installement.getId());

        if (lastPayment != null) {
            return lastPayment.getStatus();
        }

        LocalDate today = LocalDate.now();
        LocalDate dueDate = installement.getDueDate();
        long daysPastDue = ChronoUnit.DAYS.between(dueDate, today);

        if (daysPastDue < 0) {
            return null;
        } else if (daysPastDue >= 0 && daysPastDue < 5) {
            return null;
        } else if (daysPastDue >= 5 && daysPastDue <= 30) {
            return PaymentStatusEnum.LATE;
        } else if (daysPastDue > 30) {
            return PaymentStatusEnum.UNPAID_UNSETTLED;
        }

        return null;
    }
}