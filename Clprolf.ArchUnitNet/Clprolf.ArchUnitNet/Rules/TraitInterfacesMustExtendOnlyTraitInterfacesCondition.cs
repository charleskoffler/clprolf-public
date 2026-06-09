using ArchUnitNET.Domain;
using ArchUnitNET.Domain.Extensions;
using ArchUnitNET.Fluent.Conditions;
using Clprolf.ArchUnitNet.Attributes;

namespace Clprolf.ArchUnitNet.Rules;

[ClAgent]
internal sealed class TraitInterfacesMustExtendOnlyTraitInterfacesCondition : ICondition<Interface>
{
    public string Description => "extend only trait interfaces";

    public IEnumerable<ConditionResult> Check(IEnumerable<Interface> objects, Architecture architecture)
    {
        foreach (var interf in objects)
        {
            // 1. Cas d'exclusion global : si ce n'est pas un Trait, on l'ignore (en renvoyant true)
            if (!interf.IsTrait())
            {
                yield return new ConditionResult(
                    interf,
                    true,
                    $"{interf.FullName} is ignored (Not a [ClTrait] interface)");
                continue;
            }

            // 2. Si l'interface Trait n'hérite d'absolument rien, elle est valide d'office
            if (interf.ImplementedInterfaces.IsNullOrEmpty())
            {
                yield return new ConditionResult(
                    interf,
                    true,
                    $"{interf.FullName} has no parent interface");
                continue;
            }

            // 3. Validation globale : TOUTES les interfaces parentes doivent être des [ClTrait]
            bool allParentsAreTraits = interf.ImplementedInterfaces.All(parent => parent.IsTrait());

            // On isole le premier parent fautif (qui n'est pas un Trait) pour le rapport d'erreur
            var faultyParent = interf.ImplementedInterfaces.FirstOrDefault(parent => !parent.IsTrait());

            yield return new ConditionResult(
                interf,
                allParentsAreTraits,
                allParentsAreTraits
                    ? $"All parent interfaces of {interf.FullName} are traits."
                    : $"{interf.FullName} is a trait interface but it inherits from {faultyParent?.FullName} which is NOT a trait interface");
        }
    }

    public bool CheckEmpty() => true;
}