package com.backend.controller;

import com.backend.model.Property;
import com.backend.service.PropertyServiceImpl;
import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/properties")
public class PropertyController {
    private final PropertyServiceImpl propertyService;

    @Autowired
    public PropertyController(final PropertyServiceImpl propertyService) {
        this.propertyService = propertyService;
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- GET -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- */

    @GetMapping("/count")
    public Integer getNumberOfProperties() {
        return propertyService.getNumberOfProperties();
    }

    @GetMapping
    public List<Property> getListOfProperties() {
        return propertyService.getAllProperties();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Property> getPropertyById(final @PathVariable Long id) {
        return propertyService.getPropertyById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- POST -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- */

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Property createProperty(
        @RequestPart("property") @Valid Property property,
        @RequestPart(value = "imageFile", required = false) MultipartFile imageFile
    ) {
        return propertyService.saveProperty(property, imageFile);
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- PUT -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= */

    @PutMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Property> updateProperty(
        final @PathVariable Long id,
        @RequestPart("property") @Valid Property updatedProperty,
        @RequestPart(value = "imageFile", required = false) MultipartFile imageFile
    ) {
        try {
            return ResponseEntity.ok(propertyService.updateProperty(id, updatedProperty, imageFile));
        } catch (final RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- DELETE -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- */

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProperty(final @PathVariable Long id) {
        if (propertyService.getPropertyById(id).isPresent()) {
            propertyService.deleteProperty(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
