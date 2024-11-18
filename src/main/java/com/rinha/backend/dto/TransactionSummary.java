package com.rinha.backend.dto;

import lombok.Data;

@Data
public class TransactionSummary {
    private Integer limite;
    private Integer saldo;

    public TransactionSummary(Integer limite, Integer saldo) {
        this.limite = limite;
        this.saldo = saldo;
    }
}