using Clprolf.ArchUnitNet.Attributes;
using Clprolf.Examples.Graph.Workers;
using Clprolf.Examples.Graphs.Agents.Org.Clprolf.Graph.Agents;
using Clprolf.Examples.Graphs.Worker.Impl.Clprolf.Examples.Graphs.Worker.Impl;
using System;
using System.Collections.Generic;
using System.Text;

namespace Clprolf.Examples.Graphs.Agents.Impl
{
    [ClAgent]
    public class GraphImpl : IGraph
    {
        private readonly Dictionary<int, List<int>> _adj = new Dictionary<int, List<int>>();
        private readonly IGraphWorker _worker;

        // Propriétés C# (Getters automatiques)
        public int Start { get; }
        public Dictionary<int, List<int>> Adj => _adj;
        public List<List<int>> ResultingPaths { get; private set; }
        public List<List<int>> PartialPaths { get; private set; }

        public GraphImpl(int defaultStartNode)
        {
            Start = defaultStartNode;
            _worker = new GraphWorkerImpl(this);
        }

        public void AddEdge(int u, int v, bool isLastEdge)
        {
            AddEdge(u, v);
            if (isLastEdge)
            {
                _worker.PrintGraph();
            }
        }

        public void AddEdge(int u, int v)
        {
            // Équivalent C# moderne de computeIfAbsent
            if (!_adj.TryGetValue(u, out var listU))
            {
                listU = new List<int>();
                _adj[u] = listU;
            }
            listU.Add(v);

            if (!_adj.TryGetValue(v, out var listV))
            {
                listV = new List<int>();
                _adj[v] = listV;
            }
            listV.Add(u);
        }

        // Classe interne pour stocker l'état du DFS
        [ClAgent]
        private class State
        {
            public int Node { get; }
            public List<int> Path { get; }

            public State(int node, List<int> path)
            {
                Node = node;
                Path = path;
            }
        }

        public void ComputeAllPathsFrom()
        {
            var allPaths = new List<List<int>>();
            var visited = new HashSet<int>();
            var stack = new Stack<State>();

            // État initial
            stack.Push(new State(Start, new List<int> { Start }));

            while (stack.Count > 0)
            {
                var state = stack.Pop();
                int node = state.Node;
                var path = state.Path;

                if (visited.Contains(node)) continue;
                visited.Add(node);

                // Enregistrer le chemin
                allPaths.Add(path);

                // Récupérer les voisins (Sécurité null avec GetValueOrDefault)
                var neighbors = _adj.GetValueOrDefault(node, new List<int>());

                // Inversion en place (comme Collections.reverse)
                neighbors.Reverse();

                // Explorer les voisins
                foreach (int neighbor in neighbors)
                {
                    if (!visited.Contains(neighbor))
                    {
                        var newPath = new List<int>(path) { neighbor };
                        stack.Push(new State(neighbor, newPath));
                    }
                }
            }

            ResultingPaths = allPaths;
        }

        public void ComputeNoFilter()
        {
            if (ResultingPaths == null || ResultingPaths.Count == 0)
            {
                ComputeAllPathsFrom();
            }
            _worker.PrintAllResultingPaths();
        }

        public void ComputeLongestPathsFrom()
        {
            // 1. Calculer si ce n'est pas déjà fait
            if (ResultingPaths == null || ResultingPaths.Count == 0)
            {
                ComputeAllPathsFrom();
            }

            // 2. Trouver la longueur max (LINQ gère le type primitif en direct)
            int max = ResultingPaths.Count > 0
                ? ResultingPaths.Max(p => p.Count)
                : 0;

            // 3. Filtrer les chemins les plus longs avec LINQ
            List<List<int>> longest = ResultingPaths
                .Where(p => p.Count == max)
                .ToList();

            // 4. Stocker et notifier
            PartialPaths = longest;
            _worker.PrintPartialPaths();
        }

        public void ComputePathsPassingThrough(int node)
        {
            if (ResultingPaths == null) ComputeAllPathsFrom();

            PartialPaths = ResultingPaths
                .Where(p => p.Contains(node))
                .ToList();

            _worker.PrintPartialPaths();
        }

        public void ComputeTerminalPaths()
        {
            if (ResultingPaths == null) ComputeAllPathsFrom();

            PartialPaths = ResultingPaths
                .Where(path =>
                {
                    int last = path[^1]; // Syntaxe C# moderne [^1] pour prendre le dernier élément
                    var neighbors = _adj.GetValueOrDefault(last, new List<int>());

                    // Terminal si tous les voisins sont déjà dans le chemin (All de LINQ)
                    return neighbors.All(path.Contains);
                })
                .ToList();

            _worker.PrintPartialPaths();
        }

        public void RecursiveDFS()
        {
            var visited = new HashSet<int>();
            RecursiveDFS(Start, visited);
        }

        private void RecursiveDFS(int node, HashSet<int> visited)
        {
            if (visited.Contains(node)) return;

            visited.Add(node);
            Console.WriteLine("Visited : " + node);

            var neighbors = _adj.GetValueOrDefault(node, new List<int>());
            foreach (int neighbor in neighbors)
            {
                RecursiveDFS(neighbor, visited);
            }
        }
    }
}
