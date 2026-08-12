# Clprolf (CLear Programming Language and Framework)

**Clprolf** ("Clear PROgramming Language and Framework") is a standalone architectural framework, for Java and C# .NET. 

It helps Java and .net teams make class responsibilities explicit by distinguishing:

- `@ClAgent` (or `[ClAgent]` in C#): business or conceptual classes
- `@ClWorker` (or `[ClWorker]`): technical or infrastructure classes
- (optional) `@ClSystem` (or `[ClSystem]`): system-oriented agents
- `@ClDraft` (or `[ClDraft]`): temporarily undefined classes

The framework helps adhere to the well-known SOLID principles.

With the ArchUnit checker(for Java or C#), Clprolf rules can be verified automatically during tests and CI.

## Target Uses and Prerequisites

It is a specialized framework designed for the following scenarios:

* A pedagogical framework designed to teach Object-Oriented Programming (OOP), interfaces, and even immutability.
* scientific applications
* simulations
* Enterprise applications looking to adopt Clprolf alongside standard architectures
* highly complex applications with a large number of classes or interfaces
* major refactoring required on a large existing application
* or simply for those who enjoy this style of programming guided by an architectural checker for classes and interfaces.

It requires a basic knowledge of OOP and object-oriented design principles. The framework does not claim to be indispensable, and naturally, alternative solutions exist. The resulting code can easily revert to pure Java or C# simply by removing or ignoring the annotations.

## Information

Feel free to reach out by email if you want more information

## Articles

* [**Clprolf — Official Documentation**](https://github.com/charleskoffler/clprolf-public/blob/main/articles/clprolf_24_official_doc.md)

* [French][**Clprolf — Documentation Officielle**](https://github.com/charleskoffler/clprolf-public/blob/main/articles/clprolf_25_french_official_doc.md)

## Checker for Clprolf

* [**ArchUnit Checker and framework annotations**](https://github.com/charleskoffler/clprolf-public/tree/main/clprolf_checker)
* [**ArchUnitNet Checker for .Net and framework C# attributes**](https://github.com/charleskoffler/clprolf-public/tree/main/Clprolf.ArchUnitNet)

## Examples

* [**Spring boot integration example**](https://github.com/charleskoffler/clprolf-public/tree/main/examples/clprolf_weather)

* [**.Net C# WPF Example**](https://github.com/charleskoffler/clprolf-public/tree/main/examples/Clprolf.Examples.WPF)

* [**(.Net C#)Graphs example**](https://github.com/charleskoffler/clprolf-public/tree/main/examples/dotnet/Clprolf.Examples.Graphs)

* [**(Java)Graphs example**](https://github.com/charleskoffler/clprolf-public/tree/main/examples/java/examples.graphe)

* [**Snake (Java example)**](https://github.com/charleskoffler/clprolf-public/tree/main/examples/snake-game)
