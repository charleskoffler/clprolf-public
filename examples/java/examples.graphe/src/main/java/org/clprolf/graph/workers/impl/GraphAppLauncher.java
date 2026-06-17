package org.clprolf;

import org.clprolf.framework.ClWorker;
import org.clprolf.graph.agents.Graph;
import org.clprolf.graph.agents.impl.GraphImpl;

/**
 *
 *
 */

@ClWorker
public class GraphAppLauncher
{
    public static void main(String[] args) {
        int defaultStartNode = 1;
        Graph g = new GraphImpl(defaultStartNode);

        g.addEdge(1, 2);
        g.addEdge(1, 3);
        g.addEdge(2, 4);
        g.addEdge(2, 5);
        g.addEdge(3, 6, true);

        System.out.println("\nComputing all paths from default start node " + defaultStartNode);
        g.computeAllPathsFrom();
        System.out.println("\nAll paths are");
        g.computeNoFilter();
        System.out.println("\nThe longest paths are: ");
        g.computeLongestPathsFrom();

        int node = 2;
        System.out.println("\nCompute paths passing through " + 2);
        g.computePathsPassingThrough(node);

        System.out.println("\nComputing all terminal paths");
        g.computeTerminalPaths();

        System.out.println("\nRecursive DFS");
        g.recursiveDFS();

    }
}
