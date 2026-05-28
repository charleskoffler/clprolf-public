# Clprolf — A new way to express your talent with OOP

🚀 Clprolf — Quick Entry

## 🎯 The Problem

In classical OOP, classes tend to drift over time:

* a class starts simple
* responsibilities accumulate
* business logic and technical code get mixed
* structure becomes unclear

👉 Result: **blurred architecture and hard maintenance**

---

## 💡 The Clprolf Idea

Clprolf enforces one simple rule:

> **A class must declare what it is — and never drift away from it.**

This leads to a simple and explicit structural model

---

## 🧱 Two Kinds of Classes

### 🔵 1. `agent` → business logic

* represents a clear domain responsibility
* contains decisions and rules
* does NOT perform technical work

Example:

```clprolf
public agent OrderProcessor {
    public void process(Order order) {
        if (order.total() <= 0) throw Error;
        repository.save(order);
        notifier.notify(order);
    }
}
```

---

### ⚙️ 2. `worker` → technical work

* performs machine-related tasks
* database, I/O, UI, infrastructure

```clprolf
public worker OrderRepository {
    public void save(Order order) {
        // DB logic
    }
}
```

---

## 🔥 Core Rule

> ❌ Never mix business logic and technical code
> ✅ Agents delegate technical work to workers

---

## 🧠 Inheritance (Simple)

Clprolf replaces `extends` with:

```clprolf
nature
```

👉 Meaning:

> “inheritance within the same nature”

So:

* an `agent` inherits from an `agent`
* a `worker` inherits from a `worker`
* otherwise → use composition

---

## ⚖️ What You Get

With these rules:

* ✔️ clear class roles
* ✔️ no mixing of concerns
* ✔️ meaningful inheritance
* ✔️ stable architecture over time

---

## 🧪 Before / After

### ❌ Classical OOP

```java
class OrderManager {
    void process() {
        validate();
        saveToDB();
        sendEmail();
    }
}
```

👉 everything mixed together

---

### ✅ Clprolf

```clprolf
agent OrderProcessor
worker OrderRepository
worker OrderNotifier
```

👉 clear separation

---

## 🎯 In One Sentence

> **Clprolf makes explicit what developers already try to enforce implicitly.**

---

## 🔚 Conclusion

Clprolf does not add arbitrary complexity.

👉 It makes structure explicit:

* one responsibility
* clear separation
* coherent architecture

---

## 🎯 When should you use Clprolf?

Use Clprolf when architectural clarity matters more than flexibility

---

### ✅ Good fit

Clprolf is well-suited for:

* **Complex systems**
  where responsibilities tend to drift over time

* **Long-lived projects**
  where architectural stability is critical

* **Educational contexts**
  to teach clear separation of concerns

* **Simulation and multi-agent systems**
  where roles and interactions must remain explicit

---

### ⚖️ Trade-off

Clprolf introduces **structural constraints**:

* classes must declare a clear nature
* inheritance must preserve that nature
* technical and business logic are strictly separated

👉 This reduces ambiguity, but also reduces freedom.

---

### ❌ Less suited for

Clprolf may not be ideal for:

* **small or short-lived projects**
* **rapid prototyping**
* **cases where flexibility is preferred over structure**

---

## 🎯 In short

> **Use Clprolf when you want your architecture to stay clear, stable, and explicit over time.**

---