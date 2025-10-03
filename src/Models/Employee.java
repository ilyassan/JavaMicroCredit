package Models;

import Enums.ContractType;
import Enums.FamilyStatus;
import Enums.SectorType;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Employee extends Person {
    private Double salary;
    private Integer monthsInWork;
    private String position;
    private ContractType contractType;
    private SectorType employmentSector;

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
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.salary = salary;
        this.monthsInWork = monthsInWork;
        this.position = position;
        this.contractType = contractType;
        this.employmentSector = employmentSector;
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
}
