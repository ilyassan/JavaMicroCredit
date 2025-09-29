package Models;

import Enums.ContractType;
import Enums.FamilyStatus;
import Enums.SectorType;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Professional extends Model {
    private Integer id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String city;
    private Boolean investment;
    private Boolean placement;
    private Integer childrenCount;
    private FamilyStatus familyStatus;
    private Double score;
    private Double income;
    private String taxRegistrationNumber;
    private SectorType businessSector;
    private String activity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

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
        this.income = income;
        this.taxRegistrationNumber = taxRegistrationNumber;
        this.businessSector = businessSector;
        this.activity = activity;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Boolean getInvestment() {
        return investment;
    }

    public void setInvestment(Boolean investment) {
        this.investment = investment;
    }

    public Boolean getPlacement() {
        return placement;
    }

    public void setPlacement(Boolean placement) {
        this.placement = placement;
    }

    public Integer getChildrenCount() {
        return childrenCount;
    }

    public void setChildrenCount(Integer childrenCount) {
        this.childrenCount = childrenCount;
    }

    public FamilyStatus getFamilyStatus() {
        return familyStatus;
    }

    public void setFamilyStatus(FamilyStatus familyStatus) {
        this.familyStatus = familyStatus;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
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

                return professional;
            }
            throw new RuntimeException("Failed to create professional: no ID returned");
        });
    }
}