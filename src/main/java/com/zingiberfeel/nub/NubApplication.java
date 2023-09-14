package com.zingiberfeel.nub;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.zingiberfeel.nub.rpc.RabbitRPC;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class NubApplication {

    private final RabbitRPC rabbitRPC;

    public NubApplication(RabbitRPC rabbitRPC) {
        this.rabbitRPC = rabbitRPC;
    }

    public static void main(String[] args) {

        SpringApplication.run(NubApplication.class, args);
    }

    @PostConstruct
    public void startRabbitRPC() throws Exception {
        rabbitRPC.run();
    }
}
