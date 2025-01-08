package com.backend.service.interfaces;

import com.backend.model.Contract;
import com.backend.model.requests.ContractRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ContractService {
    Contract getContractById(Long id);
    List<Contract> getListOfContracts();
    List<Contract> getContractsByUserId(Long userId);

    void deleteContract(Long id);
    Contract createContract(ContractRequest contract);
    Contract updateContract(Long id, ContractRequest updatedContract);
}
