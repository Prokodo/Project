package com.backend.service;

import com.backend.model.Request;
import com.backend.repository.RequestRepository;
import com.backend.service.interfaces.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;

    @Autowired
    public RequestServiceImpl(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    public List<Request> getAllRequests() {
        return requestRepository.findAll();
    }

    public Request getRequestById(final long id) {
        return requestRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Request not found with ID " + id));
    }

    public Request createRequest(final Request request) {
        return requestRepository.save(request);
    }

    public Request updateRequest(final long id, final Request updatedRequest) {
        Request request = getRequestById(id);
        request.setDescription(updatedRequest.getDescription());
        request.setStatus(updatedRequest.getStatus());
        return requestRepository.save(request);
    }

    public void deleteRequest(final long id) {
        requestRepository.deleteById(id);
    }
}
