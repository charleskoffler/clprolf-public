using ArchUnitNET.Domain;
using ArchUnitNET.Domain.Extensions;
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
    internal sealed class ClassShouldNotImplementTraitDirectlyCondition : ICondition<Class>
    {
        public string Description => "avoid direct implementation of [ClTrait]";

        public IEnumerable<ConditionResult> Check(
            IEnumerable<Class> objects,
            Architecture architecture)
        {
            foreach (var clazz in objects)
            {
                // 1. Cas d'exclusion globaux pour la classe
                if (clazz.IsDraft() || (!clazz.IsAgent() && !clazz.IsWorker()))
                {
                    yield return new ConditionResult(
                        clazz,
                        true,
                        $"{clazz.FullName} is ignored (Draft or neither Agent nor Worker)");
                    continue;
                }

                // 2. Si la classe n'a aucune interface, elle est valide d'office
                if (clazz.ImplementedInterfaces.IsNullOrEmpty())
                {
                    yield return new ConditionResult(
                        clazz,
                        true,
                        $"{clazz.FullName} does not implement any interface");
                    continue;
                }

                // 3. Validation globale de toutes les interfaces implémentées
                // On s'assure qu'AUCUNE interface n'est un Trait (sauf si Bypass)
                bool allInterfacesAreValid = clazz.ImplementedInterfaces.All(interf =>
                    !interf.IsTrait() || clazz.HasInterfaceBypass()
                );

                // On récupère le premier Trait qui pose problème pour avoir un super message d'erreur
                var faultyTrait = clazz.ImplementedInterfaces.FirstOrDefault(interf =>
                    interf.IsTrait() && !clazz.HasInterfaceBypass()
                );

                yield return new ConditionResult(
                    clazz,
                    allInterfacesAreValid,
                    allInterfacesAreValid
                        ? $"{clazz.FullName} correctly avoids direct implementation of [ClTrait]."
                        : $"{clazz.FullName} directly implements trait {faultyTrait?.FullName}; prefer family_interface -> trait_interface");
            }
        }

        public bool CheckEmpty() => true;
    }
}