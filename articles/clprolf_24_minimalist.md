# Clprolf Minimalist — Official Documentation

## Introduction

**Clprolf Minimalist** is a simplified version of the Clprolf language.

Its goal is to make certain object-oriented programming best practices explicit, without introducing heavy architecture or a steep learning curve.

Clprolf Minimalist is based on one simple idea:

> A class must clearly express its main role.

The language therefore helps to:

* separate business logic from technical code,
* limit architectural drift,
* make inheritance more coherent,
* improve system readability.

Clprolf Minimalist remains fully object-oriented and close to Java.

---

# I) The Two Fundamental Principles

Clprolf Minimalist is based on two central principles.

---

## 1. A class is either business-oriented or technical

Each class belongs to one of the following two worlds:

### Business / domain world

The class represents a business or conceptual responsibility.

Examples:

* order management,
* business logic,
* simulation,
* functional orchestration.

These classes are declared with:

```clprolf
agent
```

---

### Technical world

The class performs technical work:

* database access,
* networking,
* files,
* rendering,
* infrastructure.

These classes are declared with:

```clprolf
worker
```

---

## 2. Inheritance must preserve the domain

A class should inherit only from a class belonging to the same conceptual domain.

Otherwise:

> composition should be used.

This principle prevents incoherent hierarchies and mixed responsibilities.

---

# II) Class Types

Clprolf Minimalist has only three class types.

---

## II.1) `agent`

Represents a business or conceptual class.

An `agent`:

* contains business logic,
* orchestrates processes,
* makes decisions,
* avoids heavy technical code.

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

* performs machine tasks,
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

Object without a defined role.

Used:

* during prototyping,
* during refactoring,
* when the role is not yet clear.

Example:

```clprolf
public indef_obj TemporaryManager {
}
```

`indef_obj` enables a flexible approach close to classical OOP.

---

## II.4) Main Domain and Technical Code

Clprolf encourages moving as much technical code as possible from `agent` classes into `worker` classes.

However, an `agent` may still contain a reasonable amount of technical code when it improves the simplicity or readability of the system.

An `agent` always has a main domain representing its central responsibility.

Secondary responsibilities may exist as long as they remain coherent with this main domain.

---

## II.5) Freedom of Interpretation

The choice between `agent` and `worker` is left to the developer.

Some responsibilities may be interpreted differently depending on the chosen architectural vision.

For example, a connection may be represented:

* as an `agent`, if it is viewed as a functional abstraction;
* or as a `worker`, if it is considered a purely technical mechanism.

If one wishes to clearly separate the functional logic related to the connection from its technical implementation, one may use:

* an `agent` to represent the connection,
* and delegate the technical code to one or more `worker` classes.

---

# III) Inheritance

Clprolf Minimalist greatly simplifies inheritance.

The `nature` keyword disappears.
The rule becomes implicit:

> a class inherits only from a class belonging to the same domain.

---

## Valid example

```clprolf
public agent Animal {
}

public agent Dog extends Animal {
}
```

---

## Discouraged example

```clprolf
public worker DatabaseConnection {
}

public agent Dog extends DatabaseConnection {
}
```

Here, the domains are incompatible.
Composition should be used instead.

Class inheritance forcing remains possible with `@Forc_inh` above the class.

---

# IV) Flexible Mode

Clprolf Minimalist operates only in:

```text
flexible mode
```

The developer therefore keeps their freedom:

* mixing remains possible if necessary,
* progressive migration is allowed,
* compatibility with existing code is preserved,
* but there is always a main domain.

The language mainly acts as:

> a structural guide.

---

# V) Interfaces

In Clprolf Minimalist, interfaces are viewed as:

> abstract forms of inheritance.

They therefore participate in the structural continuity of the system.

```text
family_interf  = primary interface of a family
trait_interf   = trait, shared capability between families
compat_interf  = unrestricted interface
```

In Clprolf, interfaces are not viewed as simple technical contracts.

The `extends` and `implements` relationships are considered genuine forms of conceptual inheritance, hence the term “family”.

---

# V.1) `family_interf`

Interface representing an abstract family.

Used for:

* polymorphism,
* decoupling,
* implementation variants.

Family interfaces also possess a target role:

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

And will naturally lead to:

```clprolf
public agent AnimalImpl implements Animal { (...) }
```

```clprolf
public agent HorseImpl extends AnimalImpl implements Horse { (...) }
```

---

# V.2) `trait_interf`

