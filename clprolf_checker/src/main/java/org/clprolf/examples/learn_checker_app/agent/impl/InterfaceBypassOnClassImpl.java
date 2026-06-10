package org.clprolf.examples.learn_checker_app.agent.impl;
import org.clprolf.examples.learn_checker_app.agent.Animal;
import org.clprolf.examples.learn_checker_app.agent.MyWorker;
import org.clprolf.framework.*;

@ClInterfaceBypass
@ClBypass
@ClAgent
public class InterfaceBypassOnClassImpl implements Animal, MyWorker {
}
