namespace Clprolf.Examples.Graphs.Worker.Impl
{
  
    using global::Clprolf.ArchUnitNet.Attributes;
    using global::Clprolf.Examples.Graph.Workers;
    using global::Clprolf.Examples.Graphs.Agents.Org.Clprolf.Graph.Agents;
  
    using System;
    using System.Collections.Generic;

    namespace Clprolf.Examples.Graphs.Worker.Impl
    {
        [ClWorker]
        public class GraphWorkerImpl : IGraphWorker
        {
            private readonly IGraph _graph;

            // Le constructeur reçoit l'implémentation concrète (GraphImpl) comme en Java
            public GraphWorkerImpl(IGraph theGraph)
            {
                _graph = theGraph;
            }

            // Display graph structure
            public void PrintGraph()
            {
                var adj = _graph.Adj;

                // En C#, on itère directement sur le dictionnaire (pas besoin de entrySet)
                foreach (var entry in adj)
                {
                    // string.Join permet d'afficher proprement le contenu de la List<int>
                    Console.WriteLine($"{entry.Key} -> [{string.Join(", ", entry.Value)}]");
                }
            }

            public void PrintAllResultingPaths()
            {
                PrintAllResultingPaths(null);
            }

            public void PrintPartialPaths()
            {
                PrintAllResultingPaths(_graph.PartialPaths);
            }

            private void PrintAllResultingPaths(List<List<int>> myChosenPaths)
            {
                // Utilisation de l'opérateur de coalescence nulle (??) de C# :
                // Si myChosenPaths n'est pas null, on l'utilise, sinon on prend _graph.ResultingPaths
                List<List<int>> paths = myChosenPaths ?? _graph.ResultingPaths;

                if (paths != null)
                {
                    foreach (List<int> path in paths)
                    {
                        Console.WriteLine($"Path: [{string.Join(", ", path)}]");
                    }
                }
            }
        }
    }
}
