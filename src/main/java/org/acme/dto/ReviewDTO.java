package org.acme.dto;

import java.time.Instant;

public class ReviewDTO {
    public Integer id;
    public Long reviewerId;
    public String reviewerName;
    public Long sellerId;
    public String sellerName;
    public Integer listingId;
    public Integer rating;
    public String content;
    public Instant createdAt;
}
