package com.example.project.controller;

import com.example.project.model.Car;
import com.example.project.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CarController {
    private final CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;

        carService.getAllCars().add(new Car("ABCDE 123", "blue", 20, 20.5F));
    }

    @GetMapping("/")
    public String getCars(Model model) {
        model.addAttribute("cars",  carService.getAllCars());
        return "cars";
    }

    @GetMapping("/details/{index}")
    public String getDetails(Model model, @PathVariable int index) {
        Car car = carService.getCarById(index);
        if (car != null) {
            model.addAttribute("car", car);
            return "details";
        }
        return "redirect:/";
    }

    @GetMapping("/delete/{index}")
    public String delete(@PathVariable int index) {
        carService.removeCarById(index);
        return "redirect:/";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("car",  new Car());
        model.addAttribute("edit", false);
        return "edit";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Car car) {
        carService.saveCar(car);
        return "redirect:/";
    }

    @GetMapping("/edit/{index}")
    public String delete(Model model, @PathVariable int index) {
        Car car = carService.getCarById(index);
        if (car != null) {
            car.setId(index);
            model.addAttribute("car", car);
            model.addAttribute("edit", true);
            return "edit";
        }
        return "redirect:/";
    }
}
