package com.vini.hackathon.controller.definition;

import com.vini.hackathon.dto.ControllerResponse;
import com.vini.hackathon.dto.request.SolicitacaoSimulacaoRequest;
import com.vini.hackathon.dto.response.listagem.ListagemGeralSimulacoesResponse;
import com.vini.hackathon.dto.response.solicitacao.SolicitacaoSimulacaoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "SIMULACAO")
public interface SimulacaoControllerDef {

    @Operation(
            summary = "Realiza simulação - POST"
    )
    ResponseEntity<ControllerResponse<SolicitacaoSimulacaoResponse>> realizarSimulacao(SolicitacaoSimulacaoRequest solicitacaoSimulacaoRequest);

    @Operation(
            summary = "Lista simulações - GET"
    )
    ResponseEntity<ControllerResponse<ListagemGeralSimulacoesResponse>> listarSimulacoes(String pagina);

}
