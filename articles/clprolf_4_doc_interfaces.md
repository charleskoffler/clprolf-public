# Clprolf Docs #4 — Interfaces in Clprolf: A Complete Overview


In Clprolf, interfaces are viewed as:

> abstract forms of inheritance.

They therefore participate in the structural continuity of the system.

```text
family_interf = primary family interface
trait_interf  = trait, shared capability between families
compat_interf = unrestricted interface
```

In Clprolf, interfaces are not viewed as simple technical contracts.

Both `extends` and `implements` relationships are considered genuine conceptual inheritance relationships.

---

## 1) `family_interf`

An interface representing an abstract family.

Used for:

* polymorphism,
* decoupling,
* implementation variants.

Family interfaces also have a target role:

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

Which may lead to:

```clprolf
public agent AnimalImpl implements Animal { (...) }
```

```clprolf
public agent HorseImpl extends AnimalImpl implements Horse { (...) }
```

---

## 2) `trait_interf`

An interface representing a shared capability across multiple `family_interf`.

Traits also use a target role:

* `agent`
* `worker`

---

### Business Example

```clprolf
public trait_interf agent Payable {
    void pay();
}
```

---

### Technical Example

```clprolf
public trait_interf worker Persistable {
    void save();
}
```

---

## 3) `compat_interf`

A generic interface without a specific role.

Allows flexibility.

---

### Example

```clprolf
public compat_interf ExternalApi {
}
```

---

# 4) Interface Usage

In Clprolf, `family_interf` interfaces are the equivalent of pure abstract classes.

They are intended to be implemented by one or more future Clprolf classes.

They therefore possess a target role (`agent` or `worker`).

A class may implement only one primary `family_interf` at a time, and the role of the class must match the target role of the interface.

Clprolf therefore adopts a simple interface implementation model, in the same way that Java uses single class inheritance. Indeed, a `family_interf` is always the structural reflection of its implementation. This notably enables systematic loose coupling.

However, multiple interface implementation is not removed; it is simply moved to the `family_interf` implemented by the class.

This family interface may itself inherit from multiple `family_interf` and/or `trait_interf` interfaces.

As a result, interfaces that would otherwise have been implemented directly by the class are grouped at the level of its primary `family_interf`.

Clprolf therefore preserves the expressive power of multiple interface inheritance while maintaining a simple and coherent structure for concrete classes.

---

`trait_interf` interfaces express a capability shared among multiple `family_interf` interfaces.

A `trait_interf` therefore represents a cross-cutting trait shared by several families.

Normally, a `trait_interf` may only be inherited by a `family_interf`, not directly by a class.

However, direct implementation of a `trait_interf` by a class remains tolerated in Clprolf, although discouraged.

```text
Concrete Class
      ↓ implements
family_interf
      ↓ inherits from
trait_interf
```

A `family_interf` may inherit from multiple `family_interf` and/or `trait_interf`.

A `trait_interf` may inherit only from other `trait_interf`, since a trait remains a trait.

Interface inheritance may still be forced using `@Forc_int_inh` (or `@Forc_inh` when forcing inheritance across different target roles).

---
