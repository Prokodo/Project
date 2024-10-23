package com.example.project.service;

import com.example.project.model.Driver;

import java.util.ArrayList;

public interface DriverService {
    ArrayList<Driver> getAllDrivers();
    Driver getDriverById(int id);
    void removeDriverById(int id);
    void saveNewDriver(Driver driver);
    int getCount();
}
