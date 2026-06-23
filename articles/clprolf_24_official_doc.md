# Clprolf — Official Documentation

## Introduction

**Clprolf**("Clear PROgramming Language and Framework") is Java (or C#) framework.

Its goal is to make certain object-oriented programming best practices explicit, without introducing heavy architecture or a steep learning curve.

Clprolf is based on a simple idea:

> A class should clearly express its primary role.

The framework helps to:

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

# II) Class Types

Clprolf contains only three class types.

---

## II.1) `ClAgent`

Represents a business or conceptual class.

An `agent`:

* contains business or conceptual logic,
* orchestrates processes,
* makes decisions,
* avoids heavy technical code, which is often delegated to an associated worker,
* can be system-oriented, such as `Connection` or `Socket`.

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

## II.2) `ClWorker`

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

## II.3) `ClDraft`

An object without a defined role.

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

## II.4) Primary Domain and Technical Code

Clprolf encourages moving as much technical code as possible from `agent` classes into `worker` classes.

However, an `agent` may contain a reasonable amount of technical code when doing so improves simplicity or readability.

An `agent` always has a primary domain representing its central responsibility.

Secondary responsibilities may exist as long as they remain consistent with that primary domain.

---

## II.5) An "opinionated" framework for the agent/worker choice

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

# III) Inheritance

> A class only inherits from another class within the same domain.

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

# IV) Flexibility

Clprolf is flexible.

The developer therefore keeps their freedom:

* mixing responsibilities when necessary,
* progressive migration,
* compatibility with existing code,
* while always maintaining a primary domain.

The framework mainly acts as:

> a structural guide.

---

# V) Interfaces

In Clprolf, interfaces are viewed as:

> abstract forms of inheritance.

They therefore participate in the structural continuity of the system.

```text
ClFamily = primary family interface
ClTrait  = trait, shared capability between families
ClDraft = unrestricted interface
```

In Clprolf, interfaces are not viewed as simple technical contracts.

Both `extends` and `implements` relationships are considered genuine conceptual inheritance relationships.

---

## V.1) `ClFamily`

An interface representing an abstract family.

Used for:

* polymorphism,
* decoupling,
* implementation variants.

Family interfaces also have a target role:

* `ClAgent`
* or `ClWorker`

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

Which may lead to:

```java
@ClAgent
public class AnimalImpl implements Animal { (...) }
```

```java
@ClAgent
public class HorseImpl extends AnimalImpl implements Horse { (...) }
```

---

## V.2) `ClTrait`

An interface representing a shared capability across multiple `ClFamily`.

Traits also use a target role:

* `ClAgent`
* `ClWorker`

> **Note: a `@ClTrait` may be annotated with both `@ClAgent` and `@ClWorker`.**
>
> **This exception is reserved for genuinely cross-cutting traits that can be used by both agents and workers.**

---

### Business Example

```java
@ClAgent
@ClTrait
public interface Payable {
    void pay();
}
```

---

### Technical Example

```java
@ClWorker
@ClTrait
public interface Persistable {
    void save();
}
```

---

## V.3) Illustration of the Interface Family / Implementation Parallel

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

## V.4) `ClFree`

A generic interface without a specific role.

Allows flexibility.

---

### Example

```java
@ClFree
public interface ExternalApi {
}
```

---

## V.5) Using Interfaces

In Clprolf, `Family` interfaces closely resemble pure abstract classes.

They are intended to be implemented by one or more future Clprolf classes.
Therefore, they have a target role (`agent` or `worker`).

A class can only implement a single main `Family` at a time, and the class role must match the target role of the interface.
Clprolf thus uses single implementation for interfaces, in the same way that Java uses single inheritance for classes. Indeed, a `Family` is always the structural reflection of its implementation. This notably allows for systematic loose coupling.
However, multiple implementation is not removed, but rather shifted to the `Family` implemented by the class.
This family interface can itself inherit from multiple `Family` or `Trait` interfaces.
Thus, the interfaces that would have been implemented directly by the class are grouped together at the level of its main `Family`.
Clprolf therefore preserves the richness of multiple interface inheritance, while maintaining a simple and cohesive structure for concrete classes.

## Note: In non-strict mode, it is possible for a class to implement multiple `ClFamily` interfaces, to stay closer to standard Java and C# practices.

`Trait` interfaces express a common functionality across multiple `Family` interfaces.

A `Trait` therefore represents a cross-cutting trait shared among several families.

Normally, a `Trait` can only be inherited by a `Family` interface, and not directly implemented by a class.

```text
Concrete class
    ↓ implements
ClFamily
    ↓ inherits from
ClTrait

```

Note: A `Family` interface can inherit from multiple `Family` or `Trait` interfaces.

A `Trait` interface can only inherit from other `Traits`, because a trait remains a trait.

Forcing interface inheritance remains possible using `@ClInterfaceBypass` above the interface (or `@ClBypass` to force inheritance between different target roles).

Note: In non-strict mode, direct implementation of a `Trait` by a class is allowed.

---

## V.6) Note on Clprolf and the Interface Segregation Principle (ISP)

Clprolf inherently respects the ISP; it is simply a matter of adapting the design of your classes and interfaces using the appropriate families and traits:

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
    // (...)
}

