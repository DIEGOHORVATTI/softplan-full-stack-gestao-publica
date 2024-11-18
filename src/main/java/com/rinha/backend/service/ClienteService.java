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
    public TransacaoResponse executeTransaction(Long id, TransacaoRequest request) {
        Cliente cliente = clienteRepository.findByIdWithLock(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        int novoSaldo = cliente.getSaldo();
        if (request.getTipo().equals("d")) {
            novoSaldo -= request.getValor();
            if (novoSaldo < -cliente.getLimite()) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
            }
        } else {
            novoSaldo += request.getValor();
        }

        cliente.setSaldo(novoSaldo);
        clienteRepository.save(cliente);

        Transacao transacao = new Transacao();
        transacao.setCliente(cliente);
        transacao.setValor(request.getValor());
        transacao.setTipo(request.getTipo());
        transacao.setDescricao(request.getDescricao());
        transacao.setRealizadaEm(LocalDateTime.now());
        transacaoRepository.save(transacao);

        return new TransacaoResponse(cliente.getLimite(), cliente.getSaldo());
    }

    public ExtratoResponse getTransactionStatement(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        ExtratoResponse response = new ExtratoResponse();

        ExtratoResponse.Saldo saldo = new ExtratoResponse.Saldo();
        saldo.setTotal(cliente.getSaldo());
        saldo.setData_extrato(LocalDateTime.now());
        saldo.setLimite(cliente.getLimite());
        response.setSaldo(saldo);

        response.setUltimas_transacoes(
                transacaoRepository.findTop10ByClienteIdOrderByRealizadaEmDesc(id)
                        .stream()
                        .map(t -> {
                            ExtratoResponse.TransacaoExtrato te = new ExtratoResponse.TransacaoExtrato();
                            te.setValor(t.getValor());
                            te.setTipo(t.getTipo());
                            te.setDescricao(t.getDescricao());
                            te.setRealizada_em(t.getRealizadaEm());
                            return te;
                        })
                        .collect(Collectors.toList()));

        return response;
    }
}