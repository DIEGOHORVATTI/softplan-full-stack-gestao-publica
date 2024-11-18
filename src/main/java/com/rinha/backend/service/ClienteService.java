package com.rinha.backend.service;

import com.rinha.backend.dto.*;
import com.rinha.backend.dto.ExtratoResponse.TransacaoExtrato;
import com.rinha.backend.model.*;
import com.rinha.backend.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ClienteService {
    private final ClienteRepository clienteRepository;
    private final TransacaoRepository transacaoRepository;
    private Stream<Transaction> transacaoStream;

    @Transactional
    public TransactionSummary executeTransaction(Long id, TransacaoRequest request) {
        Cliente client = clienteRepository.findByIdWithLock(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        int newBalance = client.getSaldo();
        if (request.getTipo().equals("d")) {
            newBalance -= request.getValor();
            if (newBalance < -client.getLimite()) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
            }
        } else {
            newBalance += request.getValor();
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

        return new TransactionSummary(client.getLimite(), client.getSaldo());
    }

    public ExtratoResponse getTransactionStatement(Long id) {
        Cliente client = clienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        ExtratoResponse.Saldo balanceInfo = new ExtratoResponse.Saldo();
        balanceInfo.setTotal(client.getSaldo());
        balanceInfo.setData_extrato(LocalDateTime.now());
        balanceInfo.setLimite(client.getLimite());

        ExtratoResponse response = new ExtratoResponse();
        response.setSaldo(balanceInfo);

        transacaoStream = transacaoRepository.findTop10ByClienteIdOrderByRealizadaEmDesc(id).stream();
        Stream<TransacaoExtrato> transactionToExtratoMap = transacaoStream
                .map(transaction -> {
                    ExtratoResponse.TransacaoExtrato transactionExtrato = new ExtratoResponse.TransacaoExtrato();
                    transactionExtrato.setValor(transaction.getValor());
                    transactionExtrato.setTipo(transaction.getTipo());
                    transactionExtrato.setDescricao(transaction.getDescricao());
                    transactionExtrato.setRealizada_em(transaction.getRealizadaEm());

                    return transactionExtrato;
                });

        response.setUltimas_transacoes(transactionToExtratoMap.collect(Collectors.toList()));

        return response;
    }
}