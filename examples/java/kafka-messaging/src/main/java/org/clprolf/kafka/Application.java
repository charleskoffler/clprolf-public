package org.clprolf.kafka;

import org.clprolf.framework.ClSystem;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * Broker startup:  docker compose up -d
 * Terminal input:  docker exec -it clprolf-kafka kafka-console-producer --bootstrap-server localhost:9092 --topic text-processing-send
 * Terminal output: docker exec -it clprolf-kafka kafka-console-consumer --bootstrap-server localhost:9092 --topic text-processing-response --from-beginning
 * Sample input:    {"requestId": "req-001", "content": "   Hello !   ", "timestamp": 178845000000 }
 * Sample output:   {"requestId":"req-001","originalContent":"   Hello !   ","cleanedContent":"Hello !","characterCount":7}
 */
@ClSystem
@EnableKafka // Enables Kafka topic listeners across the application context
@SpringBootApplication // Marks this class as a Spring Boot application and enables auto-configuration
public class Application {

    public static void main(String[] args) {
        // Bootstraps the framework and starts the embedded Spring container
        SpringApplication.run(Application.class, args);
    }
}