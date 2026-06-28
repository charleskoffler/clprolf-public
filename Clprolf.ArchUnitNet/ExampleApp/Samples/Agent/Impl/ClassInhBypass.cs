using Clprolf.ArchUnitNet.Attributes;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Clprolf.Checker.ExampleApp.Samples.Agents.Impl
{
    [ClBypass]
    [ClWorker]
    public class ClassInhBypass: AnimalImpl
    {
    }
}
