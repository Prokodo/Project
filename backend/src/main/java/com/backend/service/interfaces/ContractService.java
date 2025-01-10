package com.backend.service.interfaces;

import com.backend.model.Contract;
import com.backend.model.requests.ContractRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ContractService {
    List<Contract> getListOfContracts();
    Optional<Contract> getContractById(Long id);
    List<Contract> getContractsByUserId(Long userId);

    void deleteContract(Long id);
    Contract createContract(ContractRequest contract);
    Contract updateContract(Long id, ContractRequest updatedContract);
}
