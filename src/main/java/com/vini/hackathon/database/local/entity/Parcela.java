package com.vini.hackathon.database.local.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Parcela")
@Data
public class Parcela {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer numero;

    private Double valorAmortizacao;

    private Double valorJuros;

    private Double valorPrestacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resultado_id")
    private ResultadoSimulacao resultadoSimulacao;

}
