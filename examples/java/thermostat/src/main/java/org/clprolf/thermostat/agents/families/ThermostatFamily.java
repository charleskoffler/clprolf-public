package org.clprolf.thermostat.agents.families;

import org.clprolf.framework.ClAgent;
import org.clprolf.framework.ClFamily;
import org.clprolf.thermostat.agents.traits.MonitorableTrait;

// Mirror family of the domain regulation agent (target @ClAgent)
@ClFamily
@ClAgent
public interface ThermostatFamily extends MonitorableTrait {
    void evaluateTargetTemperature(double currentTemp);
}
