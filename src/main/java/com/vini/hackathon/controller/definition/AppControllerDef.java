package com.vini.hackathon.controller.definition;

import com.vini.hackathon.dto.ControllerResponse;
import com.vini.hackathon.dto.request.SolicitacaoSimulacaoRequest;
import com.vini.hackathon.dto.response.listagem.ListagemGeralSimulacoesResponse;
import com.vini.hackathon.dto.response.solicitacao.SolicitacaoSimulacaoResponse;
import com.vini.hackathon.exception.BusinessException;
import org.springframework.http.ResponseEntity;

// TODO: Posteriormente adicionar as tags para o swagger
public interface AppControllerDef {

    ResponseEntity<ControllerResponse<SolicitacaoSimulacaoResponse>> realizarSimulacao(SolicitacaoSimulacaoRequest solicitacaoSimulacaoRequest) throws BusinessException;

    ResponseEntity<ControllerResponse<ListagemGeralSimulacoesResponse>> listarSimulacoes(String pagina) throws BusinessException;

}
