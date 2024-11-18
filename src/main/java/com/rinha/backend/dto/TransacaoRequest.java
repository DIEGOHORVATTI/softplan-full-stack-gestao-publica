package com.rinha.backend.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class TransacaoRequest {
    @Positive(message = "Valor deve ser positivo")
    private Integer valor;

    @Pattern(regexp = "^[rd]$", message = "Tipo deve ser 'r' ou 'd'")
    private String tipo;

    @Size(min = 1, max = 10, message = "Descrição deve ter entre 1 e 10 caracteres")
    @NotBlank(message = "Descrição é obrigatória")
    private String descricao;
}