package com.vini.hackathon.dto.response.listagem;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class RegistroDTO {

    private UUID idSimulacao;

    private double valorDesejado;

    private int prazo;

    private double valorTotalParcelas;

}
