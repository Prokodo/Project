package com.backend.repository;

import com.backend.model.Request;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByPropertyId(Long propertyId);
}