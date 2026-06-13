using ArchUnitNET.Domain;
using Clprolf.ArchUnitNet.Attributes;

namespace Clprolf.ArchUnitNet.Rules;

[ClAgent]
internal static class ClprolfTypeExtensions
{
    public static bool HasClAttribute(this IType type, Type attributeType)
    {
        return type.Attributes.Any(attribute =>
            attribute.FullName == attributeType.FullName ||
            attribute.FullName == attributeType.Name ||
            attribute.Name == attributeType.Name);
    }

    public static bool IsAgent(this IType type) => type.HasClAttribute(typeof(ClAgentAttribute));
    public static bool IsWorker(this IType type) => type.HasClAttribute(typeof(ClWorkerAttribute));
    public static bool IsDraft(this IType type) => type.HasClAttribute(typeof(ClDraftAttribute));
    public static bool IsFamily(this IType type) => type.HasClAttribute(typeof(ClFamilyAttribute));
    public static bool IsTrait(this IType type) => type.HasClAttribute(typeof(ClTraitAttribute));
    public static bool IsFree(this IType type) => type.HasClAttribute(typeof(ClFreeAttribute));

    /// <summary>
    /// Checks if the type belongs to the Clprolf semantic model by having at least one core structural attribute.
    /// </summary>
    public static bool IsClprolf(this IType type)
    {
        return type.IsAgent() ||
               type.IsWorker() ||
               type.IsFamily() ||
               type.IsTrait() ||
               type.IsFree() ||
               type.IsDraft();
    }

    public static bool HasBypass(this IType type) => type.HasClAttribute(typeof(ClBypassAttribute));
    public static bool HasInterfaceBypass(this IType type) => type.HasClAttribute(typeof(ClInterfaceBypassAttribute));
}