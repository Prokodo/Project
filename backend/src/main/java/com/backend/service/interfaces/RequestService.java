package com.backend.service.interfaces;

import com.backend.model.Request;
import com.backend.model.enums.RequestStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RequestService {
     void deleteRequest(Long id);
     List<Request> getAllRequests();
     Request getRequestById(Long id);
     Request createRequest(Request request);
     List<Request> getRequestByProperty(Long propertyId);
     Request updateRequest(Long id, Request updatedRequest);
     Request updateRequestStatus(Long id, RequestStatus status);
}
