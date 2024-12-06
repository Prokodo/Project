package com.backend.service;

import com.backend.model.Contract;

import com.backend.repository.ContractRepository;
import com.backend.service.interfaces.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContractServiceImpl implements ContractService {
    private final ContractRepository contractRepository;

    @Autowired
    public ContractServiceImpl(final ContractRepository contractRepository) {
        this.contractRepository = contractRepository;
    }

    public List<Contract> getAllContracts() {
        return contractRepository.findAll();
    }

    public Contract getContractById(final long id) {
        return contractRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Contract not found with ID " + id));
    }

    public void deleteContract(final long id) {
        contractRepository.deleteById(id);
    }

    public Contract createContract(final Contract contract) {
        return contractRepository.save(contract);
    }

    public Contract updateContract(final long id, final Contract updatedContract) {
        Contract contract = getContractById(id);
        contract.setProperty(updatedContract.getProperty());
        contract.setTenant(updatedContract.getTenant());
        contract.setStartDate(updatedContract.getStartDate());
        contract.setEndDate(updatedContract.getEndDate());
        contract.setMonthlyRent(updatedContract.getMonthlyRent());
        return contractRepository.save(contract);
    }
}
