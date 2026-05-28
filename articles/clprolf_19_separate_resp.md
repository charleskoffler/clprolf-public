# Separating Class Responsibilities with Clprolf

Designing clean, well-structured classes is a central challenge in object-oriented programming.
Clprolf addresses this by introducing **declensions** — a simple way to express the nature of a class and the domain it belongs to.

Rather than replacing your existing architecture, Clprolf integrates naturally with it.
Your **repositories, services, controllers, entities, abstractions** all stay where they are.
You simply add a declension to each class, and Clprolf helps maintain a clear separation between domains.

## 🌐 Why the term “declension”?

The word *declension* is borrowed from languages, where a word changes form depending on its use while keeping the same core identity.
Clprolf applies the idea to objects: a **declension** tells whether a class belongs to the **business/domain layer** (`agent`) or the **technical layer** (`worker`).

Within a declension, synonyms simply refine the intention without changing the domain:

* **agent** → active business element
* **abstraction** → conceptual domain element
* **simu_real_obj** → active entity in a simulation

It’s a compact way to express:
**one architectural identity, several possible nuances — just like in natural languages.**

---

## 1) Two main declensions for two domains

Every class with methods must declare its domain through one of two declensions:

### **🔹 agent**

Business logic, domain concepts, abstractions, simulation entities.

### **🔹 worker**

Technical components: repositories, display logic, file operations, system interactions, launchers.

This approach keeps the **business layer** and the **technical layer** cleanly separated.

---

## 2) Synonyms to express different perspectives

Declensions also have synonyms, allowing the developer to choose the term that best reflects the class’s intention.

### For the **agent** declension:

* **agent** — main, general-purpose synonym
* **abstraction** — for conceptual or structural elements (`List`, `Button`, `Color`, `Connection`)

  > A `List` **is an abstraction**, not a technical component.
* **simu_real_obj** — for objects inside a simulation

### For **worker**:

* **worker** — main synonym
* **comp_as_worker** — same meaning, with a simulation nuance

These synonyms do not change the actual domain; they simply refine how the developer describes their component.

---

> One nice effect of the system is that developers rarely disagree on which synonym to use.
> The distinctions are natural, non-overlapping, and easy to recognize: “this is an abstraction”, “this is a business agent”, “this belongs to a simulation”.
> The result is both expressive and stable — a balance that many object-oriented developers appreciate.

---

## 3) Declensions work *with* your existing components

Clprolf does **not** replace Service, Repository, or Controller roles.
It **adds** a declension on top of them to ensure consistency.

A typical mapping is:

| Component type   | Declension   |
| ---------------- | ------------ |
| Repository / DAO | worker |
| Service          | agent        |
| Controller       | agent        |

Clprolf then supervises inheritance between these components **indirectly**:

* A `worker` **cannot** inherit from an `agent`
  → a Repository cannot inherit from a Service
* An `agent` cannot inherit from a technical class
* Two synonyms of the same declension may inherit, but a warning indicates differing perspectives

This approach ensures that **your components remain clean and simple**, and remain in their intended domain.

**Note:**
*Different teams may interpret controllers differently.
Clprolf maps them to `agent` because they coordinate domain behavior rather than executing technical work. Still, if a particular architecture treats controllers as purely technical endpoints, using `worker` is acceptable as long as the choice remains consistent across the application.*

---

## 4) Domain consistency through inheritance rules

Because the domain is declared in the class, inheritance remains coherent.

### ✔ Allowed

* `abstraction` → `agent` (same declension, different synonym → warning)

### ❌ Not allowed

* `worker` → `agent`
* DAO → Service
* Technical → Business

This prevents accidental cross-domain inheritance and avoids mixing responsibilities over time.

Clprolf does not redefine object-oriented programming — it simply guides component relationships so domain boundaries remain intact.

---

## 5) Why this is useful in practice

With Clprolf declensions:

* your business logic and technical logic stay separated,
* your class hierarchy cannot drift into inconsistent states,
* abstraction objects stay abstractions,
* technical classes cannot absorb business code,
* and domain organization becomes clearer for any developer joining the project.

Clprolf keeps your components **predictable, readable, and consistent**.

---

## 6) Class declensions and SOLID

Declensions are only one part of Clprolf, but they support a powerful idea:

> **Each object expresses a unique and well-defined kind of work.**

This aligns naturally with the **Single Responsibility Principle**.
A class belongs to exactly one domain, expressed through its declension.
Its methods then follow that domain.

