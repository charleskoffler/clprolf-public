package org.clprolf.examples.animal_app.agent.impl;

import org.clprolf.examples.animal_app.agent.Dog;
import org.clprolf.examples.animal_app.agent.trait.Growable;
import org.clprolf.framework.Worker;

@Worker /* Bad role for a dog */
public class DogImpl extends AnimalImpl implements Dog, Growable /* Direct implementation */{

    @Override
    public void grow() {

    }

}
