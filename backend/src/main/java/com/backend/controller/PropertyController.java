package com.backend.controller;

import com.backend.model.Property;
import com.backend.security.SecurityUtils;
import com.backend.security.model.CustomUserPrincipal;
import com.backend.service.PropertyServiceImpl;
import jakarta.validation.Valid;
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
    private final PropertyServiceImpl propertyService;

    @Autowired
    public PropertyController(final PropertyServiceImpl propertyService) {
        this.propertyService = propertyService;
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- GET -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- */

    @GetMapping("/count")
    public ResponseEntity<Integer> getNumberOfProperties() {
        return ResponseEntity.ok(propertyService.getNumberOfProperties());
    }

    @GetMapping
    public ResponseEntity<List<Property>> getListOfProperties() {
        final @NotNull CustomUserPrincipal user = SecurityUtils.getCurrentUser();
        if (SecurityUtils.isAdmin(user)) {
            return ResponseEntity.ok(propertyService.getAllProperties());
        }
        return ResponseEntity.ok(propertyService.getPropertiesByUserId(user.userId()));
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
        @RequestPart("property") @Valid Property requestProperty,
        @RequestPart(value = "imageFile", required = false) MultipartFile imageFile
    ) {
        final Property updatedProperty = propertyService.updateProperty(id, requestProperty, imageFile);
        return ResponseEntity.ok(updatedProperty);
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
