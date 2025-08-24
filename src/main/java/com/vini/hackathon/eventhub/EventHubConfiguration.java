package com.vini.hackathon.eventhub;

import com.azure.messaging.eventhubs.EventHubClientBuilder;
import com.azure.messaging.eventhubs.EventHubProducerClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventHubConfiguration {

    @Value("${eventhub.endpoint}")
    private String endpoint;

    @Value("${eventhub.shared-access-key-name}")
    private String sharedAccessKeyName;

    @Value("${eventhub.shared-access-key}")
    private String sharedAccessKey;

    @Value("${eventhub.entity-path}")
    private String entityPath;

    private String buildConnectionString() {
        return String.format(
                "Endpoint=%s;SharedAccessKeyName=%s;SharedAccessKey=%s;EntityPath=%s",
                endpoint, sharedAccessKeyName, sharedAccessKey, entityPath
        );
    }

    @Bean
    public EventHubProducerClient eventHubProducerClient() {
        return new EventHubClientBuilder()
                .connectionString(buildConnectionString())
                .buildProducerClient();
    }

}
