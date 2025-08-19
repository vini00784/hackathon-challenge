package com.vini.hackathon.dto.response.solicitacao;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class SolicitacaoSimulacaoResponse {

    private int idSimulacao;

    private int codigoProduto;

    private String descricaoProduto;

    private double taxaJuros;

    private List<ResultadoSimulacaoDTO> resultadoSimulacaoDTO;

}
