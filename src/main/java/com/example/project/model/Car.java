package com.example.project.model;

public class Car {
    private int id;
    private String spz;
    private String color;
    private int numberOfSeats;
    private float tankCapacity;

    public Car() {

    }

    public Car(String spz, String color, int numberOfSeats, float tankCapacity) {
        this.id = -1;
        this.spz = spz;
        this.color = color;
        this.tankCapacity = tankCapacity;
        this.numberOfSeats = numberOfSeats;
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

    public void setId(int id) {
        this.id = id;
    }

    public void setSpz(String spz) {
        this.spz = spz;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public void setTankCapacity(float tankCapacity) {
        this.tankCapacity = tankCapacity;
    }
}
