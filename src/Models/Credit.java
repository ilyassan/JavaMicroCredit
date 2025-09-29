package Models;

import Enums.CreditType;
import Enums.DecisionEnum;
import java.time.LocalDate;

public class Credit extends Model {
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
}