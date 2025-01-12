package com.backend.service.interfaces;

import com.backend.model.Request;
import com.backend.model.requests.RequestRequest;
import com.backend.model.requests.RequestStatusRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RequestService {
     Request getRequestById(Long id);
     List<Request> getListOfRequests();
     List<Request> getListOfRequestsByUserId(Long userId);
     List<Request> getListOfRequestsByPropertyId(Long propertyId);

     Request createRequest(Request request);
     Request updateRequest(Long id, RequestRequest updatedRequest);
     Request updateRequestStatus(Long id, RequestStatusRequest status);
}
