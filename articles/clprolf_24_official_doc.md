# Clprolf — Embedded DSL (EDSL) and Architectural Framework

## Target Use Cases and Prerequisites

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

## Introduction

Clprolf is an **Embedded DSL (EDSL)** built upon a simple idea:

> A class should clearly express its primary role.

Acting as a domain-specific language embedded directly into the host code alongside a lightweight architectural framework, it helps to:

* separate business logic from technical code,
* limit architectural drift,
* make inheritance more coherent,
* improve system readability.
  
---

### In Java (ArchUnit)

In the Java ecosystem, components are marked using annotations:

```java
import org.clprolf.framework.ClAgent;

@ClAgent
public class CarImpl implements Car {
    // Agent logic...
}

```

### In C# (ArchUnitNET)

In the .NET ecosystem, the strict equivalent uses **C# Attributes** enclosed in brackets `[...]` placed directly above the class:

```csharp
using Clprolf.ArchUnitNet.Attributes;

namespace MyApp.Agents {

    [ClAgent]
    public class Car : ICar
    {
        // Agent logic...
    }

}
```

---

### 💡 Note for Framework Users (.NET vs Java)

* **Syntax:** Java's `@Annotation` becomes `[Attribute]` in C#.

---


### Theoretical Foundations

Clprolf is supported by a formal study that provides a theoretical foundation for its architectural model and the structural principles underlying its use of components, families, and traits.

For readers interested in the conceptual and formal foundations of the framework, see:

