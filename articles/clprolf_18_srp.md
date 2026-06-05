# “One Reason to Change” — The SRP in Clprolf Framework

## 1. Trying to precise SRP

The *Single Responsibility Principle* (SRP) is quoted everywhere.

Its famous sentence —

> **“A class should have only one reason to change.”**

— is powerful, but also ambiguous.

What is a *reason*?
Whose responsibility are we talking about?
And where does that responsibility come from?

The SRP was never false.
It simply stopped before naming the source of responsibility.

That is where **Clprolf** steps in: not to oppose the SRP, but to complete its logic.

---

## 2. What the SRP really implies

If all methods of a class should change for the same reason, then they must all participate in the same coherent work.

That leads to two important consequences:

1. **Technical and conceptual responsibilities must not be mixed randomly.**
   A database driver change should not affect business rules.
   A business rule change should not force a refactoring of low-level technical code.

2. **Each class must have one main meaning.**
   This meaning unites its methods, gives the class its identity, and defines its natural scope.

In other words, respecting the SRP means respecting both:

> **separation of concerns**
> and
> **unity of meaning**

A class is not coherent because it has few methods.
It is coherent because its methods serve the same main responsibility.

---

## 3. Responsibility belongs to the class, not to isolated methods

Responsibility does not live in methods individually.

Each method performs a specific action, but the class itself carries the overarching responsibility.

Example:

A **Doctor** class has one main responsibility: caring for the health of patients.

Its methods may be:

```java
takeBloodPressure()
prescribeTreatment()
analyzeResults()
```

These methods do different things, but they all serve the same responsibility.

They are not separate responsibilities.
They are different expressions of one coherent duty.

Methods **express** the responsibility.
They do not define it by themselves.

Confusing method-level actions with class-level responsibility is what leads to fragmentation, over-engineering, and endless SRP debates.

---

## 4. "SRP does not mean “one method per class”

A common misunderstanding says:

> “A class should do only one thing, so it should have only one method.”

This is not SRP.
It is a reduction of SRP.

An object is defined by a set of related operations belonging to the same conceptual domain.

A class may have many methods, and often should have many methods, as long as they all serve the same main meaning.

SRP is violated when a class mixes unrelated domains, not when it contains several methods belonging to the same one.

This confusion comes from treating “responsibility” as an isolated action, instead of understanding it as:

> **a conceptual duty flowing from what the class is meant to represent.**

---

## 5. The chain of meaning: Change → Responsibility → Nature

A class does not change for arbitrary reasons.

It changes because its responsibility evolves.

And that responsibility exists because the class has a main conceptual nature: what the class is meant to represent in the program.

Example:

The way a doctor takes blood pressure might change:

* a new device,
* a new procedure,
* a new medical standard.

But the reason for the change remains the same:

> protecting and improving the patient’s health.

The technical details may evolve, but the responsibility remains coherent because it is rooted in the same conceptual nature.

So when we say:

> “A class should have only one reason to change,”

we are really saying:

> **Change belongs to responsibility, and responsibility belongs to nature.**

When this hierarchy is respected, evolution no longer breaks the class.
It confirms its role.

---

## 6. Where Clprolf framework could bring clarity

This is exactly where **Clprolf** goes beyond discussion.

Clprolf does not merely interpret the SRP.
It makes its intention visible.

In Clprolf:

* every class declares its main role;
* that role reveals the class’s conceptual nature;
* its responsibility flows from that domain;
* its methods are expected to remain coherent with that responsibility.

A class is no longer just a block of code.
It declares what kind of object it is.

For example:

```java
@ClAgent
public class Doctor {
}
```

This says:

> Doctor is an agent: a conceptual object carrying domain meaning.

And:

```java
@ClWorker
public class OrderDAO {
}
```

This says:

> OrderDAO is a worker: a technical performer serving execution.

Clprolf framework does not make bad design impossible.
But it makes incoherence visible.

The class has declared its role, so any contradiction becomes easier to see, discuss, test, or even detect automatically.

---

## 7. From moral advice to structural clarity

SRP often remains a moral principle:

> “Try to keep your classes focused.”

With Clprolf framework, the principle becomes structural:

```java
@ClAgent
public class OrderProcessor {
}
```

```java
@ClWorker
public class OrderRepository {
}
```

The code itself starts to say:

> This class carries conceptual logic.
> This class performs technical work.

That does not remove judgment.
But it gives judgment a concrete basis.

Instead of asking vaguely:

> “Does this class respect SRP?”

we can ask:

> “Does this class remain coherent with its declared role?”

That is much easier to reason about.

---

## 8. Agents, workers, and practical flexibility

Clprolf should not be understood as a rigid purity system.

An **agent** is a class whose main responsibility is conceptual, domain-oriented, or meaningful in the application.

A **worker** is a class whose main responsibility is technical execution: launching, displaying, storing, rendering, sending, reading, writing, or interacting with the system.

An agent may contain a small amount of technical code if it remains secondary and improves readability.

But heavy technical work should usually be delegated to workers.

