package com.backend.controller;

import com.backend.model.Contract;
import com.backend.model.requests.ContractRequest;
import com.backend.security.model.CustomUserPrincipal;
import com.backend.service.ContractServiceImpl;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contracts")
public class ContractController {
    private final @NotNull ContractServiceImpl contractService;

    @Autowired
    public ContractController(final @NotNull ContractServiceImpl contractService) {
        this.contractService = contractService;
    }

    @GetMapping
    public ResponseEntity<List<Contract>> getAllContracts() {
        final @NotNull Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof @NotNull CustomUserPrincipal principal)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        final @NotNull Long userId = principal.userId();
        final boolean isAdmin = principal.authorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));

        final @NotNull List<@NotNull Contract> contracts = isAdmin
                ? contractService.getAllContracts()
                : contractService.getContractsByUserId(userId);
        return ResponseEntity.ok(contracts);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContract(final @PathVariable Long id) {
        contractService.deleteContract(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contract> getContractById(final @PathVariable long id) {
        return ResponseEntity.ok(contractService.getContractById(id));
    }

    @PostMapping
    public ResponseEntity<Contract> createContract(final @RequestBody ContractRequest contract) {
        return ResponseEntity.ok(contractService.createContract(contract));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Contract> updateContract(final @PathVariable long id, final @RequestBody ContractRequest updatedContract) {
        return ResponseEntity.ok(contractService.updateContract(id, updatedContract));
    }
}
