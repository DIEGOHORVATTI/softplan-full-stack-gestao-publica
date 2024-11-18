package com.rinha.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "transacoes")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer valor;
    private String tipo;
    private String descricao;
    private LocalDateTime realizadaEm;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
}