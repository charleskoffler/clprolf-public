package org.clprolf.thermostat.workers.families;

import org.clprolf.framework.ClFamily;
import org.clprolf.framework.ClWorker;

@ClFamily
@ClWorker
public interface HeaterActuatorFamily {
    void sendPowerCommand(boolean activate);
}

