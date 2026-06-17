using ArchUnitNET.Domain;
using ArchUnitNET.Fluent.Conditions;
using Clprolf.ArchUnitNet.Attributes;

namespace Clprolf.ArchUnitNet.Rules;

[ClAgent]
internal sealed class InheritingInterfaceRoleMustMatchTraitInterfaceTargetRoleCondition : ICondition<Interface>
{
    public string Description => "inherit only trait interfaces with compatible target roles";

    public IEnumerable<ConditionResult> Check(IEnumerable<Interface> objects, Architecture architecture)
    {
        foreach (var interf in objects)
        {
            // 1. Cas d'exclusion globaux pour l'interface courante (on renvoie true au lieu de continue)
            if (!interf.IsFamily() && !interf.IsTrait())
            {
                yield return new ConditionResult(
                    interf,
                    true,
                    $"{interf.FullName} is ignored (Neither Family nor Trait interface)");
                continue;
            }

            // On extrait uniquement les parents qui sont des [ClTrait]
            var parentTraitInterfaces = interf.ImplementedInterfaces.Where(p => p.IsTrait()).ToList();

            // 2. Si elle n'hérite d'aucun Trait, elle est valide d'office
            if (!parentTraitInterfaces.Any())
            {
                yield return new ConditionResult(
                    interf,
                    true,
                    $"{interf.FullName} does not inherit from any [ClTrait] interface");
                continue;
            }

            // 3. Validation globale : TOUTES les interfaces parentes de type Trait doivent être compatibles
            bool allTraitsAreCompatible = parentTraitInterfaces.All(parent =>
                interf.HasBypass()
                || (interf.IsAgent() && parent.IsAgent())
                || (interf.IsWorker() && parent.IsWorker())
            );

            // On cible le premier parent fautif pour le rapport d'erreur
            var faultyTrait = parentTraitInterfaces.FirstOrDefault(parent => !(
                interf.HasBypass()
                || (interf.IsAgent() && parent.IsAgent())
                || (interf.IsWorker() && parent.IsWorker())
            ));

            yield return new ConditionResult(
                interf,
                allTraitsAreCompatible,
                allTraitsAreCompatible
                    ? $"All inherited trait interfaces are compatible with {interf.FullName}."
                    : $"{interf.FullName} cannot inherit trait interface {faultyTrait?.FullName} because their target roles are incompatible");
        }
    }

    public bool CheckEmpty() => true;
}