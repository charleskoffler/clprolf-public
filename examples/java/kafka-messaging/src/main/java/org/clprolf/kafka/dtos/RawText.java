package org.clprolf.kafka.dtos;

import org.clprolf.framework.ClDraft;

@ClDraft
public record RawText(
        String requestId,
        String content,
        long timestamp
) {}
