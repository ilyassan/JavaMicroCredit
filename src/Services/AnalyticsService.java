package Services;

import Models.Employee;
import Models.Professional;
import Models.Person;
import Models.Credit;
import Models.PaymentRecord;
import Repositories.EmployeeRepository;
import Repositories.ProfessionalRepository;
import Repositories.CreditRepository;
import Repositories.PaymentRecordRepository;
import Enums.ContractType;
import Enums.FamilyStatus;
import Enums.SectorType;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Comparator;

public class AnalyticsService {

    public static List<Person> sortByScore(boolean descending) {
        List<Employee> employees = EmployeeRepository.getAll();
        List<Professional> professionals = ProfessionalRepository.getAll();

        List<Person> sorted = Stream.concat(employees.stream(), professionals.stream())
            .sorted((p1, p2) -> {
                double score1 = p1.getScore() != null ? p1.getScore() : 0;
                double score2 = p2.getScore() != null ? p2.getScore() : 0;

                if (descending) {
                    return Double.compare(score2, score1);
                } else {
                    return Double.compare(score1, score2);
                }
            })
            .collect(Collectors.toList());

        return sorted;
    }

    public static List<Person> sortByIncome(boolean descending) {
        List<Employee> employees = EmployeeRepository.getAll();
        List<Professional> professionals = ProfessionalRepository.getAll();

        List<Person> sorted = Stream.concat(employees.stream(), professionals.stream())
            .sorted((p1, p2) -> {
                double income1 = getIncome(p1);
                double income2 = getIncome(p2);

                if (descending) {
                    return Double.compare(income2, income1);
                } else {
                    return Double.compare(income1, income2);
                }
            })
            .collect(Collectors.toList());

        return sorted;
    }

    public static List<Person> sortByRelationshipSeniority(boolean descending) {
        List<Employee> employees = EmployeeRepository.getAll();
        List<Professional> professionals = ProfessionalRepository.getAll();

        List<Person> sorted = Stream.concat(employees.stream(), professionals.stream())
            .sorted((p1, p2) -> {
                if (p1.getCreatedAt() == null && p2.getCreatedAt() == null) {
                    return 0;
                }
                if (p1.getCreatedAt() == null) {
                    return 1;
                }
                if (p2.getCreatedAt() == null) {
                    return -1;
                }

                if (descending) {
                    return p2.getCreatedAt().compareTo(p1.getCreatedAt());
                } else {
                    return p1.getCreatedAt().compareTo(p2.getCreatedAt());
                }
            })
            .collect(Collectors.toList());

        return sorted;
    }

    private static double getIncome(Person person) {
        if (person instanceof Employee) {
            return ((Employee) person).getSalary();
        } else if (person instanceof Professional) {
            return ((Professional) person).getIncome();
        }
        return 0;
    }

    public static List<Employee> findMortgageEligibleClients() {
        List<Employee> employees = EmployeeRepository.getAll();
        LocalDate today = LocalDate.now();

        List<Employee> result = employees.stream()
            .filter(emp -> {
                long age = ChronoUnit.YEARS.between(emp.getDateOfBirth(), today);
                return age >= 25 && age <= 50;
            })
            .filter(emp -> {
                return emp.getSalary() != null && emp.getSalary() > 4000;
            })
            .filter(emp -> {
                return emp.getContractType() == ContractType.PERMANENT;
            })
            .filter(emp -> {
                return emp.getScore() != null && emp.getScore() > 70;
            })
            .filter(emp -> {
                return emp.getFamilyStatus() == FamilyStatus.MARRIED;
            })
            .collect(Collectors.toList());

        return result;
    }

    public static List<Person> findAtRiskClients() {
        List<Employee> employees = EmployeeRepository.getAll();
        List<Professional> professionals = ProfessionalRepository.getAll();
        List<PaymentRecord> allPaymentRecords = PaymentRecordRepository.getAll();

        LocalDate sixMonthsAgo = LocalDate.now().minusMonths(6);

        List<Person> lowScoreClients = Stream.concat(employees.stream(), professionals.stream())
            .filter(person -> {
                return person.getScore() != null && person.getScore() < 60;
            })
            .collect(Collectors.toList());

        List<PaymentRecord> recentRecords = allPaymentRecords.stream()
            .filter(record -> {
                LocalDate recordDate = record.getCreatedAt().toLocalDate();
                return recordDate.isAfter(sixMonthsAgo);
            })
            .collect(Collectors.toList());

        List<Person> atRiskClients = lowScoreClients.stream()
            .filter(person -> !recentRecords.isEmpty())
            .collect(Collectors.toList());

        List<Person> sortedClients = atRiskClients.stream()
            .sorted((p1, p2) -> {
                double score1 = p1.getScore() != null ? p1.getScore() : 0;
                double score2 = p2.getScore() != null ? p2.getScore() : 0;
                return Double.compare(score2, score1);
            })
            .collect(Collectors.toList());

        List<Person> top10 = sortedClients.stream()
            .limit(10)
            .collect(Collectors.toList());

        return top10;
    }

