# Clprolf — Introduction

Clprolf stands for CLear PROgramming Language and Framework.

> **A structured approach to object-oriented programming.**
> Roles and responsibilities become explicit.

Clprolf is a **lightweight architectural-role framework for Java and C#**.

It adds a conceptual layer on top of Java/C# OOP by making roles, responsibilities, and structural rules explicit. The framework helps adhere to the well-known SOLID principles.

You model systems with **agents**, **workers**, **family interfaces**, and **trait interfaces**, while architectural rules can be verified through automated checks.

---

## Scope and Positioning

It is particularly suited to:

* teams that want a lightweight structural guide for object-oriented design without adopting a heavy architectural framework,
* educational contexts focused on architectural clarity,
* complex systems,
* simulation and MAS-like applications,
* long-lived codebases where explicit responsibilities and coherent inheritance are important.

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
@ClAgent
public class OrderService {
    private final OrderRepository repo;

    public void checkout(Order order) {
        repo.save(order);
    }
}

@ClWorker
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

* Role-based classes: `agent`, `worker`, `draft`
* Structured interfaces: `family`, `trait`, `free`
* Domain-preserving inheritance
* Composition when inheritance would mix incompatible domains
* a **framework** with annotations for Java, C#

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

It is thanks to the class domain that the boundaries between roles remain clear.

A technical class is primarily intended to support agent classes rather than be organized around a class domain.

Workers provide technical support and infrastructure services. They may coordinate or use low-level agent classes such as `File`, `Connection`, `Random`, `Logger`, or `Parser`, but unlike those classes, a worker is not organized around a class domain of its own.

Instead, it exists to support other components through technical mechanisms, infrastructure access, application startup, operating-system interaction, or similar responsibilities.
