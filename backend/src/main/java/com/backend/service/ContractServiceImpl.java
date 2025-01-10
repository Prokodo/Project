package com.backend.service;

import com.backend.errors.ResourceNotFoundException;
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
import java.util.Optional;

@Service
public class ContractServiceImpl implements ContractService {
    private final UserRepository userRepository;
    private final ContractRepository contractRepository;
    private final PropertyRepository propertyRepository;

    @Autowired
    public ContractServiceImpl(final ContractRepository contractRepository, final PropertyRepository propertyRepository, final UserRepository userRepository) {
        this.userRepository = userRepository;
        this.contractRepository = contractRepository;
        this.propertyRepository = propertyRepository;
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- GET -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- */

    public List<Contract> getListOfContracts() {
        return contractRepository.findAll();
    }

    public Optional<Contract> getContractById(final Long id) {
        return contractRepository.findById(id);
    }

    public List<Contract> getContractsByUserId(final Long id) {
        return contractRepository.findContractByTenantId(id);
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- POST -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- */

    public Contract createContract(final ContractRequest request) {
        validateRequest(request);

        final Property property = getPropertyOrThrow(request.propertyId());
        final User tenant = getTenantOrThrow(request.tenantId());
        return contractRepository.save(new Contract(
            property, tenant,
            request.startDate(), request.endDate(),
            request.monthlyRent()
        ));
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- PUT -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= */

    public Contract updateContract(final Long id, final ContractRequest request) {
        validateRequest(request);

        final Property property = getPropertyOrThrow(request.propertyId());
        final User tenant = getTenantOrThrow(request.tenantId());

        final Contract contract = getContractById(id).orElseThrow(() -> new RuntimeException("Contract not found with ID " + id));
        contract.setTenant(tenant);
        contract.setProperty(property);
        contract.setEndDate(request.endDate());
        contract.setStartDate(request.startDate());
        contract.setMonthlyRent(request.monthlyRent());
        return contractRepository.save(contract);
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- DELETE -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- */

    public void deleteContract(final Long id) {
        contractRepository.deleteById(id);
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- UTILS -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= */

    /**
     * Validate common ContractRequest fields before creating/updating a Contract.
     */
    private void validateRequest(final ContractRequest request) {
        if (request.propertyId() == null || request.propertyId() <= 0) {
            throw new IllegalArgumentException("Property ID is required");
        }
        if (request.tenantId() == null || request.tenantId() <= 0) {
            throw new IllegalArgumentException("Tenant ID is required");
        }
        if (request.monthlyRent() == null || request.monthlyRent() <= 0) {
            throw new IllegalArgumentException("Monthly rent must be greater than 0");
        }
        if (request.startDate() == null || request.endDate() == null || request.startDate().isAfter(request.endDate())) {
            throw new IllegalArgumentException("Invalid start or end date. Start date cannot be after end date.");
        }
    }

    /**
     * Fetch a User by ID or throw a ResourceNotFoundException if not found.
     */
    private User getTenantOrThrow(final Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant not found with ID: " + userId));
    }

    /**
     * Fetch a Property by ID or throw a ResourceNotFoundException if not found.
     */
    private Property getPropertyOrThrow(final Long propertyId) {
        return propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with ID: " + propertyId));
    }
}
