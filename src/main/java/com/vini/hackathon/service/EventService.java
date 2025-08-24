package com.vini.hackathon.service;

import com.azure.messaging.eventhubs.EventData;
import com.azure.messaging.eventhubs.EventDataBatch;
import com.azure.messaging.eventhubs.EventHubProducerClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vini.hackathon.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EventService {

    private final EventHubProducerClient producerClient;
    private final ObjectMapper objectMapper;

    public EventService(EventHubProducerClient producerClient, ObjectMapper objectMapper) {
        this.producerClient = producerClient;
        this.objectMapper = objectMapper;
    }

    public void enviar(Object obj) {
        try {
            String jsonMessage = serialize(obj);
            EventDataBatch batch = buildBatch(jsonMessage);

            producerClient.send(batch);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("Erro ao enviar mensagem para Event Hub", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String serialize(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new BusinessException("Erro ao converter objeto para JSON", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private EventDataBatch buildBatch(String jsonMessage) {
        try {
            EventDataBatch batch = producerClient.createBatch();
            if (!batch.tryAdd(new EventData(jsonMessage))) {
                throw new BusinessException("Mensagem muito grande para ser adicionada ao batch", HttpStatus.BAD_REQUEST);
            }
            return batch;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("Erro ao preparar mensagem para Event Hub", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
