package com.example.project.controller;

import com.example.project.model.Driver;
import com.example.project.service.DriverService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/drivers")
public class DriverController {
    private final DriverService driverService;

    @Autowired
    public DriverController(DriverService driverService) {
        this.driverService = driverService;
        this.driverService.getAllDrivers().add(new Driver());
    }

    @GetMapping("/")
    public String getDrivers(Model model) {
        model.addAttribute("drivers",  driverService.getAllDrivers());
        return "driver_list";
    }

    @GetMapping("/details/{id}")
    public String getDetails(Model model, @PathVariable long id) {
        Driver driver = driverService.getDriverById(id);
        if (driver != null) {
            model.addAttribute("driver", driver);
            return "driver_details";
        }
        return "redirect:/drivers/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable long id) {
        driverService.removeDriverById(id);
        return "redirect:/drivers/";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("driver", new Driver());
        model.addAttribute("edit", false);
        return "driver_edit";
    }

    @PostMapping("/save")
    public String save(@Valid Driver driver, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("edit", true);
            return "driver_edit";
        }
        driverService.saveNewDriver(driver);
        return "redirect:/drivers/";
    }

    @GetMapping("/edit/{id}")
    public String delete(Model model, @PathVariable long id) {
        Driver driver = driverService.getDriverById(id);
        if (driver != null) {
            model.addAttribute("driver", driver);
            model.addAttribute("edit", true);
            return "driver_edit";
        }
        return "redirect:/drivers/";
    }
}
