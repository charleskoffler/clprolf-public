# Refactoring Your Classes with Clprolf framework

Most developers agree on the **Single Responsibility Principle (SRP)**.
The real difficulty is not *what* to do — it’s **when** to do it.

In real projects, we usually:

1. write code that works,
2. understand it better over time,
3. refactor when responsibilities become clearer.

**Clprolf framework fits naturally into this workflow** by supporting postponed architectural decisions through refactoring.

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

## 2️⃣ Using Clprolf framework Without Thinking About Roles

In Clprolf, you can start **exactly the same way**.

No need to decide anything upfront.
You simply acknowledge that the architectural role is **not yet clear**.

```java
@ClDraft
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

`ClDraft` behaves like classic OOP — **but consciously**.

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
Before (ClDraft)

OrderManager
 ├─ business rule
 ├─ persistence
 ├─ logging
 └─ notification


After (SRP made explicit)

OrderProcessor (ClAgent)
 ├─ business decision

OrderRepository (ClWorker)
 └─ persistence

OrderNotifierWorker (ClWorker)
 ├─ logging
 └─ messaging
```

### 🎯 Business Responsibility → `agent`

```java
@ClAgent
public class OrderProcessor {

    private final OrderRepository repository;
    private final OrderNotifierWorker worker;

    public OrderProcessor(OrderRepository repository,
                          OrderNotifierWorker worker) {
        this.repository = repository;
        this.worker = worker;
    }

    public void process(Order order) {
        if (order.getTotal() <= 0) {
            throw new IllegalArgumentException("Invalid order");
        }

        repository.save(order);
        worker.notify(order);
    }
}
```

The **agent**:

* expresses **business intent**,
* coordinates actions,
* contains **no technical details**.

---

### ⚙️ Technical Responsibilities → `worker`

```java
@ClWorker
public class OrderRepository {

    public void save(Order order) {
        System.out.println("Saving order " + order.getId());
    }
}

@ClWorker
public class OrderNotifierWorker {

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

Workers may group related technical concerns when they serve the same technical purpose:

* I/O,
* formatting,
* logging,
* infrastructure.

This is **acceptable** because business meaning remains in the agent. And because most of the time, the worker supports an agent.

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

Clprolf framework does **not** ask developers to think harder —
and not earlier.

You can:

* write code first,
* understand it later,
* refactor responsibilities when needed.

`ClDraft` is **not a goal** —
it is a **temporary state** that supports real-world development.

Clprolf framework can work as a **refactoring tool for architectural clarity**.

---