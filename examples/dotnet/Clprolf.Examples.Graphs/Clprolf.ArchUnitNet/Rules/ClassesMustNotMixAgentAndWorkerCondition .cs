using ArchUnitNET.Domain;
using ArchUnitNET.Fluent.Conditions;
using Clprolf.ArchUnitNet.Attributes;

namespace Clprolf.ArchUnitNet.Rules;

[ClAgent]
internal sealed class ClassesMustNotMixAgentAndWorkerCondition : ICondition<Class>
{
    public string Description => "not mix [ClAgent] and [ClWorker]";

    public IEnumerable<ConditionResult> Check(IEnumerable<Class> objects, Architecture architecture)
    {
        foreach (var clazz in objects)
        {
            if (clazz.IsDraft() || (!clazz.IsAgent() && !clazz.IsWorker()))
            {
                yield return new ConditionResult(
                    clazz,
                    true,
                    $"{clazz.FullName} is ignored (Draft or neither Agent nor Worker)");
                continue;
            }

            bool ok = !(clazz.IsAgent() && clazz.IsWorker());

            yield return new ConditionResult(
                clazz,
                ok,
                ok ? "Class roles are distinct"
                   : $"{clazz.FullName} cannot be both [ClAgent] and [ClWorker]");
        }
    }

    public bool CheckEmpty() => true;
}