Furthermore, **Clprolf interfaces can target these class roles**, allowing family and trait interfaces to match the intended domain of the classes that implement them.

This gives developers a clear, structured way to define both **what an object is** and **what kind of work it performs**, while remaining fully compatible with established SOLID principles.

---

## 📌 How does this relate to DDD or Clean Architecture?

Clprolf does not replace DDD or Clean Architecture — it works alongside them.
DDD focuses on how to *model* the domain, while Clean Architecture defines how to *separate* the domain from technical concerns.

Clprolf is closest to Clean Architecture in spirit, but with a key difference:
**it enforces architecturally clean boundaries at the language level.**

* Domain classes (`agent`) and technical classes (`worker`) cannot be mixed through inheritance.
* Abstractions (`abstraction`) remain structural domain concepts, never technical utilities.
* Technical components cannot “leak” into domain logic by accident.

Where DDD and Clean Architecture rely on discipline and conventions,
**Clprolf turns those boundaries into compiler-checked guarantees**, ensuring that domain and technical layers stay clean over time — without additional frameworks or architectural policing.

### 📌 Summary comparison

| Approach               | Main focus                               | Boundary enforcement                           |
| ---------------------- | ---------------------------------------- | ---------------------------------------------- |
| **DDD**                | Domain modelling, ubiquitous language    | By conventions and team discipline             |
| **Clean Architecture** | Separation of domain vs technical layers | By guidelines, patterns, and project structure |
| **Clprolf**            | Declensions: domain vs technical roles   | **Compiler-checked, enforced by the language** |

---

## 📌 Is Clprolf too rigid?

Clprolf may appear strict at first because it enforces a clear boundary between domain classes (`agent`) and technical classes (`worker`).
But this strictness is no greater than what developers already follow with the Single Responsibility Principle.

In practice, Clprolf simply makes explicit what good design requires anyway:
a class should have one kind of responsibility, and belong to one area of the architecture.
Declensions formalize this separation so it remains consistent over time — without adding more constraints than SRP itself.

---

## Examples with the Clprolf Java Framework (one-to-one with the Clprolf language declensions)

Below are a few simple examples using the Clprolf Java framework. They demonstrate how declensions integrate naturally into everyday classes.

### ✅ Example 1 — A simple Service using a Repository

```java
@Agent
public class OrderService {

    private final OrderRepository repository = new OrderRepository();

    public void validateAndStore(String orderId) {
        // Business logic
        System.out.println("Validating order " + orderId);

        // Technical operation delegated to a worker
        repository.save(orderId);
    }
}
```

➡️ `OrderService` is **pure business logic**
➡️ The service uses a technical component, not the other way around

---

### ✅ Example 2 — A simple Repository (technical layer)

```java
@Worker
public class OrderRepository {

    public void save(String orderId) {
        System.out.println("Saving order " + orderId);
    }
}
```

➡️ `OrderRepository` is **technical work**
➡️ No business rules inside

---

### ❌ Example 3 — Forbidden inheritance (Repository → Service)

```java
@Worker
public class WrongRepo extends OrderService {
    // ❌ Compilation error:
    // A worker cannot inherit from an agent.
}
```

Clprolf prevents mixing domains through inheritance.
A technical class cannot extend a business class.

---

### ✅ Example 4 — A simple real Agent (clean example)

```java
@Agent
public class Animal {

    public void eat(String food) {
        System.out.println("The animal eats " + food);
    }
}
```

➡️ A real domain object
➡️ No abstraction tricks, no simulation complexity
➡️ Just a clean agent example

---

### ✅ Example 5 - A Checkout service

```java
@Agent
public class CheckoutService {

    private final OrderRepository orders = new OrderRepository();
    private final PaymentRepository payments = new PaymentRepository();

    public void checkout(String orderId) {
        orders.save(orderId);
        payments.process(orderId); // Another technical operation
    }
}
```

### 🟦 What these examples demonstrate

* You keep your usual components: **Service, Repository, Controller…**
* Clprolf adds **@Agent** or **@Worker** on top
* The service naturally *contains* its technical worker (Repository)
* **Inheritance stays clean**: business ←→ technical separation is preserved
* The domain is easier to maintain and reason about

---

## 🟩 Final line

> **Declensions are one part of Clprolf: they define objects with a single, clear type of work, support solid domain separation, remain compatible with SOLID, and can even be targeted by Clprolf interfaces.**

---