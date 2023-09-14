package com.zingiberfeel.nub.rpc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rabbitmq.client.*;
import com.zingiberfeel.nub.model.Snippet;
import com.zingiberfeel.nub.rpc.dao.SnippetDAO;
import com.zingiberfeel.nub.service.SnippetService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class RabbitRPC {

    @Autowired
    public RabbitRPC(SnippetService snippetService) {
        this.snippetService = snippetService;
    }

    private final SnippetService snippetService;

    private static final String RPC_QUEUE_NAME = "rpc_queue";

    public void run() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null);
        channel.queuePurge(RPC_QUEUE_NAME);

        channel.basicQos(1);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                    .Builder()
                    .correlationId(delivery.getProperties().getCorrelationId())
                    .build();

            String response = "";

            try {
                String message = new String(delivery.getBody(), "UTF-8");
                SnippetDAO snippetDAO = convertJsonToSnippetDAO(message);
                Snippet snippet = new Snippet(snippetDAO.getText(), snippetDAO.getExtension(), snippetDAO.getLifetime());
                snippetService.createSnippet(snippet);
                response = "localhost:8080/" + snippet.getHash();
            } catch (RuntimeException e) {
                System.out.println(" [.] " + e);
            } finally {
                channel.basicPublish("", delivery.getProperties().getReplyTo(), replyProps, response.getBytes("UTF-8"));
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
        };
        channel.basicConsume(RPC_QUEUE_NAME, false, deliverCallback, (consumerTag -> {}));
    }

    protected static SnippetDAO convertJsonToSnippetDAO(String jsonString) {
        try {
            ObjectMapper objectMapper = new ObjectMapper()
                    .registerModule(new JavaTimeModule());
            SnippetDAO snippet = objectMapper.readValue(jsonString, SnippetDAO.class);
            return snippet;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
