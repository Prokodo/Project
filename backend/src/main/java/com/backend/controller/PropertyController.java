package com.backend.controller;

import com.backend.model.Property;
import com.backend.service.PropertyServiceImpl;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/properties")
public class PropertyController {
    private final @NotNull PropertyServiceImpl propertyService;

    @Autowired
    public PropertyController(final @NotNull PropertyServiceImpl propertyService) {
        this.propertyService = propertyService;
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- */

    @GetMapping
    public List<Property> getAllProperties() {
        return propertyService.getAllProperties();
    }

    @GetMapping("/count")
    public long getPropertyCount() {
        return propertyService.getAllProperties().size();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Property> getPropertyById(final @PathVariable long id) {
        return propertyService
            .getPropertyById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- */

    /*
    @PostMapping
    public Property createProperty(final @RequestBody Property property) {
        return propertyService.saveProperty(property);
    }
     */

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Property createProperty(
        @RequestPart("property") @NotNull Property property,
        @RequestPart(value = "file", required = false) MultipartFile imageFile
    ) {
        return propertyService.saveProperty(property, imageFile);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProperty(final @PathVariable long id) {
        if (propertyService.getPropertyById(id).isPresent()) {
            propertyService.deleteProperty(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Property> updateProperty(
        final @PathVariable long id,
        @RequestPart("property") @NotNull Property updatedProperty,
        @RequestPart(value = "file", required = false) MultipartFile imageFile
    ) {
        try {
            return ResponseEntity.ok(propertyService.updateProperty(id, updatedProperty));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
