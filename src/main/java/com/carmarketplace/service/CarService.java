
package com.carmarketplace.service;

import com.carmarketplace.dto.CarRequest;
import com.carmarketplace.dto.CarResponse;
import com.carmarketplace.entity.Car;
import com.carmarketplace.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.carmarketplace.entity.User;
import com.carmarketplace.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;
    private final UserRepository userRepository;

    public CarResponse createCar(CarRequest request) {

        UserDetails userDetails = (UserDetails)
                SecurityContextHolder.getContext()
                        .getAuthentication().getPrincipal();

        User owner = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Car car = mapToEntity(request);
        car.setOwner(owner);

        Car savedCar = carRepository.save(car);

        return mapToResponse(savedCar);
    }
    public List<CarResponse> getAllCars() {
        return carRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public CarResponse getCarById(Long id) {
        return mapToResponse(findCarById(id));
    }

    public void deleteCar(Long id) {

        Car car = findCarById(id);

        checkOwnershipOrAdmin(car);

        carRepository.delete(car);
    }
    public Car updateCar(Long id, CarRequest request) {

        Car existingCar = findCarById(id);

        checkOwnershipOrAdmin(existingCar);

        existingCar.setBrand(request.getBrand());
        existingCar.setModel(request.getModel());
        existingCar.setYear(request.getYear());
        existingCar.setPrice(request.getPrice());
        existingCar.setMileage(request.getMileage());
        existingCar.setFuelType(request.getFuelType());
        existingCar.setTransmission(request.getTransmission());
        existingCar.setColor(request.getColor());
        existingCar.setDescription(request.getDescription());

        return carRepository.save(existingCar);
    }
    private void checkOwnershipOrAdmin(Car car) {

        var authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String currentEmail = authentication.getName();

        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_ADMIN"));

        boolean isOwner = car.getOwner() != null &&
                car.getOwner().getEmail().equals(currentEmail);

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException(
                    "You are not allowed to modify this car"
            );
        }
    }
    private Car findCarById(Long id) {
        return carRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Car not found with id: " + id));
    }

    private Car mapToEntity(CarRequest request) {
        Car car = new Car();

        car.setBrand(request.getBrand());
        car.setModel(request.getModel());
        car.setYear(request.getYear());
        car.setPrice(request.getPrice());
        car.setMileage(request.getMileage());
        car.setFuelType(request.getFuelType());
        car.setTransmission(request.getTransmission());
        car.setColor(request.getColor());
        car.setDescription(request.getDescription());

        return car;
    }

    private CarResponse mapToResponse(Car car) {
        CarResponse response = new CarResponse();

        response.setId(car.getId());
        response.setBrand(car.getBrand());
        response.setModel(car.getModel());
        response.setYear(car.getYear());
        response.setPrice(car.getPrice());
        response.setMileage(car.getMileage());
        response.setFuelType(car.getFuelType());
        response.setTransmission(car.getTransmission());
        response.setColor(car.getColor());
        response.setDescription(car.getDescription());

        if (car.getOwner() != null) {
            response.setOwnerName(
                    car.getOwner().getFirstName() + " " +
                            car.getOwner().getLastName()
            );
        }

        return response;
    }
}
