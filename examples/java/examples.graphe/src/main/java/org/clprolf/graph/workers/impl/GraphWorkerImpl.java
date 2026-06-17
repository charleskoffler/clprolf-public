package org.clprolf.graph.workers.impl;

import org.clprolf.framework.ClWorker;
import org.clprolf.graph.agents.Graph;
import org.clprolf.graph.agents.impl.GraphImpl;
import org.clprolf.graph.workers.GraphWorker;

import java.util.List;
import java.util.Map;

@ClWorker
public class GraphWorkerImpl implements GraphWorker {
    private Graph graph;

    public GraphWorkerImpl(Graph theGraph){
        this.graph = theGraph;
    }

    // Display graph structure
    public void printGraph() {
        Map<Integer, List<Integer>> adj = graph.getAdj();
        for (var entry : adj.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }

    public void printAllResultingPaths() {
        printAllResultingPaths(null);
    }

    public void printPartialPaths(){
        printAllResultingPaths(this.graph.getPartialPaths());
    }

    private void printAllResultingPaths(List<List<Integer>> myChosenPaths) {
        List<List<Integer>> paths;

        if (myChosenPaths != null) {
            paths = myChosenPaths;
        }
        else {
            paths = graph.getResultingPaths();
        }
        // Display paths separately
        for (List<Integer> path : paths) {
            System.out.println("Path: " + path);
        }
    }
}
