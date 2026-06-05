# Getting Started with Clprolf: Structuring Responsibilities in OOP

## Prerequisites

Clprolf (CLear PROgramming Language and Framework) builds upon classical Object-Oriented Programming.

To fully benefit from this guide, readers should already be familiar with:

* the fundamentals of OOP,
* inheritance and composition,
* basic design principles.

---

## Making Responsibilities Explicit

Clprolf is a Java and C# framework designed to make architectural roles explicit.

In many object-oriented systems, separation of responsibilities relies primarily on conventions and developer discipline.

As projects evolve, responsibilities may gradually become mixed, making the original architecture harder to understand and maintain.

Clprolf addresses this by making architectural intent part of the programming model itself.

Every class explicitly declares its nature.

---

## Foundational Principles of Clprolf

Clprolf is based on two core principles:

1. A class is either technical or organized around a well-defined class domain.
2. Inheritance must preserve the class domain; otherwise, composition should be used.

These principles define how Clprolf structures classes and relationships.

---

### What Is a Class Domain?

A class domain is the central subject around which a class is organized.

It represents what the class fundamentally models and what it is primarily responsible for.

Examples:

* a `File` class belongs to a file-management domain,
* a `Connection` class belongs to a connection-management domain,
* a `RandomGenerator` class belongs to a random-generation domain,
* an `OrderProcessor` class belongs to an order-processing domain.

Low-level technical abstractions such as File, Connection, Logger or Parser still possess their own class domain and are therefore typically modeled as agents.

A technical class is different.

A technical class is primarily intended to support agent classes rather than be organized around a class domain.
Workers provide technical support and infrastructure services. They may coordinate or use low-level agent classes such as `File`, `Connection`, `Random`, `Logger`, or `Parser`, but unlike those classes, a worker is not organized around a class domain of its own.
Instead, it exists to support other components through technical mechanisms, infrastructure access, application startup, operating-system interaction, or similar responsibilities.

A `worker`:

* provides technical support,
* manages infrastructure and execution mechanisms,
* contains technical code.

---

## 1️⃣ Declaring a Nature

In classical OOP, a class is typically declared as:

```java
public class OrderManager { }
```

In Clprolf, the architectural nature is made explicit:

```java
@ClAgent
public class OrderProcessor { }
```

or

```java
@ClWorker
public class OrderRepository { }
```

Each class declares its nature from the beginning.

The core natures are:

* `agent` → domain-oriented responsibility
* `worker` → support/technical responsibility
* `draft` → temporary undefined responsibility

---

## 2️⃣ Starting Flexible with `draft`

Architectural clarity is not always immediate.

During prototyping or exploration, a class may begin as:

```java
@ClDraft
public class OrderManager {

    public void process(Order order) {
        validate(order);
        save(order);
        notify(order);
    }

}
```

`draft` represents a temporary state.

The responsibility can be clarified later through refactoring.

---

## 3️⃣ Making Responsibilities Explicit

After analyzing the class, we identify several distinct responsibilities:

| Responsibility        | Nature    |
| --------------------- | --------- |
| Order processing      | Domain    |
| Persistence           | Technical |
| Notification delivery | Technical |

The class can then be reorganized:

```java
@ClAgent
public class OrderProcessor {

    private OrderRepository repository;
    private OrderNotifier notifier;

    public void process(Order order) {

        validate(order);

        repository.save(order);

        notifier.notify(order);
    }

}
```

```java
@ClWorker
public class OrderRepository { ... }

@ClWorker
public class OrderNotifier { ... }
```

The behavior remains unchanged.

The architecture becomes explicit.

---

## 4️⃣ Structural Rules Are Enforced

Clprolf does not merely recommend architectural discipline.

It can enforce structural coherence.

Example:

```java
@ClAgent
public class AnimalImpl extends AnimalWorker { }
```

This attempts to inherit from a class of a different nature.

This is considered as a violation by the ArchUnit checker.

The equivalent Java code would compile:

```java
public class AnimalImpl extends AnimalWorker { }
```

Clprolf rejects it as architecturally inconsistent.

---

## 5️⃣ Structured Interfaces

Clprolf also introduces structured interface categories.

### `family`

Represents an abstract family of related implementations.

```clprolf
@ClAgent
@ClFamily
public interface Animal {

    void eat(int quantity);

}
```

### `trait`

Represents a capability shared across multiple families.

```java
@ClAgent
@ClTrait
public interface Payable {

    void pay();

}
```

### `free`

Represents a flexible interface without a predefined structural role.

```java
@ClFree
public interface ExternalApi {
}
```

Unlike traditional interfaces, Clprolf interfaces participate in the structural model of the system.

---

## 6️⃣ What Clprolf Changes

Clprolf does not change:

* algorithms,
* execution flow,
* runtime behavior.

Instead, it changes:

* role visibility,
* architectural clarity,
* inheritance consistency,
* structural guarantees.

---

## 7️⃣ Philosophy in One Sentence

> Clprolf encodes architectural responsibility at the language level instead of leaving it entirely to convention.
