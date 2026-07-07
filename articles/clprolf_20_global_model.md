# Clprolf Framework — Global Model

Clprolf framework introduces simple class roles to clarify the natural place of each class in a project.

Many developers understand the basic distinction:

* a `ClAgent` carries meaning, intention, or domain behavior;
* a `ClWorker` is a system service and performs technical execution.

But when a project grows, another question appears:

> How do these classes relate to each other globally?

This document provides a conceptual overview of how the main Clprolf roles interact in practice.

It explains:

* how agents, systems, and workers depend on each other;
* how domain-level classes stay separated from technical classes;
* how system-oriented objects such as streams, sockets, files, threads, or even framework components like HTTP Controllers and Middlewares can be understood;
* how workers act as bridges between conceptual objects and low-level execution.

This is not a formal package-isolation compiler rule-set.

It is a **mental model**: a guide that helps developers classify new classes more naturally and understand Clprolf architecture as a whole.

---

## 1. The basic idea

The core model is simple:

```text
ClAgent  = domain meaning
ClWorker = technical execution
ClSystem = system capability or boundary object

```

A `ClAgent` represents something conceptually meaningful in the business or simulation program.

A `ClWorker` is a system service. It performs the technical work needed to support agents, the application, or the infrastructure.

We also constantly encounter classes that are close to the operating system, the framework, or the runtime, but still possess a clear conceptual identity and orchestrate behaviors.

Examples:

* `Stream`
* `Socket`
* `Thread`
* `File`
* `HttpController`
* `AuthMiddleware`
* `Window`

These are explicitly declared using the **`ClSystem`** role. They are still agents in spirit because they represent conceptual objects, but their domain is inherently connected to a system boundary or an infrastructure framework capability.

---

## 2. Global conceptual diagram

```text
┌────────────────────────────────────────────────────┐
│                      @ClAgent                      │ ◄─── (Can be orchestrated by ClSystem)
│     conceptual behavior, domain responsibility     │
│                                                    │
└─────────────────────────┬──────────────────────────┘
                          │
                          │ uses / delegates to
                          ▼
┌────────────────────────────────────────────────────┐
│                      @ClWorker                     │
│       system service for technical execution       │
│                  serving an agent                  │
└─────────────────────────┬──────────────────────────┘
                          │
                          │ may use
                          ▼
┌────────────────────────────────────────────────────┐
│                      @ClSystem                     │
│   conceptual object connected to system behavior   │
│ examples: stream, socket, controller, file, window │
└─────────────────────────┬──────────────────────────┘
                          │
                          │ delegates low-level work to
                          ▼
┌────────────────────────────────────────────────────┐
│                 LOW-LEVEL WORKER                   │
│   native call, rendering, I/O, OS/runtime work     │
└────────────────────────────────────────────────────┘

```

This diagram should not be read as a strict one-way package constraint. It shows the natural direction of responsibility:

```text
domain meaning → execution → system capability → low-level operation

```

---

## 3. ClAgent

A `ClAgent` represents a meaningful object or behavior. It carries intention, responsibility, and conceptual identity.

Examples:

* `OrderProcessor`
* `CheckoutService`
* `Snake`
* `FoodExpert`
* `UserEntity`

An agent answers questions such as:

* What does this object mean?
* What responsibility does it carry?
* What behavior belongs to its domain?

### ClAgent guidelines

A `ClAgent` may:

* call other agents;
* delegate technical work to workers;
* hold domain state;
* express business, application, or simulation meaning.

A `ClAgent` should avoid:

* directly performing heavy technical or native work;
* depending directly on low-level system files or sockets when a worker or a `ClSystem` wrapper can handle them;
* becoming a mixed class where domain decisions and technical execution are indistinguishable.

---

## 4. ClWorker

A `ClWorker` performs technical execution. It is a system service. It does not primarily represent a business domain concept. It performs work for an agent, the application, or the infrastructure.

Examples:

* `OrderRepository`
* `FileWriterWorker`
* `DatabaseWorker`
* `SocketWorker`
* `DirectoryExplorerWorker`

A worker answers questions such as:

* What technical operation must be performed?
* What system, framework, I/O, or rendering mechanism must be called?
* What concrete execution is needed by an agent?

### ClWorker guidelines

A `ClWorker` may:

* call other workers;
* use `ClSystem` components (like standard files or streams);
* perform I/O, rendering, persistence, networking, or application launching;
* call back an agent when acting as a technical bridge (UI events, async callbacks, notifications).

A `ClWorker` should avoid:

* containing domain or business decisions;
* becoming the conceptual brain of the application;
* hiding business rules inside technical code.

---

## 5. ClSystem

A `ClSystem` component is a system-oriented agent or an architectural boundary object whose conceptual domain is connected to a system or framework capability.

It is not a pure technical worker because it represents a stateful or behavioral subject with a clear role, and it often orchestrates or uses standard agents to react to external triggers.

Examples:

* A `Stream` or `Socket` represents a communication endpoint concept;
* A `File` represents a filesystem object concept;
* An `HttpController` or `AuthMiddleware` represents a conceptual entry point or router imposed by third-party frameworks.

These objects are conceptually meaningful. However, their implementation inherently interacts with external system mechanics. That low-level work is delegated to, or handled by, internal workers.

### ClSystem guidelines

A `ClSystem` component may:

* expose conceptual methods such as `open()`, `read()`, `handleRequest()`, or `close()`;
* orchestrate or call standard `ClAgent` instances (obvious for a Controller calling business logic);
* delegate its heavy technical realization to workers;
* interact with other `ClSystem` components within the same system domain.

A `ClSystem` component should avoid:

* mixing high-level orchestration or system routing with raw low-level technical operations (it should delegate those to a worker);
* bypassing standard roles to inherit from standard `ClAgent` or `ClWorker` classes (the checker treats `ClSystem` as an independent role to prevent inheritance mixing).

---

## 6. Summary table

Here is a simplified model of direct usage guidelines.

| From / To | `ClAgent` | `ClWorker` | `ClSystem` |
| --- | --- | --- | --- |
| **`ClAgent`** | Yes | Yes | Usually no (delegates to a worker) |
| **`ClWorker`** | Controlled callback / bridge | Yes | Yes |
| **`ClSystem`** | Yes (e.g., Controllers calling domain) | Yes | Yes, if same system domain |

This table is a structural guide, not a rigid flicage on package imports. The important idea is to preserve the direction of meaning and prevent chaotic inheritance.

---

## 7. Purpose of this model

This model helps developers:

* classify new classes correctly without a false binary choice between "pure business" and "raw technical";
* understand why third-party boundary classes (like Controllers) or low-level wrappers (like Sockets) are treated as explicit conceptual roles (`ClSystem`) rather than being misclassified as workers;
* decide when a class represents a **subject** to manipulate or orchestrate (`ClAgent`, `ClSystem`) or a **support** mechanism (`ClWorker`).

---

## 8. Final synthesis

Clprolf gives each class a natural place.

```text
@ClAgent
   → carries business, application, or simulation meaning.

@ClWorker
   → performs technical execution and serves agents.

@ClSystem
   → represents a conceptual system capability or an architectural framework boundary.

```

If the class represents something meaningful, it is an agent or a system component. If it mainly executes technical work, it is a worker. That is the global logic of Clprolf.

---

## Final line

> Agents carry meaning.
> Workers perform execution.
> ClSystem components represent meaningful system boundaries.
> Together, they make architecture easier to read, reason about, and maintain.