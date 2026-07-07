# 🧱 Two Structural Principles Behind Clprolf

## Prerequisites

Clprolf builds upon classical Object-Oriented Programming.

To fully understand this article, readers should already be familiar with:

* the fundamentals of OOP,
* inheritance and composition,
* common design principles such as SRP,
* the guideline "favor composition over inheritance".

---

Clprolf is built around two central principles.

---

## 1. A class is either business/conceptual or technical

Every class belongs to one of the following worlds:

### Business / Domain World

The class represents a business or conceptual responsibility.

Examples:

* order management,
* business logic,
* simulation,
* functional orchestration,
* but also system-oriented agents.

These classes are declared using:

```java
@ClAgent

```

---

### Technical World

The class performs a technical task executed by the system:

* database access (via system agents),
* networking (often by utilizing low-level agents),
* file handling (most frequently with system-oriented agents),
* display / rendering,
* infrastructure,
* no conceptual domain; it is just the system executing technical agents or starting an application,
* generally a system technical service associated with an agent.

These classes are declared using:

```java
@ClWorker

```

## 2. Inheritance must preserve the domain

A class should only inherit from another class belonging to the same conceptual domain.

Otherwise:

> composition should be used.

This principle prevents incoherent hierarchies and mixed responsibilities.

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

They define how classes are interpreted and how inheritance is understood within the framework.

From them emerge the structural distinctions that Clprolf makes explicit:

* domain-oriented classes (`ClAgent`),
* technical support classes (`ClWorker`),
* coherent inheritance,
* explicit architectural responsibilities.

Everything else in Clprolf builds upon these two ideas.
