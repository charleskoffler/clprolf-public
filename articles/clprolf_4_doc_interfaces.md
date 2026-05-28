# Clprolf Docs #4 — Interfaces in Clprolf: A Complete Overview

In Clprolf, interfaces are viewed as:

> abstract forms of inheritance.

They therefore participate in the structural continuity of the system.

```text
family_interf  = primary interface of a family
trait_interf   = trait, shared capability between families
compat_interf  = unrestricted interface
```

In Clprolf, interfaces are not viewed as simple technical contracts.

The `nature` and `contracts` relationships are considered genuine forms of conceptual inheritance, hence the term “family”.

---

## 1) `family_interf`

Interface representing an abstract family.

Used for:

* polymorphism,
* decoupling,
* implementation variants.

Family interfaces also possess a target role:

* `agent`
* or `worker`

---

## Example

```clprolf
public family_interf agent Animal {

    void eat(int quantity);

}
```

The hierarchy of `family_interf` interfaces naturally reflects the hierarchy of concrete classes.

```clprolf
public family_interf agent Horse extends Animal {

    void jump(int height);

}
```

And will naturally lead to:

```clprolf
public agent AnimalImpl contracts Animal { (...) }
```

```clprolf
public agent HorseImpl nature AnimalImpl contracts Horse { (...) }
```

---

## 2) `trait_interf`

Interface representing a shared functionality between several `family_interf`.

Traits use a target role, just like `family_interf`:

* `agent`
* `worker`

---

## Business-side example

```clprolf
public trait_interf agent Payable {
    void pay();
}
```

---

## Technical-side example

```clprolf
public trait_interf worker Persistable {
    void save();
}
```

---

## 3) `compat_interf`

Generic interface without any particular role.

Allows the system to remain flexible.

---

## Example

```clprolf
public compat_interf ExternalApi {
}
```

---

## 4) Interface Usage

In Clprolf, `family_interf` interfaces are the equivalent of pure abstract classes.

They are intended to be implemented by one or more future Clprolf classes.
They therefore possess a target role (`agent` or `worker`).

A class may implement only one main `family_interf` at a time, and the role of the class must match the target role of the interface.

Clprolf therefore uses simple interface implementation, in the same way that Java uses simple class inheritance.

Indeed, a `family_interf` is always the structural reflection of its implementation.
This notably enables systematic loose coupling.

---

`trait_interf` interfaces express a shared functionality between several `family_interf` interfaces.

A `trait_interf` therefore represents a transversal trait shared across multiple families.

Normally, a `trait_interf` may only be inherited by a `family_interf` interface, and not directly by a class.

However, in Clprolf Minimalist, direct implementation of a `trait_interf` by a class remains tolerated, although discouraged.

```text
Concrete class
    ↓ implements
family_interf
    ↓ inherits from
trait_interf
```

Note: a `family_interf` interface may inherit from multiple `family_interf` or `trait_interf` interfaces.

A `trait_interf` interface may inherit only from other `trait_interf` interfaces, because a trait remains a trait.

Interface inheritance forcing remains possible with `@Forc_int_inh` above the interface (or `@Forc_inh` to force inheritance between different target roles).

---


## 5) `with_compat`

Whenever a variable, parameter, or field uses an interface type, it must be preceded by `with_compat` (or `@With_compat` in the framework).
This marks the point of **loose coupling**.

Exception: method return types do not require `with_compat`.

```java
@With_compat UserDAO dao;
```

---

## 6) `contracts`

The Java keyword `implements` is replaced by `contracts`.
A class lists all the contracts (interfaces) it fulfills.
In the framework, use `@Contracts`.

```java
@Worker
public class UserDAOImpl implements @Contracts UserDAO {
    public User getUser(int id) { /* … */ }
}
```

---
