package Models;

import Enums.FamilyStatus;
import Enums.SectorType;
import Services.ScoringService;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Professional extends Person {
    private Double income;
    private String taxRegistrationNumber;
    private SectorType businessSector;
    private String activity;

    public Professional() {}

    public Professional(Integer id, String firstName, String lastName, LocalDate dateOfBirth, String city,
                       Boolean investment, Boolean placement, Integer childrenCount, FamilyStatus familyStatus,
                       Double score, Double income, String taxRegistrationNumber, SectorType businessSector,
                       String activity, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.city = city;
        this.investment = investment;
        this.placement = placement;
        this.childrenCount = childrenCount;
        this.familyStatus = familyStatus;
        this.score = score;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.income = income;
        this.taxRegistrationNumber = taxRegistrationNumber;
        this.businessSector = businessSector;
        this.activity = activity;
    }

    public Double getIncome() {
        return income;
    }

    public void setIncome(Double income) {
        this.income = income;
    }

    public String getTaxRegistrationNumber() {
        return taxRegistrationNumber;
    }

    public void setTaxRegistrationNumber(String taxRegistrationNumber) {
        this.taxRegistrationNumber = taxRegistrationNumber;
    }

    public SectorType getBusinessSector() {
        return businessSector;
    }

    public void setBusinessSector(SectorType businessSector) {
        this.businessSector = businessSector;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public static Professional findById(int professionalId) {
        String sql = "SELECT * FROM Professional WHERE id = ?";

        return withStatement(sql, stmt -> {
            stmt.setInt(1, professionalId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Professional(
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
                        rs.getDouble("income"),
                        rs.getString("taxRegistrationNumber"),
                        SectorType.valueOf(rs.getString("businessSector")),
                        rs.getString("activity"),
                        rs.getObject("createdAt", LocalDateTime.class),
                        rs.getObject("updatedAt", LocalDateTime.class)
                );
            }
            return null;
        });
    }

    public static boolean delete(Integer professionalId) {
        String deleteSql = "DELETE FROM Professional WHERE id = ?";

        return withStatement(deleteSql, stmt -> {
            stmt.setInt(1, professionalId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        });
    }

    public static boolean update(Professional professional) {
        String updateSql = "UPDATE Professional SET firstName = ?, lastName = ?, dateOfBirth = ?, city = ?, investment = ?, placement = ?, childrenCount = ?, familyStatus = ?::FamilyStatus, income = ?, taxRegistrationNumber = ?, businessSector = ?::SectorType, activity = ?, updatedAt = ? WHERE id = ?";

        return withStatement(updateSql, stmt -> {
            stmt.setString(1, professional.getFirstName());
            stmt.setString(2, professional.getLastName());
            stmt.setObject(3, professional.getDateOfBirth());
            stmt.setString(4, professional.getCity());
            stmt.setBoolean(5, professional.getInvestment());
            stmt.setBoolean(6, professional.getPlacement());
            stmt.setInt(7, professional.getChildrenCount());
            stmt.setString(8, professional.getFamilyStatus().name());
            stmt.setDouble(9, professional.getIncome());
            stmt.setString(10, professional.getTaxRegistrationNumber());
            stmt.setString(11, professional.getBusinessSector().name());
            stmt.setString(12, professional.getActivity());
            stmt.setObject(13, professional.getUpdatedAt());
            stmt.setInt(14, professional.getId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        });
    }


    public static List<Professional> getAll() {
        String sql = "SELECT * FROM Professional";

        return withStatement(sql, stmt -> {
            ResultSet rs = stmt.executeQuery();
            List<Professional> professionals = new ArrayList<>();
            while (rs.next()) {
                professionals.add(
                        new Professional(
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
                                rs.getDouble("income"),
                                rs.getString("taxRegistrationNumber"),
                                SectorType.valueOf(rs.getString("businessSector")),
                                rs.getString("activity"),
                                rs.getObject("createdAt", LocalDateTime.class),
                                rs.getObject("updatedAt", LocalDateTime.class)
                        )
                );
            }
            return professionals;
        });
    }

    public static Professional create(String firstName, String lastName, LocalDate dateOfBirth, String city,
                                    Boolean investment, Boolean placement, Integer childrenCount, FamilyStatus familyStatus,
                                    Double income, String taxRegistrationNumber, SectorType businessSector, String activity) {
        String insertSql = "INSERT INTO Professional (firstName, lastName, dateOfBirth, city, investment, placement, childrenCount, familyStatus, income, taxRegistrationNumber, businessSector, activity) VALUES (?, ?, ?, ?, ?, ?, ?, ?::FamilyStatus, ?, ?, ?::SectorType, ?)";

        return withStatementReturning(insertSql, stmt -> {
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setObject(3, dateOfBirth);
            stmt.setString(4, city);
            stmt.setBoolean(5, investment != null ? investment : false);
            stmt.setBoolean(6, placement != null ? placement : false);
            stmt.setInt(7, childrenCount != null ? childrenCount : 0);
            stmt.setString(8, familyStatus.name());
            stmt.setDouble(9, income);
            stmt.setString(10, taxRegistrationNumber);
            stmt.setString(11, businessSector.name());
            stmt.setString(12, activity);

            stmt.executeUpdate();
            java.sql.ResultSet keys = stmt.getGeneratedKeys();

            if (keys.next()) {
                Integer professionalId = keys.getInt(1);

                Professional professional = new Professional();
                professional.setId(professionalId);
                professional.setFirstName(firstName);
                professional.setLastName(lastName);
                professional.setDateOfBirth(dateOfBirth);
                professional.setCity(city);
                professional.setInvestment(investment != null ? investment : false);
                professional.setPlacement(placement != null ? placement : false);
                professional.setChildrenCount(childrenCount != null ? childrenCount : 0);
                professional.setFamilyStatus(familyStatus);
                professional.setIncome(income);
                professional.setTaxRegistrationNumber(taxRegistrationNumber);
                professional.setBusinessSector(businessSector);
                professional.setActivity(activity);
                professional.setCreatedAt(LocalDateTime.now());
                professional.setUpdatedAt(LocalDateTime.now());

                double initialScore = ScoringService.calculateScore(professional);
                professional.setScore(initialScore);

                String updateScoreSql = "UPDATE Professional SET score = ? WHERE id = ?";
                withStatement(updateScoreSql, updateStmt -> {
                    updateStmt.setDouble(1, initialScore);
                    updateStmt.setInt(2, professionalId);
                    updateStmt.executeUpdate();
                    return null;
                });

                return professional;
            }
            throw new RuntimeException("Failed to create professional: no ID returned");
        });
    }
}
