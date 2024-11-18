package com.rinha.backend.dto;

import lombok.Data;

@Data
public class TransacaoResponse {
    private Integer limite;
    private Integer saldo;
    
    public TransacaoResponse(Integer limite, Integer saldo) {
        this.limite = limite;
        this.saldo = saldo;
    }
}