package org.clprolf.examples.animal_app.agent;

import org.clprolf.framework.ClFamily;
import org.clprolf.framework.ClWorker;

@ClWorker // Bad role
@ClFamily
public interface Dog extends Animal /* Not same role in inheritance */{
}
