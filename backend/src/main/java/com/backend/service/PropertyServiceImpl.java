package com.backend.service;

import com.backend.model.Property;
import com.backend.repository.PropertyRepository;
import com.backend.service.interfaces.PropertyService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class PropertyServiceImpl implements PropertyService {
    private final @NotNull PropertyRepository propertyRepository;

    @Autowired
    public PropertyServiceImpl(final @NotNull PropertyRepository propertyRepository) {
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

    public Property saveProperty(final @NotNull Property property) {
        return propertyRepository.save(property);
    }

    public Property saveProperty(final @NotNull Property property, final MultipartFile imageFile) {
        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                property.setImage(imageFile.getBytes());
            }
            return saveProperty(property);
        } catch (final IOException e) {
            throw new RuntimeException("Failed to save image: " + e.getMessage(), e);
        }
    }

    public Property updateProperty(final long id, final @NotNull Property updatedProperty) {
        return propertyRepository.findById(id).map(property -> {
            property.setName(updatedProperty.getName());
            property.setType(updatedProperty.getType());
            property.setPrice(updatedProperty.getPrice());
            property.setAddress(updatedProperty.getAddress());
            property.setDescription(updatedProperty.getDescription());
            return propertyRepository.save(property);
        }).orElseThrow(() -> new RuntimeException("Property not found with id " + id));
    }
}
