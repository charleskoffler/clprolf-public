# Preventing Architectural Drift in Object-Oriented Systems

## A Structural Approach with Clprolf

Most large object-oriented systems do not collapse because developers lack expressive tools.

They collapse because of **architectural drift**.

A class is created with a clear intention.
Over time, new responsibilities are added.
Technical code enters domain classes.
Domain decisions appear inside infrastructure classes.
Inheritance is used for convenience instead of conceptual coherence.

The name of the class remains the same.

But its role has changed.

This is not necessarily a failure of developers.
It is often a structural weakness of unconstrained class design.

When the role of a class is only implicit, nothing prevents that role from drifting silently.

Clprolf addresses this problem by making the primary role of each class explicit.

---

## 1. Declaring the role of a class

In classical object-oriented programming, a class is often introduced only by its name:

```java
class Sorter {
}
```

The name may suggest an intention, but the architecture does not know what kind of class it is.

Is it a domain object?
Is it a technical utility?
Is it an application coordinator?
Is it infrastructure?

Clprolf makes the role explicit:

```clprolf
public agent Sorter {
}
```

or:

```clprolf
public worker Sorter {
}
```

The class is no longer only a named container.

It declares its primary architectural role.

An `agent` carries conceptual meaning, domain behavior, application logic, simulation logic, or coordination.

A `worker` performs technical execution: I/O, rendering, persistence, networking, launching, system access, or other infrastructure work.

This small declaration changes the mindset.

The class is no longer just “a class”.

It is a class with a declared place in the architecture.

---

## 2. From description to commitment

A class name describes.

A Clprolf declension commits.

For example:

```clprolf
public agent OrderProcessor {
}
```

means:

> This class carries conceptual or application responsibility.

And:

```clprolf
public worker OrderRepository {
}
```

means:

> This class performs technical work.

That distinction matters because architectural drift often begins when a class slowly stops matching its original meaning.

A service starts doing persistence.
A repository starts making business decisions.
A UI class becomes the brain of the application.
A technical helper becomes a hidden domain object.

In traditional OOP, these shifts can happen silently.

In Clprolf, they become visible because the class has already declared what kind of role it is supposed to play.

The declaration does not remove human judgment.

But it gives judgment a structure.

---

## 3. Structural discipline, not convention

Many architectural principles already tell developers what to do:

* separate concerns;
* avoid God Objects;
* respect layers;
* apply SOLID;
* keep technical code away from domain logic;
* prefer composition when inheritance is conceptually wrong.

These principles are valuable, but they are often expressed as advice.

Clprolf turns part of this advice into structure.

It does not rely only on naming conventions or team memory.

It makes role separation visible in the code itself.

For example:

```clprolf
public agent CheckoutService {
}
```

```clprolf
public worker PaymentRepository {
}
```

The reader immediately sees the difference:

* `CheckoutService` carries application meaning;
* `PaymentRepository` performs technical work.

The code does not merely follow a convention.

It expresses an architectural decision.

---

## 4. Inheritance must respect the declared role

Architectural drift becomes especially dangerous through inheritance.

Inheritance is not just a way to reuse code.

It expresses conceptual continuity.

If a class inherits from another class, it says:

> I belong to the same conceptual family.

That is why Clprolf enforces a simple rule:

> A class should inherit only from another class belonging to the same coherent domain.

For example:

```clprolf
public agent Animal {
}

public agent Dog extends Animal {
}
```

This is coherent.

`Dog` remains in the same conceptual domain as `Animal`.

But this is not coherent:

```clprolf
public worker DatabaseConnection {
}

public agent Dog extends DatabaseConnection {
}
```

This inheritance mixes unrelated roles.

`Dog` is a conceptual object.
`DatabaseConnection` is technical infrastructure.

The correct solution is composition.

The agent may use a worker, but it should not inherit from one.

---

## 5. Preventing silent migration across layers

In many systems, classes do not become problematic all at once.

They migrate slowly.

A class starts as domain logic.
Then it gets logging.
Then database calls.
Then formatting.
Then HTTP handling.
Then retry logic.
Then business decisions from another domain.

Eventually, the class still has the same name, but no longer the same responsibility.

Clprolf prevents this migration from being silent.

If a class is declared as an `agent`, technical work should remain secondary or be delegated to workers.

If a class is declared as a `worker`, it should not become the place where domain decisions are made.

This does not mean Clprolf is rigid.

An agent may contain a reasonable amount of technical code when that improves simplicity or readability.

A worker may call back an agent when acting as a bridge, for example in UI events, callbacks, or adapters.

But the primary role must remain clear.

That is the structural lock.

Not a prison.

A visible boundary.

---

## 6. Why this matters in large systems

In small systems, discipline is often enough.

A few developers can remember the intended design.

They know which classes are domain objects, which classes are technical, and which ones are temporary.

But large systems live longer than individual decisions.

Teams change.
Features accumulate.
Shortcuts become permanent.
Names stop reflecting reality.

In that context, implicit architecture is fragile.

Clprolf helps by making important architectural distinctions explicit:

```text
agent  = conceptual meaning
worker = technical execution
```

This gives developers a shared vocabulary.

It also makes violations easier to detect, either manually during code review or automatically through tooling such as architecture tests.

A class can still be badly designed.

But it can no longer contradict its declared role silently.

---

## 7. Clprolf and architectural maintenance

Clprolf is not only useful when writing new code.

It is also useful during maintenance.

When a class becomes hard to understand, Clprolf encourages a simple question:

> What is the primary role of this class?

If the answer is not clear, the class may temporarily remain an `indef_obj`.

If the answer becomes clear, it can be refactored into an `agent`, a `worker`, or several smaller classes with clearer responsibilities.

This makes refactoring more natural.

You do not need to classify everything perfectly from the beginning.

But over time, the codebase can move toward clearer roles.

Clprolf therefore helps prevent architectural drift in two ways:

* by making roles explicit when classes are created;
* by supporting refactoring when roles become clearer later.

---

## 8. What Clprolf does not do

Clprolf does not reduce what can be built.

It does not remove object-oriented programming.

It does not replace existing architectures such as MVC, Clean Architecture, or DDD.

It does not prevent developers from making decisions.

Instead, it makes those decisions visible.

Clprolf does not say:

> “There is only one possible architecture.”

It says:

> “Declare the role of each class, and keep the structure coherent with that role.”

That is a small rule with large consequences.

---

## Conclusion

Architectural drift is one of the main reasons object-oriented systems become difficult to maintain.

The problem is not that developers do not care about design.

The problem is that, in classical OOP, the role of a class is often only implicit.

Clprolf makes that role explicit.

An `agent` carries meaning.
A `worker` performs technical execution.
Inheritance must preserve conceptual coherence.
Composition is used when domains diverge.

The result is not heavier architecture.

It is clearer architecture.

Clprolf does not merely describe responsibility.

It gives responsibility a visible place in the structure of the code.

And in long-lived systems, that visibility can be decisive.
