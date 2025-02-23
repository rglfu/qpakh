package com.app.docker.compose.service.connection;

import org.springframework.boot.autoconfigure.kafka.KafkaConnectionDetails;
import org.springframework.boot.docker.compose.core.RunningService;
import org.springframework.boot.docker.compose.service.connection.DockerComposeConnectionDetailsFactory;
import org.springframework.boot.docker.compose.service.connection.DockerComposeConnectionSource;

import java.util.Collections;
import java.util.List;

class KafkaDockerComposeConnectionDetailsFactory extends DockerComposeConnectionDetailsFactory<KafkaConnectionDetails> {
    private static final String[] KAFKA_CONTAINER_NAMES = {"kafka", "apache/kafka"};
    private static final int KAFKA_PORT = 9092;

    KafkaDockerComposeConnectionDetailsFactory() {
        super(KAFKA_CONTAINER_NAMES);
    }

    @Override
    protected KafkaConnectionDetails getDockerComposeConnectionDetails(DockerComposeConnectionSource source) {
        return new KafkaDockerComposeConnectionDetails(source.getRunningService());
    }

    static class KafkaDockerComposeConnectionDetails extends DockerComposeConnectionDetails
            implements KafkaConnectionDetails {
        private final String bootstrapServer;

        KafkaDockerComposeConnectionDetails(RunningService service) {
            super(service);
            this.bootstrapServer = service.host() + ":" + service.ports().get(KAFKA_PORT);
        }

        @Override
        public List<String> getBootstrapServers() {
            return Collections.singletonList(bootstrapServer);
        }
    }
}