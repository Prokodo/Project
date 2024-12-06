package com.backend.service.interfaces;

import com.backend.model.Request;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RequestService {
     List<Request> getAllRequests();
     Request getRequestById(long id) ;
     Request createRequest(Request request) ;
     Request updateRequest(long id, Request updatedRequest) ;
     void deleteRequest(long id);
}
