# Builder Pattern in Clprolf framework — Example with a House

In traditional OOP, the Builder pattern was invented to avoid **telescoping constructors**:
classes with too many parameters, often optional, that force you to write many overloaded constructors.
The result is confusing, and the object ends up polluted with initialization logic.

In Clprolf framework, the solution is clear:

* The **Builder** is an **agent** dedicated to the construction of another agent.
* The **Product** is usually an **`agent`**.
* The Builder composes and prepares the product step by step, then delivers it with `build()`.

---

## Example: Building a House

Imagine we need to create a `House` with optional attributes.
Instead of multiple constructors, we use a **Builder Agent**.

---

### The Product (Simulated Real Object)

```java
@ClAgent
public class House {
    String name;
    int floors;
    boolean garage;

    public void show(){
        System.out.println("House " + name + ", " + floors + " floors, garage: " + garage);
    }
}
```

---

### The Builder Agent (Composition)

```java
@ClAgent
public class HouseBuilder {
    private House house = new House(); // composition

    public HouseBuilder withName(String name){
        house.name = name;
        return this;
    }

    public HouseBuilder withFloors(int floors){
        house.floors = floors;
        return this;
    }

    public HouseBuilder withGarage(boolean garage){
        house.garage = garage;
        return this;
    }

    public House build(){
        if(house.name == null){
            throw new IllegalStateException("House must have a name");
        }
        return house;
    }
}
```

👉 Here:

* `House` is an **agent** — the product we want to build.
* `HouseBuilder` is an **agent** that prepares it step by step.
* No duplication of attributes: the Builder works directly with the object in composition.

---

### The Launcher

```java
@ClWorker
public class Launcher {
    public static void main(String[] args){
        House villa = new HouseBuilder()
            .withName("Villa Clprolf")
            .withFloors(2)
            .withGarage(true)
            .build();

        villa.show();
    }
}
```

---

## Console Output

```
House Villa Clprolf, 2 floors, garage: true
```

---

## When to Use Builder

The Builder is not for every object.
It is useful when:

* The product has **many parameters**, some optional.
* You want **all construction possibilities**, without cluttering the class.
* You need to **check conditions** before instantiation.

For simpler objects, a constructor is enough.

---

## Factory vs Builder in Clprolf framework

Both Factory and Builder are **creational patterns**, but they solve different problems:

| **Factory (agent)**                                  | **Builder (agent)**                                                |
| ---------------------------------------------------- | ------------------------------------------------------------------ |
| Decides **which variant** of an object to create     | Prepares **how to construct** an object step by step               |
| Often returns different implementations              | Always prepares the same type, with variations                     |
| Example: `CarFactory` → `ElectricCar` or `DieselCar` | Example: `HouseBuilder` → builds one `House` with flexible options |
| Class role: `agent`      `                           | Class role: `agent`                                                |

👉 Factory answers *“which object?”*
👉 Builder answers *“how to assemble it?”*

---

## Fluent Builder

The most common style is the **Fluent Builder**, where each method returns the builder itself.
This allows chaining calls naturally:

```java
House villa = new HouseBuilder()
    .withName("Fluent Villa")
    .withFloors(3)
    .withGarage(false)
    .build();
```

This style makes the Builder look almost as natural as a constructor, but without the complexity of overloaded constructors.

---

## Internal Builder (Variation)

Sometimes, the Builder is placed **inside the product class**.

```java
@ClAgent
public class House {
    String name;
    int floors;
    boolean garage;

    public void show(){
        System.out.println("House " + name + ", " + floors + " floors, garage: " + garage);
    }

    @ClAgent
    public static class Builder {
    
        private House house;

        public Builder(){
            reset();
        }

        public void reset(){
            house = new House();
            // default values
            house.floors = 1;
            house.garage = false;
        }

        public Builder withName(String name){
            house.name = name;
            return this;
        }

        public Builder withFloors(int floors){
            house.floors = floors;
            return this;
        }

        public Builder withGarage(boolean garage){
            house.garage = garage;
            return this;
        }

        public House build(){
            if(house.name == null){
                throw new IllegalStateException("House must have a name");
            }
            House product = house;
            reset();
            return product;
        }
    }

}
```

Usage:

```java
House villa = new House.Builder()
    .withName("Internal Builder Villa")
    .withFloors(2)
    .withGarage(true)
    .build();
```

👉 In Clprolf, even as an internal class, the `Builder` remains a **separate agent**, so roles are explicit.

---

## Conclusion

In Clprolf framework, the **Builder Pattern** becomes clearer:

* The product is usually an **`agent`**.
* The Builder is an **agent** that constructs it step by step.
* Fluent style makes the usage compact and natural.
* And the separation of roles is explicit: one class simulates the object, the other acts as the constructor agent.

✨ The Builder is not for every case, but when you need all construction possibilities, it gives you clarity and control — without cluttering your product objects.

---
