# Clprolf — Official Documentation

## Introduction

**Clprolf** is both a language and an equivalent framework.

Its goal is to make certain object-oriented programming best practices explicit, without introducing heavy architecture or a steep learning curve.

Clprolf is based on a simple idea:

> A class should clearly express its primary role.

The language helps to:

* separate business logic from technical code,
* limit architectural drift,
* make inheritance more coherent,
* improve system readability.

Clprolf remains fully object-oriented and close to Java.

---

# I) The Two Fundamental Principles

Clprolf is built around two central principles.

---

## 1. A class is either business-oriented or technical

Every class belongs to one of the following worlds:

### Business / Domain World

The class represents a business or conceptual responsibility.

Examples:

* order management,
* business logic,
* simulation,
* functional orchestration.

These classes are declared using:

```clprolf
agent
```

---

### Technical World

The class performs a technical task:

* database access,
* networking,
* file handling,
* display,
* infrastructure.

These classes are declared using:

```clprolf
worker
```

---

## 2. Inheritance must preserve the domain

A class should only inherit from another class belonging to the same conceptual domain.

Otherwise:

> composition should be used.

This principle prevents incoherent hierarchies and mixed responsibilities.

---

# II) Class Types

Clprolf contains only three class types.

---

## II.1) `agent`

Represents a business or conceptual class.

An `agent`:

* contains business logic,
* orchestrates processes,
* makes decisions,
* avoids heavy technical code.

Note: entities and DTOs are typically classified as agents, since they represent domain data.

Example:

```clprolf
public agent OrderProcessor {

    private OrderRepository repository;

    public void process(Order order) {
        if(order.total() <= 0) {
            throw Error;
        }
        repository.save(order);
    }
}
```

---

## II.2) `worker`

Represents a technical class.

A `worker`:

* performs machine-oriented tasks,
* manages infrastructure,
* contains technical code.

Example:

```clprolf
public worker OrderRepository {

    public void save(Order order) {

        // database access

    }
}
```

---

## II.3) `indef_obj`

An object without a defined role.

Used:

* during prototyping,
* during refactoring,
* when the role is not yet clear.

Example:

```clprolf
public indef_obj TemporaryManager {
}
```

`indef_obj` enables a flexible approach similar to classical OOP.

---

## II.4) Primary Domain and Technical Code

Clprolf encourages moving as much technical code as possible from `agent` classes into `worker` classes.

However, an `agent` may contain a reasonable amount of technical code when doing so improves simplicity or readability.

An `agent` always has a primary domain representing its central responsibility.

Secondary responsibilities may exist as long as they remain consistent with that primary domain.

---

## II.5) Freedom of Interpretation and Language Recommendations

The choice between `agent` and `worker` is left to the developer.

Some responsibilities may be interpreted differently depending on the adopted architectural vision.

For example, a connection may be represented:

* as an `agent` if viewed as a functional abstraction;
* or as a `worker` if viewed as a purely technical mechanism.

However, in these cases, Clprolf recommends using an agent. For example, for a `Connection` class:

* an `agent` to represent the connection,
* and delegate technical code to one or more `worker` classes.

---

# III) Inheritance

> A class only inherits from another class within the same domain.

---

## Valid Example

```clprolf
public agent Animal {
}

public agent Dog extends Animal {
}
```

---

## Discouraged Example

```clprolf
public worker ClientRepository {
}

public agent Dog extends ClientRepository {
}
```

Here, the domains are incompatible.

Composition should be used instead.

Inheritance can be forced using `@Forc_inh` above the class.

---

# IV) Flexibility

Clprolf is flexible.

The developer therefore keeps their freedom:

* mixing responsibilities when necessary,
* progressive migration,
* compatibility with existing code,
* while always maintaining a primary domain.

The language mainly acts as:

> a structural guide.

---

# V) Interfaces

In Clprolf, interfaces are viewed as:

