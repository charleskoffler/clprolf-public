# Clprolf — A New Way to Express Your Talent with OOP

> **A structured approach to object-oriented programming.**
> Roles and responsibilities become explicit.

Clprolf is both a language and a framework designed to make architectural intent visible within object-oriented systems.

Its goal is not to replace classical OOP, but to make certain important distinctions explicit:

* business responsibilities versus technical responsibilities,
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

The language is based on a simple idea:

> **A class should clearly express its primary role.**

To support this idea, Clprolf introduces explicit class roles and structural guidelines.

---

## 🧱 Class Roles

### `agent`

An `agent` represents a business or conceptual responsibility.

Typical examples include:

* business logic,
* simulations,
* orchestration,
* decision-making processes.

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

An agent primarily focuses on business concerns.

When appropriate, technical work can be delegated to one or more workers.

---

### `worker`

A `worker` represents a technical responsibility.

Typical examples include:

* database access,
* networking,
* file management,
* infrastructure services.

```clprolf
public worker OrderRepository {

    public void save(Order order) {

        // database access

    }
}
```

Workers provide technical capabilities that can be used by agents.

---

### `indef_obj`

An `indef_obj` represents an object whose role has not yet been clearly identified.

It can be useful during:

* prototyping,
* refactoring,
* exploratory development.

```clprolf
public indef_obj TemporaryManager {
}
```

---

## 🧠 Inheritance

Clprolf encourages inheritance only between classes belonging to the same conceptual domain.

```clprolf
public agent Animal {
}

public agent Dog extends Animal {
}
```

When domains do not match, composition is generally preferred.

```clprolf
public worker DatabaseConnection {
}

public agent Dog extends DatabaseConnection {
}
```

In this case, composition would usually provide a clearer design.

---

## 🔗 Interfaces

Clprolf extends the same philosophy to interfaces.

Three interface categories are available:

* `family_interf` — an abstract family of related implementations,
* `trait_interf` — a shared capability across multiple families,
* `compat_interf` — a flexible compatibility interface.

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

The language acts primarily as a structural guide rather than a rigid architectural framework.

---

## 🎯 When to Use Clprolf

Clprolf is particularly well suited for:

* long-lived software projects,
* complex systems,
* educational environments,
* simulation and multi-agent systems,
* projects where architectural clarity is an important goal.

---

## ⚖️ Philosophy

Clprolf intentionally introduces structural constraints.

These constraints are not designed to restrict creativity, but to make architectural decisions explicit.

Developers who prefer unrestricted object modeling may find traditional object-oriented languages more appropriate.

Developers who value long-term clarity and structural consistency may benefit from the additional guidance provided by Clprolf.

---

## 🔚 In One Sentence

> **Clprolf makes explicit what many developers already try to enforce implicitly.**
