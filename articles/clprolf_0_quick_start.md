# Clprolf — Quick Start

Clprolf stands for CLear PROgramming Language and Framework.

> **"Don't use it - You don't need it!"**

> *Clprolf is both a Java and C# architectural framework designed to make architectural intent visible within object-oriented systems.*

It acts as a lightweight structural layer between pure OOP and your architecture (DDD, Clean, Hexagonal, etc.).

---

## 🎯 The Problem

In traditional object-oriented systems, class responsibilities often become unclear over time. A class may start with a well-defined purpose, but gradually accumulate business rules, technical implementation details, and unrelated infrastructure logic. As systems grow, architectural intent drifts.

---

## 💡 The Clprolf Approach

Clprolf encourages developers to identify and express the primary responsibility of each class. The framework is based on a simple idea:

> **A class should clearly express its primary role.**

---

## 🧱 Class Roles

### `ClAgent`

Represents a business or conceptual class. It contains business logic, orchestrates processes, and makes decisions. Entities and DTOs are classified as agents since they represent domain data.

```java
@ClAgent
public class OrderProcessor {
    private OrderRepository repository;

    public void process(Order order) {
        if(order.total() <= 0) {
            throw new Error();
        }
        repository.save(order);
    }
}

```

### `ClWorker`

Represents a technical service or infrastructure class. It exists solely to support agents through technical mechanisms (database access, application startup, OS interactions, parsing). It is not organized around a business domain.

```java
@ClWorker
public class OrderRepository {
    public void save(Order order) {
        // Direct database access code here
    }
}

```

### `ClSystem` (Optional)

Represents a system-oriented agent or components at the boundaries of the application (e.g., Sockets, Files, HTTP Controllers, Middlewares). It bridges system behaviors and naturally orchestrates or uses standard agents.

### `ClDraft`

An object without a defined role yet. Used during prototyping or heavy refactoring when the destination of the class isn't clear.

---

## 🧩 Flexibility & Tailored Adoption

Clprolf acts primarily as a structural guide rather than a rigid architectural framework. It is designed to adapt to your project, not the other way around.

### 1. Step-by-Step Integration

You don't have to adopt everything on day one:

* **Step 1: Classes First.** Focus exclusively on separating `@ClAgent` (or `ClSystem`) and `@ClWorker`. This allows your team to clean up the codebase without friction.
* **Step 2: Interfaces Later (Optional).** The interface system (`ClFamily`, `ClTrait`) can be introduced in a second phase. If you find it too unconventional for your habits, you can completely ignore it. The framework remains 100% effective just with class annotations.

### 2. Custom Terminology (Agnostic Naming)

If the words `Agent` or `Worker` do not match your team's culture, you can change them! Because the automated ArchUnit checker relies on the underlying types, a simple **automated Rename** in your IDE allows you to adapt the vocabulary:

| Default Role | Conceptual Alternative | Technical Alternative |
| --- | --- | --- |
| **`@ClAgent`** | `@ClConcept` | `@ClDomain` |
| **`@ClWorker`** | `@ClMechanism` | `@ClInfrastructure` |
| **`@ClSystem`** | `@ClBridge` | `@ClLowLevel` |

---

## 🧠 Inheritance & Domain Preservation

Clprolf enforces inheritance **only** between classes belonging to the same conceptual domain.

```java
// ✅ VALID: Same domain continuity
@ClAgent public class Animal {}
@ClAgent public class Dog extends Animal {}

// ❌ DISCOURAGED: Mixing technical and conceptual worlds
@ClWorker public class AppLauncher {}
@ClAgent public class Dog extends AppLauncher {} // Use Composition instead!

```

---

## 🔗 Interfaces

Clprolf extends this philosophy to abstractions by introducing three categories, which also declare a target role (`@ClAgent` or `@ClWorker`) to maintain structural continuity:

* **`ClFamily`**: Represents an abstract family of related implementations (mirrors pure abstract classes).
* **`ClTrait`**: Represents a cross-cutting, shared capability across multiple families.
* **`ClFree`**: A generic, unrestricted interface for external integrations.

---

## 🛠 nighttime Automated Validation (ArchUnit & ArchUnitNET)

Clprolf includes an open-source semantic checker for Java and .NET. **It does not enforce rigid package structures.** It only ensures your classes respect their declared identities and inheritance rules through 8 standard automated tests.

---

## 🔚 In One Sentence

> **Clprolf makes explicit what many developers already try to enforce implicitly.**