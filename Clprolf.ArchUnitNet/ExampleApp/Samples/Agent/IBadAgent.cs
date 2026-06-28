using Clprolf.ArchUnitNet.Attributes;
using Clprolf.Checker.ExampleApp.Samples.Agent.Trait;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Clprolf.Checker.ExampleApp.Samples.Agents
{
    [ClWorker]
    [ClFamily]
    public interface IBadAgent: IGrowable
    {
    }
}
