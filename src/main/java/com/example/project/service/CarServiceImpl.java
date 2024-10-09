package com.example.project.service;

import com.example.project.model.Car;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CarServiceImpl implements CarService {
    ArrayList<Car> cars = new ArrayList<>();

    @Override
    public ArrayList<Car> getAllCars() {
        return cars;
    }

    @Override
    public Car getCarById(final int id) {
        if (id > -1 && cars.size() > id) {
            return cars.get(id);
        }
        return null;
    }

    @Override
    public void removeCarById(final int id) {
        if (id > -1 && cars.size() > id) {
            cars.remove(id);
        }
    }

    @Override
    public void saveCar(final Car car) {
        if (car.getId() > -1) {
            cars.remove(car.getId());
        }
        cars.add(car);
    }
}
