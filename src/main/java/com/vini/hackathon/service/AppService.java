package com.vini.hackathon.service;

import com.vini.hackathon.database.azure.repository.ProdutoRepository;
import com.vini.hackathon.dto.ControllerResponse;
import com.vini.hackathon.dto.request.SolicitacaoSimulacaoRequest;
import com.vini.hackathon.dto.response.solicitacao.SolicitacaoSimulacaoResponse;
import org.springframework.stereotype.Service;

@Service
public class AppService {

    private final ProdutoRepository produtoRepository;

    public AppService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    ControllerResponse<SolicitacaoSimulacaoResponse> solicitarSimulacaoCredito(SolicitacaoSimulacaoRequest req) {
        return new ControllerResponse<>();
    }

}
