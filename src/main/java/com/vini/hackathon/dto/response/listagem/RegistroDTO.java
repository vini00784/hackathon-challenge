package com.vini.hackathon.dto.response.listagem;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class RegistroDTO {

    private int idSimulacao;

    private double valorDesejado;

    private int prazo;

    private int valorTotalParcelas;

}