* [**Axiomatic Formalization of an Object-Oriented Software Topology and Static Domain Verification: The Clprolf Model**](https://doi.org/10.5281/zenodo.24310061) *(Zenodo Preprint)*
  

# I) The Two Fundamental Principles

Clprolf is based on two core principles.

---

## 1. A class is either business/conceptual or technical

Every class belongs to one of the following two worlds:

### Business / Domain World

The class represents a business or conceptual responsibility.

Examples:

* order management,
* business logic,
* simulation,
* functional orchestration,
* but also system-oriented agents.

These classes are declared with:

```java
@ClAgent

```

or

```java
@ClSystem

```

---

### Technical World

The class performs technical work executed by the system:

* database access (via system agents),
* network (often using low-level agents),
* usage of system abstractions such as files,
* UI / rendering,
* infrastructure,
* no conceptual domain, just the system executing technical agents or bootstrapping an application,
* typically a system technical service associated with an agent.

These classes are declared with:

```java
@ClWorker

```

---

## 2. Inheritance must preserve the domain

A class must only inherit from a class belonging to the same conceptual domain.

Otherwise:

> composition should be used instead.

This principle prevents incoherent hierarchies and mixed responsibilities.

---

# II) Progressive Adoption, Customization and Automatic Detection

The framework offers total flexibility in both its deployment strategy and its terminology.

---

## II.1) Step-by-Step Adoption

Integrating Clprolf into a project can be done incrementally. There is no need to enforce every rule from day one.

* **Step 1: Classes First.** You can focus exclusively on the core separation between `@ClAgent` (or `ClSystem`) and `@ClWorker`. This allows the team to master the class splitting without initial friction.
* **Step 2: Interfaces Later (Optional).** The interface model (`ClFamily`, `ClTrait`) can be introduced at a later stage.

> If you find the Clprolf interface system too disruptive compared to your habits or traditional OOP practices, you can choose to ignore it entirely. The framework remains fully functional and highly effective just for your classes.

---

## II.2) Tailoring the Terminology (Built-in Aliases)

Does Clprolf's default vocabulary not quite match your team's nomenclature?
The framework provides **built-in equivalent annotations/attributes** to naturally fit your team's culture.
The ArchUnit/ArchUnitNET checker treats these aliases as completely equivalent to the reference keywords during automated validations:

| Default Role | Conceptual Alternative | DDD Alternative |
| --- | --- | --- |
| **`@ClAgent`** | `@ClConcept` | `@ClDomain` |
| **`@ClWorker`** | `@ClMechanism` | `@ClInfrastructure` |
| **`@ClSystem`** | `@ClBridge` | `@ClLowLevel` |

> *Notes: You are free to use the aliases that resonate most with your architecture, or even extend the checker to register your team's custom annotations.*
> *The checker treats all aliases as strictly equivalent. You can use whichever vocabulary you prefer on a project without risking incompatibility, as the checker guarantees the exact same underlying architectural consistency.*

---
## II.3) Automatic Detection and Seamless Integration

To ease adoption and prevent visual clutter in the code, the Clprolf ArchUnit checker centralizes role qualification through its predicate methods (`isAgent()`, `isWorker()`, `isSystem()`).
This allows the framework to automatically map native annotations and attributes from your standard ecosystems (such as Spring or ASP.NET Core) to Clprolf roles. For example, a `@RestController` or an `[ApiController]` will be instantly recognized as a `@ClSystem`, without requiring any additional Clprolf annotations on your classes.
This approach delivers the full enforcement power of the framework in a completely invisible and progressive manner, without affecting the readability of your day-to-day code. As a result, your existing applications can benefit from the Clprolf checker's controls straight away, without adding a single annotation to your existing codebase.


# III) Hexagonal, DDD & Clprolf Alignment

Clprolf formalizes the major principles of **Hexagonal Architecture** and **Domain-Driven Design (DDD)** in the form of explicit OOP constraints and annotations.
It provides one vision of these architectures, but it is not the only one possible. Clprolf does not seek to be exhaustive, but rather to offer assistance in its own way.

## 1. The Equivalence Matrix

| Hexa / DDD / Clean Concept | Role & Responsibility | Clprolf Role | Typing & Annotations |
| --- | --- | --- | --- |
| **Aggregate / Entity** *(DDD)* | Business core, state, invariants, and pure domain rules. | **`Agent`** | `@ClAgent` |
| **Domain Service** *(DDD)* | Orchestration of business rules combining multiple aggregates. | **`Agent`** | `@ClAgent` |
| **Use Case** *(Clean)* / **Application Service** *(DDD)* | Functional scenario expressing business intent *(e.g., `RegisterCustomer`)*. | **`Agent`** | `@ClAgent` |
| **Primary Ports** *(Driving)* | Interfaces exposed by the domain to be driven from the outside. | **`Family`** | `@ClAgent @ClFamily` |
| **Secondary Ports** *(Driven)* | Interfaces required by the domain to communicate with the outside. | **`Family`** | `@ClWorker @ClFamily` |
| **Inbound Adapters** *(Hexa)* | System entry points *(REST Controllers, CLI Handlers, Consumers)*. | **`System`** | `@ClSystem` |
| **Outbound Adapters** *(Hexa)* / **Infrastructure Service** *(DDD)* | Technical executors *(Repository implementations, API clients, Senders)*. | **`Worker`** | `@ClWorker` |
| **Value Objects / DTOs** | Immutable, behaviorless data objects. | *Pure Data* | **Unannotated** |

---

## 2. Canonical Execution Flow

*Note: A flow is called **canonical** because it represents the official, standard reference execution model.*

```text
 ┌──────────────────────────────────────────────────────────────────┐
 │ INBOUND ADAPTERS (Driving)                                       │
 │                                                                  │
 │   ┌─────────────┐                                                │
 │   │  @ClSystem  │  (REST Controller, CLI, Kafka Consumer)          │
 │   └──────┬──────┘                                                │
 └──────────┼───────────────────────────────────────────────────────┘
            │
            │ Calls via Primary Port (@ClAgent @ClFamily)
            ▼
 ┌──────────────────────────────────────────────────────────────────┐
 │ DOMAIN CORE / USE CASES & AGGREGATES                             │
 │                                                                  │
 │   ┌─────────────┐  Orchestrates &  ┌─────────────┐               │
 │   │  @ClAgent   │ ───────────────> │  @ClAgent   │               │
 │   │ (Use Case)  │    manipulates   │ (Aggregate) │               │
 │   └──────┬──────┘                  └─────────────┘               │
 └──────────┼───────────────────────────────────────────────────────┘
            │
            │ Calls via Secondary Port (@ClWorker @ClFamily)
            ▼
 ┌──────────────────────────────────────────────────────────────────┐
 │ OUTBOUND ADAPTERS (Driven)                                       │
 │                                                                  │
 │   ┌─────────────┐                                                │
 │   │  @ClWorker  │  (JpaRepository, StripeClient, EmailSender)    │
 │   └─────────────┘                                                │
 └──────────────────────────────────────────────────────────────────┘

```

# IV) Class Types

Clprolf contains only four class types. Classes without methods (entities, DTOs, etc.) are not annotated.

---

## IV.1) `ClAgent`

Represents a business or conceptual class.

An `agent`:

* contains business or conceptual logic,
* orchestrates processes,
* makes decisions,
* avoids heavy technical code, which is often delegated to an associated worker,
* can be system-oriented like Connection or Socket (in which case `ClSystem` is used).

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

---

## IV.2) `ClWorker`

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

## IV.3) The `ClSystem` Role

The `@ClSystem` annotation (or `[ClSystem]` attribute in C#) must be used for system-oriented agents.
Inheritance cannot be mixed between standard agents and system-oriented agents. They are thus treated by the checker as an independent role.
System abstractions (such as `File`) are therefore annotated with `@ClSystem` instead of `@ClAgent`.
Note that classes imposed by third-party frameworks, such as Controllers, Routing, or Middlewares (Filters/Interceptors in Java), are treated as `ClSystem` classes.
`ClSystem` provides a more technical perspective while preserving the concept of domain.

### Clarifications

For example, a connection is represented in Clprolf as:

* a `ClSystem` system abstraction,
* its domain being the connection domain.

This results in:

* a `ClSystem` to represent the connection,
* purely technical code delegated to one or more `worker` classes,
* the ability to change the technical implementation if needed without modifying the conceptual code (see the Java `File` example).

### Java Example Illustrating `ClSystem`: `java.io.File`

The recent OpenJDK implementation of `java.io.File` reveals a class of roughly 2,000 lines. The class delegates all purely technical, non-domain work to a field serving as a worker equivalent (`FileSystem`).

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
CLPROLF CONCEPT                    JAVA SOURCE CODE (OpenJDK)
┌──────────────────────────┐            ┌──────────────────────────┐
│         @ClSystem        │            │       java.io.File       │
│    (System Abstraction)  │            │                          │
│ Represents the concept   │            │ Manages the file         │
│ of a file and its path.  │            │ abstraction and status.  │
│ Conceptual methods       │            │ Conceptual methods       │
└────────────┬─────────────┘            └────────────┬─────────────┘
             │                                       │
             │ delegates to                          │ calls
             ▼                                       ▼
┌──────────────────────────┐            ┌──────────────────────────┐
│         @ClWorker        │            │    java.io.FileSystem    │
│    (Low-Level Worker)    │            │       (FS variable)      │
│ Performs OS-specific     │            │ OS-dependent impl.,      │
│ access and validation    │            │ WinNT/UnixFileSystem.    │
└──────────────────────────┘            └──────────────────────────┘

```

Note: `java.io.UnixFileSystem` and `WinNTFileSystem` contain many `native` methods.

---

## IV.4) `ClDraft`

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

## IV.5) The Architectural Atom (`@ClAgent` / `@ClSystem` + `@ClWorker`)

In Clprolf, object-oriented automation applications are organized around **architectural atoms**, which compose together to form the different parts (or subsystems) of the software.

* **Conceptually and formally:** An atom is defined as the canonical pair associating a primary component—either a domain decision-maker (`@ClAgent`) or a system component (`@ClSystem`)—with its dedicated technical worker (`@ClWorker`).
* **In practice (non-restrictive checker):** Clprolf does not enforce structural cardinality rules on the internal composition of an atom. A `@ClAgent` or `@ClSystem` may freely collaborate with multiple `@ClWorker` instances (e.g., a *Service* paired with multiple *DAOs* or adapters), ensuring seamless compatibility with existing macro-architectures.

## IV.6) Primary Domain and Technical Code

Clprolf encourages moving as much technical code as possible from `agent` classes into `worker` classes.

However, an `agent` may contain a reasonable amount of technical code when doing so improves simplicity or readability.

An `agent` always has a primary domain representing its central responsibility.

Secondary responsibilities may exist as long as they remain consistent with that primary domain.

---

# V) Inheritance

> A class must only inherit from a class belonging to the same conceptual domain.

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

# VI) Flexibility

Clprolf is flexible.

The developer therefore keeps their freedom:

* mixing responsibilities when necessary,
* progressive migration,
* compatibility with existing code,
* while always maintaining a primary domain.

The framework mainly acts as:

> a structural guide.

---

### VII) Subsystems — Partitioning and Organization

In Clprolf, the concept of a subsystem (formally corresponding to a **part** $p$ of the application) is the logical organizational unit that structures the application model. A subsystem groups a coherent set of pilot components (`@ClAgent` and `@ClSystem`) and `@ClWorker` components—canonically organized into **architectural atoms**, and complemented where appropriate by standalone pilot components without a dedicated worker—to simulate a specific functional subdomain.

In Java, a subsystem is materialized by a `package` or a module; its equivalent in .NET C# is a `namespace` or an `assembly`.

#### 1. The Default Subsystem Rule

By construction (Axiom 1), every Clprolf application possesses at least one subsystem.

In its simplest form, this root subsystem groups all application components within the main package (or namespace). This minimal structure is often sufficient to cover the needs of small- to medium-sized applications.

#### 2. Specialization and Fine-Grained Partitioning

As application complexity grows, it becomes appropriate to distribute responsibilities across multiple subsystems according to the major domains or use cases derived from the modeled human work (e.g., *Billing*, *InventoryManagement*, *ThermalRegulation* subsystems). Each subsystem is primarily driven by business atoms (`@ClAgent` + `@ClWorker`), supported by the system atoms (`@ClSystem` + `@ClWorker`) and standalone pilot components required for its operation.

> *Note: In the case of a reusable library (such as collections, generic components, or system agents), a subsystem may exist without its own business domain, awaiting integration by the subsystems of a future application.*

> *Note: An atom may also be reused in another application; in that case, its package (or namespace) simply indicates its original subsystem, without binding all external usages to that part.*


#### 3. Compatibility with Existing Modular Architectures (DDD, Bounded Contexts, Microservices)

Clprolf subsystems do not replace established macroscopic partitioning paradigms; rather, they overlay them naturally.

* **Bounded Contexts (Domain-Driven Design):** A Clprolf subsystem can directly align with the boundary of a *Bounded Context*. Where DDD defines the semantic boundary of the domain model and its ubiquitous language, Clprolf structures and statically verifies the interior of that context into coherent atoms (`@ClAgent` / `@ClSystem` + `@ClWorker`).
* **Modular Monoliths and Microservices:** Each module of a modular monolith—or the internal application core of a microservice—can be organized as one or more Clprolf subsystems, without imposing constraints on external communication protocols (REST, gRPC, events).

# VIII) Interfaces

In Clprolf, there are two types of interfaces. Only one is directly implementable by a class: family interfaces (`ClFamily`).
The contract is more demanding than standard contracts. The class must be a component of the same type as the target role required by the contract (the "Target").
The class is restricted to a single possible contract. In non-strict mode, a class can implement multiple interfaces, including traits (`ClTrait`).
In pure Clprolf, traits are requirements for family interfaces and cannot be directly implemented by classes. They are not contracts for classes—or rather, they can be described as contracts for family interfaces. Traits represent common, cross-cutting features shared across family hierarchies (`ClFamily`).
They can only extend family interfaces that possess one of the target roles required by the trait.

In Clprolf, interfaces are viewed as:

> abstract forms of inheritance.

They therefore participate in the structural continuity of the system.

ClFamily = main interface of a family
ClTrait = trait, shared capability between families
ClFree = unrestricted interface

The `extends` and `implements` relationships are considered genuine conceptual inheritance relationships—hence the term "family".

---

## VIII.1) `ClFamily` and the Mirror Contract

An interface representing an abstract family, providing a mirror contract for future implementation by a class.

Used for:

- polymorphism,
- decoupling,
- implementation variants.

Family interfaces also possess a target role:

- `@ClAgent`
- `@ClWorker`
- or `@ClSystem`

---

## Example

```java
@ClAgent
@ClFamily
public interface Animal {

    void eat(int quantity);

}

```

The hierarchy of `ClFamily` interfaces naturally reflects the hierarchy of concrete classes.

```java
@ClAgent
@ClFamily
public interface Horse extends Animal {

    void jump(int height);

}

```

Which yields:

```java
@ClAgent
public class AnimalImpl implements Animal { (...) }


```

```java
@ClAgent
public class HorseImpl extends AnimalImpl implements Horse { (...) }


```

---


## VIII.2) `ClTrait`

An interface representing a shared feature across multiple `ClFamily` interfaces. It is not a contract for a class.
Traits use a target role, just like `ClFamily`:

* `@ClAgent`
* `@ClWorker`
* `@ClSystem`

However, a `@ClTrait` interface can be annotated with multiple component roles: `@ClAgent`, `@ClWorker`, or `@ClSystem`.
This exception is reserved for genuinely cross-cutting traits usable across several component types.
This means that the inheriting family interface must possess one of these roles.
In general, a trait possesses only one target role.

```java
@ClAgent
@ClWorker
@ClTrait
public interface Runnable {
    public void run();
}

```

---

## Business Example

```java
@ClAgent
@ClTrait
public interface Payable {
    void pay();
}

```

---

## Technical Example

```java
@ClWorker
@ClTrait
public interface Launcher {
    void start();
}

```

---

## VIII.3) Illustration of the Interface Family / Implementation Parallel

```text
[ABSTRACT WORLD / INTERFACES]          │    [CONCRETE WORLD / CLASSES]
                                       │
       @ClAgent @ClFamily              │        @ClAgent
        interface Animal               │       class AnimalImpl
               ▲                       │               ▲
               │ (extends)             │               │ (extends)
               │                       │               │
       @ClAgent @ClFamily              │        @ClAgent
         interface Horse               │       class HorseImpl
               ▲                       │               ▼ (implements)
               │                       │       👉 implements Horse
               └───────────────────────┼───────  (and extends AnimalImpl)
                (Structural Inheritance)│
                                       │
 ──────────────────────────────────────┴─────────────────────────────────
  👉 THE TRAIT (Cross-cutting):
  
       @ClAgent @ClTrait               │
        interface Jumpable             │
               ▲                       │
               │ (inherited by Family) │
               │                       │
     Horse extends Jumpable            │

```

## VIII.4) `@ClFree`

A generic interface without a specific role.
Allows flexibility. It is not essential.

---

## Example

```java
@ClFree
public interface ExternalApi {
}

```

---

## VIII.5) Using Interfaces

* In Clprolf, `Family` interfaces act as a mirror of components, but without an implementation.

The contract is more demanding than in Java or C#, because the class must not only implement the methods but also be a component of the targeted type (`ClAgent`, `ClWorker`, or `ClSystem`).
Finally, the contract dictates that a class implements only this single family interface (and no others) to maintain a mirror relationship between interfaces and their implementations.
However, multiple components (classes) can implement the same `ClFamily` interface.

A `Family` is always the structural reflection of its implementation. This systematically ensures loose coupling.
Implementing multiple family interfaces is not removed, but rather shifted to the `Family` implemented by the class. Everything remains possible.
**This only applies to the checker in strict mode.**

* `Trait` interfaces express a shared capability across multiple `Family` interfaces.

A `Trait` therefore represents a cross-cutting trait shared among several families. It is not a contract with a class.

A `Trait` can only be inherited by a `Family` interface (or extend another `ClTrait`), and cannot be directly implemented by a class.

**This is only true in the strict mode of the checker. Under non-strict checks, a trait can be directly implemented by a class, though some structural invariants are lost in the process.**

```text
Concrete class
    ↓ implements
ClFamily
    ↓ inherits from
ClTrait

```

Note:
Forcing interface inheritance remains possible using `@ClInterfaceBypass` above the interface (or `@ClBypass` to force inheritance between different target roles).
However, these bypasses should be rare.

### VIII.5.2) Strict Mode for Interfaces with Flexibility

It is possible to use `@ClFree` to remain strict only on selected interfaces while using `ClFamily` when greater rigor is needed in specific cases. In this way, even in strict mode, the framework remains open and flexible.
Native Java or C# interfaces are accepted and treated as `@ClFree`.

---

## VIII.6) Systematic Loose Coupling via the Mirror Interface

The mirror contract and the separation of traits provide structural loose coupling of implementations.

### A Real-world Example of `ClFamily` and `ClTrait` Interfaces

```java
package com.tngtech.archunit.lang;

@ClAgent @ClFamily
public interface ArchRule extends CanBeEvaluated, CanOverrideDescription<ArchRule> {
//(…)
}

```

```java
package com.tngtech.archunit.library;

public final class Architectures {
//(…)
@ClAgent
public static final class LayeredArchitecture implements ArchRule {
// (…)
}

}

```

> In this example, `CanBeEvaluated` and `CanOverrideDescription` act as `@ClTrait`s, while `ArchRule` formalizes the `@ClFamily`. Notice that the `LayeredArchitecture` class only implements the family, which itself inherits the traits.
> The class does not directly implement `CanBeEvaluated` or `CanOverrideDescription`.

## VIII.7) Note on Clprolf Interfaces and the Interface Segregation Principle (ISP)

Clprolf provides the tools to respect the ISP and helps prevent "fat interfaces" (monolithic interfaces).

### The Classic *Fat Interface* Problem

There is often a tendency to create a single monolithic interface grouping all possible features for a domain:

```java
// Anti-pattern: Fat Interface violating ISP
public interface Machine {
    void print(Document doc);
    void scan(Document doc);
    void fax(Document doc);
}

```

In this scenario, an older printer (`OldPrinterImpl`) that can only print would still be forced to implement `scan()` and `fax()`, often throwing an exception (`UnsupportedOperationException`), directly violating the ISP.

### The Clprolf Approach

The code tends to improve maintainability, scalability, and readability.

```java
@ClAgent
@ClTrait
public interface Scanner {
    void scan(Document doc);
}

@ClAgent
@ClTrait
public interface Fax {
    void fax(Document doc);
}

@ClAgent
@ClTrait
public interface Printer {
    void print(Document doc);
}

@ClAgent
@ClFamily
public interface OldPrinter extends Printer {
    
}

@ClAgent
@ClFamily
public interface ModernPrinter extends OldPrinter, Scanner, Fax {
}

@ClAgent
public class OldPrinterImpl implements OldPrinter {
//(…)
}

@ClAgent
public class ModernPrinterImpl implements ModernPrinter {
//(…)
}

```

# IX) Immutability

It is also possible to make `Clprolf` classes immutable, for instance by encapsulating the state within a record. Every method that modifies the state will return a new object, allowing further methods to be called in a fluent manner.
Much like the `String` class in Java/.NET, this approach guarantees native thread safety, eliminates side effects, and provides a fluent, expressive API. Naturally, the decision to use immutability should be evaluated on a case-by-case basis, considering trade-offs, performance costs, and preferences.
Below is an example implementation in Java and C#, though other solutions exist depending on your language version and whether you need to interact with mutable APIs.
These examples are not patterns, but merely illustrations of applying immutability within Clprolf.

```java

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@ClAgent
@ClFamily
public interface Car {

    // Immutable state definition
    // Automatically public static inside an interface

    record State(
            String make,
            int mileage,
            List<String> options
    ) {
        // Compact constructor enforcing domain invariants
        public State {
            Objects.requireNonNull(make, "Make cannot be null");
            if (mileage < 0) {
                throw new IllegalArgumentException("Mileage cannot be negative");
            }
            
            // Guarantees deep immutability via defensive copy and prevents null elements.
            // If 'options' is already an unmodifiable list (e.g., produced by Stream.toList() or List.of),
            // Java optimizes this call by returning the same instance without re-allocating memory.
            options = List.copyOf(options); 
        }
    }

    State state();

    // Business methods returning the contract abstraction
    Car drive(int distance);
    Car addOption(String newOption);
}

@ClAgent
public class CarImpl implements Car {

    // 🔒 Strictly final state reference
    private final State state;

    public CarImpl(State state) {
        this.state = Objects.requireNonNull(state, "State cannot be null");
    }

    @Override
    public State state() {
        return this.state;
    }

    // Covariant return types: methods return 'CarImpl' instead of 'Car'
    @Override
    public CarImpl drive(int distance) {
        if (distance <= 0) {
            return this; // No state change occurs
        }

        var newState = new State(
                state.make(),
                state.mileage() + distance,
                state.options()
        );
        return new CarImpl(newState);
    }

    @Override
    public CarImpl addOption(String newOption) {
        Objects.requireNonNull(newOption, "New option cannot be null");

        // OPTION 1: Optimized Stream concatenation (Single allocation)
        // Stream.toList() (Java 16+) returns an unmodifiable list directly.
        // When passed to State's constructor, List.copyOf(options) recognizes 
        // it as already unmodifiable and avoids a second copy.
        var updatedOptions = Stream.concat(state.options().stream(), Stream.of(newOption))
                                   .toList();

        /* 
        // OPTION 2: Alternative using a temporary mutable ArrayList
        // Simple and readable, but results in 2 list allocations in heap memory:
        // 1) The temporary ArrayList created here.
        // 2) The unmodifiable copy created inside State's constructor by List.copyOf().
        
        var updatedOptions = new ArrayList<>(state.options());
        updatedOptions.add(newOption);
        */

        return new CarImpl(new State(state.make(), state.mileage(), updatedOptions));
    }

    public static void main(String[] args) {
        // Using the specific type
        CarImpl initialCar = new CarImpl(new Car.State("Tesla", 10000, List.of("Autopilot")));
        CarImpl drivenCar = initialCar.drive(150);

        // Using the interface type
        Car genericCar = initialCar;
        Car updatedCar = genericCar.addOption("Premium Audio"); // Returns a Car
    }
}

```

## 2) C-Sharp version

```csharp

using System;
using System.Collections.Generic;
using System.Collections.Immutable;

[ClAgent]
[ClFamily]
public interface ICar
{

    public record State
    {
        public string Make { get; init; }
        public int Mileage { get; init; }

        // Strongly-typed property using the concrete immutable type
        public ImmutableArray<string> Options { get; init; }

        public State(string make, int mileage, IEnumerable<string> options)
        {
            ArgumentNullException.ThrowIfNull(make);
            ArgumentNullException.ThrowIfNull(options);

            if (mileage < 0)
            {
                throw new ArgumentOutOfRangeException(nameof(mileage), "Mileage cannot be negative.");
            }

            Make = make;
            Mileage = mileage;

            // ToImmutableArray() handles automatically if options is already an ImmutableArray
            Options = options.ToImmutableArray();

            // PERFORMANCE NOTE:
            // If the collection contains a very large number of items and undergoes VERY frequent additions/modifications,
            // consider using ImmutableList<string> and ToImmutableList() to optimize memory reuse (AVL tree):
            // Options = options.ToImmutableList();
        }
    }

    State CarState { get; }

    // Business methods returning the contract abstraction
    ICar Drive(int distance);
    ICar AddOption(string newOption);
}

[ClAgent]
public class CarImpl : ICar
{
    // Strictly read-only state reference
    public ICar.State CarState { get; }

    public CarImpl(ICar.State state)
    {
        CarState = state;
    }

    public CarImpl Drive(int distance)
    {
        if (distance <= 0)
        {
            return this; // No state change occurs
        }

        // C# 'with' expression creates a copy of State updating only Mileage
        var newState = CarState with { Mileage = CarState.Mileage + distance };
        return new CarImpl(newState);
    }

    public CarImpl AddOption(string newOption)
    {
        // Clean and direct syntax without casts or conversions.
        // Options.Add() returns a new ImmutableArray instance.
        var newState = CarState with { Options = CarState.Options.Add(newOption) };
        return new CarImpl(newState);
    }

    // Explicit interface implementation to satisfy the ICar contract in C#
    ICar ICar.Drive(int distance) => Drive(distance);
    ICar ICar.AddOption(string newOption) => AddOption(newOption);

    public static void Main(string[] args)
    {
        // Using the specific type
        CarImpl initialCar = new(new ICar.State("Tesla", 10000, new[] { "Autopilot" }));
        CarImpl drivenCar = initialCar.Drive(150);

        // Using the interface type
        ICar genericCar = initialCar;
        ICar updatedCar = genericCar.AddOption("Premium Audio");
    }
}

```

# X) Overall Architecture

Clprolf naturally encourages a simple architecture.

```text
agent
    ↓ delegates to
worker

```

`Agents` contain:

* business rules,
* decisions,
* orchestration.

`Workers` handle:

* technical execution,
* system access,
* machine-level operations.

An agent (including system-oriented `ClSystem`) delegates technical code to one or more workers. It may execute technical tasks, but only by invoking a worker method.
The worker serves the agent.

```text
┌────────────────────────────────────────────────────┐
│                       AGENT                        │
│               conceptual behavior,                 │
│          domain / business responsibility          │
└─────────────────────────┬──────────────────────────┘
                          │
                          │ uses / delegates to
                          │
                          ▼
┌────────────────────────────────────────────────────┐
│                       WORKER                       │
│       system service for technical execution,      │
│                 serving an agent                   │
└─────────────────────────┬──────────────────────────┘
                          │
                          │ may use
                          │
                          ▼
┌────────────────────────────────────────────────────┐
│               SYSTEM DOMAIN (ClSystem)             │
│    conceptual object tied to system behavior       │
│ examples: stream, socket, thread, file, window     │
└─────────────────────────┬──────────────────────────┘
                          │
                          │ delegates low-level work to
                          │
                          ▼
┌────────────────────────────────────────────────────┐
│                LOW-LEVEL WORKER                    │
│   native calls, rendering, I/O, OS / runtime tasks │
└────────────────────────────────────────────────────┘

```

---

# XI) Purpose of the framework

Clprolf does not aim to replace classical OOP.

It aims to make certain important distinctions explicit:

* business vs technical,
* coherent inheritance vs composition,
* primary responsibility of a class.

---

# XII) ArchUnit Checker

An ArchUnit-based checker is available for the Clprolf Framework. It consists of two classes, `ClprolfArchTest` and `ClprolfStrictArchTest`. It validates semantic rules.
The rules in `ClprolfStrictArchTest` are optional. Likewise, it is easy to change the annotation names if you prefer a different vocabulary.

## 1. Java Version (ArchUnit)

The Java checker is structured around two main test classes:

* **`ClprolfArchTest`**: Validates the framework's standard and fundamental semantic rules.
* **`ClprolfStrictArchTest`**: Enforces optional, more rigid constraints for demanding projects (such as forbidding a class from directly implementing a `ClTrait`).
Both checkers (Java and .NET) also contain the definitions of Clprolf annotations (or attributes).
To simplify the documentation, the `ClSystem` annotation is not included in the description of the rules. It is simply handled as an independent role by the checker.

---

## 2. C# .NET Version (ArchUnitNET)

The Visual Studio solution (2022 to date) contains a project with the framework and ArchUnit rules, along with an xUnit project for the tests.
A third example project is also included. There are currently 8 mandatory tests and 4 strict tests.

## Rules to Follow

### clprolf_classes_must_not_mix_agent_and_worker:
A class cannot be annotated as both `@ClAgent` and `@ClWorker`.

### agent_worker_inheritance_must_not_mix
A `@ClWorker` class cannot inherit from a `@ClAgent` class, and vice versa.

### family_role_must_match_implementation
The target role of a `@ClFamily` interface must match the role of the implementing class (`@ClAgent` or `@ClWorker`). Bypassing is possible using `@ClBypass`.

### (non-strict mode) trait_interface_role_must_match_direct_implementation
A class that directly implements a trait must have a compatible role (unless bypassed with `@ClBypass`). Forbidden in strict mode.

### trait_interfaces_must_extend_only_trait_interfaces
`@ClTrait` interfaces can only inherit from other `@ClTrait` interfaces. Bypassing is possible using `@ClInterfaceBypass`.

### clprolf_interfaces_must_have_target_role
`@ClFamily` interfaces must have exactly one target role: `@ClAgent` or `@ClWorker`.
`@ClTrait` interfaces must have at least one target role: `@ClAgent`, `@ClWorker`, or exceptionally both.

### inheriting_interface_role_must_match_trait_interface_target_role
Interfaces (family or trait) that inherit from a trait must have a role compatible with that trait (unless bypassed with `@ClBypass`).

### family_interface_target_role_must_match_inherited_family_interface
Family interfaces inherited by another family interface must have a compatible role, unless `@ClBypass` is used.

Stricter Rules:

### optional_all_classes_should_have_clprolf_role 
All classes must have a Clprolf role (`@ClAgent`, `@ClWorker`, or `@ClDraft`).

### optional_all_interfaces_should_have_clprolf_role 
All interfaces must have a Clprolf role (`@ClFamily`, `@ClTrait`, or `@ClFree`).

### optional_class_should_not_implement_trait_directly
A class cannot directly implement a `@ClTrait` interface (unless `@ClInterfaceBypass` is used).

### optional_class_must_implement_only_one_family_interface (OPTIONAL)
A Clprolf class can only implement a single `@ClFamily` interface. Bypassing is possible using `@ClInterfaceBypass`.

---

# XIII) Clprolf and the SOLID Principles

Clprolf consequently provides support for the SOLID principles. It does not claim to be the only possible vision of these principles, nor the only interpretation of SOLID.

## **S** — Single Responsibility Principle (SRP)

The framework naturally **helps apply** the Single Responsibility Principle (SRP). Indeed, each class possesses its own conceptual or business domain, which is strictly preserved during inheritance. Adhering strictly to Clprolf makes achieving this even easier.

## **O** — Open-Closed Principle (OCP)

This principle encourages us to anticipate future evolutions as extensions rather than code corrections. The Clprolf framework **facilitates** extensions through the strict separation of conceptual domains and workers. `ClFamily` interfaces promote clear visibility of the interfaces used within classes and push for well-thought-out features. `ClTrait` interfaces allow us to keep `ClFamily` interfaces even simpler and purer, while enabling traits to be shared and reused seamlessly.

## **L** — Liskov Substitution Principle (LSP)

Clprolf enforces inheritance that remains strictly within the same conceptual domain, alongside the separation between `ClAgent` and `ClWorker`. Thus, Liskov's LSP principle is more easily taken into account, as a `Square` class does not share the same conceptual domain as `Rectangle`.
Indeed, a `Giraffe` class belongs to the same domain as an `Animal`, from which it inherits its natural behaviors. Conversely, a `Square` class does not share the true nature of a `Rectangle` (it cannot have independent length and width). In Clprolf, they therefore do not belong to the same conceptual domain, which prevents improper inheritance and helps prevent certain violations of the LSP.

## **I** — Interface Segregation Principle (ISP)

This principle suggests that a client should not be forced to implement methods **it does not use**. With Clprolf, the interface is custom-tailored for the client, and traits are precise. This naturally promotes compliance with the ISP.

## **D** — Dependency Injection (DI)

Dependency injection is particularly effective when dependencies are loosely coupled to implementations. This loose coupling is encouraged and facilitated by `ClFamily` interfaces, which are closely **linked** to classes and act as a mirror of implementations. It then becomes very easy to replace an implementation with an interface in a variable declaration.

## "Favoring Composition over Inheritance"

The Clprolf framework ensures that inheritance is used sparingly and with precision, while relying on composition for everything else. It offers a practical and intuitive way to choose between the two.

# XIV) Clprolf and Existing Architectures

Clprolf is compatible with existing architectural approaches such as Domain-Driven Design (DDD), Model-View-Controller (MVC), Clean Architecture, Hexagonal Architecture, and others.
Rather than replacing these architectures, Clprolf acts as an additional layer between Object-Oriented Programming (OOP) and software architecture. Its purpose is to complement, clarify, and reinforce existing architectural principles by making class roles and inheritance relationships more explicit.
In this way, Clprolf helps improve architectural consistency while remaining fully compatible with established design practices.
Furthermore, Clprolf is not just meant for enterprise software, but for all types of applications, including simulations and scientific applications.

# XV) Clprolf and AI: Preserving Conceptual Mastery and Code Longevity

In the era of AI-assisted development, the risk goes beyond business logic drift: even at the heart of an algorithm or a purely technical module, an AI tends to generate flat, purely operational code, struggling to partition responsibilities conceptually.

Clprolf restores this irreplaceable structuring role to the human architect at every level:
- **Thinking in abstractions before execution:** It is up to the human architect to discern when an algorithm or technical flow benefits from introducing an intermediate conceptual `@ClAgent` to orchestrate logic, a `@ClSystem` to govern the technical substrate, and `@ClWorker` components for concrete execution.
- **Building an architecture that endures over time:** Once this skeleton is laid down by the human (via roles, `@ClFamily` interfaces, and the **Mirror** contract), AI can fill in the implementations under the strict supervision of the static checker. The resulting code remains clear, controllable, and maintainable over the long term instead of becoming an opaque black box.

# XVI) Summary

Clprolf introduces very few concepts.

## Classes

```text
ClAgent
ClWorker
ClSystem
ClDraft
```

## Interfaces

```text
ClFamily
ClTrait
ClFree
```

## Two Fundamental Rules

```text
1. Separate business and technical concerns.
2. Inherit only within the same domain.
```

---
