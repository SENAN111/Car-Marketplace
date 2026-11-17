package com.carmarketplace.dto;

import com.carmarketplace.entity.enums.FuelType;
import com.carmarketplace.entity.enums.Transmission;
import jakarta.validation.constraints.*;
import com.carmarketplace.dto.CarRequest;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CarRequest {

    @NotBlank
    private String brand;

    @NotBlank
    private String model;

    @NotNull
    @Min(1886)
    private Integer year;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal price;

    @NotNull
    @PositiveOrZero
    private Long mileage;

    @NotNull
    private FuelType fuelType;

    @NotNull
    private Transmission transmission;

    @NotBlank
    @Size(max = 50)
    private String color;

    @Size(max = 2000)
    private String description;
}