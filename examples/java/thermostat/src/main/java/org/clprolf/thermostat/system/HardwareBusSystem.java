package org.clprolf.thermostat.system;

import org.clprolf.framework.ClSystem;
import org.clprolf.thermostat.system.families.HardwareBusSystemFamily;
import org.clprolf.thermostat.workers.families.SocketDriverFamily;

// System agent governing access to the hardware bus (used only by @ClWorker or @ClSystem)
@ClSystem
public class HardwareBusSystem implements HardwareBusSystemFamily {
    private final SocketDriverFamily socketDriver; // // Delegation to its associated worker
    private boolean busReady = true;

    public HardwareBusSystem(SocketDriverFamily socketDriver) {
        this.socketDriver = socketDriver;
    }

    @Override
    public void publishFrame(String topic, String payload) {
        if (busReady) {
            socketDriver.writeRawBytes((topic + ":" + payload).getBytes());
        }
    }

    @Override
    public String getDiagnosticStatus() {
        return busReady ? "BUS_READY" : "BUS_DOWN";
    }
}
