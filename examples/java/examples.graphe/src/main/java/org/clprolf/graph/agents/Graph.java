package org.clprolf.graph.agents;

import org.clprolf.framework.ClAgent;
import org.clprolf.framework.ClFamily;

import org.clprolf.graph.workers.GraphWorker;
import org.clprolf.graph.workers.impl.GraphWorkerImpl;

import java.util.*;

@ClAgent
@ClFamily
public interface Graph {

    /**
     * Getting the default node
     * @return
     */
    public int getStart();

    Map<Integer, List<Integer>> getAdj() ;

    /**
     * Getting all the paths found
     * @return
     */
    List<List<Integer>> getResultingPaths() ;

    /**
     * Getting the paths found while applying the current filter
     * @return
     */
    List<List<Integer>> getPartialPaths();

    /**
     *  Add an edge (undirected graph)
      */
    void addEdge(int u, int v);

    /**
     * Add an edge. Especially used to indicate that it is the last edge of the graph
     * @param u
     * @param v
     * @param isLastEdge
     */
    void addEdge(int u, int v, boolean isLastEdge);

    /**
     * All the paths with no filters
     */
    void computeNoFilter();

    /**
     * DFS that compute all paths starting from the given node, before calling the filters.
      */
    void computeAllPathsFrom();

    /**
     * Longest paths filter
     */
    void computeLongestPathsFrom();

    void computePathsPassingThrough(int node);

    void computeTerminalPaths();

    /**
     * Recursive DFS
     */
    void recursiveDFS();

}
