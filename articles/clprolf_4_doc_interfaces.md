# Clprolf Docs #4 — Interfaces in Clprolf: A Complete Overview

In Clprolf, interfaces are viewed as:

> abstract forms of inheritance.

They therefore participate in the structural continuity of the system.

```text
ClFamily = primary family interface
ClTrait  = trait, shared capability between families
ClDraft = unrestricted interface
```

In Clprolf, interfaces are not viewed as simple technical contracts.

Both `extends` and `implements` relationships are considered genuine conceptual inheritance relationships.

---

## 1) `ClFamily`

An interface representing an abstract family.

Used for:

* polymorphism,
* decoupling,
* implementation variants.

Family interfaces also have a target role:

* `ClAgent`
* or `ClWorker`

---

## Example

```java
@ClAgent
@ClFamily
public interface Animal {

    void eat(int quantity);

}
```

The hierarchy of `ClFamily` interfaces naturally reflects the hierarchy of concrete classes.

```java
@ClAgent
@ClFamily
public interface Horse extends Animal {

    void jump(int height);

}
```

Which may lead to:

```java
@ClAgent
public class AnimalImpl implements Animal { (...) }
```

```java
@ClAgent
public class HorseImpl extends AnimalImpl implements Horse { (...) }
```

---

## 2) `ClTrait`

An interface representing a shared capability across multiple `ClFamily`.

Traits also use a target role:

* `ClAgent`
* `ClWorker`

> **Note: a `@ClTrait` may be annotated with both `@ClAgent` and `@ClWorker`.**
>
> **This exception is reserved for genuinely cross-cutting traits that can be used by both agents and workers.**

---

### Business Example

```java
@ClAgent
@ClTrait
public interface Payable {
    void pay();
}
```

---

### Technical Example

```java
@ClWorker
@ClTrait
public interface worker Persistable {
    void save();
}
```

---

## 3) `ClFree`

A generic interface without a specific role.

Allows flexibility.

---

### Example

```java
@ClFree
public interface ExternalApi {
}
```

---

## 4) Interface Usage

In Clprolf, `Family` interfaces closely resemble pure abstract classes.

They are intended to be implemented by one or more future Clprolf classes.
Therefore, they have a target role (`agent` or `worker`).

A class can only implement a single main `Family` at a time, and the class role must match the target role of the interface.
Clprolf thus uses single implementation for interfaces, in the same way that Java uses single inheritance for classes. Indeed, a `Family` is always the structural reflection of its implementation. This notably allows for systematic loose coupling.
However, multiple implementation is not removed, but rather shifted to the `Family` implemented by the class.
This family interface can itself inherit from multiple `Family` or `Trait` interfaces.
Thus, the interfaces that would have been implemented directly by the class are grouped together at the level of its main `Family`.
Clprolf therefore preserves the richness of multiple interface inheritance, while maintaining a simple and cohesive structure for concrete classes.

## Note: In non-strict mode, it is possible for a class to implement multiple `ClFamily` interfaces, to stay closer to standard Java and C# practices.

`Trait` interfaces express a common functionality across multiple `Family` interfaces.

A `Trait` therefore represents a cross-cutting trait shared among several families.

Normally, a `Trait` can only be inherited by a `Family` interface, and not directly implemented by a class.

```text
Concrete class
    ↓ implements
ClFamily
    ↓ inherits from
ClTrait

```

Note: A `Family` interface can inherit from multiple `Family` or `Trait` interfaces.

A `Trait` interface can only inherit from other `Traits`, because a trait remains a trait.

Forcing interface inheritance remains possible using `@ClInterfaceBypass` above the interface (or `@ClBypass` to force inheritance between different target roles).

Note: In non-strict mode, direct implementation of a `Trait` by a class is allowed.

---

## 5) Illustration of the Interface Family / Implementation Parallel

```text
[ABSTRACT WORLD / INTERFACES]          │    [CONCRETE WORLD / CLASSES]
                                       │
       @ClAgent @ClFamily              │        @ClAgent
        interface Animal               │       class AnimalImpl
               ▲                       │               ▲
               │ (extends)             │               │ (extends)
               │                       │               │
       @ClAgent @ClFamily              │        @ClAgent
         interface Horse               │       class HorseImpl
               ▲                       │               ▼ (implements)
               │                       │       👉 implements Horse
               └───────────────────────┼───────  (and extends AnimalImpl)
                (Structural Inheritance)│
                                       │
 ──────────────────────────────────────┴─────────────────────────────────
  👉 THE TRAIT (Cross-cutting):
  
       @ClAgent @ClTrait               │
        interface Jumpable             │
               ▲                       │
               │ (inherited by Family) │
               │                       │
     Horse extends Jumpable            │
```

## 6) Note on Clprolf and the Interface Segregation Principle (ISP)

Clprolf inherently respects the ISP; it is simply a matter of adapting the design of your classes and interfaces using the appropriate families and traits:

```java
@ClAgent
@ClTrait
public interface Scanner {
    void scan(Document doc);
}

@ClAgent
@ClTrait
public interface Fax {
    void fax(Document doc);
}

@ClAgent
@ClTrait
public interface Printer {
    void print(Document doc);
}

@ClAgent
@ClFamily
public interface OldPrinter extends Printer {
    
}

@ClAgent
@ClFamily
public interface ModernPrinter extends OldPrinter, Scanner, Fax {
}

@ClAgent
public class OldPrinterImpl implements OldPrinter {
    // (...)
}

@ClAgent
public class ModernPrinterImpl implements ModernPrinter {
    // (...)
}
