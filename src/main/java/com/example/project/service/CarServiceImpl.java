package com.example.project.service;

import com.example.project.model.Car;
import com.example.project.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarServiceImpl implements CarService {
    CarRepository carRepository;

    @Autowired
    public CarServiceImpl(final CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Override
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    @Override
    public Car getCarById(final long id) {
        return carRepository.findById(id).orElse(null);
    }

    @Override
    public void saveNewCar(final Car car) {
        carRepository.save(car);
    }

    @Override
    public void removeCarById(final long id) {
        carRepository.findById(id).ifPresent(value -> {
            carRepository.delete(value);
        });
    }
}
