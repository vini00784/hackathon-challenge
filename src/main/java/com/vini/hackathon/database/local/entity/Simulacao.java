package com.vini.hackathon.database.local.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "simulacao", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ResultadoSimulacao> resultados = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Double getValorTotalParcelas() {
        return resultados.stream()
                .flatMap(r -> r.getParcelas().stream())
                .mapToDouble(Parcela::getValorPrestacao)
                .sum();
    }

}
