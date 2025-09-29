package Models;

import Enums.PaymentStatusEnum;
import java.time.LocalDateTime;

public class PaymentRecord extends Model {
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

    public static PaymentRecord create(Integer installementId, PaymentStatusEnum status) {
        String insertSql = "INSERT INTO PaymentRecord (installementId, status) VALUES (?, ?::PaymentStatusEnum)";

        return withStatementReturning(insertSql, stmt -> {
            stmt.setInt(1, installementId);
            stmt.setString(2, status.name());

            stmt.executeUpdate();
            java.sql.ResultSet keys = stmt.getGeneratedKeys();

            if (keys.next()) {
                Integer paymentRecordId = keys.getInt(1);

                PaymentRecord paymentRecord = new PaymentRecord();
                paymentRecord.setId(paymentRecordId);
                paymentRecord.setInstallementId(installementId);
                paymentRecord.setStatus(status);
                paymentRecord.setCreatedAt(LocalDateTime.now());

                return paymentRecord;
            }
            throw new RuntimeException("Failed to create payment record: no ID returned");
        });
    }
}