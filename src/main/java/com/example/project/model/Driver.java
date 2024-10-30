package com.example.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "Drivers")
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    //@Min(value = 18, message = "Age must be at least 18.")
    //@Max(value = 99, message = "Age must be lower than 99.")
    private int age;

    //@Min(value = 10000)
    private int salary;

    //@NotBlank(message = "Name is required.")
    //@Size(min = 5, message = "Name must be at least 5 characters long.")
    private String name;

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(final int age) {
        this.age = age;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(final int salary) {
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
