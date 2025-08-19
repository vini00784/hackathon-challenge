package com.vini.hackathon.dto.response.solicitacao;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class ParcelaDTO {

    private int numero;

    private double valorAmortizacao;

    private double valorJuros;

    private double valorPrestacao;

}
