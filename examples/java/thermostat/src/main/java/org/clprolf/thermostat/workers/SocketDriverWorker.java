package org.clprolf.thermostat.workers;

// --- SYSTEM ATOM: HardwareBusSystem (@ClSystem) + SocketDriverWorker (@ClWorker) ---

import org.clprolf.framework.ClWorker;
import org.clprolf.thermostat.workers.families.SocketDriverFamily;

// Low-level worker associated with the system atom (purely operational, no functional state)
@ClWorker
public class SocketDriverWorker implements SocketDriverFamily {
    @Override
    public void writeRawBytes(byte[] frame) {
        System.out.println("[SOCKET] Sending raw " + frame.length + " bytes.");
    }
}
