package org.clprolf.graph.workers;

import org.clprolf.framework.ClFamily;
import org.clprolf.framework.ClWorker;
import org.clprolf.graph.agents.impl.GraphImpl;

@ClWorker
@ClFamily
public interface GraphWorker {

    // Display graph structure
    void printGraph();

    void printAllResultingPaths();
    public void printPartialPaths();
}
