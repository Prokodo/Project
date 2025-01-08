package com.backend.controller;

import com.backend.model.Contract;
import com.backend.model.requests.ContractRequest;
import com.backend.security.SecurityUtils;
import com.backend.security.model.CustomUserPrincipal;
import com.backend.service.interfaces.ContractService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contracts")
public class ContractController {
    private final ContractService contractService;

    @Autowired
    public ContractController(final ContractService contractService) {
        this.contractService = contractService;
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- GET -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- */

    @GetMapping
    public ResponseEntity<List<Contract>> getAllContracts() {
        final CustomUserPrincipal user = SecurityUtils.getCurrentUser();
        final boolean isAdmin = SecurityUtils.isAdmin(user);
        final Long userId = user.userId();

        final List<Contract> contracts = isAdmin
                ? contractService.getListOfContracts()
                : contractService.getContractsByUserId(userId);
        return ResponseEntity.ok(contracts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contract> getContractById(final @PathVariable Long id) {
        return ResponseEntity.ok(contractService.getContractById(id));
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- POST -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- */

    @PostMapping
    public ResponseEntity<Contract> createContract(final @RequestBody ContractRequest contract) {
        return ResponseEntity.ok(contractService.createContract(contract));
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- PUT -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= */

    @PutMapping("/{id}")
    public ResponseEntity<Contract> updateContract(final @PathVariable Long id, final @RequestBody ContractRequest updatedContract) {
        return ResponseEntity.ok(contractService.updateContract(id, updatedContract));
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- DELETE -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- */

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContract(final @NotNull@PathVariable Long id) {
        contractService.deleteContract(id);
        return ResponseEntity.noContent().build();
    }
}