    public static Map<String, EmploymentTypeStats> getEmploymentTypeDistribution() {
        List<Employee> employees = EmployeeRepository.getAll();
        List<Credit> allCredits = CreditRepository.getAll();

        Map<String, EmploymentTypeStats> distribution = new HashMap<>();

        Map<String, List<Employee>> groupedByType = new HashMap<>();
        groupedByType.put("CDI_PUBLIC", new java.util.ArrayList<>());
        groupedByType.put("CDI_PRIVATE_LARGE", new java.util.ArrayList<>());
        groupedByType.put("CDI_SME", new java.util.ArrayList<>());
        groupedByType.put("CDD", new java.util.ArrayList<>());
        groupedByType.put("INTERIM", new java.util.ArrayList<>());
        groupedByType.put("OTHER", new java.util.ArrayList<>());

        for (Employee emp : employees) {
            String type = getEmploymentType(emp);
            groupedByType.get(type).add(emp);
        }

        for (String type : groupedByType.keySet()) {
            List<Employee> employeeList = groupedByType.get(type);

            if (employeeList.isEmpty()) {
                continue;
            }

            int count = employeeList.size();

            List<Employee> employeesWithScore = employeeList.stream()
                .filter(e -> e.getScore() != null)
                .collect(Collectors.toList());

            double totalScore = employeesWithScore.stream()
                .mapToDouble(e -> e.getScore())
                .sum();
            double avgScore = employeesWithScore.isEmpty() ? 0.0 : totalScore / employeesWithScore.size();

            List<Employee> employeesWithSalary = employeeList.stream()
                .filter(e -> e.getSalary() != null)
                .collect(Collectors.toList());

            double totalIncome = employeesWithSalary.stream()
                .mapToDouble(e -> e.getSalary())
                .sum();
            double avgIncome = employeesWithSalary.isEmpty() ? 0.0 : totalIncome / employeesWithSalary.size();

            List<Credit> groupCredits = allCredits.stream()
                .filter(credit -> {
                    for (Employee emp : employeeList) {
                        if (emp.getId().equals(credit.getEmployeeId())) {
                            return true;
                        }
                    }
                    return false;
                })
                .collect(Collectors.toList());

            long totalCredits = groupCredits.size();

            long approvedCredits = groupCredits.stream()
                .filter(c -> {
                    return c.getDecision() != null && c.getDecision().name().contains("APPROVAL");
                })
                .count();

            double approvalRate = totalCredits > 0 ? (approvedCredits * 100.0 / totalCredits) : 0.0;

            distribution.put(type, new EmploymentTypeStats(count, avgScore, avgIncome, approvalRate));
        }

        return distribution;
    }

    private static String getEmploymentType(Employee emp) {
        if (emp.getContractType() == ContractType.PERMANENT) {
            if (emp.getEmploymentSector() == SectorType.PUBLIC) {
                return "CDI_PUBLIC";
            } else if (emp.getEmploymentSector() == SectorType.PRIVATE_LARGE) {
                return "CDI_PRIVATE_LARGE";
            } else if (emp.getEmploymentSector() == SectorType.SME) {
                return "CDI_SME";
            }
        } else if (emp.getContractType() == ContractType.TEMPORARY) {
            return "CDD";
        } else if (emp.getContractType() == ContractType.INTERIM) {
            return "INTERIM";
        }
        return "OTHER";
    }

    public static class EmploymentTypeStats {
        private int count;
        private double avgScore;
        private double avgIncome;
        private double approvalRate;

        public EmploymentTypeStats(int count, double avgScore, double avgIncome, double approvalRate) {
            this.count = count;
            this.avgScore = avgScore;
            this.avgIncome = avgIncome;
            this.approvalRate = approvalRate;
        }

        public int getCount() {
            return count;
        }

        public double getAvgScore() {
            return avgScore;
        }

        public double getAvgIncome() {
            return avgIncome;
        }

        public double getApprovalRate() {
            return approvalRate;
        }
    }
}
