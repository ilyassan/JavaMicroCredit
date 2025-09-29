package Models;

import Enums.ContractType;
import Enums.FamilyStatus;
import Enums.SectorType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Employee extends Model {
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
    private Double salary;
    private Integer monthsInWork;
    private String position;
    private ContractType contractType;
    private SectorType employmentSector;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Employee() {}

    public Employee(Integer id, String firstName, String lastName, LocalDate dateOfBirth, String city,
                   Boolean investment, Boolean placement, Integer childrenCount, FamilyStatus familyStatus,
                   Double score, Double salary, Integer monthsInWork, String position,
                   ContractType contractType, SectorType employmentSector,
                   LocalDateTime createdAt, LocalDateTime updatedAt) {
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
        this.salary = salary;
        this.monthsInWork = monthsInWork;
        this.position = position;
        this.contractType = contractType;
        this.employmentSector = employmentSector;
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

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public Integer getMonthsInWork() {
        return monthsInWork;
    }

    public void setMonthsInWork(Integer monthsInWork) {
        this.monthsInWork = monthsInWork;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public ContractType getContractType() {
        return contractType;
    }

    public void setContractType(ContractType contractType) {
        this.contractType = contractType;
    }

    public SectorType getEmploymentSector() {
        return employmentSector;
    }

    public void setEmploymentSector(SectorType employmentSector) {
        this.employmentSector = employmentSector;
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

                return employee;
            }
            throw new RuntimeException("Failed to create employee: no ID returned");
        });
    }
}