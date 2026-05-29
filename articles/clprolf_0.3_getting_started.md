# Getting Started with Clprolf: Structuring Responsibilities in OOP

## Prerequisites

Clprolf builds upon classical Object-Oriented Programming.

To fully benefit from this guide, readers should already be familiar with:

* the fundamentals of OOP,
* inheritance and composition,
* basic design principles.

---

## Making Responsibilities Explicit

Clprolf is a language and framework designed to make architectural roles explicit.

In many object-oriented systems, separation of responsibilities relies primarily on conventions and developer discipline.

As projects evolve, responsibilities may gradually become mixed, making the original architecture harder to understand and maintain.

Clprolf addresses this by making architectural intent part of the language itself.

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

A technical class is different.

Rather than representing a conceptual domain, it provides technical support such as:

* logging,
* parsing,
* infrastructure services,
* low-level utilities.

---

## 1️⃣ Declaring a Nature

In classical OOP, a class is typically declared as:

```java
public class OrderManager { }
```

In Clprolf, the architectural nature is made explicit:

```clprolf
public class_for agent OrderProcessor { }
```

or

```clprolf
public class_for worker OrderRepository { }
```

Each class declares its nature from the beginning.

The core natures are:

* `agent` → domain-oriented responsibility
* `worker` → technical responsibility
* `indef_obj` → temporary undefined responsibility

---

## 2️⃣ Starting Flexible with `indef_obj`

Architectural clarity is not always immediate.

During prototyping or exploration, a class may begin as:

```clprolf
public class_for indef_obj OrderManager {

    public void process(Order order) {
        validate(order);
        save(order);
        notify(order);
    }

}
```

`indef_obj` represents a temporary state.

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

```clprolf
public class_for agent OrderProcessor {

    private OrderRepository repository;
    private OrderNotifier notifier;

    public void process(Order order) {

        validate(order);

        repository.save(order);

        notifier.notify(order);
    }

}
```

```clprolf
public class_for worker OrderRepository { ... }

public class_for worker OrderNotifier { ... }
```

The behavior remains unchanged.

The architecture becomes explicit.

---

## 4️⃣ Structural Rules Are Enforced

Clprolf does not merely recommend architectural discipline.

It can enforce structural coherence.

Example:

```clprolf
public class_for agent A1AnimalImpl nature AnimalWorker { }
```

This attempts to inherit from a class of a different nature.

Result:

```text
ARCH-A1 => Class A1AnimalImpl:
the parent class should be an agent (AnimalWorker)
```

The equivalent Java code would compile:

```java
public class A1AnimalImpl extends AnimalWorker { }
```

Clprolf rejects it as architecturally inconsistent.

---

## 5️⃣ Structured Interfaces

Clprolf also introduces structured interface categories.

### `family_interf`

Represents an abstract family of related implementations.

```clprolf
public family_interf agent Animal {

    void eat(int quantity);

}
```

### `trait_interf`

Represents a capability shared across multiple families.

```clprolf
public trait_interf agent Payable {

    void pay();

}
```

### `compat_interf`

Represents a flexible compatibility interface without a predefined structural role.

```clprolf
public compat_interf ExternalApi {
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
