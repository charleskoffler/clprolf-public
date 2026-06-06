# A Global Model of How Classes Relate to Each Other in Clprolf framework

Clprolf framework introduces simple class roles to clarify the natural place of each class in a project.

Many developers understand the basic distinction:

* an `agent` carries meaning, intention, or domain behavior;
* a `worker` performs technical execution.

But when a project grows, another question appears:

> How do these classes relate to each other globally?

This document provides a conceptual overview of how the main Clprolf roles interact in practice.

It explains:

* how agents and workers depend on each other;
* how domain-level classes stay separated from technical classes;
* how system-oriented objects such as streams, sockets, files, or threads can be understood;
* how workers act as bridges between conceptual objects and low-level execution.

This is not a formal compiler rule-set.

It is a **mental model**: a guide that helps developers classify new classes more naturally and understand Clprolf architecture as a whole.

---

## 1. The basic idea

The core model is simple:

```text
Agent  = meaning
Worker = execution
```

An `agent` represents something conceptually meaningful in the program.

A `worker` performs the technical work needed to support agents, the application, or the system.

In many systems, we also encounter classes that are close to the operating system or runtime, but still have a conceptual identity.

Examples:

* `Stream`
* `Socket`
* `Thread`
* `Channel`
* `File`
* `Window`
* `Button`

These can be understood as **system-oriented agents**.

They are still agents because they represent conceptual objects, but their internal behavior often depends on low-level workers.

A system-oriented agent is not necessarily a new official class role.

It is a conceptual category:

> an agent whose domain is connected to a system capability.

---

## 2. Global conceptual diagram

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
│        technical execution serving an agent        │
│                                                    │
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

This diagram should not be read as a strict one-way call graph.

It shows the natural direction of responsibility:

```text
meaning → execution → system capability → low-level operation
```

---

## 3. Agent

An `agent` represents a meaningful object or behavior.

It carries intention, responsibility, and conceptual identity.

Examples:

* `OrderProcessor`
* `CheckoutService`
* `Snake`
* `FoodExpert`
* `WindowObserver`
* `Button`
* `DirectoryExplorer`
* `Animal`

An agent answers questions such as:

* What does this object mean?
* What responsibility does it carry?
* What behavior belongs to its domain?

### Agent guidelines

An agent may:

* call other agents;
* delegate technical work to workers;
* hold domain state;
* express business, application, simulation, or UI meaning.

An agent should avoid:

* directly performing heavy technical work;
* depending directly on low-level system mechanisms when a worker can handle them;
* becoming a mixed class where domain decisions and technical execution are indistinguishable.

If an agent needs system-level behavior, it usually delegates it to a worker.

---

## 4. Worker

A `worker` performs technical execution.

It does not primarily represent a domain concept.
It performs work for an agent, the application, or the system.

Examples:

* `OrderRepository`
* `FileWriterWorker`
* `DatabaseWorker`
* `RendererWorker`
* `Launcher`
* `SocketWorker`
* `SwingRenderer`
* `DirectoryExplorerWorker`

A worker answers questions such as:

* What technical operation must be performed?
* What system, framework, I/O, or rendering mechanism must be called?
* What concrete execution is needed by an agent?

### Worker guidelines

A worker may:

* call other workers;
* use system-oriented agents;
* perform I/O, rendering, persistence, networking, or launching;
* call back an agent when acting as a technical bridge, such as in UI events, callbacks, adapters, or asynchronous notifications.

A worker should avoid:

* containing domain decisions;
* becoming the conceptual brain of the application;
* hiding business rules inside technical code.

A worker can bridge the domain world and the system world, but it should not absorb the domain.

---

## 5. System-oriented agent

A **system-oriented agent** is an agent whose conceptual domain is connected to a system capability.

It is not simply a worker, because it represents an object with meaning.

Examples:

* a `Stream` represents a flow of data;
* a `Socket` represents a communication endpoint;
* a `Thread` represents an execution path;
* a `File` represents a filesystem object;
* a `Window` represents a visible interaction space;
* a `Button` represents a clickable UI object.

These objects are conceptually meaningful.

However, their implementation often requires technical operations:

* native calls,
* OS interaction,
* rendering,
* I/O,
* event dispatching,
* memory or runtime management.

That low-level work should be handled internally by workers.

### System-oriented agent guidelines

A system-oriented agent may:

* expose conceptual methods such as `open()`, `read()`, `write()`, `click()`, `show()`, or `close()`;
* delegate its technical realization to workers;
* interact with other system-oriented agents in the same coherent system domain.

A system-oriented agent should avoid:

* calling unrelated domain agents directly;
* mixing high-level business decisions with low-level system mechanics;
* exposing technical implementation details as its main identity.

The key idea is:

> A system-oriented agent represents a system capability as a meaningful object.
> A worker performs the low-level execution behind it.

---

## 6. Summary table

Here is a simplified model of direct usage.

| From / To                 |                        Agent | Worker |      System-oriented agent |
| ------------------------- | ---------------------------: | -----: | -------------------------: |
| **Agent**                 |                          Yes |    Yes |                 Usually no |
| **Worker**                | Controlled callback / bridge |    Yes |                        Yes |
| **System-oriented agent** |                   Usually no |    Yes | Yes, if same system domain |

This table is a guide, not a rigid rule-set.

The important idea is not to forbid every possible dependency.

The important idea is to preserve the direction of meaning:

```text
agents carry meaning
workers execute
system-oriented agents represent system capabilities
low-level workers perform the technical realization
```

---

## 7. Purpose of this model

This model helps developers:

* classify new classes correctly;
* avoid accidental mixing of concerns;
* navigate complex systems more safely;
* recognize patterns inside existing codebases;
* understand why some system classes still feel like agents;
* decide when a UI object, listener, socket, stream, or file should be modeled as an agent or as a worker.

It also explains why Clprolf is not just:

```text
business = agent
technical = worker
```

The real model is slightly richer:

```text
agent  = conceptual meaning
worker = technical execution
```

Some conceptual objects are close to the system.

That does not automatically make them workers.

A button can be an agent.
A listener can be an observer agent.
A stream can be a system-oriented agent.
Their rendering, event dispatching, or native operations can be workers.

---

## 8. Final synthesis

Clprolf gives each class a natural place.

At the highest level:

```text
Agent  = meaning
Worker = execution
```

Then the model expands:

```text
Domain agent
    → carries business, application, simulation, or UI meaning

Worker
    → performs technical execution

System-oriented agent
    → represents a conceptual system capability

Low-level worker
    → performs native, rendering, I/O, or runtime operations
```

This model keeps Clprolf simple while explaining real-world cases more accurately.

It avoids a false choice between “everything technical is worker” and “everything conceptual is pure business.”

Instead, it asks one practical question:

> What is the main responsibility of this class?

If the class represents something meaningful, it is probably an agent.

If it mainly executes technical work, it is probably a worker.

That is the global logic of Clprolf.

---

## Final line

> Agents carry meaning.
> Workers perform execution.
> System-oriented agents represent meaningful system capabilities.
> Together, they make architecture easier to read, reason about, and maintain.
