package com.vini.hackathon.utils;

import com.vini.hackathon.database.azure.entity.Produto;
import com.vini.hackathon.dto.request.SolicitacaoSimulacaoRequest;
import com.vini.hackathon.dto.response.solicitacao.ParcelaDTO;
import com.vini.hackathon.dto.response.solicitacao.ResultadoSimulacaoDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.vini.hackathon.utils.DecimalUtils.roundValue;

public class Calculadora {

    public static ResultadoSimulacaoDTO calculadoraSac(SolicitacaoSimulacaoRequest req, Produto produto) {
        ResultadoSimulacaoDTO simulacaoSac = new ResultadoSimulacaoDTO();
        simulacaoSac.setTipo("SAC");

        List<ParcelaDTO> parcelas = new ArrayList<>();
        double amortizacao = req.getValorDesejado() / req.getPrazo();

        for(int i = 1; i <= req.getPrazo(); i++) {
            ParcelaDTO parcela = new ParcelaDTO();

            double saldoDevedor = req.getValorDesejado() - (amortizacao * (i - 1));
            BigDecimal valorJuros = roundValue(saldoDevedor).multiply(produto.getPcTaxaJuros());
            double valorPrestacao = roundValue(amortizacao + valorJuros.doubleValue()).doubleValue();

            parcela.setNumero(i);
            parcela.setValorAmortizacao(amortizacao);
            parcela.setValorJuros(valorJuros.doubleValue());
            parcela.setValorPrestacao(valorPrestacao);

            parcelas.add(parcela);
        }

        simulacaoSac.setParcelas(parcelas);

        return simulacaoSac;
    }

    public static ResultadoSimulacaoDTO calculadoraPrice(SolicitacaoSimulacaoRequest req, Produto produto) {
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

            double valorFinalFormatado = roundValue(valorPrestacao).doubleValue();

            double valorJuros = saldoDevedor * produto.getPcTaxaJuros().doubleValue();
            valorJuros = roundValue(valorJuros).doubleValue();

            double amortizacao = valorPrestacao - valorJuros;
            amortizacao = roundValue(amortizacao).doubleValue();

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
