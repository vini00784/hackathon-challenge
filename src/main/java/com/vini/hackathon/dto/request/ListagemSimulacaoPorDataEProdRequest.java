package com.vini.hackathon.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class ListagemSimulacaoPorDataEProdRequest {

    @Pattern(regexp = "\\d{2}-\\d{2}-\\d{4}", message = "Data deve estar no formato dd-MM-yyyy")
    private String dataReferencia;

    private Integer codigoProduto;

}
