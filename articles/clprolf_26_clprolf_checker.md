# Introducing the ArchUnit Checker for the Clprolf Framework

One of the most interesting aspects of architectural frameworks is not the rules themselves, but the ability to verify that those rules are actually being followed.

The Clprolf Framework now includes an ArchUnit-based checker for Java projects. Rather than relying solely on documentation, Clprolf's structural principles can be validated automatically during development.

## What is Clprolf?

Clprolf is a lightweight object-oriented framework built around a simple idea:

> A class should clearly express its primary role.

To make this explicit, Clprolf distinguishes between business-oriented classes and technical classes.

```text
Classes
--------
Agent
Worker
Draft

Interfaces
----------
Family
Trait
Free
```

The framework is based on two fundamental principles:

1. Separate business concerns from technical concerns.
2. Preserve the conceptual domain through inheritance.

## Bringing Architecture into the Build Process

Architectural guidelines are often written down, discussed, and eventually forgotten.

The ArchUnit checker takes a different approach: architectural rules become executable tests.

As a result, violations can be detected automatically from IntelliJ IDEA, Maven, or any CI/CD pipeline supporting JUnit.

## Core Rules

The `ClprolfArchTest` checker validates the framework's core rules.

### A class cannot be both Agent and Worker

```java
@ClAgent
@ClWorker
public class InvalidClass {
}
```

A class must have a single primary role.

---

### Inheritance must preserve the role

A `@ClWorker` class cannot inherit from an `@ClAgent` class, and vice versa.

This reflects one of Clprolf's central principles:

> inheritance should preserve the conceptual domain.

---

### Family interfaces must match implementation roles

A class implementing a `@ClFamily` must have a role compatible with the target role of that interface.

For example, an `@ClAgent` family interface should normally be implemented by an `@ClAgent` class.

---

### Trait interfaces may only extend other trait interfaces

A trait remains a trait throughout the hierarchy.

This keeps interface inheritance structurally coherent.

---

### Clprolf interfaces must declare a target role

Every `@ClFamily` and `@ClTrait` must explicitly declare whether it belongs to the Agent world or the Worker world.

A @ClFamily must declare exactly one target role: @ClAgent or @ClWorker.
A @ClTrait must declare at least one target role: @ClAgent, @ClWorker, or exceptionally both.

---

### Inheriting interfaces must have a role compatible with inherited traits

When a `@ClFamily` or `@ClTrait` inherits from a `@Trait_interf`, its target role must be compatible with the role of the inherited trait.
For example, an `@ClAgent` family interface may inherit from an `@ClAgent` trait, but not from a `@ClWorker` trait.
Traits annotated with both `@ClAgent` and `@ClWorker` are considered compatible with either role.
The rule may be overridden using `@Forc_inh`.

### Family interfaces must have compatible inherited family interfaces

When a `@ClFamily` inherits from another `@Family_interf`, both interfaces must have compatible target roles.
For example, an `@ClAgent` family interface may inherit from another `@ClAgent` family interface, but not from a `@ClWorker` family interface.
This rule preserves the conceptual domain across family interface hierarchies.
The rule may be overridden using `@Forc_inh`.

### Direct trait implementation must respect trait roles

Although direct implementation of a `@ClTrait` by a concrete class is tolerated in non-strict mode, the class must still have a role compatible with the trait.

For example:

```java
@ClAgent
public class Order implements Payable {
}
```

is valid if `Payable` is an `@ClAgent` trait.

However:

```java
@ClAgent
public class Order implements Persistable {
}
```

is invalid if `Persistable` is a `@ClWorker` trait.

Traits annotated with both `@ClAgent` and `@ClWorker` are compatible with either type of class.

The rule may be overridden using `@Forc_inh`.

---

## Optional Strict Validation

A second checker, `ClprolfStrictArchTest`, provides stricter validation rules.

These rules are optional and intended for teams that want complete Clprolf classification across their codebase.

### Every class should declare a Clprolf role

Each class should explicitly be:

```java
@ClAgent
```

or

```java
@ClWorker
```

or

```java
@ClDraft
```

---

### Every interface should declare a Clprolf role

Each interface should explicitly be:

```java
@ClFamily
```

or

```java
@ClTrait
```

or

```java
@ClFree
```

---

### Classes should not directly implement trait interfaces

Traits are intended to be inherited through family interfaces rather than implemented directly by concrete classes.

---

### Classes should implement only one primary family interface

Clprolf encourages a structure where a concrete class implements a single primary family interface.

This does not remove multiple interface inheritance. Instead, multiple inheritance is moved to the family interface itself.

For example:

```java
@ClAgent
@ClFamily
public interface Horse
        extends Animal,
                Mammal,
                Payable {
}
```

The concrete implementation remains simple:

```java
@ClAgent
public class HorseImpl implements Horse {
}
```

This preserves the expressive power of multiple interface inheritance while maintaining a coherent structure for concrete classes.

## Why It Matters

Many frameworks provide architectural recommendations.

Clprolf goes one step further by making those recommendations automatically verifiable.

The result is a framework whose structural rules can be checked continuously throughout development, helping teams maintain consistency as projects evolve.

For developers interested in architecture, ArchUnit, or lightweight approaches to object-oriented design, the Clprolf checker provides an interesting example of how architectural principles can become executable rules.
