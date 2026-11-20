# Clprolf (CLear Programming Language and Framework)

**Clprolf** ("Clear PROgramming Language and Framework") is a standalone architectural framework, for Java and C# .NET. 

It helps Java and .NET teams make class responsibilities explicit by distinguishing:

- `@ClAgent` (or `[ClAgent]` in C#): business or conceptual classes
- `@ClWorker` (or `[ClWorker]`): technical or infrastructure classes
- (optional) `@ClSystem` (or `[ClSystem]`): system-oriented agents
- `@ClDraft` (or `[ClDraft]`): temporarily undefined classes

The framework helps adhere to the well-known SOLID principles.

With the ArchUnit checker(for Java or C#), Clprolf rules can be verified automatically during tests and CI.

## Target Uses and Prerequisites

**Clprolf** (*"Clear PROgramming Language and Framework"*) is a lightweight, standalone **architectural Embedded Domain-Specific Language (EDSL)** designed for Java and C# .NET. It acts as a middle layer between standard Object-Oriented Programming (OOP) and existing macro-architectures.

It extends the host language directly through a declarative vocabulary of annotations (or C# attributes) that is statically checked at compile time or during testing. The source code (annotations and static checker), documentation, and examples are freely available on GitHub under the MIT license.

This architectural DSL is particularly suited for the following contexts:
- **Educational tool**: learning OOP, the role of interfaces, or immutability through explicit rules.
- **Scientific applications and simulations**: structuring rich models by clearly separating entities from their capabilities.
- **Enterprise applications**: complementing existing architectures with fine-grained structural enforcement.
- **Complex systems**: keeping large codebases with numerous classes and interfaces manageable.
- **Large-scale refactoring**: progressively reorganizing a massive legacy codebase under static verification.
- **Contract-driven design**: adopting a declarative programming style checked directly on types.

**Prerequisites:** A basic understanding of OOP is enough to get started; familiarity with object-oriented design principles is a plus.

This EDSL assists object-oriented design by providing a concrete, verifiable interpretation of SOLID principles, while facilitating the implementation of Hexagonal Architecture or DDD through its own structural approach. The resulting application remains **pure Java or C#**: it can revert to completely standard code at any time simply by removing the annotations or disabling the checker.

---

## Information

Feel free to reach out by email if you want more information

## Articles

* [**Clprolf — Official Documentation**](https://github.com/charleskoffler/clprolf-public/blob/main/articles/clprolf_24_official_doc.md)

* [French][**Clprolf — Documentation Officielle**](https://github.com/charleskoffler/clprolf-public/blob/main/articles/clprolf_25_french_official_doc.md)

* [**Gemini Pro Extended 3.1 review on Clprolf framework**](https://github.com/charleskoffler/clprolf-public/blob/main/articles/clprolf_29_ai_feedback.md)

* [French][**Feedback de Gemini Pro Extended 3.1 sur le framework Clprolf**](https://github.com/charleskoffler/clprolf-public/blob/main/articles/clprolf_30_french_ai_review.md)

## Checker for Clprolf

* [**ArchUnit Checker and framework annotations**](https://github.com/charleskoffler/clprolf-public/tree/main/clprolf_checker)
* [**ArchUnitNet Checker for .NET and framework C# attributes**](https://github.com/charleskoffler/clprolf-public/tree/main/Clprolf.ArchUnitNet)

## Examples

* [**Spring boot integration example**](https://github.com/charleskoffler/clprolf-public/tree/main/examples/clprolf_weather)

* [**(.NET C#) WPF Example**](https://github.com/charleskoffler/clprolf-public/tree/main/examples/Clprolf.Examples.WPF)

* [**(.NET C#) Graphs example**](https://github.com/charleskoffler/clprolf-public/tree/main/examples/dotnet/Clprolf.Examples.Graphs)

* [**(Java)Graphs example**](https://github.com/charleskoffler/clprolf-public/tree/main/examples/java/examples.graphe)

* [**(Java) Snake**](https://github.com/charleskoffler/clprolf-public/tree/main/examples/snake-game)

* [**(Java) Spring Kafka example**](https://github.com/charleskoffler/clprolf-public/tree/main/examples/java/kafka-messaging)
