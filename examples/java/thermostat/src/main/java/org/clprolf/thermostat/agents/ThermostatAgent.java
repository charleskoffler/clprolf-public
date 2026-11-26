package org.clprolf.thermostat.agents;

import org.clprolf.framework.ClAgent;
import org.clprolf.thermostat.agents.families.ThermostatFamily;
import org.clprolf.thermostat.workers.families.HeaterActuatorFamily;

// Domain regulation agent (carries the decision logic and functional state of the domain)
@ClAgent
public class ThermostatAgent implements ThermostatFamily {
    private final HeaterActuatorFamily actuator; // // Delegation to its mirror worker
    private final double targetTemp = 20.5;
    private double lastMeasuredTemp = 20.5;

    public ThermostatAgent(HeaterActuatorFamily actuator) {
        this.actuator = actuator;
    }

    @Override
    public void evaluateTargetTemperature(double currentTemp) {
        this.lastMeasuredTemp = currentTemp;
        actuator.sendPowerCommand(currentTemp < targetTemp);
    }

    @Override
    public String getDiagnosticStatus() {
        return "THERMOSTAT_ACTIVE (T=" + lastMeasuredTemp + "°C)";
    }
}
