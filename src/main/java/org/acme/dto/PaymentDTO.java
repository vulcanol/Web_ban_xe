package org.acme.dto;

import java.time.Instant;
import java.math.BigDecimal;

public class PaymentDTO {
    public Integer id;
    public Integer orderId;
    public BigDecimal amount;
    public String paymentMethod;
    public String status;
    public String transactionCode;
    public String additionalInfo;
    public Instant paymentDate;
}
