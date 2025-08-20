package com.vini.hackathon.controller;

import com.vini.hackathon.database.azure.entity.Produto;
import com.vini.hackathon.database.azure.repository.ProdutoRepository;
import com.vini.hackathon.dto.ControllerResponse;
import com.vini.hackathon.dto.request.SolicitacaoSimulacaoRequest;
import com.vini.hackathon.dto.response.solicitacao.SolicitacaoSimulacaoResponse;
import com.vini.hackathon.exception.BusinessException;
import com.vini.hackathon.service.AppService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class AppController {

    private final ProdutoRepository produtoRepository;
    private final AppService appService;

    public AppController(ProdutoRepository produtoRepository, AppService appService) {
        this.produtoRepository = produtoRepository;
        this.appService = appService;
    }

    @PostMapping( value = "/test-get", produces = MediaType.APPLICATION_JSON_VALUE )
    public ControllerResponse<List<Produto>> testGet(@RequestBody SolicitacaoSimulacaoRequest solicitacaoSimulacaoRequest) {
        ControllerResponse<List<Produto>> controllerResponse = new ControllerResponse<>();
        controllerResponse.setResponse(produtoRepository.findAll());
        return controllerResponse;
    }

    @PostMapping( value = "/realizar-simulacao", produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<ControllerResponse<SolicitacaoSimulacaoResponse>> testGetProduto(@RequestBody SolicitacaoSimulacaoRequest solicitacaoSimulacaoRequest) throws BusinessException {
        return ResponseEntity.ok(appService.solicitarSimulacaoCredito(solicitacaoSimulacaoRequest));
    }

}
