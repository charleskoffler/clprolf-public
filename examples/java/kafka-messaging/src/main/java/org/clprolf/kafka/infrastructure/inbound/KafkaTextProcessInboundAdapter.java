package org.clprolf.kafka.infrastructure.inbound;

import org.clprolf.framework.ClSystem;
import org.clprolf.kafka.domain.TextProcessingAgent;
import org.clprolf.kafka.dtos.ProcessedText;
import org.clprolf.kafka.dtos.RawText;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * [INBOUND ADAPTER / WORKER]
 * Belongs to the infrastructure layer. Acts as the Kafka consumer and orchestrator.
 */
@ClSystem
@Component
public class KafkaTextProcessInboundAdapter {
    private static final String INPUT_TOPIC = "text-processing-send";
    private static final String OUTPUT_TOPIC = "text-processing-response";

    private final TextProcessingAgent textProcessingAgent;
    private final KafkaTemplate<String, ProcessedText> kafkaTemplate;

    public KafkaTextProcessInboundAdapter(TextProcessingAgent agent,
                                   KafkaTemplate<String, ProcessedText> kafkaTemplate) {
        this.textProcessingAgent = agent;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = INPUT_TOPIC, groupId = "clprolf-group")
    public void consume(RawText rawText) {
        ProcessedText processedText = textProcessingAgent.process(rawText);
        kafkaTemplate.send(OUTPUT_TOPIC, processedText.requestId(), processedText);
    }
}
