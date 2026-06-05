# Separating Class Responsibilities with Clprolf framework

Designing clean, well-structured classes is one of the central challenges of object-oriented programming.

Clprolf framework addresses this challenge by introducing **class roles**: a simple way to express the main role of a class and the kind of work it is expected to perform.

Clprolf does not replace your existing architecture.

Your repositories, services, controllers, entities, UI components, abstractions, and domain objects all stay where they are.

Clprolf framework simply adds an explicit role to each important class, making the separation between conceptual logic and technical execution easier to read, maintain, and verify.

---

## The “class roles”

Clprolf framework uses class roles.

> A class keeps its identity, but its class role tells us what kind of role it plays in the program.

In Clprolf, the two main class roles are:

* `agent`
* `worker`

They help distinguish between classes that carry conceptual meaning and classes that perform technical execution.

---

## 1. Two class roles for two kinds of work

Every significant class should declare its main class role.

### `agent`

An `agent` is a class whose main responsibility is conceptual, domain-oriented, or meaningful in the application.

Examples:

* business services,
* domain objects,
* controllers,
* observers/listeners with a conceptual role,
* UI objects that represent meaningful user interactions,
* simulations,
* application coordinators.

An agent may contain a small amount of technical code, or secondary domains, if it's practical.

However, heavy technical work should usually be delegated to workers.

---

### `worker`

A `worker` is a class whose main responsibility is technical execution.

Examples:

* technical support classes for agent
* repositories,
* DAOs,
* file access classes,
* rendering workers,
* launchers,
* persistence classes,
* console or system I/O,

A worker performs technical work for an agent, for the application, or for the system.

It should avoid becoming the place where domain decisions are made.

---

## 2. Class roles work with your existing architecture

Clprolf does not replace familiar architectural roles such as Service, Repository, Controller, Entity, View, or DAO.

It adds a class role on top of them.

A typical mapping could be:

| Existing component                | Clprolf class role |
| --------------------------------- | ------------------ |
| Service                           | `agent`            |
| Controller                        | `agent`            |
| Entity / domain object            | `agent`            |
| Repository / DAO                  | `worker`           |
| Launcher                          | `worker`           |
| Renderer / low-level display code | `worker`           |

This mapping is not meant to erase architectural nuance.

Different teams may interpret certain components differently.

For example, controllers may be treated as application agents because they coordinate meaningful behavior.
But in some architectures, a controller may be treated as a purely technical endpoint.

Clprolf allows that flexibility, as long as the choice remains clear and consistent.

---

## 3. Domain consistency through inheritance rules

Because a class declares its main role, inheritance can be checked for coherence.

The basic rule is simple:

> A class should inherit only from a class belonging to the same coherent role or domain.

### Allowed

```java
@ClAgent
public class Animal {
}
```

```java
@ClAgent
public class Dog extends Animal {
}
```

This is coherent: `Dog` remains in the same conceptual family as `Animal`.

---

### Not allowed

```java
@ClWorker
public class ClientDAO {
}
```

```java
@ClAgent
public class Dog extends ClientDAO {
}
```

This is not coherent.

`Dog` represents a conceptual domain object.
`ClientDAO` represents technical class for database calls.

The issue is not inheritance itself.
The issue is incoherent inheritance across unrelated roles.

Clprolf makes this kind of mismatch visible and, depending on the implementation, checkable through language rules, tooling, or architecture tests.

---

## 4. Why this is useful in practice

With Clprolf class roles:

* conceptual logic and technical execution stay easier to separate;
* class hierarchies are less likely to drift into incoherent states;
* technical classes are less likely to absorb domain decisions;
* agents are encouraged to delegate heavy technical work to workers;
* roles become easier to understand for developers joining the project;
* architectural discussions become more concrete.

Instead of asking vaguely:

> “Is this class clean?”

we can ask:

> “Is this class coherent with its declared class role?”

That question is much easier to discuss, review, and automate.

---

## 5. Class roles and SOLID

Class roles are only one part of Clprolf, but they support a powerful idea:

> Each object should express a unique and well-defined kind of work.

This aligns naturally with the **Single Responsibility Principle**.

A class belongs to one main role, expressed through its class role.
Its methods should then remain coherent with that role.

This does not mean a class must have only one method.

It means that its methods should serve the same overarching responsibility.

For example, an `agent` may have several methods, as long as those methods serve the same conceptual role.

A `worker` may have several methods, as long as those methods remain focused on technical execution.

Clprolf interfaces can also target these roles.

For example:

