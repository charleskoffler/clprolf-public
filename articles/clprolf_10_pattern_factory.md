# Factory Method in Clprolf Framework: Clear Rules for Object Creation

## 🤔 The Problem

Sometimes you need to create objects,
but you don’t want the **client code** to depend on the **concrete classes**.

Example:

* A drawing application that can create different shapes (`Circle`, `Square`),
* The main code should not be hardcoded with `new Circle()` or `new Square()`.

The solution: define a **Factory**, a role dedicated to object creation.

---

## ✅ The Clprolf Solution

In Clprolf Framework:

* The **product role** (`Shape`) is a `family agent`.
* Specialized families (`Circle`, `Square`) extend the product role with `extends Shape`.
* Common implementation details go in a `ShapeImpl` base class.
* Concrete implementations (`CircleImpl`, `SquareImpl`) extend `ShapeImpl` and fulfill their family contracts.
* The **Factory role** is a `family agent`, since it’s a contract for creation.

Result: the intent (*this is a factory, not a random class*) is **explicit in the syntax**.

---

## 📝 Example: Shape Factory

We’ll model a `ShapeFactory` agent,
specialized versions for shapes (`Circle`, `Square`),
a shared implementation (`ShapeImpl`),
and concrete implementations (`CircleImpl`, `SquareImpl`).

Finally, a `DrawingAgent` uses them without knowing the details.

### Clprolf Code

```java
// 1) Generic product
@ClAgent
@ClFamily
public interface Shape {
    void setDisplayed(boolean value);
    boolean isDisplayed();

    String getColor();
    int getSize();
    void setColor(String c);
    void setSize(int s);
}

// 2) Specialized families (extending Shape)
public @ClFamily @ClAgent Circle extends Shape {}
public @ClFamily @ClAgent Square extends Shape {}

// 3) Shared base implementation
@ClAgent
public class ShapeImpl implements Shape {
    protected boolean displayed = false;
    protected String color = "black";
    protected int size = 1;

    public void setDisplayed(boolean value) {
        this.displayed = value;
    }

    public boolean isDisplayed() { return displayed; }
    public String getColor() { return color; }
    public int getSize() { return size; }
    public void setColor(String c) { this.color = c; }
    public void setSize(int s) { this.size = s; }
}

// 4) Implementations with workers
@ClAgent
public class CircleImpl extends ShapeImpl implements Circle {
    private CircleDrawerWorker drawer = new CircleDrawerWorker();

    @Override
    public void setDisplayed(boolean value) {
        super.setDisplayed(value);
        if (value) {
            drawer.drawCircle(color, size);
        }
    }
}

@ClAgent
public class SquareImpl extends ShapeImpl implements Square {
    private SquareDrawerWorker drawer = new SquareDrawerWorker();

    @Override
    public void setDisplayed(boolean value) {
        super.setDisplayed(value);
        if (value) {
            drawer.drawSquare(color, size);
        }
    }
}

// 5) Workers for technical rendering

@ClWorker
public class CircleDrawerWorker {
    public void drawCircle(String color, int size) {
        System.out.println("Drawing a " + color + " Circle of size " + size);
    }
}

@ClWorker
public class SquareDrawerWorker {
    public void drawSquare(String color, int size) {
        System.out.println("Drawing a " + color + " Square of size " + size);
    }
}

// 6) Factory role (compatibility contract, not a hierarchy)

@ClAgent
@ClFamily
public interface ShapeFactory {
    Shape createShape();
}

// 7) Concrete factories

@ClAgent
public class CircleFactory implements ShapeFactory {
    public Shape createShape() {
        Circle circle = new CircleImpl();
        return circle; // Polymorphism: Circle → Shape
    }
}

@ClAgent
public class SquareFactory implements ShapeFactory {
    public Shape createShape() {
        Square square = new SquareImpl();
        return square; // Polymorphism: Square → Shape
    }
}

// 8) Client agent

@ClAgent
public class DrawingAgent {
    private ShapeFactory factory;

    public DrawingAgent(ShapeFactory factory) {
        this.factory = factory;
    }

    public void render() {
        Shape shape = factory.createShape();
        shape.setColor("red");
        shape.setSize(5);
        shape.setDisplayed(true);
    }
}
```

---

## 👀 Bonus: Demo

```java

@ClWorker
public class FactoryDemo {
    public static void main(String[] args) {
        ShapeFactory circleFactory = new CircleFactory();
        ShapeFactory squareFactory = new SquareFactory();

        DrawingAgent drawer1 = new DrawingAgent(circleFactory);
        DrawingAgent drawer2 = new DrawingAgent(squareFactory);

        drawer1.render(); // Drawing a red Circle of size 5
        drawer2.render(); // Drawing a red Square of size 5
    }
}
```

---

## 🔎 Why this is clear in Clprolf

* `family agent Shape` → defines the **kind of product**.
* `Circle` and `Square` → specialized versions (`extends Shape`).
* `ShapeImpl` → factors common attributes and logic.
* `CircleImpl`, `SquareImpl` → extend the base, add specific workers.
* `family_interf agent ShapeFactory` → declares a **contract for creation**.
* Workers → handle the technical rendering.
* The **client agent** → depends only on the factory contract.

---

## 🎯 Key takeaway

In Clprolf framework, the Factory Method pattern is just:

> **Use `@ClFamily` for product, factor common logic in a base implementation, and `@ClFamily` for the factory role.**

This keeps the distinction clear:

* **Factories** define how the object *is created*,
* **Base implementations** capture what is common.

---

## 💡 Innovation Note: Patterns Reveal Roles

Design patterns are more than technical tricks — they carry **implicit roles**.

* Adapter → an agent that adapts,
* Strategy → an agent of interchangeable rules,
* Observer → an agent that reacts,
* Factory → an agent of creation.

👉 In Clprolf, these roles are not hidden — they are **explicit roles and interface types** (`agent`, `worker`, `family`, `trait`).
That’s why patterns feel simpler and more natural here.

---

✨ Familiar use case, clear Clprolf solution, and proof that roles make design patterns self-explaining.

---