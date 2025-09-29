
package Models;

import Enums.FamilyStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;

public abstract class Person extends Model {
    protected Integer id;
    protected String firstName;
    protected String lastName;
    protected LocalDate dateOfBirth;
    protected String city;
    protected Boolean investment;
    protected Boolean placement;
    protected Integer childrenCount;
    protected FamilyStatus familyStatus;
    protected Double score;
    protected LocalDateTime createdAt;
    protected LocalDateTime updatedAt;

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
}
