package org.clprolf.graph.agents.impl;

import org.clprolf.framework.ClAgent;
import org.clprolf.graph.agents.Graph;
import org.clprolf.graph.workers.GraphWorker;
import org.clprolf.graph.workers.impl.GraphWorkerImpl;

import java.util.*;

@ClAgent
public class GraphImpl implements Graph {

    private final Map<Integer, List<Integer>> adj = new HashMap<>();
    private List<List<Integer>> resultingPaths;
    private List<List<Integer>> terminalPaths;
    private final int start;
    private final GraphWorker worker;

    public int getStart() {
        return start;
    }

    public Map<Integer, List<Integer>> getAdj() {
        return adj;
    }

    public List<List<Integer>> getResultingPaths() {
        return resultingPaths;
    }

    public List<List<Integer>> getTerminalPaths() {
        return terminalPaths;
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

        Stack<State> stack = new Stack<>();

        // Initial state
        stack.push(new State(start, List.of(start)));

        while (!stack.isEmpty()) {
            State state = stack.pop();
            int node = state.node;
            List<Integer> path = state.path;

            // Store the path
            allPaths.add(path);

            // Attention : List.of() renvoie une liste immuable. Si on veut faire un reverse,
            // il faut d'abord copier la liste pour pouvoir la modifier.
            List<Integer> neighbors = new ArrayList<>(adj.getOrDefault(node, List.of()));
            Collections.reverse(neighbors);

            // Explore neighbors
            for (int neighbor : neighbors) {

                if (!path.contains(neighbor)) {
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
                .mapToInt(path -> path.size())
                // equivalent .mapToInt(List::size)
                .max()
                .orElse(0);

        // 3. Filter only longest paths
        List<List<Integer>> longest = resultingPaths.stream()
                .filter(p -> p.size() == max)
                .toList();

        // 4. Store in terminalPaths
        this.terminalPaths = longest;

        this.worker.printTerminalPaths();
    }

    public void computePathsPassingThrough(int node) {
        if (resultingPaths == null) computeAllPathsFrom();

        terminalPaths = resultingPaths.stream()
                .filter(p -> p.contains(node))
                .toList();

        worker.printTerminalPaths();
    }

    public void computeTerminalPaths() {
        if (resultingPaths == null) computeAllPathsFrom();

        terminalPaths = resultingPaths.stream()
                .filter(path -> {
                    int last = path.getLast();
                    List<Integer> neighbors = adj.getOrDefault(last, List.of());
                    // terminal if all neighbors are already in the path
                    return neighbors.stream().allMatch(path::contains);
                })
                .toList();

        worker.printTerminalPaths();
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
