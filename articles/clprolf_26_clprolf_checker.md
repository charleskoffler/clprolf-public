# Introducing the ArchUnit Checker for the Clprolf Framework

One of the most interesting aspects of architectural frameworks is not the rules themselves, but the ability to verify that those rules are actually being followed.

The Clprolf Framework now includes automated checker extensions for both **Java** and **.NET (C#)** projects. Rather than relying solely on documentation, Clprolf's structural principles can be validated automatically during development and continuous integration.

## What is Clprolf?

Clprolf ("Clear PROgramming Language and Framework") is a lightweight object-oriented framework built around a simple idea:

> A class should clearly express its primary role.

To make this explicit, Clprolf distinguishes between business-oriented classes and technical classes.

```text
Classes        Interfaces
--------        ----------
Agent           Family
Worker          Trait
Draft           Free

```

The framework is based on two fundamental principles:

1. Separate business concerns (Agents) from technical concerns (Workers).
2. Preserve the conceptual domain through inheritance.

## Bringing Architecture into the Build Process

Architectural guidelines are often written down, discussed, and eventually forgotten. The Clprolf checker takes a different approach: architectural rules become executable tests.

By using **ArchUnit** in Java and **ArchUnitNET** in C# .NET, violations can be detected automatically from your favorite IDE (IntelliJ IDEA, Visual Studio), build tools (Maven, dotnet CLI), or any CI/CD pipeline.

### Ecosystem Symmetry (.NET vs Java)

The framework maintains absolute functional parity between ecosystems. The syntax effortlessly mirrors each language's native style:

* **In Java (ArchUnit):** Marked using `@Annotation` syntax.
* **In .NET (ArchUnitNET):** Marked using native C# `[Attribute]` syntax enclosed in brackets.

## Core Rules

The checker validates the framework's core rules through 8 mandatory semantic tests (`ClprolfArchTest` in Java, or its xUnit equivalent in .NET).

### 1. A class cannot mix roles (`clprolf_classes_must_not_mix_agent_and_worker`)

A class cannot be annotated as both Agent and Worker.

```java
// Java
@ClAgent @ClWorker
public class InvalidClass { }

```

```csharp
// C#
[ClAgent], [ClWorker]
public class InvalidClass { }

```

A class must have a single primary responsibility.

---

### 2. Inheritance must not mix domains (`agent_worker_inheritance_must_not_mix`)

A Worker class cannot inherit from an Agent class, and vice versa. This ensures that inheritance strictly preserves the conceptual domain. Forcing an override is possible locally using `@ClBypass` (or `[ClBypass]`).

---

### 3. Family interfaces must match implementation roles (`family_role_must_match_implementation`)

A class implementing a `@ClFamily` (or `[ClFamily]`) interface must have a role compatible with the target role of that interface. For example, an Agent family interface must be implemented by an Agent class.

---

### 4. Trait interfaces must extend only trait interfaces (`trait_interfaces_must_extend_only_trait_interfaces`)

A `@ClTrait` interface can only inherit from other `@ClTrait` interfaces. A trait remains a trait throughout the contract hierarchy.

---

### 5. Clprolf interfaces must declare a target role (`clprolf_interfaces_must_have_target_role`)

Every Family and Trait interface must explicitly declare its target domain:

* A `Family` interface must declare exactly one target role: Agent or Worker.
* A `Trait` interface must declare at least one target role (Agent, Worker, or exceptionally both for cross-cutting capabilities).

---

### 6. Inheriting interface roles must match trait target roles (`inheriting_interface_role_must_match_trait_interface_target_role`)

When an interface (Family or Trait) inherits from a Trait, its target role must be compatible with that trait. Cross-cutting traits (annotated as both Agent and Worker) are universally compatible.

---

### 7. Family target roles must match inherited family interfaces (`family_interface_target_role_must_match_inherited_family_interface`)

When a Family interface inherits from another Family interface, both must share compatible target roles to preserve the conceptual domain across contract hierarchies.

---

### 8. Direct trait implementation must respect trait roles (`trait_interface_role_must_match_direct_implementation`)

While direct implementation of a Trait by a concrete class is tolerated in standard mode, the class role must match the trait's target role.

```java
// Valid: Payable is an Agent trait
@ClAgent
public class Order implements Payable { }

```

## Optional Strict Validation

A second test suite, `ClprolfStrictArchTest` (available in both GitHub repositories), adds 4 strict rules for teams demanding absolute classification. These can also be run standalone to audit technical debt on existing legacy codebases.

* **Every class must declare a role (`optional_all_classes_should_have_clprolf_role`):** All classes must explicitly be tagged as Agent, Worker, or Draft.
* **Every interface must declare a role (`optional_all_interfaces_should_have_clprolf_role`):** All interfaces must explicitly be tagged as Family, Trait, or Free.
* **No direct trait implementation (`optional_class_should_not_implement_trait_directly`):** Classes are forbidden from implementing a Trait directly; traits must be inherited through a Family interface.
* **Single Family interface implementation (`optional_class_must_implement_only_one_family_interface`):** A class can only implement one primary Family interface, effectively mimicking single inheritance patterns. Multiple inheritance is cleanly shifted into the Family interface itself:

```csharp
// C# Strict Pattern
[ClAgent], [ClFamily]
public interface IHorse : IAnimal, IMammal, IPayable { }

[ClAgent]
public class Horse : IHorse { }

```

## Explicit Escapes: Granular Bypasses

Clprolf values pragmatism over dogmatism. When edge cases occur (such as integrating legacy code or external libraries), developers can explicitly sign off on architectural exceptions using precise, language-native operators:

* **`@ClBypass` / `[ClBypass]**`: Overrides role mismatches between Agent and Worker domains.
* **`@ClInterfaceBypass` / `[ClInterfaceBypass]**`: Overrides structural interface constraints (e.g., forcing multiple family implementations or uncommon interface inheritances).

The checkers are highly intelligent: utilizing a bypass does not blind the parser. It merely allows that specific exception while continuing to recursively validate the rest of the inheritance chain.

## Why It Matters

Many frameworks provide architectural recommendations that eventually become shelfware.

Clprolf goes one step further by making those recommendations automatically verifiable across the industry's two most prominent enterprise ecosystems. Whether your infrastructure runs on Spring Boot, .NET Core, or a microservice mix of both, Clprolf provides an identical, lightweight, and executable guardrail to help teams maintain structural consistency as codebases evolve.
