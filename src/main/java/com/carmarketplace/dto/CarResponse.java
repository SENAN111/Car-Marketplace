package com.carmarketplace.dto;

import com.carmarketplace.entity.enums.FuelType;
import com.carmarketplace.entity.enums.Transmission;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CarResponse {

    private Long id;
    private String brand;
    private String model;
    private Integer year;
    private BigDecimal price;
    private Long mileage;
    private FuelType fuelType;
    private Transmission transmission;
    private String color;
    private String description;
    private String ownerName;
}