package org.acme.dto;

import java.time.Instant;

public class FavoriteDTO {
    public Integer id;
    public Long userId;
    public Integer listingId;
    public String listingTitle;
    public Instant savedAt;
}
