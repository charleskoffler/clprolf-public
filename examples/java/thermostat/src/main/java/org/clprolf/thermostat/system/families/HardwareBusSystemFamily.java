package org.clprolf.thermostat.system.families;

import org.clprolf.framework.ClFamily;
import org.clprolf.framework.ClSystem;
import org.clprolf.thermostat.agents.traits.MonitorableTrait;

// Mirror family of the hardware bus system agent (target @ClSystem)
@ClFamily
@ClSystem
public interface HardwareBusSystemFamily extends MonitorableTrait {
    void publishFrame(String topic, String payload);
}