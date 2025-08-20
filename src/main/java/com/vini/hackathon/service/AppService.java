package com.vini.hackathon.service;

import com.vini.hackathon.database.azure.entity.Produto;
import com.vini.hackathon.database.azure.repository.ProdutoRepository;
import com.vini.hackathon.dto.ControllerResponse;
import com.vini.hackathon.dto.request.SolicitacaoSimulacaoRequest;
import com.vini.hackathon.dto.response.solicitacao.ParcelaDTO;
import com.vini.hackathon.dto.response.solicitacao.ResultadoSimulacaoDTO;
import com.vini.hackathon.dto.response.solicitacao.SolicitacaoSimulacaoResponse;
import com.vini.hackathon.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class AppService {

    private final ProdutoRepository produtoRepository;

    public ControllerResponse<SolicitacaoSimulacaoResponse> solicitarSimulacaoCredito(SolicitacaoSimulacaoRequest req) throws BusinessException {
        Produto produtoEncontrado = produtoRepository.buscarProduto(req.getValorDesejado(), req.getPrazo());

        if(produtoEncontrado == null) {
            throw new BusinessException("Nenhum produto encontrado para os critérios informados");
        }

        SolicitacaoSimulacaoResponse response = new SolicitacaoSimulacaoResponse();
        setDadosProduto(response, produtoEncontrado);
        setResultadoSimulacao(req, response, produtoEncontrado);

        return new ControllerResponse<SolicitacaoSimulacaoResponse>().setResponse(response);
    }

    private void setDadosProduto(SolicitacaoSimulacaoResponse response, Produto produto) {
        response.setCodigoProduto(produto.getCoProduto());
        response.setDescricaoProduto(produto.getNoProduto());
        response.setTaxaJuros(produto.getPcTaxaJuros().setScale(4, RoundingMode.DOWN));
    }

    private void setResultadoSimulacao(SolicitacaoSimulacaoRequest req, SolicitacaoSimulacaoResponse response, Produto produto) {
        List<ResultadoSimulacaoDTO> resultado = new ArrayList<>();

        resultado.add(setSimulacaoSac(req, produto));
        resultado.add(setSimulacaoPrice(req, produto));

        response.setResultadoSimulacaoDTO(resultado);
    }

    private ResultadoSimulacaoDTO setSimulacaoSac(SolicitacaoSimulacaoRequest req, Produto produto) {
        ResultadoSimulacaoDTO simulacaoSac = new ResultadoSimulacaoDTO();
        simulacaoSac.setTipo("SAC");

        List<ParcelaDTO> parcelas = new ArrayList<>();
        double amortizacao = req.getValorDesejado() / req.getPrazo();

        for(int i = 1; i <= req.getPrazo(); i++) {
            ParcelaDTO parcela = new ParcelaDTO();

            double saldoDevedor = req.getValorDesejado() - (amortizacao * (i - 1));
            BigDecimal valorJuros = BigDecimal.valueOf(saldoDevedor).multiply(produto.getPcTaxaJuros()).setScale(2, RoundingMode.DOWN);

            parcela.setNumero(i);
            parcela.setValorAmortizacao(amortizacao);
            parcela.setValorJuros(valorJuros.doubleValue());
            parcela.setValorPrestacao(amortizacao + valorJuros.doubleValue());

            parcelas.add(parcela);
        }

        simulacaoSac.setParcelas(parcelas);

        return simulacaoSac;
    }

    private ResultadoSimulacaoDTO setSimulacaoPrice(SolicitacaoSimulacaoRequest req, Produto produto) {
        ResultadoSimulacaoDTO simulacaoPrice = new ResultadoSimulacaoDTO();
        simulacaoPrice.setTipo("PRICE");

        List<ParcelaDTO> parcelas = new ArrayList<>();

        double valorFinanciado = req.getValorDesejado();
        double taxaJuros = produto.getPcTaxaJuros().doubleValue();
        double prazo = req.getPrazo();

        double valorPrestacao = valorFinanciado * taxaJuros / (1 - Math.pow(1 + taxaJuros, -prazo));

        double saldoDevedor = valorFinanciado;

        for(int i = 1; i <= req.getPrazo(); i++) {
            ParcelaDTO parcela = new ParcelaDTO();

            double valorFinalFormatado = BigDecimal.valueOf(valorPrestacao).setScale(2, RoundingMode.DOWN).doubleValue();

            double valorJuros = saldoDevedor * produto.getPcTaxaJuros().doubleValue();
            valorJuros = BigDecimal.valueOf(valorJuros).setScale(2, RoundingMode.DOWN).doubleValue();

            double amortizacao = valorPrestacao - valorJuros;
            amortizacao = BigDecimal.valueOf(amortizacao).setScale(2, RoundingMode.DOWN).doubleValue();

            saldoDevedor -= amortizacao;

            parcela.setNumero(i);
            parcela.setValorAmortizacao(amortizacao);
            parcela.setValorJuros(valorJuros);
            parcela.setValorPrestacao(valorFinalFormatado);

            parcelas.add(parcela);
        }

        simulacaoPrice.setParcelas(parcelas);

        return simulacaoPrice;
    }

}
