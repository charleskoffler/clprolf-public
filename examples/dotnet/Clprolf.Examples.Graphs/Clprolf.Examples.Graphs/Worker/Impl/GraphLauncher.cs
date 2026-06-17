using Clprolf.ArchUnitNet.Attributes;
using Clprolf.Examples.Graphs.Agents.Impl;
using Clprolf.Examples.Graphs.Agents.Org.Clprolf.Graph.Agents;

namespace Clprolf.Examples.Graphs.Worker.Impl
{
    [ClWorker]
    public class GraphAppLauncher
    {
        public static void Main(string[] args)
        {
            int defaultStartNode = 1;
            IGraph g = new GraphImpl(defaultStartNode);

            g.AddEdge(1, 2);
            g.AddEdge(1, 3);
            g.AddEdge(2, 4);
            g.AddEdge(2, 5);
            g.AddEdge(3, 6, true);

            Console.WriteLine($"\nComputing all paths from default start node {defaultStartNode}");
            g.ComputeAllPathsFrom();

            Console.WriteLine("\nAll paths are");
            g.ComputeNoFilter();

            Console.WriteLine("\nThe longest paths are: ");
            g.ComputeLongestPathsFrom();

            int node = 2;
            Console.WriteLine($"\nCompute paths passing through {node}");
            g.ComputePathsPassingThrough(node);

            Console.WriteLine("\nComputing all terminal paths");
            g.ComputeTerminalPaths();

            Console.WriteLine("\nRecursive DFS");
            g.RecursiveDFS();
        }
    }
}