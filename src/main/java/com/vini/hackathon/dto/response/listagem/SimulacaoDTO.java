package com.vini.hackathon.dto.response.listagem;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class SimulacaoDTO {

    private int codigoProduto;

    private String descricaoProduto;

    private double taxaMediaJuro;

    private double valorMedioPrestacao;

    private double valorTotalDesejado;

    private double valorTotalCredito;

}
