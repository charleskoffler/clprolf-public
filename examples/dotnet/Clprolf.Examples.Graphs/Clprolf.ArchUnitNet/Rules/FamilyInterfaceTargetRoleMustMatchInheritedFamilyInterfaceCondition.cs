using ArchUnitNET.Domain;
using ArchUnitNET.Domain.Extensions;
using ArchUnitNET.Fluent.Conditions;
using Clprolf.ArchUnitNet.Attributes;

namespace Clprolf.ArchUnitNet.Rules;

[ClAgent]
internal sealed class FamilyInterfaceTargetRoleMustMatchInheritedFamilyInterfaceCondition : ICondition<Interface>
{
    public string Description => "inherit only family interfaces with compatible target roles";

    public IEnumerable<ConditionResult> Check(IEnumerable<Interface> objects, Architecture architecture)
    {
        foreach (var interf in objects)
        {
            // 1. Si l'interface n'hérite de rien, elle est valide d'office
            if (interf.ImplementedInterfaces.IsNullOrEmpty())
            {
                yield return new ConditionResult(
                    interf,
                    true,
                    $"{interf.FullName} has no parent interface");
                continue;
            }

            // On extrait uniquement les interfaces parentes de type "Family"
            var parentFamilyInterfaces = interf.ImplementedInterfaces.Where(p => p.IsFamily()).ToList();

            // 2. Si aucun parent n'est une interface Family, elle est valide d'office
            if (!parentFamilyInterfaces.Any())
            {
                yield return new ConditionResult(
                    interf,
                    true,
                    $"{interf.FullName} does not inherit from any [ClFamily] interface");
                continue;
            }

            // 3. Validation globale : TOUTES les interfaces parentes Family doivent être compatibles
            bool allParentsAreCompatible = parentFamilyInterfaces.All(parent =>
                interf.HasBypass()
                || (interf.IsAgent() && parent.IsAgent())
                || (interf.IsWorker() && parent.IsWorker())
            );

            // On récupère le premier parent fautif pour remonter un super message d'erreur ciblé
            var faultyParent = parentFamilyInterfaces.FirstOrDefault(parent => !(
                interf.HasBypass()
                || (interf.IsAgent() && parent.IsAgent())
                || (interf.IsWorker() && parent.IsWorker())
            ));

            yield return new ConditionResult(
                interf,
                allParentsAreCompatible,
                allParentsAreCompatible
                    ? $"All inherited family interfaces are compatible with {interf.FullName}."
                    : $"{interf.FullName} cannot inherit family interface {faultyParent?.FullName} because their target roles are incompatible");
        }
    }

    public bool CheckEmpty() => true;
}