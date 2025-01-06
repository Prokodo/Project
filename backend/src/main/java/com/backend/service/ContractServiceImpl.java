package com.backend.service;

import com.backend.model.Contract;

import com.backend.model.Property;
import com.backend.model.User;
import com.backend.model.requests.ContractRequest;
import com.backend.repository.ContractRepository;
import com.backend.repository.PropertyRepository;
import com.backend.repository.UserRepository;
import com.backend.service.interfaces.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContractServiceImpl implements ContractService {
    private final ContractRepository contractRepository;
    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;

    @Autowired
    public ContractServiceImpl(ContractRepository contractRepository, PropertyRepository propertyRepository, UserRepository userRepository) {
        this.contractRepository = contractRepository;
        this.propertyRepository = propertyRepository;
        this.userRepository = userRepository;
    }

    public List<Contract> getAllContracts() {
        return contractRepository.findAll();
    }

    public List<Contract> getContractsByUserId(final Long id) {
        return contractRepository.findContractByTenantId(id);
    }


    public Contract getContractById(final long id) {
        return contractRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Contract not found with ID " + id));
    }

    public void deleteContract(final long id) {
        contractRepository.deleteById(id);
    }

    public Contract createContract(final ContractRequest request) {
        if (request.propertyId() == null || request.propertyId() == 0) {
            throw new IllegalArgumentException("Property ID is required");
        }

        if (request.tenantId() == null || request.tenantId() == 0) {
            throw new IllegalArgumentException("Tenant ID is required");
        }

        if (request.startDate().isAfter(request.endDate())) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        Property property = propertyRepository.findById(request.propertyId())
                .orElseThrow(() -> new IllegalArgumentException("Property not found with ID: " + request.propertyId()));

        User tenant = userRepository.findById(request.tenantId())
                .orElseThrow(() -> new IllegalArgumentException("Tenant not found with ID: " + request.tenantId()));

        Contract contract = new Contract(property, tenant, request.startDate(), request.endDate(), request.monthlyRent());
        return contractRepository.save(contract);
    }

    public Contract updateContract(final long id, final ContractRequest request) {
        Contract contract = getContractById(id);

        Property property = propertyRepository.findById(request.propertyId()).orElseThrow(() -> new IllegalArgumentException("Property not found with ID: " + request.propertyId()));

        User tenant = userRepository.findById(request.tenantId()).orElseThrow(() -> new IllegalArgumentException("Tenant not found with ID: " + request.tenantId()));

        contract.setProperty(property);
        contract.setTenant(tenant);
        contract.setStartDate(request.startDate());
        contract.setEndDate(request.endDate());
        contract.setMonthlyRent(request.monthlyRent());
        return contractRepository.save(contract);
    }
}
