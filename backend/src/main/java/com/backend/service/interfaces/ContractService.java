package com.backend.service.interfaces;

import com.backend.model.Contract;
import com.backend.model.requests.ContractRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ContractService {
    void deleteContract(long id);

    List<Contract> getAllContracts();
    List<Contract> getContractsByUserId(Long userId);

    Contract getContractById(long id);
    Contract createContract(ContractRequest contract);
    Contract updateContract(long id, ContractRequest updatedContract);
}
