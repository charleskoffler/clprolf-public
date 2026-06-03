package org.clprolf.examples.animal_app.agent;

import org.clprolf.framework.Agent;
import org.clprolf.framework.Family_interf;
import org.clprolf.framework.Worker;

@Agent
@Family_interf
public interface Animal {
    public void eat(int quantity);
}
