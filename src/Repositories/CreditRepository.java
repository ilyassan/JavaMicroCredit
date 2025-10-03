package Repositories;

import Enums.CreditType;
import Enums.DecisionEnum;
import Models.Credit;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CreditRepository extends BaseRepository {

    public static List<Credit> getAll() {
        String sql = "select * from credit";

        return withStatement(sql, stmt -> {
            ResultSet rs = stmt.executeQuery();
            List<Credit> credits = new ArrayList<>();

            while (rs.next()) {
                credits.add(new Credit(
                        rs.getInt("id"),
                        rs.getObject("employeeId", Integer.class),
                        rs.getObject("professionalId", Integer.class),
                        rs.getObject("creditDate", LocalDate.class),
                        rs.getDouble("requestedAmount"),
                        rs.getDouble("approvedAmount"),
                        rs.getDouble("interestRate"),
                        rs.getInt("durationInMonths"),
                        CreditType.valueOf(rs.getString("creditType")),
                        DecisionEnum.valueOf(rs.getString("decision"))
                ));
            }
            return credits;
        });
    }

    public static Credit create(Integer employeeId, Integer professionalId, LocalDate creditDate,
                               Double requestedAmount, Double approvedAmount, Double interestRate,
                               Integer durationInMonths, CreditType creditType, DecisionEnum decision) {
        String insertSql = "INSERT INTO Credit (employeeId, professionalId, creditDate, requestedAmount, approvedAmount, interestRate, durationInMonths, creditType, decision) VALUES (?, ?, ?, ?, ?, ?, ?, ?::CreditType, ?::DecisionEnum)";

        return withStatementReturning(insertSql, stmt -> {
            stmt.setObject(1, employeeId);
            stmt.setObject(2, professionalId);
            stmt.setObject(3, creditDate);
            stmt.setDouble(4, requestedAmount);
            stmt.setDouble(5, approvedAmount);
            stmt.setDouble(6, interestRate);
            stmt.setInt(7, durationInMonths);
            stmt.setString(8, creditType.name());
            stmt.setString(9, decision.name());

            stmt.executeUpdate();
            java.sql.ResultSet keys = stmt.getGeneratedKeys();

            if (keys.next()) {
                Integer creditId = keys.getInt(1);

                Credit credit = new Credit();
                credit.setId(creditId);
                credit.setEmployeeId(employeeId);
                credit.setProfessionalId(professionalId);
                credit.setCreditDate(creditDate);
                credit.setRequestedAmount(requestedAmount);
                credit.setApprovedAmount(approvedAmount);
                credit.setInterestRate(interestRate);
                credit.setDurationInMonths(durationInMonths);
                credit.setCreditType(creditType);
                credit.setDecision(decision);

                return credit;
            }
            throw new RuntimeException("Failed to create credit: no ID returned");
        });
    }

    public static List<Credit> findByDecision(DecisionEnum decision) {
        return getAll().stream()
            .filter(c -> c.getDecision() == decision)
            .collect(Collectors.toList());
    }

    public static Credit findById(Integer creditId) {
        return getAll().stream()
            .filter(c -> c.getId().equals(creditId))
            .findFirst()
            .orElse(null);
    }

    public static boolean updateDecisionAndAmount(Integer creditId, DecisionEnum decision, Double approvedAmount) {
        String sql = "UPDATE Credit SET decision = ?::DecisionEnum, approvedAmount = ? WHERE id = ?";

        return withStatement(sql, stmt -> {
            stmt.setString(1, decision.name());
            stmt.setDouble(2, approvedAmount);
            stmt.setInt(3, creditId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        });
    }

    public static List<Credit> getCreditsByProfessionalId(Integer professionalId) {
        return getAll().stream()
            .filter(c -> professionalId.equals(c.getProfessionalId()))
            .sorted(Comparator.comparing(Credit::getCreditDate).reversed())
            .collect(Collectors.toList());
    }

    public static List<Credit> getCreditsByEmployeeId(Integer employeeId) {
        return getAll().stream()
            .filter(c -> employeeId.equals(c.getEmployeeId()))
            .sorted(Comparator.comparing(Credit::getCreditDate).reversed())
            .collect(Collectors.toList());
    }
}
