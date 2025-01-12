package com.backend.controller;

import com.backend.model.Property;
import com.backend.model.Request;
import com.backend.model.User;
import com.backend.model.enums.RequestStatus;
import com.backend.model.requests.RequestRequest;
import com.backend.model.requests.RequestStatusRequest;
import com.backend.repository.PropertyRepository;
import com.backend.repository.UserRepository;
import com.backend.security.SecurityUtils;
import com.backend.security.model.CustomUserPrincipal;
import com.backend.service.interfaces.RequestService;
import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
    public ResponseEntity<Request> createRequest(final @PathVariable Long propertyId, final @RequestBody @Valid RequestRequest request) {
        final @NotNull CustomUserPrincipal user = SecurityUtils.getCurrentUser();

        final User tenant = userRepository.findById(user.userId()).orElseThrow(() -> new RuntimeException("Tenant not found with ID: " + user.userId()));
        final Property property = propertyRepository.findById(propertyId).orElseThrow(() -> new RuntimeException("Property not found with ID: " + propertyId));

        final Request newRequest = new Request(
            tenant, property, request.description(),
            RequestStatus.REQUESTED, LocalDate.now()
        );
        return ResponseEntity.ok(requestService.createRequest(newRequest));
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- PUT -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= */

    @PutMapping("/{id}")
    public ResponseEntity<Request> updateRequest(final @PathVariable Long id, final @RequestBody @Valid RequestRequest request) {
        final Request updatedRequest = requestService.updateRequest(id, request);
        return ResponseEntity.ok(updatedRequest);
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<Request> updateRequestStatus(final @PathVariable Long id, final @RequestBody @Valid RequestStatusRequest request) {
        final Request updatedRequest = requestService.updateRequestStatus(id, request);
        return ResponseEntity.ok(updatedRequest);
    }
}
