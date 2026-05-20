package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.domain.Brand;
import org.acme.dto.BrandDTO;
import org.acme.repository.BrandRepository;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class BrandService {

    @Inject
    BrandRepository brandRepository;

    public List<BrandDTO> getAllBrands() {
        return brandRepository.listAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<BrandDTO> getActiveBrands() {
        return brandRepository.findAllActive().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<Brand> getAllBrandEntities() {
        return brandRepository.listAll();
    }

    public long countBrands() {
        return brandRepository.count();
    }

    @Transactional
    public Brand createBrand(String name, String country, String logoUrl) {
        Brand brand = new Brand();
        brand.setName(name);
        brand.setCountry(country);
        brand.setLogoUrl(logoUrl);
        brandRepository.persist(brand);
        return brand;
    }

    private BrandDTO convertToDTO(Brand brand) {
        BrandDTO dto = new BrandDTO();
        dto.id = brand.getId();
        dto.name = brand.getName();
        dto.country = brand.getCountry();
        dto.logoUrl = brand.getLogoUrl();
        dto.isActive = brand.getIsActive();
        return dto;
    }
}
