package com.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Property")
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Lob
    private byte[] image;

    private String name;
    private String type;
    private Float price;
    private String address;
    private String description;

    public Property() {}

    public Property(final long id, final String name, final String type, final Float price, final String address, final String description) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.price = price;
        this.address = address;
        this.description = description;
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- */

    public long getId() {
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

    public byte[] getImage() {
        return image;
    }

    public String getAddress() {
        return address;
    }

    public String getDescription() {
        return description;
    }

    public void setId(final long id) {
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

    public void setImage(final byte[] image) {
        this.image = image;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public void setDescription(final String description) {
        this.description = description;
    }
}
