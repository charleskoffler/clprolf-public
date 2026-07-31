package org.clprolf.graph.workers;

import org.clprolf.framework.ClFamily;
import org.clprolf.framework.ClWorker;

@ClWorker
@ClFamily
public interface GraphWorker {

    // Display graph structure
    void printGraph();

    void printAllResultingPaths();
    public void printTerminalPaths();
}
