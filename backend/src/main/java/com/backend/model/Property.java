package com.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Property")
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String type;
    private String address;
    private String description;
    private String propertyValue;

    public Property() {}

    public Property(final long id, final String name, final String type, final String value, final String address, final String description) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.propertyValue = value;
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

    public String getValue() {
        return propertyValue;
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

    public void setValue(final String value) {
        this.propertyValue = value;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public void setDescription(final String description) {
        this.description = description;
    }
}
