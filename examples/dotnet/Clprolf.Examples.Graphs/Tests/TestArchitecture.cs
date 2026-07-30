using ArchUnitNET.Domain;
using ArchUnitNET.Loader;
using Clprolf.Examples.Graphs.Agents.Impl;



namespace Clprolf.ArchUnitNet.Tests
{
   
    public static class TestArchitecture
    {
        public static readonly Architecture Architecture = new ArchLoader()
           // .LoadAssemblies(System.Reflection.Assembly.GetExecutingAssembly())
           .LoadAssemblies(typeof(GraphImpl).Assembly)
            .Build();
    }
}
