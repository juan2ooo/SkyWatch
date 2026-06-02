package com.skyWatch.msProof.Infrastructure.Configuration;

import com.skyWatch.msProof.Application.Ports.In.MessageListenerPort;
import com.skyWatch.msProof.Application.Ports.Out.DeliveryRepositoryPort;
import com.skyWatch.msProof.Application.Services.DeliveryService;
import com.skyWatch.msProof.Infrastructure.Adapters.Out.DeliveryMongoRepository;
import com.skyWatch.msProof.Infrastructure.Adapters.Out.DeliveryRepositoryAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.ObjectMapper;

@Configuration
public class BeanConfiguration {

    @Bean
    public MessageListenerPort messageListenerPort(
            DeliveryRepositoryPort repositoryPort,
            ObjectMapper objectMapper,
            RestTemplate restTemplate
    ) {
        return new DeliveryService(repositoryPort, objectMapper, restTemplate);
    }

    @Bean
    public DeliveryRepositoryPort deliveryRepositoryPort(
            DeliveryMongoRepository repository
    ) {
        return new DeliveryRepositoryAdapter(repository);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
