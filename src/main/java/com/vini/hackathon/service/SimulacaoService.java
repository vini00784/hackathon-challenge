package com.vini.hackathon.service;

import com.vini.hackathon.database.azure.entity.Produto;
import com.vini.hackathon.database.azure.repository.ProdutoRepository;
import com.vini.hackathon.database.local.entity.Parcela;
import com.vini.hackathon.database.local.entity.ResultadoSimulacao;
import com.vini.hackathon.database.local.entity.Simulacao;
import com.vini.hackathon.database.local.repository.SimulacaoRepository;
import com.vini.hackathon.dto.ControllerResponse;
import com.vini.hackathon.dto.request.ListagemSimulacaoPorDataEProdRequest;
import com.vini.hackathon.dto.request.SolicitacaoSimulacaoRequest;
import com.vini.hackathon.dto.response.listagem.ListagemEspecificaSimulacoesResponse;
import com.vini.hackathon.dto.response.listagem.ListagemGeralSimulacoesResponse;
import com.vini.hackathon.dto.response.listagem.RegistroDTO;
import com.vini.hackathon.dto.response.listagem.SimulacaoDTO;
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

import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.vini.hackathon.utils.Calculadora.calculadoraPrice;
import static com.vini.hackathon.utils.Calculadora.calculadoraSac;
import static com.vini.hackathon.utils.DecimalUtils.roundValue;

@Slf4j
@Service
@AllArgsConstructor
public class SimulacaoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private SimulacaoRepository simulacaoRepository;

    @Autowired
    private EventService eventService;

    public ControllerResponse<SolicitacaoSimulacaoResponse> solicitarSimulacaoCredito(SolicitacaoSimulacaoRequest req) {
        Produto produtoEncontrado = produtoRepository.buscarProduto(req.getValorDesejado(), req.getPrazo());

        if(produtoEncontrado == null) {
            throw new BusinessException("Nenhum produto encontrado para os critérios informados", HttpStatus.NOT_FOUND);
        }

        SolicitacaoSimulacaoResponse response = new SolicitacaoSimulacaoResponse();
        response.setIdSimulacao(UUID.randomUUID());
        setDadosProduto(response, produtoEncontrado);
        setResultadoSimulacao(req, response, produtoEncontrado);

        try {
            Simulacao simulacao = mapToEntity(response, req);

            simulacaoRepository.save(simulacao);
        } catch (Exception e) {
            throw new BusinessException("Erro ao persistir simulação", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        eventService.enviar(response);

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

    @Transactional("localTransactionManager")
    public ControllerResponse<ListagemEspecificaSimulacoesResponse> listarSimulacoesPorDataEProduto(ListagemSimulacaoPorDataEProdRequest req) {
        ListagemEspecificaSimulacoesResponse response = new ListagemEspecificaSimulacoesResponse();
        LocalDate dataReferencia = LocalDate.parse(req.getDataReferencia());
        response.setDataReferencia(dataReferencia);

        boolean produtoEncontrado = produtoRepository.findById(req.getCodigoProduto()).isEmpty();

        if(produtoEncontrado) {
            throw new BusinessException("O produto informado não existe", HttpStatus.NOT_FOUND);
        }

        List<Simulacao> simulacoes = simulacaoRepository.listarPorDataEProduto(req.getCodigoProduto(), dataReferencia);

        if(simulacoes.isEmpty()) {
            throw new BusinessException("Nenhuma simulação encontrada para o produto " + req.getCodigoProduto(), HttpStatus.NOT_FOUND);
        }

        SimulacaoDTO dto = new SimulacaoDTO();
        dto.setCodigoProduto(req.getCodigoProduto());
        dto.setDescricaoProduto(simulacoes.get(0).getDescricaoProduto());

        // Cálculos

        // TODO: Revisar os cálculos abaixo
        double taxaMediaJuro = simulacoes.stream().mapToDouble(Simulacao::getTaxaJuros).average().orElse(0);
        dto.setTaxaMediaJuro(roundValue(taxaMediaJuro).doubleValue());

        double valorMedioPrestacao = simulacoes.stream().mapToDouble(Simulacao::getValorMedioPrestacao).average().orElse(0.0);
        dto.setValorMedioPrestacao(roundValue(valorMedioPrestacao).doubleValue());

        double valorTotalDesejado = simulacoes.stream().mapToDouble(Simulacao::getValorDesejado).sum();
        dto.setValorTotalDesejado(roundValue(valorTotalDesejado).doubleValue());

//        double valorTotalCredito = simulacoes.stream().mapToDouble(Simulacao::getValorCredito).sum();

        response.setSimulacoes(List.of(dto));

        return new ControllerResponse<ListagemEspecificaSimulacoesResponse>().setResponse(response);
    }

    private void setDadosProduto(SolicitacaoSimulacaoResponse response, Produto produto) {
        response.setCodigoProduto(produto.getCoProduto());
        response.setDescricaoProduto(produto.getNoProduto());
        response.setTaxaJuros(produto.getPcTaxaJuros().setScale(4, RoundingMode.DOWN));
    }

    private void setResultadoSimulacao(SolicitacaoSimulacaoRequest req, SolicitacaoSimulacaoResponse response, Produto produto) {
        List<ResultadoSimulacaoDTO> resultado = new ArrayList<>();

        resultado.add(calculadoraSac(req, produto));
        resultado.add(calculadoraPrice(req, produto));

        response.setResultadoSimulacaoDTO(resultado);
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
