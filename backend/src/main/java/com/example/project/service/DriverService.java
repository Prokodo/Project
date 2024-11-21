package com.example.project.service;

import com.example.project.model.Driver;

import java.util.List;

public interface DriverService {
    List<Driver> getAllDrivers();
    Driver getDriverById(long id);
    void removeDriverById(long id);
    void saveNewDriver(Driver driver);
}