@ClAgent
public class ModernPrinterImpl implements ModernPrinter {
    // (...)
}
```

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

# VIII) Purpose of the framework

Clprolf does not aim to replace classical OOP.

It aims to make certain important distinctions explicit:

* business vs technical,
* coherent inheritance vs composition,
* primary responsibility of a class.

---

# IX) ArchUnit Checker

An ArchUnit-based checker is available for the Clprolf Framework on GitHub. It is open-source and consists of two classes: `ClprolfArchTest` and `ClprolfStrictArchTest`. It validates semantic rules.
The rules in `ClprolfStrictArchTest` are optional. Likewise, it is easy to change the annotation names if you prefer a different vocabulary.

## 1. Java Version (ArchUnit)

Available on GitHub, the Java checker is open-source and centers around two main test classes:
* **`ClprolfArchTest`**: Validates the standard and fundamental semantic rules of the framework.
* **`ClprolfStrictArchTest`**: Gathers optional, more rigid constraints for demanding projects (such as forbidding a class from directly implementing a `ClTrait`).
The checkers (both Java and .NET) also contain the definitions for Clprolf annotations (or attributes).

---

## 2. C# .NET Version (ArchUnitNET)

The port of the .NET extension is available and published on GitHub.

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

# X) Alignment with SOLID and Design Principles

Clprolf does not aim to replace object-oriented programming best practices, but rather to offer a strict and verifiable structural framework to apply them naturally.

## 1. Single Responsibility Principle (SRP) & Separation of Concerns

Clprolf's first fundamental principle (Business vs. Technical) is a direct application of the SRP at the architectural level.

* A `@ClAgent` focuses exclusively on the "what" (conceptual behavior and business rules).
* A `@ClWorker` focuses on the "how" (infrastructure, technical aspects, and execution details).

By enforcing this separation, Clprolf prevents the creation of "God Classes" that handle both business logic and network calls or SQL queries.

## 2. "Favor Composition over Inheritance"

This is one of Clprolf's core battles. Inheritance is too often misused for the sole purpose of reusing technical code (for example, making a business class inherit from a technical utility).

Clprolf's rule stating that **a class can only inherit from another class within the same domain** eliminates this inappropriate coupling. If the domains diverge, the framework blocks the analysis and structurally pushes the developer to use **composition**.

## 3. Liskov Substitution Principle (LSP)

Clprolf secures the Liskov Substitution Principle. By prohibiting the mixing of roles (`@ClAgent` and `@ClWorker`) within the same inheritance hierarchy, the framework guarantees that subclasses share the same conceptual nature as their parent class. An agent will always substitute for an agent, without major behavioral or technical surprises.

## 4. Interface Segregation Principle (ISP) & Loose Coupling

Thanks to the clear distinction between `@ClFamily` and `@ClTrait`, Clprolf provides a precise semantic model for designing interfaces:

* `@ClTrait` interfaces force the creation of small, specialized, and cross-cutting interfaces (such as `Scanner`, `Fax`, `Printer`), which is the very essence of the ISP.
* The strict parallel between the abstract world (`ClFamily`) and the concrete world ensures systematic **loose coupling**, making the system natively ready for **Dependency Injection (DI)**.

## 5. Open/Closed Principle (OCP)

Clprolf's architecture (where concrete classes implement a `ClFamily` that aggregates various `ClTrait` interfaces) allows system capabilities to be extended without modifying existing code. New behaviors can be added by creating new traits or new family implementations, while the checker guarantees that the overall architecture remains impervious to drift.

---

# XI) Clprolf and Existing Architectures

Clprolf is compatible with existing architectural approaches such as Domain-Driven Design (DDD), Model-View-Controller (MVC), Clean Architecture, Hexagonal Architecture, and others.

Rather than replacing these architectures, Clprolf acts as an additional layer between Object-Oriented Programming (OOP) and software architecture. Its purpose is to complement, clarify, and reinforce existing architectural principles by making class roles and inheritance relationships more explicit.

In this way, Clprolf helps improve architectural consistency while remaining fully compatible with established design practices.


# XII) Summary

Clprolf introduces very few concepts.

## Classes

```text
Agent
Worker
Draft
```

## Interfaces

```text
Family
Trait
Free
```

## Two Fundamental Rules

```text
1. Separate business and technical concerns.
2. Inherit only within the same domain.
```