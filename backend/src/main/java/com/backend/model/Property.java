package com.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "property")
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private Byte[] image;

    @NotBlank(message = "Property name cannot be blank.")
    @Size(max = 100, message = "Property name must be at most 100 characters.")
    private String name;

    @NotBlank(message = "Property type cannot be blank.")
    @Size(max = 50, message = "Property type must be at most 50 characters.")
    private String type;

    @NotNull(message = "Price cannot be null.")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0.")
    private Float price;

    @NotBlank(message = "Address cannot be blank.")
    @Size(max = 255, message = "Address must be at most 255 characters.")
    private String address;

    @Size(max = 500, message = "Description must be at most 500 characters.")
    private String description;

    @JsonIgnore
    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private final List<Request> requests = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private final List<Contract> contracts = new ArrayList<>();

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- CONSTRUCTORS -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= */

    public Property() {}

    public Property(final long id, final String name, final String type, final Float price, final String address, final String description) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.price = price;
        this.address = address;
        this.description = description;
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- GETTERS -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- */

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Float getPrice() {
        return price;
    }

    public Byte[] getImage() {
        return image;
    }

    public String getAddress() {
        return address;
    }

    public String getDescription() {
        return description;
    }

    public List<Contract> getContracts() {
        return contracts;
    }

    public List<Request> getRequests() {
        return requests;
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- SETTERS -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- */

    public void setId(final Long id) {
        this.id = id;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public void setPrice(final Float price) {
        this.price = price;
    }

    public void setImage(final Byte[] image) {
        this.image = image;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public void setDescription(final String description) {
        this.description = description;
    }
}
