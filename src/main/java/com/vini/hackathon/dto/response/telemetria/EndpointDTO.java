package com.vini.hackathon.dto.response.telemetria;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class EndpointDTO {

    private String nomeApi;

    private int qtdRequisicoes;

    private int tempoMedio;

    private int tempoMaximo;

    private double percentualSucesso;

}
