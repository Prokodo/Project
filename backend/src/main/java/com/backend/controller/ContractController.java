package com.backend.controller;

import com.backend.model.Contract;
import com.backend.model.requests.ContractRequest;
import com.backend.security.SecurityUtils;
import com.backend.security.model.CustomUserPrincipal;
import com.backend.service.interfaces.ContractService;
import jakarta.validation.Valid;
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
        if (SecurityUtils.isAdmin(user)) {
            return ResponseEntity.ok(contractService.getListOfContracts());
        }
        return ResponseEntity.ok(contractService.getContractsByUserId(user.userId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contract> getContractById(final @PathVariable Long id) {
        return contractService.getContractById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- POST -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- */

    @PostMapping
    public ResponseEntity<Contract> createContract(final @RequestBody @Valid ContractRequest contract) {
        return ResponseEntity.ok(contractService.createContract(contract));
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- PUT -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= */

    @PutMapping("/{id}")
    public ResponseEntity<Contract> updateContract(final @PathVariable Long id, final @RequestBody @Valid ContractRequest request) {
        final Contract updatedContract = contractService.updateContract(id, request);
        return ResponseEntity.ok(updatedContract);
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- DELETE -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- */

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContract(final @PathVariable Long id) {
        if (contractService.getContractById(id).isPresent()) {
            contractService.deleteContract(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
