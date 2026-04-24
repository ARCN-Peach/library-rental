package com.library.rental.infrastructure.adapters;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.rental.application.port.DomainEventPublisher;
import com.library.rental.domain.event.DomainEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RabbitDomainEventPublisher implements DomainEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    private final String exchangeName;

    public RabbitDomainEventPublisher(RabbitTemplate rabbitTemplate,
                                      ObjectMapper objectMapper,
                                      @Value("${app.rabbit.exchange:rental}") String exchangeName) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
        this.exchangeName = exchangeName;
    }

    @Override
    public void publish(DomainEvent event) {
        try {
            var message = new DomainEventMessage(event.type(), event.occurredAt(), objectMapper.valueToTree(event));
            rabbitTemplate.convertAndSend(exchangeName, event.type(), message);
        } catch (Exception exception) {
            throw new IllegalStateException("No se pudo publicar el evento en RabbitMQ", exception);
        }
    }

    private record DomainEventMessage(String type, java.time.Instant occurredAt, JsonNode payload) {
    }
}