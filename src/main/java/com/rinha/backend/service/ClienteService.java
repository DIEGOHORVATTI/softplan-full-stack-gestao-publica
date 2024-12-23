package com.rinha.backend.service;

import com.rinha.backend.dto.*;
import com.rinha.backend.model.*;
import com.rinha.backend.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClienteService {
    private final ClienteRepository clienteRepository;
    private final TransacaoRepository transacaoRepository;

    @Transactional
    public TransactionSummary executeTransaction(Long id, TransacaoRequest request) {
        Cliente client = clienteRepository.findByIdWithLock(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));

        int limit = client.getLimite();
        int newBalance = client.getSaldo();

        // r para recebível | d para débito
        switch (request.getTipo()) {
            case "d":
                newBalance -= request.getValor();

                if (newBalance < -limit) {
                    throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                            "Saldo não pode ficar abaixo do limite");
                }

                break;
            case "r":
                newBalance += request.getValor();
                break;
            default:
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Tipo de transação inválido");
        }

        client.setSaldo(newBalance);
        clienteRepository.save(client);

        Transaction transaction = new Transaction();
        transaction.setCliente(client);
        transaction.setValor(request.getValor());
        transaction.setTipo(request.getTipo());
        transaction.setDescricao(request.getDescricao());
        transaction.setRealizadaEm(LocalDateTime.now());
        transacaoRepository.save(transaction);

        return new TransactionSummary(limit, newBalance);
    }

    public ExtratoResponse getTransactionStatement(Long id) {
        Cliente client = clienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));

        ExtratoResponse.Saldo balanceInfo = new ExtratoResponse.Saldo();
        balanceInfo.setTotal(client.getSaldo());
        balanceInfo.setData_extrato(LocalDateTime.now());
        balanceInfo.setLimite(client.getLimite());

        ExtratoResponse response = new ExtratoResponse();
        response.setSaldo(balanceInfo);

        response.setUltimas_transacoes(transacaoRepository
                .findTop10ByClienteIdOrderByRealizadaEmDesc(id).stream()
                .map(transaction -> {
                    ExtratoResponse.TransacaoExtrato transactionExtrato = new ExtratoResponse.TransacaoExtrato();
                    transactionExtrato.setValor(transaction.getValor());
                    transactionExtrato.setTipo(transaction.getTipo());
                    transactionExtrato.setDescricao(transaction.getDescricao());
                    transactionExtrato.setRealizada_em(transaction.getRealizadaEm());

                    return transactionExtrato;
                }).collect(Collectors.toList()));

        return response;
    }
}