> abstract forms of inheritance.

They therefore participate in the structural continuity of the system.

```text
family_interf = primary family interface
trait_interf  = trait, shared capability between families
compat_interf = unrestricted interface
```

In Clprolf, interfaces are not viewed as simple technical contracts.

Both `extends` and `implements` relationships are considered genuine conceptual inheritance relationships.

---

## V.1) `family_interf`

An interface representing an abstract family.

Used for:

* polymorphism,
* decoupling,
* implementation variants.

Family interfaces also have a target role:

* `agent`
* or `worker`

---

## Example

```clprolf
public family_interf agent Animal {

    void eat(int quantity);

}
```

The hierarchy of `family_interf` interfaces naturally reflects the hierarchy of concrete classes.

```clprolf
public family_interf agent Horse extends Animal {

    void jump(int height);

}
```

Which may lead to:

```clprolf
public agent AnimalImpl implements Animal { (...) }
```

```clprolf
public agent HorseImpl extends AnimalImpl implements Horse { (...) }
```

---

## V.2) `trait_interf`

An interface representing a shared capability across multiple `family_interf`.

Traits also use a target role:

* `agent`
* `worker`

> **Note: a `@Trait_interf` may be annotated with both `@Agent` and `@Worker`.**
>
> **This exception is reserved for genuinely cross-cutting traits that can be used by both agents and workers.**

---

### Business Example

```clprolf
public trait_interf agent Payable {
    void pay();
}
```

---

### Technical Example

```clprolf
public trait_interf worker Persistable {
    void save();
}
```

---

## V.3) `compat_interf`

A generic interface without a specific role.

Allows flexibility.

---

### Example

```clprolf
public compat_interf ExternalApi {
}
```

---

# V.4) Interface Usage

In Clprolf, `family_interf` interfaces are the equivalent of pure abstract classes.

They are intended to be implemented by one or more future Clprolf classes.

They therefore possess a target role (`agent` or `worker`).

A class may implement only one primary `family_interf` at a time, and the role of the class must match the target role of the interface.

Clprolf therefore adopts a simple interface implementation model, in the same way that Java uses single class inheritance. Indeed, a `family_interf` is always the structural reflection of its implementation. This notably enables systematic loose coupling.

However, multiple interface implementation is not removed; it is simply moved to the `family_interf` implemented by the class.

This family interface may itself inherit from multiple `family_interf` and/or `trait_interf` interfaces.

As a result, interfaces that would otherwise have been implemented directly by the class are grouped at the level of its primary `family_interf`.

Clprolf therefore preserves the expressive power of multiple interface inheritance while maintaining a simple and coherent structure for concrete classes.

---

`trait_interf` interfaces express a capability shared among multiple `family_interf` interfaces.

A `trait_interf` therefore represents a cross-cutting trait shared by several families.

Normally, a `trait_interf` may only be inherited by a `family_interf`, not directly by a class.

However, direct implementation of a `trait_interf` by a class remains tolerated in Clprolf, although discouraged.

```text
Concrete Class
      ↓ implements
family_interf
      ↓ inherits from
trait_interf
```

A `family_interf` may inherit from multiple `family_interf` and/or `trait_interf`.

A `trait_interf` may inherit only from other `trait_interf`, since a trait remains a trait.

Interface inheritance may still be forced using `@Forc_int_inh` (or `@Forc_inh` when forcing inheritance across different target roles).

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

---

# VII) Clprolf Framework

Clprolf can also be used as a framework within an existing language such as Java.

In that case, keywords are replaced with annotations.

---

## VII.1) Classes

### Agent

```java
@Agent
public class OrderProcessor {
}
```

### Worker

```java
@Worker
public class OrderRepository {
}
```

### Indefinite Object

```java
@Indef_obj
public class TemporaryManager {
}
```

---

## VII.2) Family Interfaces

Family interfaces use two annotations:

* a role annotation,
* plus `@Family_interf`.

