package com.example.project.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class Car {
    private int id;

    @Size(min = 7, max = 7)
    private String spz;
    @NotBlank
    private String color;
    @Min(value = 1)
    private int numberOfSeats;
    @Size(min = 30, max = 70)
    private float tankCapacity;

    public Car() {
        this.id = -1;
    }

    public int getId() {
        return id;
    }

    public String getSpz() {
        return spz;
    }

    public String getColor() {
        return color;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public float getTankCapacity() {
        return tankCapacity;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public void setSpz(final String spz) {
        this.spz = spz;
    }

    public void setColor(final String color) {
        this.color = color;
    }

    public void setNumberOfSeats(final int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public void setTankCapacity(final float tankCapacity) {
        this.tankCapacity = tankCapacity;
    }
}
