# Strategy Pattern in Clprolf: Clear Roles for Swappable Behavior

## 🤔 The Problem

You have a **behavior** that should be easy to **swap** (at runtime or configuration time).
Example: apply different **discount** policies without changing the checkout code.

---

## ✅ The Clprolf Solution

In Clprolf:

* a strategy is modeled as a **`family_interf agent`** (a simple, swappable rule),
* each concrete strategy is also an **`agent`** that **implements** the base one,
* the context declares its dependency.

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
public family_interf agent Discount {
    int apply(int price);
}

// 2) Concrete strategies
public agent NoDiscount implements Discount {
    public int apply(int price) { return price; }
}

public agent PercentageDiscount implements Discount {
    private int percent;

    public PercentageDiscount(int percent) { this.percent = percent; }

    public int apply(int price) {
        return price - (price * percent / 100);
    }
}

// 3) Context depending on a strategy
public agent Checkout {
    private Discount strategy;

    public Checkout(Discount strategy) {
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
public worker StrategyDemo {
    public static void main(String[] args) {
        int[] cart = new int[] { 4000, 2000, 1500 }; // total = 7500

        // No discount
        Discount none = new NoDiscount();
        Checkout checkoutNone = new Checkout(none);
        System.out.println(checkoutNone.total(cart)); // 7500

        // 15% discount
        Discount promo15 = new PercentageDiscount(15);
        Checkout checkout15 = new Checkout(promo15);
        System.out.println(checkout15.total(cart)); // 6375
    }
}
```

---

## 🔎 Why this is clear in Clprolf

* `family_interf agent` shows immediately that a discount is a **generic, swappable rule**.
* Swapping is just supplying a different **agent** — the context stays untouched.

---

## 🎯 Key takeaway

In Clprolf, Strategy is not a trick — it’s simply:

> **Declare a rule (`agent`), provide implementations, and swap them.**

Behavior becomes interchangeable, and the intent is visible in the syntax.

---

## 💡 Innovation Note: Patterns reveal roles

In traditional OOP, Strategy is often shown as a purely technical structure.
But in reality, it already carries a **business role**: “choosing a behavior.”

👉 Clprolf makes this dimension explicit.

* Here, the role is `agent` → a discount is a rule.
* In another case, a Strategy could be an `agent` (e.g., choosing a routing algorithm).
* For technical variations, it might be a `worker` (like multiple DAO implementations).

**This is where Clprolf innovates:**

> Design patterns don’t just solve problems — they map naturally to roles (`agent`, `agent`, `worker`).
> And when the role is explicit, the pattern’s intent becomes crystal clear.

---

✨ That’s it — familiar use case, clear solution, and an innovation: roles turn patterns into self-explaining designs.

---
