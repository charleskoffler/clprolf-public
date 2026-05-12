# Strategy Pattern in Clprolf: Clear Roles for Swappable Behavior

## 🤔 The Problem

You have a **behavior** that should be easy to **swap** (at runtime or configuration time).
Example: apply different **discount** policies without changing the checkout code.

---

## ✅ The Clprolf Solution

In Clprolf:

* a strategy is modeled as a **`version_inh abstraction`** (a simple, swappable rule),
* each concrete strategy is also an **`abstraction`** that **contracts** the base one,
* the context declares its dependency with `with_compat`.

Result: the strategy’s role, implementations, and dependency are **explicit**.

---

## 📝 Example: Discount strategies

We’ll define a `Discount` and two implementations:

* `NoDiscount` (does nothing)
* `PercentageDiscount` (e.g., 15% off)

Then a `Checkout` context that uses whichever discount it’s given.

### Clprolf Code

```java
// 1) Strategy contract (a generic rule for discounts)
public version_inh abstraction Discount {
    int apply(int price);
}

// 2) Concrete strategies
public abstraction NoDiscount contracts Discount {
    public int apply(int price) { return price; }
}

public abstraction PercentageDiscount contracts Discount {
    private int percent;

    public PercentageDiscount(int percent) { this.percent = percent; }

    public int apply(int price) {
        return price - (price * percent / 100);
    }
}

// 3) Context depending on a strategy
public agent Checkout {
    private with_compat Discount strategy;

    public Checkout(with_compat Discount strategy) {
        this.strategy = strategy;
    }

    public int total(int[] prices) {
        int sum = 0;
        for (int p : prices) sum += p;
        return strategy.apply(sum);
    }
}
```

---

## 👀 Bonus: Demo with swappable discounts

```java
public worker_agent StrategyDemo {
    public static void main(String[] args) {
        int[] cart = new int[] { 4000, 2000, 1500 }; // total = 7500

        // No discount
        with_compat Discount none = new NoDiscount();
        Checkout checkoutNone = new Checkout(none);
        System.out.println(checkoutNone.total(cart)); // 7500

        // 15% discount
        with_compat Discount promo15 = new PercentageDiscount(15);
        Checkout checkout15 = new Checkout(promo15);
        System.out.println(checkout15.total(cart)); // 6375
    }
}
```

---

## 🔎 Why this is clear in Clprolf

* `version_inh abstraction` shows immediately that a discount is a **generic, swappable rule**, not a central agent.
* `contracts` makes each concrete discount a true implementation of that rule.
* `with_compat` declares the dependency directly: the checkout expects “a discount,” no hidden wiring.
* Swapping is just supplying a different **abstraction** — the context stays untouched.

---

## 🎯 Key takeaway

In Clprolf, Strategy is not a trick — it’s simply:

> **Declare a rule (`abstraction`), provide implementations, and swap them with `with_compat`.**

Behavior becomes interchangeable, and the intent is visible in the syntax.

---

## 💡 Innovation Note: Patterns reveal roles

In traditional OOP, Strategy is often shown as a purely technical structure.
But in reality, it already carries a **business role**: “choosing a behavior.”

👉 Clprolf makes this dimension explicit.

* Here, the role is `abstraction` → a discount is a rule, not an agent.
* In another case, a Strategy could be an `agent` (e.g., choosing a routing algorithm).
* For technical variations, it might be a `worker_agent` (like multiple DAO implementations).

**This is where Clprolf innovates:**

> Design patterns don’t just solve problems — they map naturally to roles (`agent`, `abstraction`, `worker_agent`).
> And when the role is explicit, the pattern’s intent becomes crystal clear.

---

✨ That’s it — familiar use case, clear solution, and an innovation: roles turn patterns into self-explaining designs.

---
