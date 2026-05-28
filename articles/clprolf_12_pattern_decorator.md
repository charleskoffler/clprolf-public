# Decorator Pattern in Clprolf — Example with Coffee

In traditional OOP, the **Decorator** is used to dynamically add responsibilities to objects. Instead of creating subclasses for every combination of features, we compose them step by step.

---

## Example: Decorating a Coffee ☕

Imagine a coffee shop where you can order a coffee with **milk**, **sugar**, or both.
Instead of creating every possible class (`CoffeeWithMilkAndSugar`, `CoffeeWithSugarAndWhippedCream`…), we use the **Decorator pattern**.

👉 The goal is to **avoid a class explosion and code duplication**.
With Decorator, we don’t need a new class for every combination — we just combine a few decorators dynamically.

---

### The Abstraction

```java
public family_interf simu_real_obj CoffeeWithAdditional {
    public double getCost();
    public String getDescription();
}
```

👉 This is the **family contract**: every coffee, decorated or not, must provide a `cost` and a `description`.

---


### The Abstract Decorator

```java
public abstract simu_real_obj CoffeeWithAdditionalImpl contracts CoffeeWithAdditional {
    private final with_compat CoffeeWithAdditional decoratedCoffee;

    public CoffeeWithAdditionalImpl(with_compat CoffeeWithAdditional decoratedCoffee) {
        this.decoratedCoffee = decoratedCoffee;
    }

    @Override
    public double getCost() {
        return decoratedCoffee.getCost();
    }

    @Override
    public String getDescription() {
        return decoratedCoffee.getDescription();
    }
}
```

👉 This class is the **base for all decorators**.
It forwards calls to the wrapped coffee and lets subclasses add extra behavior.

---

### The Concrete Product

```java
public simu_real_obj SimpleCoffee contracts CoffeeWithAdditional {
    @Override
    public double getCost() {
        return 2.0; // Base cost of simple coffee
    }

    @Override
    public String getDescription() {
        return "Simple Coffee";
    }
}
```

---

### The Concrete Decorators

```java
public simu_real_obj CoffeeWithMilk nature CoffeeWithAdditionalImpl {
    public CoffeeWithMilk(with_compat CoffeeWithAdditional decoratedCoffee) {
        super(decoratedCoffee);
    }

    @Override
    public double getCost() {
        return super.getCost() + 0.5; // Add cost of milk
    }

    @Override
    public String getDescription() {
        return super.getDescription() + ", with Milk";
    }
}

public simu_real_obj CoffeeWithSugar nature CoffeeWithAdditionalImpl {
    public CoffeeWithSugar(with_compat CoffeeWithAdditional decoratedCoffee) {
        super(decoratedCoffee);
    }

    @Override
    public double getCost() {
        return super.getCost() + 0.2; // Add cost of sugar
    }

    @Override
    public String getDescription() {
        return super.getDescription() + ", with Sugar";
    }
}
```

---

### The Launcher

```java
public worker_agent CoffeeShopMaker {
    public static void main(String[] args) {
        // Order a simple coffee
        with_compat CoffeeWithAdditional coffee = new SimpleCoffee();
        System.out.println("Cost: $" + coffee.getCost());
        System.out.println("Description: " + coffee.getDescription());

        // Decorate it with Milk
        coffee = new CoffeeWithMilk(coffee);
        System.out.println("Cost: $" + coffee.getCost());
        System.out.println("Description: " + coffee.getDescription());

        // Decorate it with Sugar
        coffee = new CoffeeWithSugar(coffee);
        System.out.println("Cost: $" + coffee.getCost());
        System.out.println("Description: " + coffee.getDescription());
    }
}
```

---

### Console Output

```
Cost: $2.0
Description: Simple Coffee
Cost: $2.5
Description: Simple Coffee, with Milk
Cost: $2.7
Description: Simple Coffee, with Milk, with Sugar
```

---

## Why Decorator Is Clearer in Clprolf

In traditional OOP, the **Decorator** often looks confusingly close to a **Proxy** or an **Adapter**.
With **Clprolf**, the roles become explicit:

* All coffees (`SimpleCoffee`, `CoffeeWithMilk`, `CoffeeWithSugar`) are **`simu_real_obj`**.
  👉 They all represent the same *real-world object*: a coffee.
* The abstract decorator (`CoffeeWithAdditionalImpl`) is also a **`simu_real_obj`**, ensuring the chain of decorations is still the same object type.
* `with_compat` makes the recursive structure visible and safe.

The result: there is **only one coffee** — multiple instances are just a technical solution to decorate it.

---

## Decorator vs Proxy in Clprolf

| **Proxy**                                    | **Decorator**                                       |
| -------------------------------------------- | --------------------------------------------------- |
| Controls or delays access (technical intent) | Adds responsibilities or features (business intent) |
| Example: `ProxyImage` loads an image lazily  | Example: `CoffeeWithMilk` adds cost + description   |
| Different reason for existence               | Same abstraction, but enriched                      |
| Often a sibling `abstraction`                | Always a `simu_real_obj` of the same family         |

👉 In Clprolf, the intent is visible in the **declension**.

---

## Conclusion

The **Decorator Pattern in Clprolf** becomes simpler and more consistent:

* There is always **one real object** (`simu_real_obj`) being enriched.
* The abstract decorator ensures chaining works naturally.
* `with_compat` makes recursive composition explicit.

✨ With Clprolf, the Decorator is no longer a confusing “wrapper.”
It is clearly a way to **enrich a simulated real object** step by step, without cluttering your code with endless subclasses.

---