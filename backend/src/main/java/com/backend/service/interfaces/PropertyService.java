package com.backend.service.interfaces;

import com.backend.model.Property;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public interface PropertyService {
    Integer getNumberOfProperties();
    List<Property> getAllProperties();
    Optional<Property> getPropertyById(Long id);
    List<Property> getPropertiesByUserId(Long userId);

    void deleteProperty(Long id);
    Property saveProperty(Property property);
    Property updateProperty(Long id, Property updatedProperty, MultipartFile imageFile);
}
