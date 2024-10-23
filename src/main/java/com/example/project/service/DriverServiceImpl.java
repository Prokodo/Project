package com.example.project.service;

import com.example.project.model.Driver;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class DriverServiceImpl implements DriverService {
    ArrayList<Driver> drivers = new ArrayList<>();

    @Override
    public ArrayList<Driver> getAllDrivers() {
        return drivers;
    }

    @Override
    public Driver getDriverById(final int id) {
        if (id > -1 && drivers.size() > id) {
            return drivers.get(id);
        }
        return null;
    }

    @Override
    public void saveNewDriver(final Driver driver) {
        if (driver.getId() > -1) {
            drivers.remove(driver.getId());
        }
        drivers.add(driver);
    }

    @Override
    public void removeDriverById(final int id) {
        if (id > -1 && drivers.size() > id) {
            drivers.remove(id);
        }
    }

    @Override
    public int getCount() {
        return drivers.size();
    }
}
