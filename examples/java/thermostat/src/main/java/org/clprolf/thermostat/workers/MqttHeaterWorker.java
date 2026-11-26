package org.clprolf.thermostat.workers;

import org.clprolf.framework.ClWorker;
import org.clprolf.thermostat.system.families.HardwareBusSystemFamily;
import org.clprolf.thermostat.workers.families.HeaterActuatorFamily;

// Technical executor of the business atom: translates the business command and relies on @ClSystem
@ClWorker
public class MqttHeaterWorker implements HeaterActuatorFamily {
    private final HardwareBusSystemFamily hardwareBus; // A @ClWorker can use a @ClSystem

    public MqttHeaterWorker(HardwareBusSystemFamily hardwareBus) {
        this.hardwareBus = hardwareBus;
    }

    @Override
    public void sendPowerCommand(boolean activate) {
        hardwareBus.publishFrame("home/heater/cmd", activate ? "ON" : "OFF");
    }
}