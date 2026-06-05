# Adapter Pattern in Clprolf framework: From Enumeration to Iterator

## 🤔 The Problem

Imagine you have an existing class that implements an **old interface**, but your application now expects the **modern version** of that interface.

* You cannot change the old class (it’s legacy code, or external).
* You need a way to reuse it, but expose it through the **newer contract**.

That’s where the **Adapter** comes in.

---

## ✅ The Clprolf Solution

In Clprolf, the rule is simple:

* A concrete **agent** can implement **only one** `family_inh`.
* So you **cannot make the same class both an “old” and a “modern” implementation**.
* Instead, you **create a new agent — the Adapter — which implements the modern version, and internally uses the old one.**

---

## 📝 Example: Enumeration → Iterator

Old Java APIs used `Enumeration`, but modern code expects `Iterator`. We want to reuse existing `Enumeration` implementations without rewriting them.
For illustration purposes, the examples below re-declare simplified versions of the Java interfaces.

### Clprolf Code

```java
// 1. Old contract (an agent)
@ClAgent
@ClFamily
public interface Enumeration<E> {
    boolean hasMoreElements();
    E nextElement();
}

// 2. Modern contract (an agent)
@ClAgent
@ClFamily
public interface Iterator<E> {
    boolean hasNext();
    E next();
}

// 3. Adapter agent: implements the modern version
@ClAgent
public class EnumToIterAdapter<E> implements Iterator<E> {
    private Enumeration<E> enumeration;

    public EnumToIterAdapter(Enumeration<E> enumeration) {
        this.enumeration = enumeration;
    }

    public boolean hasNext() {
        return enumeration.hasMoreElements();
    }

    public E next() {
        return enumeration.nextElement();
    }
}
```

---

## 🔎 Why this is clear in Clprolf Framework

* `family` makes it explicit: these are **family interfaces meant to be implemented** by agents.
* No hidden tricks: we immediately see that the Adapter is **a new agent** created for translation.

And here’s an important detail:

* **`Enumeration` is an agent**
* **`Iterator` is a full agent**, representing the modern iteration model.

Both represent coherent iteration domains.
The adapter exists because the domains are related but structurally distinct.

---

## 🎯 Key takeaway

In Clprolf framework, the Adapter is never a “magical disguise.”
It’s simply:

> **A new agent that translates one family interface into another.**

This removes confusion and makes the intent crystal clear.

---

## 👀 Bonus: Using the Adapter in a Demo

For completeness, here’s how a client would actually use the Adapter.
Even if the old API gives you an `Enumeration`, the Adapter lets you treat it as a modern `Iterator`:

```java
@ClWorker
public class AdapterDemo {
    public static void main(String[] args) {
        Vector<String> legacyVector = new Vector<>();
        legacyVector.add("one");
        legacyVector.add("two");
        Enumeration<String> enumeration = legacyVector.elements();

        Iterator<String> iterator = new EnumToIterAdapter<>(enumeration);

        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }
}
```

---

✨ What do you think? Have you used the Adapter pattern in real projects (e.g., bridging old APIs with modern ones)?

---