```java
@ClAgent
@ClFamily
public interface PaymentService {
}
```

```java
@ClWorker
@Family
public interface PaymentRepository {
}
```

This allows family and trait interfaces to match the intended role of the classes that implement them.

As a result, developers can define both:

* what kind of object a class is;
* what kind of work it is expected to perform.

All while staying compatible with established object-oriented and SOLID principles.

---

## 6. How does this relate to DDD or Clean Architecture?

Clprolf does not replace Domain-Driven Design or Clean Architecture.

It works alongside them.

DDD focuses on modeling the business domain, defining concepts, language, and boundaries.

Clean Architecture focuses on separating domain logic from technical concerns through layers and dependency rules.

Clprolf works at a more local level:

> It makes the main role of each class explicit.

Clprolf is close to Clean Architecture in spirit, because it also tries to preserve clean boundaries between conceptual logic and technical execution.

The difference is that Clprolf expresses those boundaries directly in the code through class roles.

The framework allows to check with ArchUnit the application of the Clprolf annotations.

So Clprolf does not require developers to rely only on discipline and convention.

It gives teams a structure that can be read, discussed, and verified.

---

## Summary comparison

| Approach           | Main focus                                                     | Boundary enforcement                                                      |
| ------------------ | -------------------------------------------------------------- | ------------------------------------------------------------------------- |
| DDD                | Domain modeling, ubiquitous language, bounded contexts         | Mostly conventions and team discipline                                    |
| Clean Architecture | Separation of domain and technical concerns                    | Guidelines, layers, dependency rules                                      |
| Clprolf framework  | Class roles: explicit class roles such as `agent` and `worker` | Annotations and architecture tests |

---

## 7. Examples

Below are simple examples using the Clprolf Java framework.

They show how class roles integrate naturally into everyday Java classes.

---

### Example 1 — A service using a repository

```java
@ClAgent
public class OrderService {

    private final OrderRepository repository = new OrderRepository();

    public void validateAndStore(String orderId) {
        // Application/domain logic
        if (orderId == null || orderId.isBlank()) {
            throw new IllegalArgumentException("Invalid order id");
        }

        // Technical operation delegated to a worker
        repository.save(orderId);
    }
}
```

`OrderService` carries application logic and delegates technical persistence to a worker.

---

### Example 2 — A repository

```java
@ClWorker
public class OrderRepository {

    public void save(String orderId) {
        // Technical persistence code
        System.out.println("Saving order " + orderId);
    }
}
```

`OrderRepository` performs technical work.

It does not decide whether the order is valid.
It simply saves what it is asked to save.

---

### Example 3 — Forbidden inheritance

```java
@ClWorker
public class WrongRepository extends OrderService {
    // Not coherent:
    // a technical worker should not inherit from an agent service.
}
```

Clprolf prevents, or at least highlights, this kind of cross-role inheritance.

A technical class should not extend a conceptual service.

Composition should be used instead.

---

### Example 4 — A simple domain agent

```java
@ClAgent
public class Animal {

    public void eat(String food) {
        System.out.println("The animal eats " + food);
    }

    public void sleep() {
        System.out.println("The animal sleeps");
    }
}
```

`Animal` is an agent because it represents a conceptual object.

It can have several methods because they remain coherent with the same domain.

---

### Example 5 — A checkout service

```java
@ClAgent
public class CheckoutService {

    private final OrderRepository orders = new OrderRepository();
    private final PaymentRepository payments = new PaymentRepository();

    public void checkout(String orderId) {
        if (orderId == null || orderId.isBlank()) {
            throw new IllegalArgumentException("Invalid order id");
        }

        orders.save(orderId);
        payments.process(orderId);
    }
}
```

```java
@ClWorker
public class PaymentRepository {

    public void process(String orderId) {
        // Technical payment operation
        System.out.println("Processing payment for order " + orderId);
    }
}
```

The checkout service coordinates the application action.

The repositories perform technical operations.

---

## 8. What these examples demonstrate

These examples show that:

* you keep your usual components: services, repositories, controllers, entities, launchers;
* Clprolf framework adds a clear role through `@ClAgent` or `@ClWorker`;
* agents may use workers through composition;
* workers should not absorb domain decisions;
* inheritance stays coherent;
* class roles become easier to read and maintain.

Clprolf does not make architecture heavier.

It makes architectural intent visible.

---

## Final line

> Class roles are one part of Clprolf framework: they give objects a clear type of work, support clean separation between conceptual and technical responsibilities, remain compatible with SOLID, and can be targeted by Clprolf interfaces.
