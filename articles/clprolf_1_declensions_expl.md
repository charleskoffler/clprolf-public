# Clprolf Docs #1 — Declensions Explained

## Foundational Principles of Clprolf

Clprolf is based on two core principles:

1. A class is either organized around a well-defined class domain or exists to provide technical support to other classes.
2. Inheritance must preserve the class domain; otherwise, composition should be used.

These principles define how Clprolf structures components and relationships.

---

## What Is a Class Domain?

A class domain is the central subject around which a class is organized.

It defines what the class fundamentally represents and what it is responsible for.

Examples:

* `Animal`
* `File`
* `Connection`
* `Parser`
* `JsonSerializer`
* `RandomGenerator`
* `Scheduler`
* `Controller`
* `PdfGenerator`
* `PaymentService`

Each of these classes is organized around a coherent subject.

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

## What Is a Declension?

A **declension** expresses the nature of a class.

Rather than treating every class as a generic object, Clprolf makes its role explicit.

The available declensions are:

1. **`agent`**
2. **`worker`**
3. **`indef_obj`**

These roles are intentionally few in number to keep the model simple and easy to understand.

---

## `agent`

An `agent` is a class organized around a clearly identifiable domain.

An agent represents something.

Its identity comes from the domain it models rather than from the technical operations it performs.

Examples:

```text
Animal
File
Connection
Parser
JsonSerializer
Scheduler
Controller
PaymentService
InventoryService
```

An agent may contain technical code when appropriate.

However, its primary identity always comes from its domain.

---

## `worker`

A `worker` is a technical support class.

It exists primarily to help one or more agents perform technical operations.

A worker typically provides:

* execution support,
* operating-system interaction,
* rendering,
* launching,
* infrastructure access,
* platform-specific functionality.

Examples:

```text
AnimalWorker
ApplicationLauncher
AgentLauncher
ProcessLauncher
SystemExecutor
OperatingSystemWorker
ControllerWorker
```

Unlike an agent, a worker does not primarily represent a domain.

Its role is to provide technical support.

---

## `indef_obj`

An `indef_obj` is an object whose nature has not yet been identified.

It behaves similarly to a traditional object-oriented class.

Typical use cases include:

* prototyping,
* exploratory development,
* refactoring,
* incremental migration to Clprolf.

Example:

```clprolf
public class_for indef_obj TemporaryManager {
}
```

The role can be refined later into either `agent` or `worker`.

---

## Inheritance Consistency

Clprolf encourages inheritance only when domain continuity exists.

Example:

```text
Animal
  └─ Dog
```

Both classes belong to the same conceptual domain.

Inheritance is therefore coherent.

However:

```text
Dog
  └─ DatabaseConnection
```

introduces a different domain.

Composition is generally preferred.

Clprolf uses this principle to help maintain coherent hierarchies.

Inheritance exceptions may still be forced through:

```text
@Forced_inh
```

when required.

---

## Using Declensions

In pure Clprolf, the declension replaces the traditional `class` keyword.

Examples:

```clprolf
public class_for agent Animal {
}
```

```clprolf
public class_for worker AnimalWorker {
}
```

```clprolf
public class_for indef_obj TemporaryManager {
}
```

The shorter syntax is also possible:

```clprolf
public agent Animal {
}
```

---

## Framework Usage

When using Clprolf as a framework, declensions are expressed through annotations.

```java
@Agent
public class Animal {
}
```

```java
@Worker
public class AnimalWorker {
}
```

```java
@Indef_obj
public class TemporaryManager {
}
```

---

## Practical Guidelines

When a class clearly represents a coherent subject, it is usually an `agent`.

Examples:

```text
File
Parser
Connection
Controller
Scheduler
PaymentService
Microservice
```

When a class primarily exists to support, execute, launch, render, or technically assist another class, it is usually a `worker`.

Examples:

```text
AnimalWorker
ControllerWorker
ApplicationLauncher
ProcessLauncher
OperatingSystemWorker
SystemExecutor
```

Some situations may admit multiple interpretations.

Clprolf provides guidance, but the final architectural interpretation remains the developer's responsibility.

---

## Summary

Declensions transform classes into explicit architectural components.

They make responsibilities visible and provide a common vocabulary for reasoning about systems.

```text
agent     → represents a domain
worker    → supports a domain
indef_obj → undefined role
```

By making these distinctions explicit, Clprolf helps maintain readable structures, coherent inheritance hierarchies, and long-term architectural clarity.
