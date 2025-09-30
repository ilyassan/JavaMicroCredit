package Services;

import Models.*;
import Enums.ContractType;
import Enums.SectorType;
import Enums.PaymentStatusEnum;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

public class ScoringService {

    public static double calculateScore(Person person) {
        double score = 0;

        score += calculateProfessionalStability(person);
        score += calculateFinancialCapacity(person);
        score += calculateHistory(person);
        score += calculateClientRelationship(person);
        score += calculateComplementaryCriteria(person);

        return score;
    }

    private static double calculateProfessionalStability(Person person) {
        double score = 0;

        if (person instanceof Employee) {
            Employee employee = (Employee) person;

            ContractType contractType = employee.getContractType();
            SectorType sector = employee.getEmploymentSector();

            if (contractType == ContractType.PERMANENT) {
                if (sector == SectorType.PUBLIC) {
                    score += 25;
                } else if (sector == SectorType.PRIVATE_LARGE) {
                    score += 15;
                } else if (sector == SectorType.SME) {
                    score += 12;
                }
            } else if (contractType == ContractType.TEMPORARY || contractType == ContractType.INTERIM) {
                score += 10;
            }

            int monthsInWork = employee.getMonthsInWork();
            if (monthsInWork >= 60) {
                score += 5;
            } else if (monthsInWork >= 24) {
                score += 3;
            } else if (monthsInWork >= 12) {
                score += 1;
            }

        } else if (person instanceof Professional) {
            Professional professional = (Professional) person;
            score += 18;
        }

        return score;
    }

    private static double calculateFinancialCapacity(Person person) {
        double income = 0;

        if (person instanceof Employee) {
            income = ((Employee) person).getSalary();
        } else if (person instanceof Professional) {
            income = ((Professional) person).getIncome();
        }

        if (income >= 10000) {
            return 30;
        } else if (income >= 8000) {
            return 25;
        } else if (income >= 5000) {
            return 20;
        } else if (income >= 3000) {
            return 15;
        } else {
            return 10;
        }
    }

    private static double calculateHistory(Person person) {
        double score = 0;

        Integer employeeId = null;
        Integer professionalId = null;

        if (person instanceof Employee) {
            employeeId = ((Employee) person).getId();
        } else if (person instanceof Professional) {
            professionalId = ((Professional) person).getId();
        }

        List<PaymentRecord> paymentRecords = PaymentRecord.getPaymentRecordsByClientId(employeeId, professionalId);

        if (paymentRecords.isEmpty()) {
            return 0;
        }

        int unpaidUnsettledCount = 0;
        int unpaidSettledCount = 0;
        int paidLateCount = 0;
        int onTimeCount = 0;

        for (PaymentRecord record : paymentRecords) {
            PaymentStatusEnum status = record.getStatus();

            switch (status) {
                case UNPAID_UNSETTLED:
                    unpaidUnsettledCount++;
                    break;
                case UNPAID_SETTLED:
                    unpaidSettledCount++;
                    break;
                case PAID_LATE:
                    paidLateCount++;
                    break;
                case ON_TIME:
                    onTimeCount++;
                    break;
                default:
                    break;
            }
        }

        score -= unpaidUnsettledCount * 10;

        score += unpaidSettledCount * 5;

        if (paidLateCount >= 1 && paidLateCount <= 3) {
            score -= 3;
        } else if (paidLateCount >= 4) {
            score -= 5;
        }

        if (paymentRecords.size() > 0 && unpaidUnsettledCount == 0 && paidLateCount == 0) {
            score += 10;
        }

        return score;
    }

    private static double calculateClientRelationship(Person person) {
        double score = 0;

        boolean isNewClient = person.getCreatedAt() == null ||
                              ChronoUnit.MONTHS.between(person.getCreatedAt(), java.time.LocalDateTime.now()) < 12;

        if (isNewClient) {
            long age = ChronoUnit.YEARS.between(person.getDateOfBirth(), LocalDate.now());

            if (age >= 18 && age <= 25) {
                score += 4;
            } else if (age >= 26 && age <= 35) {
                score += 8;
            } else if (age >= 36 && age <= 55) {
                score += 10;
            } else if (age > 55) {
                score += 6;
            }

            switch (person.getFamilyStatus()) {
                case MARRIED:
                    score += 3;
                    break;
                case SINGLE:
                    score += 2;
                    break;
                default:
                    break;
            }

            int childrenCount = person.getChildrenCount();
            if (childrenCount == 0) {
                score += 2;
            } else if (childrenCount >= 1 && childrenCount <= 2) {
                score += 1;
            }

        } else {
            long relationshipMonths = ChronoUnit.MONTHS.between(person.getCreatedAt(), java.time.LocalDateTime.now());

            if (relationshipMonths >= 36) {
                score += 10;
            } else if (relationshipMonths >= 12) {
                score += 8;
            } else {
                score += 5;
            }
        }

        return score;
    }

    private static double calculateComplementaryCriteria(Person person) {
        if (person.getInvestment() || person.getPlacement()) {
            return 10;
        }
        return 0;
    }

    public static boolean isNewClient(Person person) {
        List<Credit> credits = Credit.getAll();

        return credits.stream().anyMatch(credit -> {
            if (person instanceof Employee) {
                return Objects.equals(person.getId(), credit.getEmployeeId());
            } else {
                return Objects.equals(person.getId(), credit.getProfessionalId());
            }
        });
    }

    public static double getMaxBorrowingAmount(Person person, double score) {
        double income = 0;

        if (person instanceof Employee) {
            income = ((Employee) person).getSalary();
        } else if (person instanceof Professional) {
            income = ((Professional) person).getIncome();
        }

        boolean isNew = isNewClient(person);

        if (isNew) {
            return income * 4;
        } else {
            if (score > 80) {
                return income * 10;
            } else {
                return income * 7;
            }
        }
    }
}