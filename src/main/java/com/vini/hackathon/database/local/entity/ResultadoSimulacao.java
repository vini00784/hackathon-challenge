package com.vini.hackathon.database.local.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ResultadoSimulacao")
@Data
public class ResultadoSimulacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "simulacao_id")
    private Simulacao simulacao;

    @OneToMany(mappedBy = "resultadoSimulacao", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Parcela> parcelas = new ArrayList<>();

}
