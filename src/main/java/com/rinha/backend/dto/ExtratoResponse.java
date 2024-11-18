package com.rinha.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ExtratoResponse {
    private Saldo saldo;
    private List<TransacaoExtrato> ultimas_transacoes;

    @Data
    public static class Saldo {
        private Integer total;
        private LocalDateTime data_extrato;
        private Integer limite;
    }

    @Data
    public static class TransacaoExtrato {
        private Integer valor;
        private String tipo; // 'r' ou 'd' -> 'Recebível' ou 'Débito'
        private String descricao;
        private LocalDateTime realizada_em;
    }
}
