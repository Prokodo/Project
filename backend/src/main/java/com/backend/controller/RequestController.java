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
    private final UserRepository userRepository;
    private final RequestService requestService;
    private final PropertyRepository propertyRepository;

    @Autowired
    public RequestController(final RequestService requestService, final UserRepository userRepository, final PropertyRepository propertyRepository) {
        this.requestService = requestService;
        this.userRepository = userRepository;
        this.propertyRepository = propertyRepository;
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- GET -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- */

    @GetMapping
    public List<Request> getListOfRequests() {
        return requestService.getListOfRequests();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<Request> getRequestByUserId(final @PathVariable Long id) {
        return ResponseEntity.ok(requestService.getRequestById(id));
    }

    @GetMapping("/properties/{id}")
    public ResponseEntity<List<Request>> getRequestsByPropertyId(final @PathVariable Long id) {
        return ResponseEntity.ok(requestService.getListOfRequestsByPropertyId(id));
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- POST -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- */

    @PostMapping("/{propertyId}")
    public ResponseEntity<Request> createRequest(final @PathVariable Long propertyId, final @RequestBody Request request) {
        request.setProperty(
            propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found with ID: " + propertyId))
        );
        request.setTenant(userRepository.findByUsername("admin").get());
        return ResponseEntity.ok(requestService.createRequest(request));
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- PUT -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= */

    @PutMapping("/{id}")
    public ResponseEntity<Request> updateRequest(final @PathVariable Long id, final @RequestBody Request updatedRequest) {
        return ResponseEntity.ok(requestService.updateRequest(id, updatedRequest));
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- DELETE -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- */

    @PutMapping("/status/{id}")
    public ResponseEntity<Request> updateRequestStatus(final @PathVariable Long id, final @RequestBody Request updatedRequest) {
        return ResponseEntity.ok(requestService.updateRequestStatus(id, updatedRequest.getStatus()));
    }
}
