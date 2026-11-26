package org.clprolf.thermostat.workers;

import org.clprolf.framework.ClWorker;
import org.clprolf.thermostat.agents.ThermostatAgent;
import org.clprolf.thermostat.system.HardwareBusSystem;
import org.clprolf.thermostat.system.families.HardwareBusSystemFamily;
import org.clprolf.thermostat.workers.families.HeaterActuatorFamily;
import org.clprolf.thermostat.workers.families.SocketDriverFamily;

// Autonomous worker for launching
@ClWorker
public class Main {
    public static void main(String[] args) {
        SocketDriverFamily socketDriver = new SocketDriverWorker();
        HardwareBusSystemFamily hardwareBusSystem = new HardwareBusSystem(socketDriver);
        HeaterActuatorFamily thermWorker = new MqttHeaterWorker(hardwareBusSystem);
        ThermostatAgent thermAgent = new ThermostatAgent(thermWorker);
        thermAgent.evaluateTargetTemperature(28.0);
        thermAgent.evaluateTargetTemperature(10.5);
    }
}