package org.clprolf.kafka.domain;

import org.clprolf.framework.ClAgent;
import org.clprolf.framework.ClFamily;
import org.clprolf.kafka.dtos.ProcessedText;
import org.clprolf.kafka.dtos.RawText;

@ClAgent
@ClFamily
public interface TextProcessingAgent {
    ProcessedText process(RawText rawText);
}
