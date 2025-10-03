package Repositories;

import Enums.ContractType;
import Enums.FamilyStatus;
import Enums.SectorType;
import Models.Employee;
import Services.ScoringService;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EmployeeRepository extends BaseRepository {

    public static List<Employee> getAll() {
        String sql = "SELECT * FROM Employee";

        return withStatement(sql, stmt -> {
            ResultSet rs = stmt.executeQuery();
            List<Employee> employees = new ArrayList<>();
            while (rs.next()) {
                employees.add(
                        new Employee(
                                rs.getInt("id"),
                                rs.getString("firstName"),
                                rs.getString("lastName"),
                                rs.getObject("dateOfBirth", LocalDate.class),
                                rs.getString("city"),
                                rs.getBoolean("investment"),
                                rs.getBoolean("placement"),
                                rs.getInt("childrenCount"),
                                FamilyStatus.valueOf(rs.getString("familyStatus")),
                                rs.getDouble("score"),
                                rs.getDouble("salary"),
                                rs.getInt("monthsInWork"),
                                rs.getString("position"),
                                ContractType.valueOf(rs.getString("contractType")),
                                SectorType.valueOf(rs.getString("employmentSector")),
                                rs.getObject("createdAt", LocalDateTime.class),
                                rs.getObject("updatedAt", LocalDateTime.class)
                        )
                );
            }
            return employees;
        });
    }

    public static Employee findById(int employeeId) {
        return getAll().stream()
            .filter(emp -> emp.getId() == employeeId)
            .findFirst()
            .orElse(null);
    }

    public static boolean delete(Integer employeeId) {
        String deleteSql = "DELETE FROM Employee WHERE id = ?";

        return withStatement(deleteSql, stmt -> {
            stmt.setInt(1, employeeId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        });
    }

    public static boolean update(Employee employee) {
        String updateSql = "UPDATE Employee SET firstName = ?, lastName = ?, dateOfBirth = ?, city = ?, investment = ?, placement = ?, childrenCount = ?, familyStatus = ?::FamilyStatus, salary = ?, monthsInWork = ?, position = ?, contractType = ?::ContractType, employmentSector = ?::SectorType, updatedAt = ? WHERE id = ?";

        return withStatement(updateSql, stmt -> {
            stmt.setString(1, employee.getFirstName());
            stmt.setString(2, employee.getLastName());
            stmt.setObject(3, employee.getDateOfBirth());
            stmt.setString(4, employee.getCity());
            stmt.setBoolean(5, employee.getInvestment());
            stmt.setBoolean(6, employee.getPlacement());
            stmt.setInt(7, employee.getChildrenCount());
            stmt.setString(8, employee.getFamilyStatus().name());
            stmt.setDouble(9, employee.getSalary());
            stmt.setInt(10, employee.getMonthsInWork());
            stmt.setString(11, employee.getPosition());
            stmt.setString(12, employee.getContractType().name());
            stmt.setString(13, employee.getEmploymentSector().name());
            stmt.setObject(14, employee.getUpdatedAt());
            stmt.setInt(15, employee.getId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        });
    }

    public static Employee create(String firstName, String lastName, LocalDate dateOfBirth, String city,
                                Boolean investment, Boolean placement, Integer childrenCount, FamilyStatus familyStatus,
                                Double salary, Integer monthsInWork, String position, ContractType contractType, SectorType employmentSector) {
        String insertSql = "INSERT INTO Employee (firstName, lastName, dateOfBirth, city, investment, placement, childrenCount, familyStatus, salary, monthsInWork, position, contractType, employmentSector) VALUES (?, ?, ?, ?, ?, ?, ?, ?::FamilyStatus, ?, ?, ?, ?::ContractType, ?::SectorType)";

        return withStatementReturning(insertSql, stmt -> {
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setObject(3, dateOfBirth);
            stmt.setString(4, city);
            stmt.setBoolean(5, investment != null ? investment : false);
            stmt.setBoolean(6, placement != null ? placement : false);
            stmt.setInt(7, childrenCount != null ? childrenCount : 0);
            stmt.setString(8, familyStatus.name());
            stmt.setDouble(9, salary);
            stmt.setInt(10, monthsInWork);
            stmt.setString(11, position);
            stmt.setString(12, contractType.name());
            stmt.setString(13, employmentSector.name());

            stmt.executeUpdate();
            ResultSet keys = stmt.getGeneratedKeys();

            if (keys.next()) {
                Integer employeeId = keys.getInt(1);

                Employee employee = new Employee();
                employee.setId(employeeId);
                employee.setFirstName(firstName);
                employee.setLastName(lastName);
                employee.setDateOfBirth(dateOfBirth);
                employee.setCity(city);
                employee.setInvestment(investment != null ? investment : false);
                employee.setPlacement(placement != null ? placement : false);
                employee.setChildrenCount(childrenCount != null ? childrenCount : 0);
                employee.setFamilyStatus(familyStatus);
                employee.setSalary(salary);
                employee.setMonthsInWork(monthsInWork);
                employee.setPosition(position);
                employee.setContractType(contractType);
                employee.setEmploymentSector(employmentSector);
                employee.setCreatedAt(LocalDateTime.now());
                employee.setUpdatedAt(LocalDateTime.now());

                double initialScore = ScoringService.calculateScore(employee);
                employee.setScore(initialScore);

                String updateScoreSql = "UPDATE Employee SET score = ? WHERE id = ?";
                withStatement(updateScoreSql, updateStmt -> {
                    updateStmt.setDouble(1, initialScore);
                    updateStmt.setInt(2, employeeId);
                    updateStmt.executeUpdate();
                    return null;
                });

                return employee;
            }
            throw new RuntimeException("Failed to create employee: no ID returned");
        });
    }
}
