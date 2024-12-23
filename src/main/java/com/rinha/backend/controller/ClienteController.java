package com.rinha.backend.controller;

import com.rinha.backend.dto.*;
import com.rinha.backend.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {
    private final ClienteService clienteService;

    @PostMapping("/{id}/transacoes")
    public ResponseEntity<TransactionSummary> executeTransaction(
            @PathVariable Long id,
            @Valid @RequestBody TransacaoRequest request) {

        return ResponseEntity.ok(clienteService.executeTransaction(id, request));
    }

    @GetMapping("/{id}/extrato")
    public ResponseEntity<ExtratoResponse> getTransactionStatement(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.getTransactionStatement(id));
    }
}