Interface representing a shared functionality between several `family_interf`.

Traits use a target role, just like `family_interf`:

* `agent`
* `worker`

---

## Business-side example

```clprolf
public trait_interf agent Payable {
    void pay();
}
```

---

## Technical-side example

```clprolf
public trait_interf worker Persistable {
    void save();
}
```

---

# V.3) `compat_interf`

Generic interface without any particular role.

Allows the system to remain flexible.

---

## Example

```clprolf
public compat_interf ExternalApi {
}
```

---

# V.4) Interface Usage

In Clprolf, `family_interf` interfaces are the equivalent of pure abstract classes.

They are intended to be implemented by one or more future Clprolf classes.
They therefore possess a target role (`agent` or `worker`).

A class may implement only one main `family_interf` at a time, and the role of the class must match the target role of the interface.

Clprolf therefore uses simple interface implementation, in the same way that Java uses simple class inheritance.

Indeed, a `family_interf` is always the structural reflection of its implementation.
This notably enables systematic loose coupling.

---

`trait_interf` interfaces express a shared functionality between several `family_interf` interfaces.

A `trait_interf` therefore represents a transversal trait shared across multiple families.

Normally, a `trait_interf` may only be inherited by a `family_interf` interface, and not directly by a class.

However, in Clprolf Minimalist, direct implementation of a `trait_interf` by a class remains tolerated, although discouraged.

```text
Concrete class
    ↓ implements
family_interf
    ↓ inherits from
trait_interf
```

Note: a `family_interf` interface may inherit from multiple `family_interf` or `trait_interf` interfaces.

A `trait_interf` interface may inherit only from other `trait_interf` interfaces, because a trait remains a trait.

Interface inheritance forcing remains possible with `@Forc_int_inh` above the interface (or `@Forc_inh` to force inheritance between different target roles).

---

# VI) General Architecture

Clprolf Minimalist naturally encourages a simple architecture.

```text
agent
    ↓ delegates to
worker
```

`agents` contain:

* business rules,
* decisions,
* orchestration.

`workers` perform:

* technical work,
* system access,
* machine operations.

An `agent` delegates technical code to one or more `worker` classes.
It may still execute technical operations itself, but by calling methods from a `worker`.

The `worker` is at the service of the `agent`.

---

# VII) Clprolf Framework

Clprolf may also be used as a framework inside an existing language such as Java.

In that case, the keywords are replaced with annotations.

---

# VII.1) Classes

## Agent

```java
@Agent
public class OrderProcessor {
}
```

---

## Worker

```java
@Worker
public class OrderRepository {
}
```

---

## Indefinite Object

```java
@Indef_obj
public class TemporaryManager {
}
```

---

# VII.2) Family Interfaces

Family interfaces use two annotations:

* a role annotation,
* plus `@Family_interf`.

---

## Business-side example

```java
@Agent
@Family_interf
public interface PaymentService {
}
```

---

## Technical-side example

```java
@Worker
@Family_interf
public interface DatabaseStorage {
}
```

---

# VII.3) Trait Interfaces

Trait interfaces use:

```java
@Trait_interf
```

with a target role.

---

## Business example

```java
@Agent
@Trait_interf
public interface Payable {
}
```

---

## Technical example

```java
@Worker
@Trait_interf
public interface Persistable {
}
```

---

# VII.4) Free Compatibility

```java
@Compat_interf
public interface ExternalApi {
}
```

---

# VIII) What Has Been Removed

In order to drastically reduce complexity:

* `model`
* `information`
* `abstraction`
* `simu_real_obj`
* `comp_as_worker`
* renaming `worker_agent` to `worker`
* renaming `version_inh` to `family_interf`
* renaming `capacity_inh` to `trait_interf`
* `nature`
* `contracts`
* `underst`
* `with_compat`
* genders
* synonyms
* interface advices replaced with a target role
* advanced concurrency/parallelism mechanisms

have been removed from the minimalist version.

---

# IX) Goal of the Language

Clprolf Minimalist does not aim to replace classical OOP.

It aims to make certain important distinctions explicit:

* business vs technical,
* coherent inheritance vs composition,
* the primary responsibility of a class.

---

# X) Summary

Clprolf Minimalist adds very few concepts.

---

## Classes

```text
agent
worker
indef_obj
```

---

## Interfaces

```text
family_interf
trait_interf
compat_interf
```

---

## Two Fundamental Rules

```text
1. Separate business and technical concerns.
2. Inherit only within the same domain.
```
