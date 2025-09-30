package Services;

import Enums.CreditType;
import Enums.DecisionEnum;
import Models.Credit;
import Models.Installement;
import Models.Employee;
import Models.Professional;
import Models.Person;
import java.time.LocalDate;
import java.util.List;

public class CreditService {

    public static Credit createCreditWithInstallments(Integer employeeId, Integer professionalId,
                                                     Double requestedAmount, Double interestRate,
                                                     Integer durationInMonths, CreditType creditType) {
        Person person = null;

        if (employeeId != null) {
            person = Employee.findById(employeeId);
        } else if (professionalId != null) {
            person = Professional.findById(professionalId);
        }

        if (person == null) {
            throw new RuntimeException("Client not found");
        }

        double score = ScoringService.calculateScore(person);
        System.out.println("{{{{{{{{{{{{{{{{{{{{{}}}}}}}}}}}}}}}}}}}}}" + score);
        person.setScore(score);
        if (person instanceof Employee) {
            Employee.update((Employee) person);
        } else if (person instanceof Professional) {
            Professional.update((Professional) person);
        }

        boolean isNewClient = ScoringService.isNewClient(person);
        double maxBorrowingAmount = ScoringService.getMaxBorrowingAmount(person, score);

        DecisionEnum decision;
        Double approvedAmount = 0.0;

        double minScore = isNewClient ? 70 : 60;
        double autoApprovalScore = 75;
        double minManualReviewScore = isNewClient ? 60 : 50;

        if (score < minManualReviewScore) {
            decision = DecisionEnum.AUTOMATIC_REJECTION;
            approvedAmount = 0.0;
        } else if (score >= autoApprovalScore && requestedAmount <= maxBorrowingAmount) {
            decision = DecisionEnum.IMMEDIATE_APPROVAL;
            approvedAmount = requestedAmount;
        } else if (score >= minScore && requestedAmount <= maxBorrowingAmount) {
            decision = DecisionEnum.MANUAL_REVIEW;
            approvedAmount = requestedAmount;
        } else {
            decision = DecisionEnum.AUTOMATIC_REJECTION;
            approvedAmount = 0.0;
        }

        LocalDate creditDate = LocalDate.now();

        Credit credit = Credit.create(
            employeeId,
            professionalId,
            creditDate,
            requestedAmount,
            approvedAmount,
            interestRate,
            durationInMonths,
            creditType,
            decision
        );

        if (decision == DecisionEnum.IMMEDIATE_APPROVAL) {
            List<Installement> installements = Installement.generateInstallements(credit);
            System.out.println("Credit approved! " + installements.size() + " installments generated.");
        } else {
            System.out.println("Credit decision: " + decision);
        }

        return credit;
    }
}