package com.backend.service.interfaces;

import com.backend.model.Contract;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ContractService {
    void deleteContract(long id);
    List<Contract> getAllContracts();
    Contract getContractById(long id);
    Contract createContract(Contract contract);
    Contract updateContract(long id, Contract updatedContract);
}
