package com.example.project.service;

import com.example.project.model.Car;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CarService {
    List<Car> getAllCars();
    Car getCarById(long id);
    void removeCarById(long id);
    void saveNewCar(Car car);
}
