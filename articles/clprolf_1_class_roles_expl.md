# Clprolf Docs #1 — Clprolf Class Roles Explained

# I) The Two Fundamental Principles

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

# II) Progressive Adoption and Customization

The framework offers total flexibility in both its deployment strategy and its terminology.

---

## II.1) Step-by-Step Adoption

Integrating Clprolf into a project can be done incrementally. There is no need to enforce every rule from day one.

* **Step 1: Classes First.** You can focus exclusively on the core separation between `@ClAgent` (or `ClSystem`) and `@ClWorker`. This allows the team to master the class splitting without initial friction.
* **Step 2: Interfaces Later (Optional).** The interface model (`ClFamily`, `ClTrait`) can be introduced at a later stage.

> If you find the Clprolf interface system too disruptive compared to your habits or traditional OOP practices, you can choose to ignore it entirely. The framework remains fully functional and highly effective just for your classes.

---

## II.2) Tailoring the Terminology

If the default vocabulary of Clprolf does not fit your team's nomenclature, the framework is completely agnostic regarding name choices. Since the ArchUnit checker targets the actual declaration types, a simple **automated refactoring (Rename)** in your IDE is all it takes to adapt the framework to your company's culture.

Here are a few examples of alternative naming conventions:

| Default Role | Conceptual Alternative | Technical Alternative |
| --- | --- | --- |
| **`@ClAgent`** | `@ClConcept` | `@ClDomain` |
| **`@ClWorker`** | `@ClMechanism` | `@ClInfrastructure` |
| **`@ClSystem`** | `@ClBridge` | `@ClLowLevel` |

---

# III) Class Types

Clprolf contains only four class types.

---

## III.1) `ClAgent`

Represents a business or conceptual class.

An `agent`:

* contains business or conceptual logic,
* orchestrates processes,
* makes decisions,
* avoids heavy technical code, which is often delegated to an associated worker,
* can be system-oriented, such as `Connection` or `Socket` (in which case `ClSystem` can also be used).

Example:

```java
@ClAgent
public class OrderProcessor {

    private OrderRepository repository;

    public void process(Order order) {
        if(order.total() <= 0) {
            throw new Error();
        }
        repository.save(order);
    }
}

```

Note: Entities are considered agents because they possess a domain, even without methods.

---

## III.2) `ClWorker`

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

## III.3) The Optional `ClSystem` Role

The `@ClSystem` annotation (or `[ClSystem]` attribute in C#) can be used for system-oriented agents. However, it is not mandatory, keeping the framework as simple as possible.
If used, mixing inheritance between standard agents and system-oriented agents is strictly forbidden. The checker will then treat them as a completely independent role.
Developers who prefer to explicitly declare and finely control system-oriented agents (such as `File`) can annotate them as `ClSystem` instead of `ClAgent`.
Note that classes imposed by third-party frameworks—such as **Controllers**, **Routing**, or **Middlewares** (*Filters/Interceptors* in Java)—can also be considered `ClSystem` classes.
`ClSystem` provides a more technical perspective for those who prefer not to view system-oriented agents as pure agents, allowing them to be separated from more traditional agents.

---

## III.4) `ClDraft`

An object without a defined role. Normally, it shouldn't be essential.

Used:

* during prototyping,
* during refactoring,
* when the role is not yet clear.

Example:

```java
@ClDraft
public class TemporaryManager {
}
```

`ClDraft` enables a flexible approach similar to classical OOP.

---

## III.5) Primary Domain and Technical Code

Clprolf encourages moving as much technical code as possible from `agent` classes into `worker` classes.

However, an `agent` may contain a reasonable amount of technical code when doing so improves simplicity or readability.

An `agent` always has a primary domain representing its central responsibility.

Secondary responsibilities may exist as long as they remain consistent with that primary domain.

---

### III.6) An "Opinionated" Framework for the Agent/Worker Choice

Some responsibilities can be interpreted in different ways depending on the architectural vision adopted.

For example, a connection can be represented:

* as an `agent` (or `ClSystem`), if viewed as a functional abstraction;
* or as a `worker`, if considered a purely technical mechanism.

However, in such cases, the Clprolf framework imposes the use of an agent. For example, for a `Connection` class:

* an `agent` to represent the connection (though you can also choose `ClSystem`),
* and delegate the technical code to one or more `worker` classes.

This is why Clprolf can be described as an "opinionated" framework.
As soon as a domain can be identified, it must be chosen over the worker perspective. This choice is argued by the fact that agents and abstractions are easier to manipulate and facilitate design.
However, declaring the `Connection` class as a `ClAgent` (or `ClSystem`) does not preclude it from having a worker for its own technical needs.

---

### Java Example Confirming the Clprolf Vision: `java.io.File`

The recent OpenJDK implementation of `java.io.File` reveals a fairly long class of about 2,000 lines. The class delegates all purely technical, non-domain-related work to an attribute that acts as the strict equivalent of a worker (`FileSystem`).

```java
private static final FileSystem FS = DefaultFileSystem.getFileSystem();

```

```java
 public boolean delete() {
        if (isInvalid()) {
            return false;
        }
        return FS.delete(this);
    }

```

```text
       CLPROLF CONCEPT                        JAVA SOURCE CODE (OpenJDK)
┌──────────────────────────┐            ┌──────────────────────────┐
│ @ClAgent (@ClSystem)     │             │       java.io.File       │
│      (System Agent)      │            │                          │
│ Represents the concept   │            │ Manages the file         │
│ of a file and its path.  │            │ abstraction and status.  │
│ Conceptual methods       │            │ Conceptual methods       │
└────────────┬─────────────┘            └────────────┬─────────────┘
             │                                       │
             │ delegates to                          │ calls
             ▼                                       ▼
┌──────────────────────────┐            ┌──────────────────────────┐
│        @ClWorker         │            │    java.io.FileSystem    │
│    (Low-Level Worker)    │            │       (FS variable)      │
│ Performs OS-specific     │            │ OS-specific implement.   │
│ validation and access.   │            │ (WinNT/UnixFileSystem).  │
└──────────────────────────┘            └──────────────────────────┘

```

*Note: `java.io.UnixFileSystem` and `WinNTFileSystem` contain many `native` methods.*

---


# IV) Inheritance

> Class inheritance can be forced using `@ClBypass` above the class, but this should be rare.

---

## Valid Example

```java
@ClAgent
public class Animal {
}

@ClAgent
public class Dog extends Animal {
}
```

---

## Discouraged Example

```java
@ClWorker
public class ClientRepository {
}

@ClAgent
public class Dog extends ClientRepository {
}
```

Here, the domains are incompatible.

Composition should be used instead.

Inheritance can be forced using `@ClBypass` above the class.

---

# V) Flexibility

Clprolf is flexible.

The developer therefore keeps their freedom:

* mixing responsibilities when necessary,
* progressive migration,
* compatibility with existing code,
* while always maintaining a primary domain.

The framework mainly acts as:

> a structural guide.

---

# VI) General Architecture

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
│              (SYSTEM-ORIENTED) AGENT (ClSystem)    │
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