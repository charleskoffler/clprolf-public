package org.clprolf.kafka.dtos;

import org.clprolf.framework.ClDraft;

@ClDraft
public record ProcessedText(
        String requestId,
        String originalContent,
        String cleanedContent, // <-- Le champ est déclaré ici
        long characterCount
){}