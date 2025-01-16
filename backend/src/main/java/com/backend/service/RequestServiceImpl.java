package com.backend.service;

import com.backend.model.Request;
import com.backend.model.requests.RequestRequest;
import com.backend.model.requests.RequestStatusRequest;
import com.backend.repository.RequestRepository;
import com.backend.service.interfaces.RequestService;
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
        return requestRepository.findByTenantId(userId);
    }

    public List<Request> getListOfRequestsByPropertyId(final Long propertyId) {
        return requestRepository.findByPropertyId(propertyId);
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- POST -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- */

    public Request createRequest(final Request request) {
        return requestRepository.save(request);
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- PUT -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= */

    public Request updateRequest(final Long id, final RequestRequest request) {
        final Request updatedRequest = getRequestById(id);
        updatedRequest.setDescription(request.description());
        return requestRepository.save(updatedRequest);
    }

    public Request updateRequestStatus(final Long id, final RequestStatusRequest request) {
        final Request updatedRequest = getRequestById(id);
        updatedRequest.setStatus(request.status());
        return requestRepository.save(updatedRequest);
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- DELETE -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-== */

    public void deleteRequest(final Long id) {
        requestRepository.deleteById(id);
    }
}
