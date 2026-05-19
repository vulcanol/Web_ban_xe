package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.domain.*;
import org.acme.dto.CarDTO;
import org.acme.repository.BrandRepository;
import org.acme.repository.CarRepository;
import org.acme.repository.CategoryRepository;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class CarService {

    @Inject
    CarRepository carRepository;

    @Inject
    BrandRepository brandRepository;

    @Inject
    CategoryRepository categoryRepository;

    public List<CarDTO> getAllCars() {
        return carRepository.listAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<CarDTO> getCarsByBrand(Long brandId) {
        return carRepository.findByBrand(brandId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<CarDTO> getCarsByCategory(Long categoryId) {
        return carRepository.findByCategory(categoryId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public Car createCar(Long brandId, Long categoryId, String name, Integer year,
            String fuelType, String transmission, Integer seats, String engine,
            Float horsePower, Float displacement, String design) {
        Brand brand = brandRepository.findById(brandId);
        Category category = categoryRepository.findById(categoryId);

        if (brand == null || category == null) {
            throw new IllegalArgumentException("Brand hoặc Category không tồn tại");
        }

        Car car = new Car();
        car.setBrand(brand);
        car.setCategory(category);
        car.setName(name);
        car.setYearManufactured(year);
        car.setFuelType(Car.FuelType.valueOf(fuelType));
        car.setTransmission(Car.TransmissionType.valueOf(transmission));
        car.setSeats(seats);
        car.setEngine(engine);
        car.setHorsePower(horsePower);
        car.setDisplacement(displacement);
        car.setDesign(design);

        carRepository.persist(car);
        return car;
    }

    private CarDTO convertToDTO(Car car) {
        CarDTO dto = new CarDTO();
        dto.id = car.getId();
        dto.brandId = car.getBrand().getId();
        dto.brandName = car.getBrand().getName();
        dto.categoryId = car.getCategory().getId();
        dto.categoryName = car.getCategory().getName();
        dto.name = car.getName();
        dto.yearManufactured = car.getYearManufactured();
        dto.fuelType = car.getFuelType().name();
        dto.transmission = car.getTransmission().name();
        dto.seats = car.getSeats();
        dto.engine = car.getEngine();
        dto.horsePower = car.getHorsePower();
        dto.displacement = car.getDisplacement();
        dto.design = car.getDesign();
        return dto;
    }
}
