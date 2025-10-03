package Models;

import java.time.LocalDate;

public class Installement {
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
}