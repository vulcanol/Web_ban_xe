package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.domain.*;
import org.acme.dto.ListingDTO;
import org.acme.repository.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ListingService {

    @Inject
    ListingRepository listingRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    CarRepository carRepository;

    public List<ListingDTO> getActiveListings() {
        return listingRepository.findActiveListings().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ListingDTO> getListingsByUser(Long userId) {
        return listingRepository.findByUser(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ListingDTO> getListingsByProvince(String province) {
        return listingRepository.findByProvince(province).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ListingDTO> getListingsByPriceRange(long minPrice, long maxPrice) {
        return listingRepository.findByPriceRange(minPrice, maxPrice).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public Listing createListing(Long userId, Long carId, String title, BigDecimal price,
            Integer mileage, String condition, String color, String licensePlate,
            String description, String province, String district) {
        User user = userRepository.findById(userId);
        Car car = carRepository.findById(carId);

        if (user == null || car == null) {
            throw new IllegalArgumentException("Người dùng hoặc Xe không tồn tại");
        }

        Listing listing = new Listing();
        listing.setUser(user);
        listing.setCar(car);
        listing.setTitle(title);
        listing.setPrice(price);
        listing.setMileage(mileage);
        listing.setCondition(Listing.Condition.valueOf(condition));
        listing.setColor(color);
        listing.setLicensePlate(licensePlate);
        listing.setDescription(description);
        listing.setProvince(province);
        listing.setDistrict(district);
        listing.setCreatedAt(Instant.now());
        listing.setUpdatedAt(Instant.now());

        listingRepository.persist(listing);
        return listing;
    }

    @Transactional
    public void incrementViewCount(Long listingId) {
        Listing listing = listingRepository.findById(listingId);
        if (listing != null) {
            listing.setViewCount((listing.getViewCount() != null ? listing.getViewCount() : 0) + 1);
            listing.setUpdatedAt(Instant.now());
            listingRepository.persist(listing);
        }
    }

    private ListingDTO convertToDTO(Listing listing) {
        ListingDTO dto = new ListingDTO();
        dto.id = listing.getId();
        dto.userId = listing.getUser().getId();
        dto.sellerName = listing.getUser().getFullName();
        dto.carId = listing.getCar().getId();
        dto.carName = listing.getCar().getName();
        dto.title = listing.getTitle();
        dto.price = listing.getPrice();
        dto.mileage = listing.getMileage();
        dto.condition = listing.getCondition().name();
        dto.color = listing.getColor();
        dto.licensePlate = listing.getLicensePlate();
        dto.description = listing.getDescription();
        dto.province = listing.getProvince();
        dto.district = listing.getDistrict();
        dto.status = listing.getStatus().name();
        dto.viewCount = listing.getViewCount();
        dto.expiryTime = listing.getExpiryTime();
        dto.createdAt = listing.getCreatedAt();
        dto.updatedAt = listing.getUpdatedAt();
        return dto;
    }
}
