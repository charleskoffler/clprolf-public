using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

using System.Reflection;
using ArchUnitNET.Domain;
using ArchUnitNET.Loader;
using Clprolf.Checker.ExampleApp.Samples.Agents.Impl;

namespace Clprolf.ArchUnitNet.Tests
{
   
    public static class TestArchitecture
    {
        public static readonly Architecture Architecture = new ArchLoader()
           // .LoadAssemblies(System.Reflection.Assembly.GetExecutingAssembly())
           .LoadAssemblies(typeof(AnimalImpl).Assembly)
            .Build();
    }
}
