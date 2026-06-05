# Clprolf — CLear PROgramming Language and Framework

> **A structured approach to object-oriented programming.**
> Roles and responsibilities become explicit.

Clprolf is both a Java and C# framework designed to make architectural intent visible within object-oriented systems.

Its goal is not to replace classical OOP, but to make certain important distinctions explicit:

* business or domain responsibilities versus technical/support responsibilities,
* coherent inheritance versus composition,
* the primary responsibility of a class.

---

## 🎯 The Problem

In traditional object-oriented systems, class responsibilities often become unclear over time.

A class may start with a well-defined purpose, but gradually accumulate additional concerns:

* business rules,
* technical implementation details,
* infrastructure logic,
* unrelated responsibilities.

As systems grow, architectural intent becomes harder to understand and maintain.

---

## 💡 The Clprolf Approach

Clprolf encourages developers to identify and express the primary responsibility of each class.

The framework is based on a simple idea:

> **A class should clearly express its primary role.**

To support this idea, Clprolf introduces explicit class roles and structural guidelines.

---

## 🧱 Class Roles

### `agent`

Represents a business or conceptual class.

An `agent`:

* contains business logic,
* orchestrates processes,
* makes decisions,
* avoids heavy technical code.

Note: entities and DTOs are typically classified as agents, since they represent domain data.

Example:

```java
@ClAgent
public class OrderProcessor {

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

### `worker`

Represents a technical class.

A technical class is primarily intended to support agent classes rather than be organized around a class domain.
Workers provide technical support and infrastructure services. They may coordinate or use low-level agent classes such as `File`, `Connection`, `Random`, `Logger`, or `Parser`, but unlike those classes, a worker is not organized around a class domain of its own.
Instead, it exists to support other components through technical mechanisms, infrastructure access, application startup, operating-system interaction, or similar responsibilities.

A `worker`:

* provides technical support,
* manages infrastructure and execution mechanisms,
* contains technical code.

Example:

```java
@ClWorker
public class OrderRepository {

    public void save(Order order) {

        // database access

    }
}
```

---

### `draft`

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

## 🧠 Inheritance

Clprolf encourages inheritance only between classes belonging to the same conceptual domain.

```java
@Agent
public class Animal {
}

@Agent
public class Dog extends Animal {
}
```

When domains do not match, composition is generally preferred.

```java
@ClWorker
public class AppLauncher {
}

@ClAgent
public class Dog extends AppLauncher {
}
```

In this case, composition would usually provide a clearer design.

---

## 🔗 Interfaces

Clprolf extends the same philosophy to interfaces.

Three interface categories are available:

* `family` — an abstract family of related implementations,
* `trait` — a shared capability across multiple families,
* `free` — a flexible interface.

Family and trait interfaces also declare a target role:

* `agent`
* `worker`

This allows abstractions to remain consistent with the roles of their implementations.

---

## ⚖️ What Clprolf Provides

By making roles explicit, Clprolf helps developers:

* understand class responsibilities more quickly,
* maintain clearer architectural boundaries,
* build more coherent inheritance hierarchies,
* reduce architectural drift over time.

The framework acts primarily as a structural guide rather than a rigid architectural framework.

---

## 🎯 When to Use Clprolf

Clprolf is well suited for:

* teams that want a lightweight structural guide for object-oriented design without adopting a heavy architectural framework,
* educational contexts focused on architectural clarity,
* complex systems,
* simulation and MAS-like applications,
* long-lived codebases where explicit responsibilities and coherent inheritance are important.

---

## ⚖️ Philosophy

Clprolf intentionally introduces light structural constraints.

These constraints are not designed to restrict creativity, but to make architectural decisions explicit.

---

## 🔚 In One Sentence

> **Clprolf makes explicit what many developers already try to enforce implicitly.**
