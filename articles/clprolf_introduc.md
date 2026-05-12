# Clprolf — A new way to express your talent with OOP

> **A structured approach to object-oriented programming.**
> Roles and responsibilities become explicit.

> Clprolf is a **structured object-oriented language and framework** that adds a conceptual layer on top of Java/C#/PHP.

> Roles, contracts, and structural rules are **first-class language elements**.
You model systems with **agents, worker agents, versions, and capacities**, while the compiler enforces clarity.

---

### Scope and Positioning

Clprolf is not intended as a mainstream replacement for Java or other general-purpose languages.

It is designed for:

* educational contexts focused on architectural clarity,
* complex systems,
* simulation and MAS-like applications,
* systems requiring strong predictability and structural discipline.

Clprolf introduces explicit structural constraints through predefined roles and natures.
These constraints are intentional and form part of the language’s philosophy.

Developers who prefer unrestricted class modeling and complete architectural freedom may find traditional object-oriented languages more suitable.

Clprolf is intended for contexts where early architectural control and long-term coherence are primary objectives.

---

### 🚀 Why Clprolf?

* **Safer architecture**: compile-time errors prevent invalid dependencies
* **Clear concurrency**: intent expressed with `one_at_a_time`, `turn_monitor`, etc.
* **Readable design**: class roles (`agent`, `worker_agent`, `model`) explain themselves

---

### 📝 Quick Example

```java
public class_for agent OrderService {
    with_compat OrderRepository repo;
    void checkout(Order o) { repo.save(o); }
}
```

> In plain OOP: architectural intent often remains implicit.
> With Clprolf: contracts explicit, roles clear, design rules enforced.
---

### ✨ Key Ideas

* Role-based classes: `agent`, `worker_agent`, `model`, `information`, `indef_obj`
* Modifiers for real-world complexity: `long_action`, `one_at_a_time`, `dependent_activity`
* Works two ways:

  * **Framework** (annotations for Java, C#, PHP 8+)
  * **Language** (compiles into pure Java)

---

### 🎯 Perfect For

* Teaching OOP and architectural thinking through structural guarantees
* Large simulations & multi-agent systems
* Scientific prototypes with interacting “actors”

---

👉 With Clprolf, your code doesn’t just run — **it explains itself.**

---

## Foundational Principles of Clprolf

Clprolf is based on two core principles:

1. A class is either technical or organized around a well-defined class domain.
2. Inheritance must preserve the class domain; if it does not, composition is used instead.

These principles define how Clprolf structures components and relationships.

---

The class domain is the central subject around which a class is organized.
It defines what the class fundamentally represents and what it is responsible for.

For example:

* A `File` class has a class domain related to file handling.
* A `Random` class has a class domain related to random generation.
* A `Connection` class has a class domain related to connection management.
* A `PdfGenerator` class has a class domain related to PDF generation.

A technical class, by contrast, does not represent a conceptual domain.
It provides technical support (e.g., logging, parsing, low-level utilities).

---
