package org.clprolf.examples.animal_app.agent.impl;

import org.clprolf.examples.animal_app.Animal;
import org.clprolf.examples.animal_app.agent.Dog;
import org.clprolf.examples.animal_app.agent.trait.Growable;
import org.clprolf.framework.Worker;

@Worker
public class DogImpl extends AnimalImpl implements Dog, Animal, Growable {

    @Override
    public void grow() {

    }

    public static class MyJavaClass {
    }
}
