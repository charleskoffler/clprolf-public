package org.clprolf.thermostat.workers.families;

import org.clprolf.framework.ClFamily;
import org.clprolf.framework.ClWorker;

// Mirror family of the low-level worker associated with the system atom (target @ClWorker)
@ClFamily @ClWorker
public interface SocketDriverFamily {
    void writeRawBytes(byte[] frame);
}
