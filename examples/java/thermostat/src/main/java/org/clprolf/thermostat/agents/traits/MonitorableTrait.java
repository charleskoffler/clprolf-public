package org.clprolf.thermostat.agents.traits;

import org.clprolf.framework.ClAgent;
import org.clprolf.framework.ClSystem;
import org.clprolf.framework.ClTrait;

// Cross-cutting capability allowed only for @ClAgent and @ClSystem roles
@ClTrait
@ClAgent @ClSystem
public interface MonitorableTrait {
    String getDiagnosticStatus();
}
