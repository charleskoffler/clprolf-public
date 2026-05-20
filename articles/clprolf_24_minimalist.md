# Clprolf Minimalist — Official Documentation

---

# Introduction

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
worker_agent
```

---

## 2. Inheritance must preserve the domain

A class should inherit only from a class belonging to the same conceptual domain.

Otherwise:

> composition must be used.

This principle prevents incoherent hierarchies and mixed responsibilities.

---

# II) Class Types

Clprolf Minimalist has only three types of classes.

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

## II.2) `worker_agent`

Represents a technical class.

A `worker_agent`:

* performs machine tasks,
* manages infrastructure,
* contains technical code.

Example:

```clprolf
public worker_agent OrderRepository {

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

# III) Inheritance

Clprolf Minimalist greatly simplifies inheritance.

The `nature` keyword disappears.
The rule becomes implicit:

> a class inherits only from a class of the same domain.

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
public worker_agent DatabaseConnection {
}

public agent Dog extends DatabaseConnection {
}
```

Here, the domains are incompatible.
Composition should be used.

---

# IV) Interfaces

In Clprolf Minimalist, interfaces are viewed as:

> abstract forms of inheritance.

They therefore participate in the structural continuity of the system.

---

# IV.1) `version_inh`

Interface representing an abstract version.

Used for:

* polymorphism,
* decoupling,
* implementation variants.

Version interfaces also possess a role:

* `agent`
* or `worker_agent`

---

## Example

```clprolf
public version_inh agent Animal {

    void eat(int quantity);

}
```

---

# IV.2) `capacity_inh`

Interface representing a common capability.

Capability interfaces use an advice:

* `@Agent_like_advice`
* or `@Worker_like_advice`

---

## Business-side example

```clprolf
@Agent_like_advice
public capacity_inh Payable {
    void pay();
}
```

---

## Technical-side example

```clprolf
@Worker_like_advice
public capacity_inh Persistable {
    void save();
}
```

---

# IV.3) `compat_interf`

Generic interface without any particular role.
Allows the system to remain flexible.

---

## Example

```clprolf
public compat_interf ExternalApi {
}
```

---

# IV.4) Interface usage

A class may implement at most one `version_inh` interface, and the role of the class must correspond to the role of the interface.

`capacity_inh` interfaces express an “interface of interface,” a capability possessed by a version interface. It is a common trait shared by several version interfaces.

They may only be inherited by a `version_inh` interface, and not directly by a class.

---

# IV.5) Philosophy of Interfaces

The suffix:

```text
_inh
```

comes from:

```text
inheritance
```

Interfaces are therefore not considered simple technical contracts,
but abstract structures participating in the conceptual inheritance of the system.

---

# V) General Architecture

Clprolf Minimalist naturally encourages a simple architecture.

```text
agent
    ↓ delegates to
worker_agent
```

`agents` contain:

* business rules,
* decisions,
* orchestration.

`worker_agents` perform:

* technical work,
* system access,
* machine operations.

---

# VI) Flexible Mode

Clprolf Minimalist works only in:

```text
flexible mode
```

The developer therefore keeps their freedom:

* mixing remains possible if necessary,
* progressive migration,
* compatibility with existing code,
* but there is always a main domain.

The language acts mainly as:

> a structural guide.

---

# VIII) Clprolf Framework

Clprolf can also be used as a framework inside an existing language such as Java.

In that case, the keywords are replaced by annotations.

---

# VIII.1) Classes

---

## Agent

```java
@Agent
public class OrderProcessor {
}
```

---

## Worker Agent

```java
@Worker_agent
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

# VIII.2) Version Interfaces

Version interfaces use two annotations:

* a role annotation,
* plus `@Version_inh`.

---

## Business-side example

```java
@Agent
@Version_inh
public interface PaymentService {
}
```

---

## Technical-side example

```java
@Worker_agent
@Version_inh
public interface DatabaseStorage {
}
```

---

# VIII.3) Capability Interfaces

Capability interfaces use:

```java
@Capacity_int(...)
```

with an advice.

---

## Business example

```java
@Capacity_int(Advice.FOR_AGENT_LIKE)
public interface Payable {
}
```

---

## Technical example

```java
@Capacity_int(Advice.FOR_WORKER_LIKE)
public interface Persistable {
}
```

---

# VIII.4) Free Compatibility

```java
@Compat_interf
public interface ExternalApi {
}
```

---

# IX) What Was Removed

In order to drastically reduce complexity:

* `model`
* `information`
* `abstraction`
* `simu_real_obj`
* `comp_as_worker`
* `nature`
* `contracts`
* `underst`
* `with_compat`
* genders
* synonyms
* advanced concurrency/parallelism mechanisms

have been removed from the minimalist version.

---

# X) Goal of the Language

Clprolf Minimalist does not seek to replace classical OOP.

It seeks to make certain important distinctions explicit:

* business vs technical,
* coherent inheritance vs composition,
* the primary responsibility of a class.

---

# XI) Summary

Clprolf Minimalist adds very few concepts.

---

## Classes

```text
agent
worker_agent
indef_obj
```

---

## Interfaces

```text
version_inh
capacity_inh
compat_interf
```

---

## Two Fundamental Rules

```text
1. Separate business and technical concerns.
2. Inherit only within the same domain.
```
