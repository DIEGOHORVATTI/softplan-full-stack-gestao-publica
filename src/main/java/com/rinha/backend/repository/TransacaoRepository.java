package com.rinha.backend.repository;

import com.rinha.backend.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface TransacaoRepository extends JpaRepository<Transaction, Long> {
    @Query(value = "SELECT * FROM transacoes WHERE cliente_id = :clienteId ORDER BY realizada_em DESC LIMIT 10", nativeQuery = true)
    List<Transaction> findTop10ByClienteIdOrderByRealizadaEmDesc(Long clienteId);
}