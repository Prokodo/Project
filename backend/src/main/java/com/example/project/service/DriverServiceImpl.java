package com.example.project.service;

import com.example.project.model.Driver;
import com.example.project.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverServiceImpl implements DriverService {
    DriverRepository driverRepository;

    @Autowired
    public DriverServiceImpl(final DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    @Override
    public List<Driver> getAllDrivers() {
        return driverRepository.findAll();
    }

    @Override
    public Driver getDriverById(final long id) {
        return driverRepository.findById(id).orElse(null);
    }

    @Override
    public void saveNewDriver(final Driver driver) {
        driverRepository.save(driver);
    }

    @Override
    public void removeDriverById(final long id) {
        driverRepository.findById(id).ifPresent(value -> {
            driverRepository.delete(value);
        });
    }
}
