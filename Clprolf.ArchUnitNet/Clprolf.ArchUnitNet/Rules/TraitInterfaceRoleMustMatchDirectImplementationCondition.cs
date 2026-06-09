using ArchUnitNET.Domain;
using ArchUnitNET.Domain.Extensions;
using ArchUnitNET.Fluent.Conditions;
using Clprolf.ArchUnitNet.Attributes;

namespace Clprolf.ArchUnitNet.Rules;

[ClAgent]
internal sealed class TraitInterfaceRoleMustMatchDirectImplementationCondition : ICondition<Class>
{
    public string Description => "implement only matching [ClTrait] role";

    public IEnumerable<ConditionResult> Check(IEnumerable<Class> objects, Architecture architecture)
    {
        foreach (var clazz in objects)
        {
            // 1. Cas d'exclusion globaux pour la classe
            if (clazz.IsDraft())
            {
                yield return new ConditionResult(
                    clazz,
                    true,
                    $"{clazz.FullName} is a draft");
                continue;
            }

            if (!clazz.IsAgent() && !clazz.IsWorker())
            {
                yield return new ConditionResult(
                    clazz,
                    true,
                    $"{clazz.FullName} is not an agent, nor a worker");
                continue;
            }

            if (clazz.ImplementedInterfaces.IsNullOrEmpty())
            {
                yield return new ConditionResult(
                    clazz,
                    true,
                    $"{clazz.FullName} has not implemented interfaces");
                continue;
            }

            // On extrait uniquement les interfaces qui sont des [ClTrait]
            var traitInterfaces = clazz.ImplementedInterfaces.Where(i => i.IsTrait()).ToList();

            // 2. Si la classe n'implémente aucun Trait, elle est valide d'office
            if (!traitInterfaces.Any())
            {
                yield return new ConditionResult(
                    clazz,
                    true,
                    $"{clazz.FullName} does not implement any [ClTrait] interface");
                continue;
            }

            // 3. Validation globale : TOUTES les interfaces de type Trait doivent être compatibles
            bool allTraitsAreCompatible = traitInterfaces.All(interf =>
                clazz.HasBypass()
                || (clazz.IsAgent() && interf.IsAgent())
                || (clazz.IsWorker() && interf.IsWorker())
            );

            // On isole le premier Trait fautif pour le rapport d'erreur
            var faultyTrait = traitInterfaces.FirstOrDefault(interf => !(
                clazz.HasBypass()
                || (clazz.IsAgent() && interf.IsAgent())
                || (clazz.IsWorker() && interf.IsWorker())
            ));

            yield return new ConditionResult(
                clazz,
                allTraitsAreCompatible,
                allTraitsAreCompatible
                    ? $"All implemented trait interfaces are compatible with {clazz.FullName}."
                    : $"{clazz.FullName} implements {faultyTrait?.FullName} but class role and [ClTrait] target role do not match");
        }
    }

    public bool CheckEmpty() => true;
}