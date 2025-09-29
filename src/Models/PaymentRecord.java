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

    public static PaymentRecord getLatestByInstallementId(Integer installementId) {
        String sql = "SELECT * FROM PaymentRecord WHERE installementId = ? ORDER BY createdAt DESC LIMIT 1";

        return withStatement(sql, stmt -> {
            stmt.setInt(1, installementId);
            java.sql.ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new PaymentRecord(
                    rs.getInt("id"),
                    rs.getInt("installementId"),
                    PaymentStatusEnum.valueOf(rs.getString("status")),
                    rs.getObject("createdAt", LocalDateTime.class)
                );
            }
            return null;
        });
    }

    public static java.util.List<PaymentRecord> getPaymentRecordsByClientId(Integer employeeId, Integer professionalId) {
        String sql = "SELECT pr.* FROM PaymentRecord pr " +
                    "INNER JOIN Installement i ON pr.installementId = i.id " +
                    "INNER JOIN Credit c ON i.creditId = c.id " +
                    "WHERE (c.employeeId = ? OR c.professionalId = ?) " +
                    "ORDER BY pr.createdAt DESC";

        return withStatement(sql, stmt -> {
            stmt.setObject(1, employeeId);
            stmt.setObject(2, professionalId);
            java.sql.ResultSet rs = stmt.executeQuery();
            java.util.List<PaymentRecord> records = new java.util.ArrayList<>();

            while (rs.next()) {
                records.add(new PaymentRecord(
                    rs.getInt("id"),
                    rs.getInt("installementId"),
                    PaymentStatusEnum.valueOf(rs.getString("status")),
                    rs.getObject("createdAt", LocalDateTime.class)
                ));
            }
            return records;
        });
    }
}