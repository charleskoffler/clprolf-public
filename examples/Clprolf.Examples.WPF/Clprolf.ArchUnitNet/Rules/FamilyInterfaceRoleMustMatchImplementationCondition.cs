using ArchUnitNET.Domain;
using ArchUnitNET.Fluent.Conditions;
using Clprolf.ArchUnitNet.Attributes;
using Clprolf.ArchUnitNet.Rules;

namespace Clprolf.ArchUnitNet.Rules;

[ClAgent]
internal sealed class FamilyInterfaceRoleMustMatchImplementationCondition : ICondition<Class>
{
    public string Description => "implement only matching [ClFamily] role";

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

            var familyInterfaces = clazz.ImplementedInterfaces.Where(i => i.IsFamily()).ToList();

            if (!familyInterfaces.Any())
            {
                yield return new ConditionResult(
                    clazz,
                    true,
                    $"{clazz.FullName} does not implement any [ClFamily] interface");
                continue;
            }

            //Overall validation of all Family interfaces found
            // We check whether ALL interfaces conform to the role. 
            // If even one fails, ‘allInterfacesAreValid’ will become ‘false’.
            bool allInterfacesAreValid = familyInterfaces.All(interf =>
                clazz.HasBypass()
                || (clazz.IsAgent() && interf.IsAgent())
                || (clazz.IsWorker() && interf.IsWorker())
            );

            // We're looking for the interface that crashed just to create a great error message if needed
            var faultyInterface = familyInterfaces.FirstOrDefault(interf => !(
                clazz.HasBypass()
                || (clazz.IsAgent() && interf.IsAgent())
                || (clazz.IsWorker() && interf.IsWorker())
            ));

            yield return new ConditionResult(
                clazz,
                allInterfacesAreValid,
                allInterfacesAreValid
                    ? $"Matching roles between {clazz.FullName} and all its family interfaces."
                    : $"{clazz.FullName} implements {faultyInterface?.FullName} but class role and [ClFamily] target role do not match"
            );
        }
    }

    public bool CheckEmpty() => true;
}