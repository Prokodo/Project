package com.backend.controller;

import com.backend.model.Request;
import com.backend.repository.PropertyRepository;
import com.backend.repository.UserRepository;
import com.backend.service.interfaces.RequestService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
public class RequestController {
    private final @NotNull UserRepository userRepository;
    private final @NotNull RequestService requestService;
    private final @NotNull PropertyRepository propertyRepository;

    @Autowired
    public RequestController(final @NotNull RequestService requestService, final @NotNull UserRepository userRepository, final @NotNull PropertyRepository propertyRepository) {
        this.requestService = requestService;
        this.userRepository = userRepository;
        this.propertyRepository = propertyRepository;
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- */

    @GetMapping
    public List<Request> getAllRequests() {
        return requestService.getAllRequests();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<Request> getRequestByUserId(final @PathVariable Long id) {
        return ResponseEntity.ok(requestService.getRequestById(id));
    }

    @GetMapping("/properties/{id}")
    public ResponseEntity<List<Request>> getRequestsByPropertyId(final @PathVariable Long id) {
        return ResponseEntity.ok(requestService.getRequestByProperty(id));
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- */

    @PostMapping("/{propertyId}")
    public ResponseEntity<Request> createRequest(final @PathVariable Long propertyId, final @RequestBody Request request) {
        request.setProperty(
            propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found with ID: " + propertyId))
        );
        request.setTenant(userRepository.findByUsername("admin").get());
        return ResponseEntity.ok(requestService.createRequest(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Request> updateRequest(final @PathVariable long id, final @RequestBody Request updatedRequest) {
        return ResponseEntity.ok(requestService.updateRequest(id, updatedRequest));
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<Request> updateRequestStatus(final @PathVariable long id, final @RequestBody Request updatedRequest) {
        return ResponseEntity.ok(requestService.updateRequestStatus(id, updatedRequest.getStatus()));
    }
}
