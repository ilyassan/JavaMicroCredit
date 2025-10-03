package Models;

import Enums.FamilyStatus;
import Enums.SectorType;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
}
