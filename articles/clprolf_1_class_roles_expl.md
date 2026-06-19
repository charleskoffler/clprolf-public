# Clprolf Docs #1 — Clprolf Class Roles Explained

## Foundational Principles of Clprolf (CLear PROgramming Language and Framework)

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

## What Is a Class Role?

It expresses the role of a class.

Rather than treating every class as a generic object, Clprolf makes its role explicit.

The available roles are:

1. **`agent`**
2. **`worker`**
3. **`draft`**

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

Represents a system service.

A worker class is primarily intended to support agent classes rather than be organized around a class domain.

Workers provide technical and infrastructure services. They may coordinate or use low-level agents (system agents) such as `File`, `Connection`, `Random`, `Logger`, or `Parser`, but unlike those classes, a worker is not organized around a class domain of its own.

Instead, it exists to support other components through technical mechanisms, infrastructure access, application startup, operating-system interaction, or similar responsibilities.

A `worker`:

* Is a system service;
* Provides technical support;
* Manages infrastructure and execution mechanisms;
* Contains technical code;
* Uses system abstractions, but is not one itself;
* Is often there to assist an agent class (including system agents) with rendering/display, direct database access, etc.;
* Allows for the separation of domain/functional code from purely technical code.

---


## `draft`

A `draft` is an object whose domain has not yet been identified.

It behaves similarly to a traditional object-oriented class.

Typical use cases include:

* prototyping,
* exploratory development,
* refactoring,
* incremental migration to Clprolf.

Example:

```java
@ClDraft
public class TemporaryManager {
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
@ClBypass
```

when required.

---

## Using Class Roles

In Clprolf, the class role is annotated on the class.

Examples:

```java
@ClAgent
public class Animal {
}
```

```java
@ClWorker
public class AnimalWorker {
}
```

```java
@ClDraft
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
ApplicationLauncher
ProcessLauncher
OperatingSystemWorker
SystemExecutor
```

---

## General Architecture

Clprolf naturally encourages a simple architecture.

```text
agent
    ↓ delegates to
worker
```

`agent` classes contain:

* business rules,
* decisions,
* orchestration.

`worker` classes perform:

* technical work,
* system access,
* machine operations.

An `agent` delegates technical code to one or more `worker` classes.

A worker serves the agent.


```text
┌────────────────────────────────────────────────────┐
│                       AGENT                        │
│       conceptual behavior, domain responsibility   │
│                                                    │
└─────────────────────────┬──────────────────────────┘
                          │
                          │ uses / delegates to
                          ▼
┌────────────────────────────────────────────────────┐
│                       WORKER                       │
│  		 system service for technical execution      │
│               serving an agent                     │
└─────────────────────────┬──────────────────────────┘
                          │
                          │ may use
                          ▼
┌────────────────────────────────────────────────────┐
│              (SYSTEM-ORIENTED) AGENT               │
│   conceptual object connected to system behavior   │
│   examples: stream, socket, thread, file, window   │
└─────────────────────────┬──────────────────────────┘
                          │
                          │ delegates low-level work to
                          ▼
┌────────────────────────────────────────────────────┐
│                    (LOW-LEVEL) WORKER              │
│     native call, rendering, I/O, OS/runtime work   │
└────────────────────────────────────────────────────┘
```

---


## An "opinionated" framework for the agent/worker choice

Some responsibilities can be interpreted in different ways depending on the architectural vision adopted.

For example, a connection can be represented:
* as an `agent`, if viewed as a functional abstraction;
* or as a `worker`, if considered a purely technical mechanism.

However, in such cases, the Clprolf framework imposes the use of an agent. For example, for a Connection class:
* an `agent` to represent the connection,
* and delegate the technical code to one or more `worker` classes.

This is why Clprolf can be described as an "opinionated" framework. 
As soon as a domain can be identified, it must be chosen over the worker perspective. This choice is argued by the fact that agents and abstractions are easier to manipulate and facilitate design.
However, declaring the Connection class as a `ClAgent` does not preclude it from having a worker for its own technical needs.


## Summary

Class roles transform classes into explicit architectural components.

They make responsibilities visible and provide a common vocabulary for reasoning about systems.

```text
agent     → represents a domain
worker    → supports a domain
draft  → domain not yet identified
```

By making these distinctions explicit, Clprolf helps maintain readable structures, coherent inheritance hierarchies, and long-term architectural clarity.
