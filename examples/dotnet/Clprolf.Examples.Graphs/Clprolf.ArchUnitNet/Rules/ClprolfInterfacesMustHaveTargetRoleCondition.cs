using ArchUnitNET.Domain;
using ArchUnitNET.Fluent.Conditions;
using Clprolf.ArchUnitNet.Attributes;
using Clprolf.ArchUnitNet.Rules;

namespace Clprolf.ArchUnitNet.Rules;

[ClAgent]
internal sealed class ClprolfInterfacesMustHaveTargetRoleCondition : ICondition<Interface>
{
    public string Description => "have a valid target role [ClAgent] and/or [ClWorker]";

    public IEnumerable<ConditionResult> Check(IEnumerable<Interface> objects, Architecture architecture)
    {
        foreach (var interf in objects)
        {
            bool isFamily = interf.IsFamily();
            bool isTrait = interf.IsTrait();

            if (!isFamily && !isTrait)
            {
                yield return new ConditionResult(
                interf,
                true,
                $"{interf.FullName} is not a family, nor a trait");
                continue;
            }
              
            bool hasAgent = interf.IsAgent();
            bool hasWorker = interf.IsWorker();

            bool ok = isFamily
                ? hasAgent ^ hasWorker
                : hasAgent || hasWorker;

            yield return new ConditionResult(
                interf,
                ok,
                $"{interf.FullName} must have a valid target role: [ClFamily] requires exactly one of [ClAgent] or [ClWorker]; [ClTrait] requires at least one of [ClAgent] or [ClWorker]");
        }
    }

    public bool CheckEmpty() => true;
}