package com.example.project.controller;

import com.example.project.model.Car;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CarController {
    List<Car> cars = new ArrayList<>();

    public CarController() {
        cars.add(new Car("ABCDE 123", "blue", 20, 20.5F));
    }

    @GetMapping("/cars")
    public String getCars(Model model) {
        model.addAttribute("cars", cars);
        return "cars";
    }
}
