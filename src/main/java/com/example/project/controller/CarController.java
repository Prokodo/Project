package com.example.project.controller;

import com.example.project.model.Car;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CarController {
    List<Car> cars = new ArrayList<>();

    public CarController() {
        cars.add(new Car("ABCDE 123", "blue", 20, 20.5F));

    }

    @GetMapping("/")
    public String getCars(Model model) {
        model.addAttribute("cars", cars);
        return "cars";
    }

    @GetMapping("/details/{index}")
    public String getDetails(Model model, @PathVariable int index) {
        if (index > -1 && cars.size() > index) {
            Car car = cars.get(index);
            model.addAttribute("car", car);
            return "details";
        }
        return "redirect:/";
    }

    @GetMapping("/delete/{index}")
    public String delete(@PathVariable int index) {
        if (index > -1 && cars.size() > index) {
            cars.remove(index);
        }
        return "redirect:/";
    }

    @GetMapping("/create")
    public String create(Model model) {
        Car car = new Car();
        cars.add(car);
        model.addAttribute("car", car);
        model.addAttribute("edit", false);
        return "edit";
    }

    @GetMapping("/edit/{index}")
    public String delete(Model model, @PathVariable int index) {
        if (index > -1 && cars.size() > index) {
            Car car = cars.get(index);
            car.setId(index);

            model.addAttribute("car", car);
            model.addAttribute("edit", true);
            return "edit";
        }
        return "redirect:/";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Car car) {
        if (car.getId() > -1) {
            cars.remove(car.getId());
        }
        cars.add(car);
        return "redirect:/";
    }
}
