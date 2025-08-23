package com.vini.hackathon.service;

import com.vini.hackathon.database.azure.entity.Produto;
import com.vini.hackathon.database.azure.repository.ProdutoRepository;
import com.vini.hackathon.database.local.entity.Parcela;
import com.vini.hackathon.database.local.entity.ResultadoSimulacao;
import com.vini.hackathon.database.local.entity.Simulacao;
import com.vini.hackathon.database.local.repository.SimulacaoRepository;
import com.vini.hackathon.dto.ControllerResponse;
import com.vini.hackathon.dto.request.SolicitacaoSimulacaoRequest;
import com.vini.hackathon.dto.response.listagem.ListagemGeralSimulacoesResponse;
import com.vini.hackathon.dto.response.listagem.RegistroDTO;
import com.vini.hackathon.dto.response.solicitacao.ParcelaDTO;
import com.vini.hackathon.dto.response.solicitacao.ResultadoSimulacaoDTO;
import com.vini.hackathon.dto.response.solicitacao.SolicitacaoSimulacaoResponse;
import com.vini.hackathon.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.vini.hackathon.utils.DecimalUtils.roundValue;

@Slf4j
@Service
@AllArgsConstructor
public class AppService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private SimulacaoRepository simulacaoRepository;

    public ControllerResponse<SolicitacaoSimulacaoResponse> solicitarSimulacaoCredito(SolicitacaoSimulacaoRequest req) throws BusinessException {
        Produto produtoEncontrado = produtoRepository.buscarProduto(req.getValorDesejado(), req.getPrazo());

        if(produtoEncontrado == null) {
            throw new BusinessException("Nenhum produto encontrado para os critérios informados", HttpStatus.NOT_FOUND);
        }

        SolicitacaoSimulacaoResponse response = new SolicitacaoSimulacaoResponse();
        response.setIdSimulacao(UUID.randomUUID());
        setDadosProduto(response, produtoEncontrado);
        setResultadoSimulacao(req, response, produtoEncontrado);

        // TODO: Enviar simuacao para o eventHub

        try {
            Simulacao simulacao = mapToEntity(response, req);

            simulacaoRepository.save(simulacao);
        } catch (Exception e) {
            throw new BusinessException("Erro ao persistir simulação", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ControllerResponse<SolicitacaoSimulacaoResponse>().setResponse(response);
    }

    @Transactional("localTransactionManager")
    public ControllerResponse<ListagemGeralSimulacoesResponse> listarSimulacoes(String pagina) {

        int qtdeTotalRegistros = (int) simulacaoRepository.count();

        if(qtdeTotalRegistros == 0) {
            throw new BusinessException("Nenhuma simulação realizada", HttpStatus.NOT_FOUND);
        }

        Pageable pageable = PageRequest.of(Integer.parseInt(pagina) - 1, 10);
        List<RegistroDTO> registros = simulacaoRepository.findAll(pageable)
                .stream()
                .map(sim -> {
                    RegistroDTO dto = new RegistroDTO();
                    dto.setIdSimulacao(sim.getIdSimulacao());
                    dto.setValorDesejado(sim.getValorDesejado());
                    dto.setPrazo(sim.getPrazo());
                    dto.setValorTotalParcelas(roundValue(sim.getValorTotalParcelas()).doubleValue());
                    return dto;
                })
                .toList();

        if(registros.isEmpty()) {
            throw new BusinessException("Nenhum registro localizado na página " + pagina, HttpStatus.NOT_FOUND);
        }

        ListagemGeralSimulacoesResponse response = new ListagemGeralSimulacoesResponse();
        response.setPagina(Integer.parseInt(pagina));
        response.setQtdRegistros(qtdeTotalRegistros);
        response.setQtdRegistrosPagina(registros.size());
        response.setRegistros(registros);

        return new ControllerResponse<ListagemGeralSimulacoesResponse>().setResponse(response);
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

    private Simulacao mapToEntity(SolicitacaoSimulacaoResponse response, SolicitacaoSimulacaoRequest request) {
        Simulacao simulacao = new Simulacao();
        simulacao.setIdSimulacao(response.getIdSimulacao());
        simulacao.setCodigoProduto(response.getCodigoProduto());
        simulacao.setDescricaoProduto(response.getDescricaoProduto());
        simulacao.setTaxaJuros(response.getTaxaJuros().doubleValue());
        simulacao.setValorDesejado(request.getValorDesejado());
        simulacao.setPrazo(request.getPrazo());

        // Converter resultados
        if (response.getResultadoSimulacaoDTO() != null) {
            response.getResultadoSimulacaoDTO().forEach(resultadoDTO -> {
                ResultadoSimulacao resultado = new ResultadoSimulacao();
                resultado.setTipo(resultadoDTO.getTipo());
                resultado.setSimulacao(simulacao);

                // Converter parcelas
                if (resultadoDTO.getParcelas() != null) {
                    resultadoDTO.getParcelas().forEach(parcelaDTO -> {
                        Parcela parcela = new Parcela();
                        parcela.setNumero(parcelaDTO.getNumero());
                        parcela.setValorAmortizacao(parcelaDTO.getValorAmortizacao());
                        parcela.setValorJuros(parcelaDTO.getValorJuros());
                        parcela.setValorPrestacao(parcelaDTO.getValorPrestacao());
                        parcela.setResultadoSimulacao(resultado);

                        resultado.getParcelas().add(parcela);
                    });
                }

                simulacao.getResultados().add(resultado);
            });
        }

        return simulacao;
    }

}
