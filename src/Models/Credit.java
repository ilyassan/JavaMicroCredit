package Models;

import Enums.CreditType;
import Enums.DecisionEnum;

import java.time.LocalDate;

public class Credit {
    private Integer id;
    private Integer employeeId;
    private Integer professionalId;
    private LocalDate creditDate;
    private Double requestedAmount;
    private Double approvedAmount;
    private Double interestRate;
    private Integer durationInMonths;
    private CreditType creditType;
    private DecisionEnum decision;

    public Credit() {}

    public Credit(Integer id, Integer employeeId, Integer professionalId, LocalDate creditDate,
                  Double requestedAmount, Double approvedAmount, Double interestRate, Integer durationInMonths,
                  CreditType creditType, DecisionEnum decision) {
        this.id = id;
        this.employeeId = employeeId;
        this.professionalId = professionalId;
        this.creditDate = creditDate;
        this.requestedAmount = requestedAmount;
        this.approvedAmount = approvedAmount;
        this.interestRate = interestRate;
        this.durationInMonths = durationInMonths;
        this.creditType = creditType;
        this.decision = decision;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getProfessionalId() {
        return professionalId;
    }

    public void setProfessionalId(Integer professionalId) {
        this.professionalId = professionalId;
    }

    public LocalDate getCreditDate() {
        return creditDate;
    }

    public void setCreditDate(LocalDate creditDate) {
        this.creditDate = creditDate;
    }

    public Double getRequestedAmount() {
        return requestedAmount;
    }

    public void setRequestedAmount(Double requestedAmount) {
        this.requestedAmount = requestedAmount;
    }

    public Double getApprovedAmount() {
        return approvedAmount;
    }

    public void setApprovedAmount(Double approvedAmount) {
        this.approvedAmount = approvedAmount;
    }

    public Double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }

    public Integer getDurationInMonths() {
        return durationInMonths;
    }

    public void setDurationInMonths(Integer durationInMonths) {
        this.durationInMonths = durationInMonths;
    }

    public CreditType getCreditType() {
        return creditType;
    }

    public void setCreditType(CreditType creditType) {
        this.creditType = creditType;
    }

    public DecisionEnum getDecision() {
        return decision;
    }

    public void setDecision(DecisionEnum decision) {
        this.decision = decision;
    }

    public boolean isForEmployee() {
        return employeeId != null;
    }

    public boolean isForProfessional() {
        return professionalId != null;
    }
}