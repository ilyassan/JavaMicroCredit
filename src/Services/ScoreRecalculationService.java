package Services;

import Models.Employee;
import Models.Professional;
import java.util.List;

public class ScoreRecalculationService {

    public static void recalculateAllScores() {
        recalculateEmployeeScores();
        recalculateProfessionalScores();
    }

    public static void recalculateEmployeeScores() {
        List<Employee> employees = Employee.getAll();

        for (Employee employee : employees) {
            double newScore = ScoringService.calculateScore(employee);
            employee.setScore(newScore);
            Employee.update(employee);
        }

        System.out.println("Recalculated scores for " + employees.size() + " employees.");
    }

    public static void recalculateProfessionalScores() {
        List<Professional> professionals = Professional.getAll();

        for (Professional professional : professionals) {
            double newScore = ScoringService.calculateScore(professional);
            professional.setScore(newScore);
            Professional.update(professional);
        }

        System.out.println("Recalculated scores for " + professionals.size() + " professionals.");
    }
}