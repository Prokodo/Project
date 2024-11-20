package com.example.project.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Cars")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long driver_id;

    //@Size(min = 7, max = 7)
    private String spz;
    //@NotBlank
    private String color;
    //@Min(value = 1)
    private int numberOfSeats;
    //@Size(min = 30, max = 70)
    private float tankCapacity;

    @ManyToOne
    @JoinColumn(name = "driver")
    private Driver driver;

    public Driver getDriver() {
        return driver;
    }

    public long getId() {
        return driver_id;
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

    public void setDriver_id(final long id) {
        this.driver_id = id;
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
