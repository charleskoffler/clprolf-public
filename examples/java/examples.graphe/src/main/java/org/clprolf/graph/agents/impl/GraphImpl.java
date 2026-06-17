package org.clprolf.graph.agents.impl;

import org.clprolf.framework.ClAgent;
import org.clprolf.graph.agents.Graph;
import org.clprolf.graph.workers.GraphWorker;
import org.clprolf.graph.workers.impl.GraphWorkerImpl;

import java.util.*;

@ClAgent
public class GraphImpl implements Graph {

    private Map<Integer, List<Integer>> adj = new HashMap<>();
    private List<List<Integer>> resultingPaths;
    private List<List<Integer>> partialPaths;
    private int start;
    private GraphWorker worker;

    public int getStart() {
        return start;
    }

    public Map<Integer, List<Integer>> getAdj() {
        return adj;
    }

    public List<List<Integer>> getResultingPaths() {
        return resultingPaths;
    }

    public List<List<Integer>> getPartialPaths() {
        return partialPaths;
    }

    public void addEdge(int u, int v, boolean isLastEdge){
        this.addEdge(u,v);
        if (isLastEdge){
            this.worker.printGraph();
        }
    }

    public void addEdge(int u, int v) {
        adj.computeIfAbsent(u, k -> new ArrayList<>()).add(v);
        adj.computeIfAbsent(v, k -> new ArrayList<>()).add(u);
    }

    // Internal class to store DFS state
    @ClAgent
    static class State {
        int node;
        List<Integer> path;

        State(int node, List<Integer> path) {
            this.node = node;
            this.path = path;
        }
    }

    public GraphImpl(int defaultStartNode){
        this.start = defaultStartNode;
        this.worker = new GraphWorkerImpl(this);
    }

    public void computeAllPathsFrom() {
        List<List<Integer>> allPaths = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();
        Stack<State> stack = new Stack<>();

        // Initial state
        stack.push(new State(start, List.of(start)));

        while (!stack.isEmpty()) {
            State state = stack.pop();
            int node = state.node;
            List<Integer> path = state.path;

            if (visited.contains(node)) continue;
            visited.add(node);

            // Store the path
            allPaths.add(path);

            List<Integer> neighbors = adj.getOrDefault(node, List.of());
            Collections.reverse(neighbors); //optional. To have a natural order of children

            // Explore neighbors
            for (int neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    List<Integer> newPath = new ArrayList<>(path);
                    newPath.add(neighbor);
                    stack.push(new State(neighbor, newPath));
                }
            }
        }

        this.resultingPaths = allPaths;
    }

    public void computeNoFilter(){
        if (resultingPaths == null || resultingPaths.isEmpty()) {
            computeAllPathsFrom();
        }
        this.worker.printAllResultingPaths();
    }

    public void computeLongestPathsFrom() {

        // 1. Compute all paths only if not already done
        if (resultingPaths == null || resultingPaths.isEmpty()) {
            computeAllPathsFrom();
        }

        // 2. Find max length
        int max = resultingPaths.stream()
                .mapToInt(List::size)
                .max()
                .orElse(0);

        // 3. Filter only longest paths
        List<List<Integer>> longest = resultingPaths.stream()
                .filter(p -> p.size() == max)
                .toList();

        // 4. Store in partialPaths
        this.partialPaths = longest;

        this.worker.printPartialPaths();
    }

    public void computePathsPassingThrough(int node) {
        if (resultingPaths == null) computeAllPathsFrom();

        partialPaths = resultingPaths.stream()
                .filter(p -> p.contains(node))
                .toList();

        worker.printPartialPaths();
    }

    public void computeTerminalPaths() {
        if (resultingPaths == null) computeAllPathsFrom();

        partialPaths = resultingPaths.stream()
                .filter(path -> {
                    int last = path.get(path.size() - 1);
                    List<Integer> neighbors = adj.getOrDefault(last, List.of());
                    // terminal if all neighbors are already in the path
                    return neighbors.stream().allMatch(path::contains);
                })
                .toList();

        worker.printPartialPaths();
    }

    public void recursiveDFS() {
        Set<Integer> visited = new HashSet<>();
        recursiveDFS(start, visited);
    }

    private void recursiveDFS(int node, Set<Integer> visited) {
        if (visited.contains(node)) return;

        visited.add(node);
        System.out.println("Visited : " + node);

        for (int neighbor : adj.getOrDefault(node, Collections.emptyList())) {
            recursiveDFS(neighbor, visited);
        }
    }

}
