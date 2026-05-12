# Observer Pattern in Clprolf: Clear Roles for Notifications

## 🤔 The Problem

You have one object (**the Subject**) whose state changes,
and multiple **Observers** that should be **notified automatically** when this happens.

Classic examples:

* a stock ticker that updates multiple dashboards,
* a button that triggers several listeners.

The challenge:

* Observers must react **without the Subject knowing their details**,
* so the Subject can focus on its own job.

---

## ✅ The Clprolf Solution

In Clprolf:

* The **Observer role** is a `version_inh agent`.
* Observers are declared with `contracts`.
* The **Subject** is an `agent` that declares its observers with `with_compat`.

Result: the intent (*who observes whom*) is **explicit in the code**.

---

## 📝 Example: Stock Updates

We’ll model a `Stock` (the Subject),
and two Observers:

* **PriceNotifier** → a neutral agent: no business logic, just delegates to a worker,
* **TrendAnalyzer** → a business agent: applies real analysis logic.

Both rely on a **DisplayWorker** for technical output.

### Clprolf Code

```java
// 1) Observer role
public version_inh agent StockObserver {
    void update(int price);
}

// 2) Neutral observer: no business logic, pure delegation
public agent PriceNotifier contracts StockObserver {
    private with_compat DisplayWorker display;

    public PriceNotifier(with_compat DisplayWorker display) {
        this.display = display;
    }

    public void update(int price) {
        display.show("Current price: " + price);
    }
}

// 3) Business observer: applies rules (domain logic)
public agent TrendAnalyzer contracts StockObserver {
    private int lastPrice = -1;
    private with_compat DisplayWorker display;

    public TrendAnalyzer(with_compat DisplayWorker display) {
        this.display = display;
    }

    public void update(int price) {
        if (lastPrice != -1) {
            if (price > lastPrice) display.show("Trend: UP");
            else if (price < lastPrice) display.show("Trend: DOWN");
            else display.show("Trend: STABLE");
        }
        lastPrice = price;
    }
}

// 4) Technical worker: handles I/O
public worker_agent DisplayWorker {
    public void show(String msg) {
        System.out.println(msg);
    }
}

// 5) Subject: manages state and observers
public agent Stock {
    private List<with_compat StockObserver> observers = new ArrayList<>();
    private int price;

    public void addObserver(with_compat StockObserver obs) { observers.add(obs); }
    public void removeObserver(with_compat StockObserver obs) { observers.remove(obs); }

    public void setPrice(int newPrice) {
        this.price = newPrice;
        notifyObservers();
    }

    private void notifyObservers() {
        for (with_compat StockObserver obs : observers) {
            obs.update(price);
        }
    }
}

// 6) Demo
public worker_agent ObserverDemo {
    public static void main(String[] args) {
        with_compat DisplayWorker display = new DisplayWorker();

        with_compat StockObserver notifier = new PriceNotifier(display);   // neutral observer
        with_compat StockObserver analyzer = new TrendAnalyzer(display);  // business observer

        Stock apple = new Stock();
        apple.addObserver(notifier);
        apple.addObserver(analyzer);

        apple.setPrice(100);
        apple.setPrice(105);
        apple.setPrice(103);
    }
}
```

---

## 👀 Bonus: Two Types of Observers

Notice the difference:

* **PriceNotifier** is a **neutral agent** → it has no domain logic, it just forwards the event to a worker.
* **TrendAnalyzer** is a **business agent** → it applies real business rules (detecting UP/DOWN/STABLE trends).

💡 Both are valid observers, but their roles are **clearly separated**:

* Domain logic in agents,
* Technical execution in workers.

---

## 🔎 Why this is clear in Clprolf

* `version_inh agent StockObserver` → shows immediately that an Observer is a role.
* `contracts` → concrete observers are bound by contract, no ambiguity.
* `with_compat` → Subject’s dependencies are **declared explicitly** as observers.
* `worker_agent DisplayWorker` → keeps technical concerns isolated.

---

## 🎯 Key takeaway

In Clprolf, Observer is just:

> **Declare an agent role, implement observers, and let the Subject notify them.**

You can model neutral observers or business analyzers,
and the separation of roles makes the system easier to understand and maintain.

---

## 💡 Innovation Note: Patterns Reveal Roles

Design patterns are more than technical tricks — they carry **implicit roles**.

* Observer → a subject and its observers.
* Strategy → a context and interchangeable rules.
* Adapter → an agent that adapts.

👉 In Clprolf, these roles are not hidden — they are **explicit keywords** (`agent`, `abstraction`, `worker_agent`).
That’s why patterns feel simpler and more natural here.

---

✨ That’s it: familiar use case, clear Clprolf solution, and a proof that roles make design patterns self-explaining.

---