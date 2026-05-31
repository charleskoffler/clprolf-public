# Clprolf ArchUnit Checker — Enforce Your Architecture at Build Time

## What is Clprolf?

Clprolf is a language and an equivalent Java framework that makes certain OOP best practices explicit.

Its core idea is simple:

> A class should clearly express its primary role.

Clprolf introduces two class types:

- **`@Agent`** — a business or domain class (business logic, orchestration, decisions)
- **`@Worker`** — a technical class (database access, networking, file handling, infrastructure)

And three interface types:

- **`@Family_interf`** — a primary family interface (equivalent to a pure abstract class)
- **`@Trait_interf`** — a cross-cutting trait shared between families
- **`@Compat_interf`** — a free, unrestricted interface

Two fundamental rules govern the whole system:

1. **Separate business and technical concerns.**
2. **Inherit only within the same domain.**

---

## The Clprolf ArchUnit Checker

The checker is an open-source ArchUnit-based test class (`ClprolfArchTest`) that enforces Clprolf rules automatically at build time, inside any Java project using the Clprolf framework annotations.

It detects **seven types of violations**:

### 1. `clprolf_classes_must_not_mix_agent_and_worker`
A class cannot be annotated with both `@Agent` and `@Worker`.

### 2. `agent_worker_inheritance_must_not_mix`
A `@Worker` class cannot inherit from an `@Agent` class, and vice versa.

### 3. `class_should_not_implement_trait_directly`
A class cannot directly implement a `@Trait_interf` interface (unless `@Forc_int_inh` is used).

### 4. `class_must_implement_only_one_family_interface`
A Clprolf class may implement only one `@Family_interf` at a time.

### 5. `family_interface_role_must_match_implementation`
The target role of a `@Family_interf` (`@Agent` or `@Worker`) must match the role of its implementing class.

### 6. `trait_interfaces_must_extend_only_trait_interfaces`
A `@Trait_interf` may only inherit from other `@Trait_interf` interfaces.

### 7. `clprolf_interfaces_must_have_target_role`
Every `@Family_interf` and `@Trait_interf` must declare a target role (`@Agent` or `@Worker`).

---

## A Concrete Example

Consider this violation:

```java
@Worker
public class ClientRepository {
}

@Agent
public class Dog extends ClientRepository { // ❌ Violation!
}
```

The checker fires `agent_worker_inheritance_must_not_mix` — a business class (`Dog`) should not inherit from a technical class (`ClientRepository`). Composition should be used instead.

All violations can be individually overridden using `@Forc_inh` or `@Forc_int_inh` when a forced inheritance is intentional and justified.

---

## Get Started

The checker is open-source and available on GitHub:

👉 [https://github.com/charleskoffler/clprolf-public/tree/main/clprolf_checker](https://github.com/charleskoffler/clprolf-public/tree/main/clprolf_checker)
