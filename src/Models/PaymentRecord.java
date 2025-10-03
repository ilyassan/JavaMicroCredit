package Models;

import Enums.PaymentStatusEnum;
import java.time.LocalDateTime;

public class PaymentRecord {
    private Integer id;
    private Integer installementId;
    private PaymentStatusEnum status;
    private LocalDateTime createdAt;

    public PaymentRecord() {}

    public PaymentRecord(Integer id, Integer installementId, PaymentStatusEnum status, LocalDateTime createdAt) {
        this.id = id;
        this.installementId = installementId;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getInstallementId() {
        return installementId;
    }

    public void setInstallementId(Integer installementId) {
        this.installementId = installementId;
    }

    public PaymentStatusEnum getStatus() {
        return status;
    }

    public void setStatus(PaymentStatusEnum status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}