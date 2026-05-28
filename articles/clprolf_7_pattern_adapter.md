# Adapter Pattern in Clprolf: From Enumeration to Iterator

## 🤔 The Problem

Imagine you have an existing class that implements an **old interface**, but your application now expects the **modern version** of that interface.

* You cannot change the old class (it’s legacy code, or external).
* You need a way to reuse it, but expose it through the **newer contract**.

That’s where the **Adapter** comes in.

---

## ✅ The Clprolf Solution

In Clprolf, the rule is simple:

* A concrete **agent** can contract **only one** `family_inh`.
* So you **cannot make the same class both an “old” and a “modern” implementation**.
* Instead, you **create a new agent — the Adapter — which contracts the modern version, and internally uses the old one via `with_compat`.**

---

## 📝 Example: Enumeration → Iterator

Old Java APIs used `Enumeration`, but modern code expects `Iterator`. We want to reuse existing `Enumeration` implementations without rewriting them.

### Clprolf Code

```java
// 1. Old contract (an abstraction)
public family_interf abstraction Enumeration<E> {
    boolean hasMoreElements();
    E nextElement();
}

// 2. Modern contract (an agent)
public family_interf agent Iterator<E> {
    boolean hasNext();
    E next();
}

// 3. Adapter agent: contracts the modern version
public agent EnumToIterAdapter<E> contracts Iterator<E> {
    private with_compat Enumeration<E> enumeration;

    public EnumToIterAdapter(with_compat Enumeration<E> enumeration) {
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

## 🔎 Why this is clear in Clprolf

* `family_interf` makes it explicit: these are **role contracts meant to be implemented** by agents.
* `contracts` shows clearly: the Adapter **is a modern `Iterator`**.
* `with_compat` highlights the dependency on the old `Enumeration`.
* No hidden tricks: we see immediately that the Adapter is **a new agent** created for translation.

And here’s an important detail:

* **`Enumeration` is an abstraction** (a very minimal contract, part of the `agent` declension in Clprolf).
* **`Iterator` is a full agent**, representing the modern iteration model.
* So the Adapter not only bridges old to new, but also shows a **shift in philosophy**: from abstraction to agent.

---

## 🎯 Key takeaway

In Clprolf, the Adapter is never a “magical disguise.”
It’s simply:

> **A new agent that contracts the modern interface, and delegates to an old implementation through `with_compat`.**

This removes confusion and makes the intent crystal clear.

---

## 👀 Bonus: Using the Adapter in a Demo

For completeness, here’s how a client would actually use the Adapter.
Even if the old API gives you an `Enumeration`, the Adapter lets you treat it as a modern `Iterator`:

```java
public worker AdapterDemo {
    public static void main(String[] args) {
        Vector<String> legacyVector = new Vector<>();
        legacyVector.add("one");
        legacyVector.add("two");
        with_compat Enumeration<String> enumeration = legacyVector.elements();

        with_compat Iterator<String> iterator = new EnumToIterAdapter<>(enumeration);

        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }
}
```

---

✨ What do you think? Have you used the Adapter pattern in real projects (e.g., bridging old APIs with modern ones)?

---
