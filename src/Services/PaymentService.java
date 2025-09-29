package Services;

import Enums.PaymentStatusEnum;
import Models.Installement;
import Models.PaymentRecord;
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

        return PaymentRecord.create(installementId, status);
    }

    public static PaymentStatusEnum determinePaymentStatus(LocalDate dueDate, LocalDate paymentDate) {
        long daysDifference = ChronoUnit.DAYS.between(dueDate, paymentDate);

        if (daysDifference <= 0) {
            return PaymentStatusEnum.ON_TIME;
        } else if (daysDifference >= 5 && daysDifference <= 30) {
            return PaymentStatusEnum.PAID_LATE;
        } else if (daysDifference > 30) {
            return PaymentStatusEnum.UNPAID_SETTLED;
        } else {
            return PaymentStatusEnum.ON_TIME;
        }
    }
}