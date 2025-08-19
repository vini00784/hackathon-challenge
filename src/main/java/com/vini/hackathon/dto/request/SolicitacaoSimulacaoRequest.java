package com.vini.hackathon.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class SolicitacaoSimulacaoRequest {

    private double valorDesejado;

    private int prazo;

}
