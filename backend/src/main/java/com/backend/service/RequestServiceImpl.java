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
    private final RequestRepository requestRepository;

    @Autowired
    public RequestServiceImpl(final RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- GET -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- */

    public List<Request> getListOfRequests() {
        return requestRepository.findAll();
    }

    public Request getRequestById(final Long id) {
        return requestRepository.findById(id).orElseThrow(() -> new RuntimeException("Request not found with ID " + id));
    }

    public List<Request> getListOfRequestsByUserId(final Long userId) {
        return requestRepository.findAll();
    }

    public List<Request> getListOfRequestsByPropertyId(final Long propertyId) {
        return requestRepository.findByPropertyId(propertyId);
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- POST -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- */

    public Request createRequest(final Request request) {
        return requestRepository.save(request);
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- PUT -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= */

    public Request updateRequest(final Long id, final Request updatedRequest) {
        final Request request = getRequestById(id);
        request.setStatus(updatedRequest.getStatus());
        request.setDescription(updatedRequest.getDescription());
        return requestRepository.save(request);
    }

    public Request updateRequestStatus(final Long id, final RequestStatus status) {
        final Request request = getRequestById(id);
        request.setStatus(status);

        return requestRepository.save(request);
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- DELETE -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- */

    public void deleteRequest(final Long id) {
        requestRepository.deleteById(id);
    }
}