### Business Example

```java
@Agent
@Family_interf
public interface PaymentService {
}
```

### Technical Example

```java
@Worker
@Family_interf
public interface DatabaseStorage {
}
```

---

## VII.3) Trait Interfaces

Trait interfaces use:

```java
@Trait_interf
```

along with a target role.

### Business Example

```java
@Agent
@Trait_interf
public interface Payable {
}
```

### Technical Example

```java
@Worker
@Trait_interf
public interface Persistable {
}
```

---

## VII.4) Free Compatibility

```java
@Compat_interf
public interface ExternalApi {
}
```

---

# VIII) Purpose of the Language

Clprolf does not aim to replace classical OOP.

It aims to make certain important distinctions explicit:

* business vs technical,
* coherent inheritance vs composition,
* primary responsibility of a class.

---

# IX) ArchUnit Checker for the Clprolf Framework

An ArchUnit-based checker is available for the Clprolf Framework on GitHub.
It is open source and consists of two classes: ClprolfArchTest and ClprolfStrictArchTest.

It validates the semantic rules of Clprolf.

The rules contained in ClprolfStrictArchTest are optional.
Likewise, annotation names can easily be customized if another vocabulary is preferred.

### clprolf_classes_must_not_mix_agent_and_worker

A class cannot be annotated with both @Agent and @Worker.

### agent_worker_inheritance_must_not_mix

A @Worker class cannot inherit from an @Agent class, and vice versa.

### family_interface_role_must_match_implementation

The target role of a @Family_interf must match the role of its implementing class (@Agent or @Worker).

Can be overridden using @Forc_inh.

### (non-strict mode) trait_interface_role_must_match_direct_implementation

A class that directly implements a trait must have a compatible role.

Can be overridden using @Forc_inh.

Direct trait implementation is forbidden in strict mode.

### trait_interfaces_must_extend_only_trait_interfaces

A @Trait_interf may only extend other @Trait_interf interfaces.

Can be overridden using @Forc_int_inh.

### clprolf_interfaces_must_have_target_role

A @Family_interf must declare exactly one target role:
@Agent or @Worker.

A @Trait_interf must declare at least one target role:
@Agent, @Worker, or exceptionally both.

### trait_interface_target_role_must_match_inheriting_interface

Any interface (family or trait) inheriting from a trait must have a role compatible with that trait.

Can be overridden using @Forc_inh.

## Stricter Rules

### optional_all_classes_should_have_clprolf_role

Every class should declare a Clprolf role:
@Agent, @Worker, or @Indef_obj.

### optional_all_interfaces_should_have_clprolf_role

Every interface should declare a Clprolf interface role:
@Family_interf, @Trait_interf, or @Compat_interf.

### optional_class_should_not_implement_trait_directly

A class should not directly implement a @Trait_interf.

Use @Forc_int_inh if this behavior is intentionally required.

### optional_class_must_implement_only_one_family_interface

A Clprolf class should implement at most one @Family_interf.

Can be overridden using @Forc_int_inh.


# X) Clprolf and Existing Architectures

Clprolf, both as a language and as a framework, is compatible with existing architectural approaches such as Domain-Driven Design (DDD), Model-View-Controller (MVC), Clean Architecture, Hexagonal Architecture, and others.

Rather than replacing these architectures, Clprolf acts as an additional layer between Object-Oriented Programming (OOP) and software architecture. Its purpose is to complement, clarify, and reinforce existing architectural principles by making class roles and inheritance relationships more explicit.

In this way, Clprolf helps improve architectural consistency while remaining fully compatible with established design practices.


# XI) Summary

Clprolf introduces very few concepts.

## Classes

```text
agent
worker
indef_obj
```

## Interfaces

```text
family_interf
trait_interf
compat_interf
```

## Two Fundamental Rules

```text
1. Separate business and technical concerns.
2. Inherit only within the same domain.
```
