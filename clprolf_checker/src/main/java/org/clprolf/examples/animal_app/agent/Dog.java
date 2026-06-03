package org.clprolf.examples.animal_app.agent;

import org.clprolf.framework.Agent;
import org.clprolf.framework.Family_interf;
import org.clprolf.framework.Worker;

@Worker // Bad role
@Family_interf
public interface Dog extends Animal /* Not same role in inheritance */{
}
