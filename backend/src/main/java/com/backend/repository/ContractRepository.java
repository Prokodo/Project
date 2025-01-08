package com.backend.repository;

import com.backend.model.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContractRepository extends JpaRepository<Contract, Long> {
    List<Contract> findContractByTenantId(Long id);
}