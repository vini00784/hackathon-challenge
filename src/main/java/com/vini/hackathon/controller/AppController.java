package com.vini.hackathon.controller;

import com.vini.hackathon.database.azure.entity.Produto;
import com.vini.hackathon.database.azure.repository.ProdutoRepository;
import com.vini.hackathon.dto.ControllerResponse;
import com.vini.hackathon.dto.request.SolicitacaoSimulacaoRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class AppController {

    private final ProdutoRepository produtoRepository;

    public AppController(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @PostMapping( value = "/test-get", produces = MediaType.APPLICATION_JSON_VALUE )
    public ControllerResponse<List<Produto>> testGet(@RequestBody SolicitacaoSimulacaoRequest solicitacaoSimulacaoRequest) {
        ControllerResponse<List<Produto>> controllerResponse = new ControllerResponse<>();
        controllerResponse.setResponse(produtoRepository.findAll());
        return controllerResponse;
    }

}
