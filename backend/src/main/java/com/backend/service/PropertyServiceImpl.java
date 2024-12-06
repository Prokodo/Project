package com.backend.service;

import com.backend.model.Property;
import com.backend.repository.PropertyRepository;
import com.backend.service.interfaces.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PropertyServiceImpl implements PropertyService {
    private final PropertyRepository propertyRepository;

    @Autowired
    public PropertyServiceImpl(final PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- */

    public List<Property> getAllProperties() {
        return propertyRepository.findAll();
    }

    public Optional<Property> getPropertyById(final long id) {
        return propertyRepository.findById(id);
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- */

    public void deleteProperty(final long id) {
        propertyRepository.deleteById(id);
    }

    public Property saveProperty(final Property property) {
        return propertyRepository.save(property);
    }

    public Property updateProperty(final long id, final Property updatedProperty) {
        return propertyRepository.findById(id).map(property -> {
            property.setName(updatedProperty.getName());
            property.setType(updatedProperty.getType());
            property.setValue(updatedProperty.getValue());
            property.setAddress(updatedProperty.getAddress());
            property.setDescription(updatedProperty.getDescription());
            return propertyRepository.save(property);
        }).orElseThrow(() -> new RuntimeException("Property not found with id " + id));
    }
}
