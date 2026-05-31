package org.clprolf.examples.animal_app.agent.trait;

import org.clprolf.examples.animal_app.Animal;
import org.clprolf.examples.animal_app.agent.Dog;
import org.clprolf.examples.animal_app.agent.impl.AnimalImpl;
import org.clprolf.framework.Agent;
import org.clprolf.framework.Forc_int_inh;
import org.clprolf.framework.Trait_interf;

@Agent
@Trait_interf
public interface Growable extends Drinkable, Animal {
    void grow();
}
