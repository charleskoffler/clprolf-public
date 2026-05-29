# Clprolf Docs #4 — Interfaces in Clprolf: A Complete Overview

In Clprolf, interfaces are viewed as:

> abstract forms of inheritance.

They therefore participate in the structural continuity of the system.

```text
family_interf  = primary interface of a family
trait_interf   = trait, shared capability between families
compat_interf  = unrestricted interface
```

In Clprolf, interfaces are not viewed solely as technical contracts.

They also participate in the structural organization of domains and capabilities.

---

## 1) `family_interf`

Interface representing an abstract family of related components sharing the same domain.

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
public agent AnimalImpl implements Animal { (...) }
```

```clprolf
public agent HorseImpl extends AnimalImpl implements Horse { (...) }
```

---

## 2) `trait_interf`

Interface representing a shared functionality between several `family_interf`.

Traits use a target role, just like `family_interf`:

* `agent`
* `worker`

---

## Agent Example

```clprolf
public trait_interf agent Payable {
    void pay();
}
```

---

## Worker example

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

A `family_interf` is the abstract structural reflection of its future implementations.
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

