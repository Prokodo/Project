package com.backend.controller;

import com.backend.model.Request;
import com.backend.service.RequestServiceImpl;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
public class RequestController {
    private final @NotNull RequestServiceImpl requestService;

    @Autowired
    public RequestController(final @NotNull RequestServiceImpl requestService) {
        this.requestService = requestService;
    }

    @GetMapping
    public List<Request> getAllRequests() {
        return requestService.getAllRequests();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Request> getRequestById(final @PathVariable Long id) {
        return ResponseEntity.ok(requestService.getRequestById(id));
    }

    @PostMapping
    public ResponseEntity<Request> createRequest(final @RequestBody Request request) {
        return ResponseEntity.ok(requestService.createRequest(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Request> updateRequest(final @PathVariable long id, final @RequestBody Request updatedRequest) {
        return ResponseEntity.ok(requestService.updateRequest(id, updatedRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequest(final @PathVariable long id) {
        requestService.deleteRequest(id);
        return ResponseEntity.noContent().build();
    }
}
