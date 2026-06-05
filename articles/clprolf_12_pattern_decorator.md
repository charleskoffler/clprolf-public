# Decorator Pattern in Clprolf Framework — Example with Coffee

In traditional OOP, the **Decorator** is used to dynamically add responsibilities to objects. Instead of creating subclasses for every combination of features, we compose them step by step.

---

## Example: Decorating a Coffee ☕

Imagine a coffee shop where you can order a coffee with **milk**, **sugar**, or both.
Instead of creating every possible class (`CoffeeWithMilkAndSugar`, `CoffeeWithSugarAndWhippedCream`…), we use the **Decorator pattern**.

👉 The goal is to **avoid a class explosion and code duplication**.
With Decorator, we don’t need a new class for every combination — we just combine a few decorators dynamically.

---

### The agent

```java
@ClAgent
@ClFamily
public interface Coffee {
    public double getCost();
    public String getDescription();
}
```

👉 This is the **family interface**: every coffee, decorated or not, must provide a `cost` and a `description`.

---


### The Abstract Decorator

```java
@ClAgent
public abstract class CoffeeDecorator implements Coffee {
    private final Coffee decoratedCoffee;

    public CoffeeDecorator(Coffee decoratedCoffee) {
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
@ClAgent
public class SimpleCoffee implements Coffee {
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
@ClAgent
public class CoffeeWithMilk extends CoffeeDecorator {
    public CoffeeWithMilk(Coffee decoratedCoffee) {
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

@ClAgent
public class CoffeeWithSugar extends CoffeeDecorator {
    public CoffeeWithSugar(Coffee decoratedCoffee) {
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
@ClWorker
public class CoffeeShopMaker {
    public static void main(String[] args) {
        // Order a simple coffee
        Coffee coffee = new SimpleCoffee();
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

## Why Decorator Is Clear in Clprolf framework

In traditional OOP, the **Decorator** often looks confusingly close to a **Proxy** or an **Adapter**.
With **Clprolf**, the roles become explicit:

* All coffees (`SimpleCoffee`, `CoffeeWithMilk`, `CoffeeWithSugar`) are **`agent`**.
  👉 They all represent the same *real-world object*: a coffee.
* The abstract decorator (`CoffeeDecorator`) is also a **`agent`**, ensuring the chain of decorations is still the same object type.

The result: there is **only one coffee** — multiple instances are just a technical solution to decorate it.

---

## Decorator vs Proxy in Clprolf

| **Proxy**                                    | **Decorator**                                       |
| -------------------------------------------- | --------------------------------------------------- |
| Controls or delays access (technical intent) | Adds responsibilities or features (business intent) |
| Example: `ProxyImage` loads an image lazily  | Example: `CoffeeWithMilk` adds cost + description   |
| Different reason for existence               | Same agent, but enriched                      |
| Usually a sibling `agent`                      | Always an `agent` of the same family         |

👉 In Clprolf, the intent is visible in the **class role**.

---

## Conclusion

The **Decorator Pattern in Clprolf framework** becomes simpler and more consistent:

* There is always **one real object** (`agent`) being enriched.
* The abstract decorator ensures chaining works naturally.

✨ With Clprolf, the Decorator is no longer a confusing “wrapper.”
It is clearly a way to **enrich a simulated real object** step by step, without cluttering your code with endless subclasses.

---