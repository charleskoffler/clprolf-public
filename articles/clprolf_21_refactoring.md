# Refactoring Your Classes with Clprolf and `indef_obj`

Most developers agree on the **Single Responsibility Principle (SRP)**.
The real difficulty is not *what* to do — it’s **when** to do it.

In real projects, we usually:

1. write code that works,
2. understand it better over time,
3. refactor when responsibilities become clearer.

**Clprolf fits naturally into this workflow** by supporting postponed architectural decisions through refactoring.

---

## 1️⃣ Starting from a Classic OOP Class

Let’s start with a very common example.
Nothing exotic. Just a class that *works*.

```java
public class OrderManager {

    public void processOrder(Order order) {
        if (order.getTotal() <= 0) {
            throw new IllegalArgumentException("Invalid order");
        }

        saveToDatabase(order);
        logOrder(order);

        String message = formatConfirmation(order);
        sendEmail(message);
    }

    private void saveToDatabase(Order order) {
        System.out.println("Saving order " + order.getId());
    }

    private void logOrder(Order order) {
        System.out.println("Order processed: " + order.getId());
    }

    private String formatConfirmation(Order order) {
        return "Order #" + order.getId() + " confirmed";
    }

    private void sendEmail(String message) {
        System.out.println("Sending email: " + message);
    }
}
```

This class:

* contains **business rules**,
* handles **persistence**,
* performs **logging**,
* formats messages,
* sends emails.

Everyone has written something like this.

---

## 2️⃣ Using Clprolf Without Thinking About Roles

In Clprolf, you can start **exactly the same way**.

No need to decide anything upfront.
You simply acknowledge that the architectural role is **not yet clear**.

```clprolf
public indef_obj OrderManager {

    public void processOrder(Order order) {
        if (order.getTotal() <= 0) {
            throw new IllegalArgumentException("Invalid order");
        }

        saveToDatabase(order);
        logOrder(order);

        String message = formatConfirmation(order);
        sendEmail(message);
    }

    private void saveToDatabase(Order order) { }
    private void logOrder(Order order) { }
    private String formatConfirmation(Order order) { return ""; }
    private void sendEmail(String message) { }
}
```

At this stage:

* nothing is forced,
* no architectural commitment is made,
* the code can remain like this for a while.

`indef_obj` behaves like classic OOP — **but consciously**.

---

## 3️⃣ Applying SRP After Understanding the Code

Later — exactly like in a classic SRP refactoring — you realize that this class mixes different responsibilities.

| Concern           | Nature    |
| ----------------- | --------- |
| Business decision | Business  |
| Persistence       | Technical |
| Logging           | Technical |
| Formatting / I/O  | Technical |

This is where **Clprolf becomes useful**.

---

## 4️⃣ Refactoring into Explicit Responsibilities

```
Before (indef_obj)

OrderManager
 ├─ business rule
 ├─ persistence
 ├─ logging
 └─ notification


After (SRP made explicit)

OrderProcessor (agent)
 ├─ business decision

OrderRepository (worker)
 └─ persistence

OrderNotifier (worker)
 ├─ logging
 └─ messaging
```

### 🎯 Business Responsibility → `agent`

```clprolf
public agent OrderProcessor {

    private final OrderRepository repository;
    private final OrderNotifier notifier;

    public OrderProcessor(OrderRepository repository,
                          OrderNotifier notifier) {
        this.repository = repository;
        this.notifier = notifier;
    }

    public void process(Order order) {
        if (order.getTotal() <= 0) {
            throw new IllegalArgumentException("Invalid order");
        }

        repository.save(order);
        notifier.notify(order);
    }
}
```

The **agent**:

* expresses **business intent**,
* coordinates actions,
* contains **no technical details**.

---

### ⚙️ Technical Responsibilities → `worker`

```clprolf
public worker OrderRepository {

    public void save(Order order) {
        System.out.println("Saving order " + order.getId());
    }
}

public worker OrderNotifier {

    public void notify(Order order) {
        log(order);
        String message = format(order);
        send(message);
    }

    private void log(Order order) {
        System.out.println("Order processed: " + order.getId());
    }

    private String format(Order order) {
        return "Order #" + order.getId() + " confirmed";
    }

    private void send(String message) {
        System.out.println("Sending email: " + message);
    }
}
```

Workers may mix technical concerns freely:

* I/O,
* formatting,
* logging,
* infrastructure.

This is **acceptable** because business meaning remains in the agent.

---

## 5️⃣ What Changed — and What Didn’t

### ❌ What did **not** change

* algorithms,
* behavior,
* performance,
* expressivity.

### ✅ What **did** change

* responsibilities became explicit,
* architectural intent became visible,
* future evolution became easier.

This is simply **SRP applied structurally**.

---

## 🧩 Conclusion

Clprolf does **not** ask developers to think harder —
and certainly not earlier.

It allows them to think **when they are ready**.

You can:

* write code first,
* understand it later,
* refactor responsibilities when needed.

`indef_obj` is **not a goal** —
it is a **temporary state** that supports real-world development.

In that sense, **Clprolf is not just a language**.
It is a **refactoring tool for architectural clarity**.

---