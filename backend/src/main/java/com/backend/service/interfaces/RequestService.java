package com.backend.service.interfaces;

import com.backend.model.Request;
import com.backend.model.enums.RequestStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RequestService {
     Request getRequestById(Long id);
     List<Request> getListOfRequests();
     List<Request> getListOfRequestsByUserId(Long userId);
     List<Request> getListOfRequestsByPropertyId(Long propertyId);

     void deleteRequest(Long id);
     Request createRequest(Request request);
     Request updateRequest(Long id, Request updatedRequest);
     Request updateRequestStatus(Long id, RequestStatus status);
}
