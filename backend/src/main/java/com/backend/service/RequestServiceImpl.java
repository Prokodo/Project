package com.backend.service;

import com.backend.model.Request;
import com.backend.model.enums.RequestStatus;
import com.backend.repository.RequestRepository;
import com.backend.service.interfaces.RequestService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestServiceImpl implements RequestService {
    private final @NotNull RequestRepository requestRepository;

    @Autowired
    public RequestServiceImpl(final @NotNull RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- */

    public List<Request> getAllRequests() {
        return requestRepository.findAll();
    }

    public Request getRequestById(final @NotNull Long id) {
        return requestRepository.findById(id).orElseThrow(() -> new RuntimeException("Request not found with ID " + id));
    }

    public List<Request> getRequestsByUserId(final @NotNull Long userId) {
        return requestRepository.findAll();
    }

    public List<Request> getRequestByProperty(final @NotNull Long propertyId) {
        return requestRepository.findByPropertyId(propertyId);
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- */

    public void deleteRequest(final @NotNull Long id) {
        requestRepository.deleteById(id);
    }

    public Request createRequest(final @NotNull Request request) {
        return requestRepository.save(request);
    }

    public Request updateRequest(final @NotNull Long id, final @NotNull Request updatedRequest) {
        final Request request = getRequestById(id);
        request.setStatus(updatedRequest.getStatus());
        request.setDescription(updatedRequest.getDescription());
        return requestRepository.save(request);
    }

    public Request updateRequestStatus(final @NotNull Long id, final @NotNull RequestStatus status) {
        final Request request = getRequestById(id);
        request.setStatus(status);

        return requestRepository.save(request);
    }
}
