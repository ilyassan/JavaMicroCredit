package Models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Installement extends Model {
    private Integer id;
    private Integer creditId;
    private LocalDate dueDate;
    private Double amount;

    public Installement() {}

    public Installement(Integer id, Integer creditId, LocalDate dueDate, Double amount) {
        this.id = id;
        this.creditId = creditId;
        this.dueDate = dueDate;
        this.amount = amount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCreditId() {
        return creditId;
    }

    public void setCreditId(Integer creditId) {
        this.creditId = creditId;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public static Installement create(Integer creditId, LocalDate dueDate, Double amount) {
        String insertSql = "INSERT INTO Installement (creditId, dueDate, amount) VALUES (?, ?, ?)";

        return withStatementReturning(insertSql, stmt -> {
            stmt.setInt(1, creditId);
            stmt.setObject(2, dueDate);
            stmt.setDouble(3, amount);

            stmt.executeUpdate();
            java.sql.ResultSet keys = stmt.getGeneratedKeys();

            if (keys.next()) {
                Integer installementId = keys.getInt(1);

                Installement installement = new Installement();
                installement.setId(installementId);
                installement.setCreditId(creditId);
                installement.setDueDate(dueDate);
                installement.setAmount(amount);

                return installement;
            }
            throw new RuntimeException("Failed to create installement: no ID returned");
        });
    }

    public static List<Installement> generateInstallements(Credit credit) {
        List<Installement> installements = new ArrayList<>();

        if (credit.getApprovedAmount() == null || credit.getApprovedAmount() <= 0) {
            return installements;
        }

        Double principal = credit.getApprovedAmount();
        Double interestRate = credit.getInterestRate();
        Integer months = credit.getDurationInMonths();

        Double totalInterest = principal * (interestRate / 100);
        Double totalAmount = principal + totalInterest;
        Double monthlyPayment = totalAmount / months;

        LocalDate firstDueDate = credit.getCreditDate().plusMonths(1);

        for (int i = 0; i < months; i++) {
            LocalDate dueDate = firstDueDate.plusMonths(i);
            Installement installement = Installement.create(credit.getId(), dueDate, monthlyPayment);
            installements.add(installement);
        }

        return installements;
    }
}