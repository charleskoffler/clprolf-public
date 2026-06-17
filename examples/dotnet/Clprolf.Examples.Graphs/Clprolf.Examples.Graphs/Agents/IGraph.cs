using System;
using System.Collections.Generic;
using System.Text;

namespace Clprolf.Examples.Graphs.Agents
{
    using System.Collections.Generic;
    using Clprolf.ArchUnitNet.Attributes;


    namespace Org.Clprolf.Graph.Agents
    {
        [ClAgent]
        [ClFamily]
        public interface IGraph
        {
            /// <summary>
            /// Getting the default node
            /// </summary>
            int Start { get; }

            Dictionary<int, List<int>> Adj { get; }

            /// <summary>
            /// Getting all the paths found
            /// </summary>
            List<List<int>> ResultingPaths { get; }

            /// <summary>
            /// Getting the paths found while applying the current filter
            /// </summary>
            List<List<int>> PartialPaths { get; }

            /// <summary>
            /// Add an edge (undirected graph)
            /// </summary>
            void AddEdge(int u, int v);

            /// <summary>
            /// Add an edge. Especially used to indicate that it is the last edge of the graph
            /// </summary>
            void AddEdge(int u, int v, bool isLastEdge);

            /// <summary>
            /// All the paths with no filters
            /// </summary>
            void ComputeNoFilter();

            /// <summary>
            /// DFS that compute all paths starting from the given node, before calling the filters.
            /// </summary>
            void ComputeAllPathsFrom();

            /// <summary>
            /// Longest paths filter
            /// </summary>
            void ComputeLongestPathsFrom();

            void ComputePathsPassingThrough(int node);

            void ComputeTerminalPaths();

            /// <summary>
            /// Recursive DFS
            /// </summary>
            void RecursiveDFS();
        }
    }
}
