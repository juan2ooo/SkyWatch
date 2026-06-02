package com.skyWatch.msProof.Infrastructure.Adapters.In;

import com.skyWatch.msProof.Application.Ports.In.MessageListenerPort;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQConsumerAdapter {

    private final MessageListenerPort messageListenerPort;

    public RabbitMQConsumerAdapter(MessageListenerPort messageListenerPort) {
        this.messageListenerPort = messageListenerPort;
    }

    @RabbitListener(queues = "fotos.queue")
    public void receiveMessage(String message) {
        System.out.println("Mensaje recibido desde RabbitMQ: " + message);

        messageListenerPort.savePhoto(message);
    }
}