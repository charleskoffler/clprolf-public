package org.clprolf.examples.animal_app.agent.impl;

import org.clprolf.examples.animal_app.agent.Animal;
import org.clprolf.examples.animal_app.agent.Dog;
import org.clprolf.framework.ClAgent;

@ClAgent
public class AnimalImpl implements Animal /* Bad inheritance */, Dog {
    @Override
    public void eat(int quantity) {
        System.out.println("Je mange " + quantity + " kg");
    }
}
