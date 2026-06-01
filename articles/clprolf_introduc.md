# Clprolf — A new way to express your talent with OOP

> **A structured approach to object-oriented programming.**
> Roles and responsibilities become explicit.

Clprolf is a **lightweight architectural-role framework for object-oriented systems**.

It adds a conceptual layer on top of Java/C#/PHP-style OOP by making roles, responsibilities, and structural rules explicit.

You model systems with **agents**, **workers**, **family interfaces**, and **trait interfaces**, while architectural rules can be verified through automated checks.

---

## Scope and Positioning

Clprolf is not intended as a mainstream replacement for Java or other general-purpose languages.

It is designed for:

* educational contexts focused on architectural clarity,
* complex systems,
* simulation and MAS-like applications,
* systems requiring strong predictability and structural discipline.

Clprolf introduces explicit structural constraints through predefined roles and natures.

These constraints are intentional and form part of the framework’s philosophy.

Developers who prefer unrestricted class modeling and complete architectural freedom may find traditional object-oriented languages more suitable.

Clprolf is intended for contexts where early architectural control and long-term coherence are primary objectives.

---

## 🚀 Why Clprolf?

* **Safer architecture**: automated checks help detect invalid role relationships.
* **Readable design**: class roles such as `agent` and `worker` explain architectural intent.
* **Clearer inheritance**: inheritance must preserve the class domain; otherwise, composition is preferred.
* **Progressive adoption**: Clprolf can be used as a framework with annotations in existing projects.

---

## 📝 Quick Example

```java
@Agent
public class OrderService {
    private final OrderRepository repo;

    public void checkout(Order order) {
        repo.save(order);
    }
}

@Worker
public class OrderRepository {
    public void save(Order order) {
        // database access
    }
}
```

In plain OOP, architectural intent often remains implicit.

With Clprolf, roles are explicit, responsibilities are clearer, and structural rules can be checked automatically.

---

## ✨ Key Ideas

* Role-based classes: `agent`, `worker`, `indef_obj`
* Structured interfaces: `family_interf`, `trait_interf`, `compat_interf`
* Domain-preserving inheritance
* Composition when inheritance would mix incompatible domains
* Usable as:

  * a **framework** with annotations for Java, C#, PHP 8+
  * a **language-oriented model** close to Java

---

## 🎯 Perfect For

* Teaching OOP and architectural thinking through structural guarantees
* Large simulations and multi-agent-like systems
* Scientific prototypes with interacting actors
* Codebases where long-term architectural coherence matters

---

> With Clprolf, your code doesn’t just run — **it explains itself.**

---

## Foundational Principles of Clprolf

Clprolf is based on two core principles:

1. A class is either technical or organized around a well-defined class domain.
2. Inheritance must preserve the class domain; if it does not, composition is used instead.

These principles define how Clprolf structures components and relationships.

---

## Class Domain

The class domain is the central subject around which a class is organized.

It defines what the class fundamentally represents and what it is responsible for.

For example:

* A `File` class has a class domain related to file handling.
* A `Random` class has a class domain related to random generation.
* A `Connection` class has a class domain related to connection management.
* A `PdfGenerator` class has a class domain related to PDF generation.

A technical class, by contrast, does not represent a conceptual domain.

It provides technical support, such as logging, parsing, low-level utilities, infrastructure access, or other support mechanisms.
