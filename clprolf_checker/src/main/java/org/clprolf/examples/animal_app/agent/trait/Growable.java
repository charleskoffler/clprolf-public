package org.clprolf.examples.animal_app.agent.trait;

import org.clprolf.examples.animal_app.agent.Animal;
import org.clprolf.framework.ClAgent;
import org.clprolf.framework.ClTrait;

@ClAgent
@ClTrait
public interface Growable extends Drinkable, Animal /* Bad inheritance */ {
    void grow();
}
