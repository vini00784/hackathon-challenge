package com.vini.hackathon.database.local.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "Simulacao")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Simulacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID idSimulacao;

    private Integer codigoProduto;

    private String descricaoProduto;

    private Double taxaJuros;

    private Double valorDesejado;

    private Integer prazo;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDate createdAt;

    @OneToMany(mappedBy = "simulacao", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ResultadoSimulacao> resultados = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDate.now();
    }

    public Double getValorTotalParcelas() {
        return resultados.stream()
                .collect(Collectors.groupingBy(ResultadoSimulacao::getTipo))
                .values().stream()
                .map(resultadosPorTipo -> resultadosPorTipo.stream()
                        .flatMap(r -> r.getParcelas().stream())
                        .mapToDouble(Parcela::getValorPrestacao)
                        .sum())
                .min(Double::compare)
                .orElse(0.0);
    }

    public Double getValorMedioPrestacao() {
        return resultados.stream()
                .flatMap(r -> r.getParcelas().stream())
                .mapToDouble(Parcela::getValorPrestacao)
                .average()
                .orElse(0.0);
    }

}
