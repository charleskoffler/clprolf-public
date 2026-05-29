# 🧱 Two Structural Principles Behind Clprolf

## Prerequisites

Clprolf builds upon classical Object-Oriented Programming.

To fully understand this article, readers should already be familiar with:

* the fundamentals of OOP,
* inheritance and composition,
* common design principles such as SRP,
* the guideline "favor composition over inheritance".

---

## Foundational Principles of Clprolf

Clprolf is based on two core principles:

1. A class is either organized around a well-defined class domain or exists to provide technical support to other classes.
2. Inheritance must preserve the class domain; otherwise, composition should be used.

These principles define how Clprolf structures classes and relationships.

---

## What Is a Class Domain?

A class domain is the central subject around which a class is organized.

It defines what the class fundamentally represents and what it is responsible for.

Examples include:

* `Animal`
* `File`
* `Connection`
* `Parser`
* `JsonSerializer`
* `Scheduler`
* `Controller`
* `PdfGenerator`
* `RandomGenerator`
* `PaymentService`
* `InventoryService`

Each of these classes represents a coherent subject.

A class domain does not need to be business-related.

It may represent:

* a business concept,
* a system concept,
* a technical concept,
* a scientific concept,
* a simulation entity,
* a service,
* or any other coherent subject.

---

## Domain-Oriented Classes

A domain-oriented class represents something.

Its identity comes from the domain it models rather than from the technical services it performs.

Examples:

```text
Animal
File
Connection
Parser
JsonSerializer
Scheduler
Controller
PdfGenerator
RandomGenerator
PaymentService
InventoryService
```

These classes remain meaningful even when considered independently.

In Clprolf, such classes are typically represented as `agent`.

---

## Technical Support Classes

Some classes do not primarily represent a domain.

Instead, they exist to support one or more agents by providing technical capabilities.

Examples include:

* launching agents,
* executing technical procedures,
* displaying information,
* interacting with the operating system,
* accessing platform-specific features,
* providing infrastructure services.

Examples:

```text
AnimalWorker
ApplicationLauncher
AgentLauncher
ProcessLauncher
SystemExecutor
NativeProcessRunner
OperatingSystemWorker
ControllerWorker
```

These classes are primarily defined by the support they provide.

Their purpose is to help agents perform technical operations.

In Clprolf, such classes are typically represented as `worker`.

---

## Agents and Workers

A useful way to understand the distinction is:

```text
agent  → represents a domain
worker → supports a domain
```

Example:

```clprolf
public agent Animal {

    private AnimalWorker worker;

    public void display() {
        worker.display(this);
    }

}
```

```clprolf
public worker AnimalWorker {

    public void display(Animal animal) {

        // rendering logic
        // platform-specific code
        // UI interaction

    }

}
```

The `Animal` agent represents the domain.

The `AnimalWorker` exists to provide technical support to that domain.

Another example:

```clprolf
public agent PaymentService {
}
```

```clprolf
public worker PaymentServiceLauncher {
}
```

The service itself represents a domain.

The launcher exists only to support its execution.

---

## Relation to SRP

The Single Responsibility Principle (SRP) states that a class should have only one reason to change.

The concept of class domain provides a structural way to think about that responsibility.

If a class remains organized around a single domain, its evolution tends to remain coherent.

Clprolf does not replace SRP.

Instead, it provides a vocabulary for expressing responsibility through domain continuity.

---

## Relation to "Favor Composition Over Inheritance"

The guideline "favor composition over inheritance" is widely accepted in object-oriented design.

Clprolf introduces a structural criterion for applying it.

When inheritance preserves the same class domain, it may be appropriate.

Example:

```text
Animal
  └─ Dog
```

Both classes belong to the same conceptual domain.

However:

```text
Dog
  └─ DatabaseConnection
```

introduces a different domain.

In such cases, composition generally provides a clearer relationship.

The principle does not eliminate developer judgment.

It simply evaluates inheritance through domain continuity.

---

## Why This Matters

As systems evolve, responsibilities tend to drift.

Classes gradually accumulate unrelated concerns and architectural intent becomes harder to identify.

By making domains explicit and by relating inheritance to domain continuity, Clprolf encourages structures that remain understandable over time.

The goal is not to restrict developers.

The goal is to make architectural decisions visible and easier to reason about.

---

## Closing

These two principles form the foundation of Clprolf.

They define how classes are interpreted and how inheritance is understood within the language.

From them emerge the structural distinctions that Clprolf makes explicit:

* domain-oriented classes (`agent`),
* technical support classes (`worker`),
* coherent inheritance,
* explicit architectural responsibilities.

Everything else in Clprolf builds upon these two ideas.
