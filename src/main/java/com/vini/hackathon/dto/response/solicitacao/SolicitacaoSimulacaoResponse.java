package com.vini.hackathon.dto.response.solicitacao;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class SolicitacaoSimulacaoResponse {

    private UUID idSimulacao;

    private int codigoProduto;

    private String descricaoProduto;

    private BigDecimal taxaJuros;

    private List<ResultadoSimulacaoDTO> resultadoSimulacaoDTO;

}
