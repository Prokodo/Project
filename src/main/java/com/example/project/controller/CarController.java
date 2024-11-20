package com.example.project.controller;

import com.example.project.model.Car;
import com.example.project.service.CarService;
import com.example.project.service.DriverService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cars")
public class CarController {
    private final CarService carService;
    private final DriverService driverService;

    @Autowired
    public CarController(final CarService carService, final DriverService driverService) {
        this.carService = carService;
        this.driverService = driverService;
    }

    @GetMapping("/")
    public String getCars(Model model) {
        model.addAttribute("cars",  carService.getAllCars());
        return "car_list";
    }

    @GetMapping("/details/{id}")
    public String getDetails(Model model, @PathVariable long id) {
        Car car = carService.getCarById(id);
        if (car != null) {
            model.addAttribute("car", car);
            return "car_details";
        }
        return "redirect:/cars/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable long id) {
        carService.removeCarById(id);
        return "redirect:/cars/";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("car",  new Car());
        model.addAttribute("edit", false);
        model.addAttribute("drivers", driverService.getAllDrivers());
        return "car_edit";
    }

    @PostMapping("/save")
    public String save(@Valid Car car, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("edit", true);
            return "car_edit";
        }
        carService.saveNewCar(car);
        return "redirect:/cars/";
    }

    @GetMapping("/edit/{id}")
    public String delete(Model model, @PathVariable long id) {
        Car car = carService.getCarById(id);
        if (car != null) {
            model.addAttribute("car", car);
            model.addAttribute("edit", true);
            model.addAttribute("drivers", driverService.getAllDrivers());
            return "car_edit";
        }
        return "redirect:/cars/";
    }
}
