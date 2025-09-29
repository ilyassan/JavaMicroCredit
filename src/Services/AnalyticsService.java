package Services;

import Models.Employee;
import Models.Professional;
import Models.Person;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Comparator;

public class AnalyticsService {

    public static List<Person> sortByScore(boolean descending) {
        List<Employee> employees = Employee.getAll();
        List<Professional> professionals = Professional.getAll();

        Stream<Person> allClients = Stream.concat(employees.stream(), professionals.stream());

        if (descending) {
            return allClients
                .sorted(Comparator.comparing(Person::getScore, Comparator.nullsLast(Comparator.reverseOrder())))
                .collect(Collectors.toList());
        } else {
            return allClients
                .sorted(Comparator.comparing(Person::getScore, Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());
        }
    }

    public static List<Person> sortByIncome(boolean descending) {
        List<Employee> employees = Employee.getAll();
        List<Professional> professionals = Professional.getAll();

        Stream<Person> allClients = Stream.concat(employees.stream(), professionals.stream());

        Comparator<Person> incomeComparator = (p1, p2) -> {
            double income1 = getIncome(p1);
            double income2 = getIncome(p2);
            return Double.compare(income1, income2);
        };

        if (descending) {
            return allClients
                .sorted(incomeComparator.reversed())
                .collect(Collectors.toList());
        } else {
            return allClients
                .sorted(incomeComparator)
                .collect(Collectors.toList());
        }
    }

    public static List<Person> sortByRelationshipSeniority(boolean descending) {
        List<Employee> employees = Employee.getAll();
        List<Professional> professionals = Professional.getAll();

        Stream<Person> allClients = Stream.concat(employees.stream(), professionals.stream());

        Comparator<Person> seniorityComparator = (p1, p2) -> {
            if (p1.getCreatedAt() == null && p2.getCreatedAt() == null) return 0;
            if (p1.getCreatedAt() == null) return 1;
            if (p2.getCreatedAt() == null) return -1;
            return p1.getCreatedAt().compareTo(p2.getCreatedAt());
        };

        if (descending) {
            return allClients
                .sorted(seniorityComparator.reversed())
                .collect(Collectors.toList());
        } else {
            return allClients
                .sorted(seniorityComparator)
                .collect(Collectors.toList());
        }
    }

    private static double getIncome(Person person) {
        if (person instanceof Employee) {
            return ((Employee) person).getSalary();
        } else if (person instanceof Professional) {
            return ((Professional) person).getIncome();
        }
        return 0;
    }
}