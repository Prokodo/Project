package com.example.project.controller;

import com.example.project.model.Car;
import com.example.project.service.CarService;
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

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("/")
    public String getCars(Model model) {
        model.addAttribute("cars",  carService.getAllCars());
        return "car_list";
    }

    @GetMapping("/details/{index}")
    public String getDetails(Model model, @PathVariable int index) {
        Car car = carService.getCarById(index);
        if (car != null) {
            model.addAttribute("car", car);
            return "car_details";
        }
        return "redirect:/cars/";
    }

    @GetMapping("/delete/{index}")
    public String delete(@PathVariable int index) {
        carService.removeCarById(index);
        return "redirect:/cars/";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("car",  new Car());
        model.addAttribute("edit", false);
        return "car_edit";
    }

    @PostMapping("/save")
    public String save(@Valid Car car, Model model, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("edit", true);
            return "car_edit";
        }
        carService.saveNewCar(car);
        return "redirect:/cars/";
    }

    @GetMapping("/edit/{index}")
    public String delete(Model model, @PathVariable int index) {
        Car car = carService.getCarById(index);
        if (car != null) {
            car.setId(index);
            model.addAttribute("car", car);
            model.addAttribute("edit", true);
            return "car_edit";
        }
        return "redirect:/cars/";
    }
}
