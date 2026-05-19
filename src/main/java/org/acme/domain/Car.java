package org.acme.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "cars")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "thuong_hieu_id", nullable = false)
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "loai_xe_id", nullable = false)
    private Category category;

    @Column(name = "ten_xe", length = 200, nullable = false)
    private String name;

    @Column(name = "nam_san_xuat", nullable = false)
    private Integer yearManufactured;

    @Enumerated(EnumType.STRING)
    @Column(name = "nhien_lieu", nullable = false)
    private FuelType fuelType = FuelType.XANG;

    @Enumerated(EnumType.STRING)
    @Column(name = "hop_so", nullable = false)
    private TransmissionType transmission = TransmissionType.SO_TU_DONG;

    @Column(name = "so_cho")
    private Integer seats = 5;

    @Column(name = "dong_co", length = 100)
    private String engine;

    @Column(name = "cong_suat")
    private Float horsePower;

    @Column(name = "dung_tich_xy_lanh")
    private Float displacement;

    @Column(name = "kieu_dang", length = 100)
    private String design;

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getYearManufactured() {
        return yearManufactured;
    }

    public void setYearManufactured(Integer yearManufactured) {
        this.yearManufactured = yearManufactured;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }

    public TransmissionType getTransmission() {
        return transmission;
    }

    public void setTransmission(TransmissionType transmission) {
        this.transmission = transmission;
    }

    public Integer getSeats() {
        return seats;
    }

    public void setSeats(Integer seats) {
        this.seats = seats;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public Float getHorsePower() {
        return horsePower;
    }

    public void setHorsePower(Float horsePower) {
        this.horsePower = horsePower;
    }

    public Float getDisplacement() {
        return displacement;
    }

    public void setDisplacement(Float displacement) {
        this.displacement = displacement;
    }

    public String getDesign() {
        return design;
    }

    public void setDesign(String design) {
        this.design = design;
    }

    public enum FuelType {
        XANG, DAU, DIEN, HYBRID, KHAC
    }

    public enum TransmissionType {
        SO_TU_DONG, SO_SAN, CVT, BAN_TU_DONG
    }
}
