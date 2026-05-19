package org.acme.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.math.BigDecimal;

@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "don_hang_id", nullable = false)
    private Order order;

    @Column(name = "so_tien", nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "phuong_thuc", nullable = false)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai", nullable = false)
    private PaymentStatus status = PaymentStatus.CHO_XU_LY;

    @Column(name = "ma_giao_dich", length = 200, unique = true)
    private String transactionCode;

    @Column(name = "thong_tin_them", columnDefinition = "JSON")
    private String additionalInfo;

    @Column(name = "ngay_thanh_toan", nullable = false, updatable = false)
    private Instant paymentDate = Instant.now();

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public Instant getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Instant paymentDate) {
        this.paymentDate = paymentDate;
    }

    public enum PaymentMethod {
        TIEN_MAT, CHUYEN_KHOAN, VNPAY, MOMO, ZALOPAY, KHAC
    }

    public enum PaymentStatus {
        CHO_XU_LY, THANH_CONG, THAT_BAI, HOAN_TIEN
    }
}
