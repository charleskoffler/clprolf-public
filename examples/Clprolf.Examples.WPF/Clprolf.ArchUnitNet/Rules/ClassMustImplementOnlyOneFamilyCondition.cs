using ArchUnitNET.Domain;
using ArchUnitNET.Fluent.Conditions;
using Clprolf.ArchUnitNet.Attributes;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Clprolf.ArchUnitNet.Rules
{
    [ClAgent]
    internal sealed class ClassMustImplementOnlyOneFamilyCondition : ICondition<Class>
    {
        public string Description => "implement at most one [ClFamily]";

        public IEnumerable<ConditionResult> Check(
            IEnumerable<Class> objects,
            Architecture architecture)
        {
            foreach (var clazz in objects)
            {
                if (clazz.IsDraft()
                    || (!clazz.IsAgent() && !clazz.IsWorker()))
                {
                    yield return new ConditionResult(
                        clazz,
                        true,
                        "Ignored");
                    continue;
                }

                int count =
                    clazz.ImplementedInterfaces
                        .Count(i => i.IsFamily());

                bool ok =
                    count <= 1
                    || clazz.HasInterfaceBypass();

                yield return new ConditionResult(
                    clazz,
                    ok,
                    $"{clazz.FullName} implements {count} [ClFamily] interfaces, maximum is 1");
            }
        }

        public bool CheckEmpty() => true;
    }
}
