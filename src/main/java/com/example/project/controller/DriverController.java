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

    @GetMapping("/details/{index}")
    public String getDetails(Model model, @PathVariable int index) {
        Driver driver = driverService.getDriverById(index);
        if (driver != null) {
            model.addAttribute("driver", driver);
            return "driver_details";
        }
        return "redirect:/drivers/";
    }

    @GetMapping("/delete/{index}")
    public String delete(@PathVariable int index) {
        driverService.removeDriverById(index);
        return "redirect:/drivers/";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("driver",  new Driver());
        model.addAttribute("edit", false);
        return "driver_edit";
    }

    @PostMapping("/save")
    public String save(@Valid Driver driver, Model model, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("edit", true);
            return "driver_edit";
        }
        driverService.saveNewDriver(driver);
        return "redirect:/drivers/";
    }

    @GetMapping("/edit/{index}")
    public String delete(Model model, @PathVariable int index) {
        Driver driver = driverService.getDriverById(index);
        if (driver != null) {
            driver.setId(index);
            model.addAttribute("driver", driver);
            model.addAttribute("edit", true);
            return "driver_edit";
        }
        return "redirect:/drivers/";
    }
}
