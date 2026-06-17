using Clprolf.ArchUnitNet.Attributes;
using System;

namespace Clprolf.Examples.Graph.Workers
{
    [ClWorker]
    [ClFamily]
    public interface IGraphWorker
    {
        // Display graph structure
        void PrintGraph();

        void PrintAllResultingPaths();

        void PrintPartialPaths();
    }
}