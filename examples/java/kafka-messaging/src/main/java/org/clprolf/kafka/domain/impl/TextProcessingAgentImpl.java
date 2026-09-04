package org.clprolf.kafka.domain.impl;

import org.clprolf.framework.ClAgent;
import org.clprolf.kafka.domain.TextProcessingAgent;
import org.clprolf.kafka.dtos.ProcessedText;
import org.clprolf.kafka.dtos.RawText;
import org.springframework.stereotype.Service;

/**
 * [DOMAIN / USE CASE]
 * The core business logic of the application (Hexagonal Core).
 */
@Service
@ClAgent
public class TextProcessingAgentImpl implements TextProcessingAgent {

    public TextProcessingAgentImpl() {
    }

    @Override
    public ProcessedText process(RawText rawText) {
        String rawContent = rawText.content() != null ? rawText.content() : "";
        String cleaned = rawContent.trim();
        long count = cleaned.length();

        ProcessedText processed = new ProcessedText(
                rawText.requestId(),
                rawContent,
                cleaned,
                count
        );

        return processed;
    }
}