package com.backend.service.interfaces;

import com.backend.model.Property;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public interface PropertyService {
    List<Property> getAllProperties();
    Optional<Property> getPropertyById(long id);
    Property saveProperty(Property property);
    Property updateProperty(long id, Property updatedProperty, MultipartFile imageFile);
    void deleteProperty(long id);
}