Likewise, a worker should avoid making domain decisions.
It may manipulate data, perform I/O, render output, or call technical APIs, but it should not become the brain of the application.

The rule is not:

> “Agents must be perfectly pure.”

The rule is:

> **The main role must stay clear.**

That is what makes Clprolf practical.

---

## 9. Method-level SRP: the real issue

SRP also has a meaningful application at the method level, but not in the simplistic way people often describe.

The key rule is:

> **A business method should not be polluted with heavy technical code.**
> **A technical method should not contain domain decisions.**

When a method mixes domain logic with infrastructure logic, it immediately gains two independent reasons to change:

* the domain rules may evolve;
* the technical mechanism may evolve.

That is a real SRP violation.

Example of a problematic method:

```java
public void processOrder(Order order) {
    if (order.total() <= 0) {
        throw new IllegalArgumentException();
    }

    Connection connection = DriverManager.getConnection(...);
    PreparedStatement statement = connection.prepareStatement(...);
    statement.executeUpdate();
}
```

This method mixes:

* order validation,
* database connection,
* SQL execution.

The method has more than one reason to change.

In Clprolf framework, the preferred structure would be:

```java
@ClAgent
public class OrderProcessor {
    private OrderRepository repository;

    public void process(Order order) {
        if (order.total() <= 0) {
            throw new IllegalArgumentException();
        }

        repository.save(order);
    }
}
```

```java
@ClWorker
public class OrderRepository {
    public void save(Order order) {
        // technical persistence code
    }
}
```

Now the roles are clear:

* the agent decides;
* the worker executes.

---

## 10. What does not violate SRP

Many things often called “SRP violations” are not necessarily violations.

For example:

* a method that is long;
* a method with several internal steps;
* a method that contains an inline algorithm;
* a class with several related methods;
* a class that performs several actions within the same coherent domain.

These are not automatically SRP problems.

Algorithmic steps do not create multiple responsibilities by themselves.

A method can have several steps and still express one intention.

A class can have several methods and still serve one responsibility.

SRP is about **cross-domain evolution**, not micro-method minimalism.

The question is not:

> “Is there more than one line?”
> “Is there more than one method?”

The question is:

> **Are multiple unrelated reasons to change being mixed together?**

---

## 11. Clprolf framework and inheritance coherence

SRP is also related to inheritance.

Inheritance is not just code reuse.
It expresses conceptual continuity.

If a class inherits from another class, it should remain in the same coherent domain.

For example:

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

This is coherent: Dog remains in the same conceptual family as Animal.

But this is not coherent:

```java
@ClWorker
public class DatabaseConnection {
}
```

```java
@ClAgent
public class Dog extends DatabaseConnection {
}
```

This mixes unrelated meanings.

The issue is not inheritance itself.
The issue is incoherent inheritance.

Clprolf makes this visible by giving each class a declared role.

If the inheritance crosses incompatible domains, the contradiction becomes obvious.

And if the developer really needs to force the relation, Clprolf can allow explicit forcing annotations — but the exception remains visible.

---

## 12. From SRP to Clprolf

The SRP says:

> A class should have only one reason to change.

Clprolf framework asks:

> Where does that reason come from?

And the answer is:

> It comes from the class’s responsibility.
> And that responsibility comes from its main nature, revealed by its declared role.

So Clprolf completes the SRP like this:

> A class should have only one reason to change
> because it has one main responsibility,
> rooted in one main conceptual nature.

This does not replace the SRP.

It explains it.

It gives it structure.

It turns a slogan into something readable in code.

---

## 13. Summary table

| Concept            | Meaning                                        | Example               |
| ------------------ | ---------------------------------------------- | --------------------- |
| **Domain**         | What the class conceptually represents         | `Doctor`              |
| **Responsibility** | What the class is responsible for as a whole   | Caring for patients   |
| **Methods**        | Concrete actions expressing the responsibility | `takeBloodPressure()` |
| **Agent**          | Class with a conceptual/domain role            | `OrderProcessor`      |
| **Worker**         | Class with a technical execution role          | `OrderRepository`     |

When nature, responsibility, methods, and role stay aligned, change becomes more predictable.

The class can evolve without losing its identity.

---

## 14. Final note — Change as a sign of duty

Change is not always a sign of instability.

Sometimes, change is the proof that a class is still faithful to its duty.

A class evolves because the responsibility it carries evolves.

If the class has a clear nature, that evolution remains coherent.

If the class mixes unrelated responsibilities, every change becomes dangerous.

That is what the SRP was trying to say.

Clprolf framework helps to make it visible.

---

## Conclusion

The SRP told us:

> “A class should have only one reason to change.”

Clprolf tries to complete the sentence:

> **A class should have only one reason to change because its responsibility flows from one main nature, made visible by its declared role.**

In the end, what the SRP truly asks for is simple:

* no cross-domain mixing;
* no incoherent inheritance;
* no silent contradiction between what a class is and what it does;
* one clear meaning given to each class.

That is the clarity Clprolf brings.
