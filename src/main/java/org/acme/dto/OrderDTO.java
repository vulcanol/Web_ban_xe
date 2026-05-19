package org.acme.dto;

import java.time.Instant;
import java.math.BigDecimal;

public class OrderDTO {
    public Integer id;
    public Long buyerId;
    public String buyerName;
    public Integer listingId;
    public String listingTitle;
    public BigDecimal transactionPrice;
    public String status;
    public String notes;
    public Instant createdAt;
    public Instant updatedAt;
}
