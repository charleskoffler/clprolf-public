# Factory Method in Clprolf: Clear Rules for Object Creation

## 🤔 The Problem

Sometimes you need to create objects,
but you don’t want the **client code** to depend on the **concrete classes**.

Example:

* A drawing application that can create different shapes (`Circle`, `Square`),
* The main code should not be hardcoded with `new Circle()` or `new Square()`.

The solution: define a **Factory**, a role dedicated to object creation.

---

## ✅ The Clprolf Solution

In Clprolf:

* The **product role** (`Shape`) is a `version_inh abstraction`.
* Specialized contracts (`Circle`, `Square`) extend the product role with `nature Shape`.
* Common implementation details go in a `ShapeImpl` base class.
* Concrete implementations (`CircleImpl`, `SquareImpl`) extend `ShapeImpl` and fulfill their contracts.
* The **Factory role** is a `compat_interf_version abstraction`, since it’s not a hierarchy of natures but a contract for creation.

Result: the intent (*this is a factory, not a random class*) is **explicit in the syntax**.

---

## 📝 Example: Shape Factory

We’ll model a `ShapeFactory` abstraction,
specialized versions for shapes (`Circle`, `Square`),
a shared implementation (`ShapeImpl`),
and concrete implementations (`CircleImpl`, `SquareImpl`).

Finally, a `DrawingAgent` uses them without knowing the details.

### Clprolf Code

```java
// 1) Generic product
public version_inh abstraction Shape {
    void setDisplayed(boolean value);
    boolean isDisplayed();

    String getColor();
    int getSize();
    void setColor(String c);
    void setSize(int s);
}

// 2) Specialized contracts (with nature Shape)
public version_inh abstraction Circle nature Shape {}
public version_inh abstraction Square nature Shape {}

// 3) Shared base implementation
public abstraction ShapeImpl contracts Shape {
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
public abstraction CircleImpl nature ShapeImpl contracts Circle {
    private with_compat CircleDrawerWorker drawer = new CircleDrawerWorker();

    @Override
    public void setDisplayed(boolean value) {
        super.setDisplayed(value);
        if (value) {
            drawer.drawCircle(color, size);
        }
    }
}

public abstraction SquareImpl nature ShapeImpl contracts Square {
    private with_compat SquareDrawerWorker drawer = new SquareDrawerWorker();

    @Override
    public void setDisplayed(boolean value) {
        super.setDisplayed(value);
        if (value) {
            drawer.drawSquare(color, size);
        }
    }
}

// 5) Workers for technical rendering
public worker_agent CircleDrawerWorker {
    public void drawCircle(String color, int size) {
        System.out.println("Drawing a " + color + " Circle of size " + size);
    }
}

public worker_agent SquareDrawerWorker {
    public void drawSquare(String color, int size) {
        System.out.println("Drawing a " + color + " Square of size " + size);
    }
}

// 6) Factory role (compatibility contract, not a hierarchy)
public compat_interf_version abstraction ShapeFactory {
    Shape createShape();
}

// 7) Concrete factories
public abstraction CircleFactory contracts ShapeFactory {
    public Shape createShape() {
        with_compat Circle circle = new CircleImpl();
        return circle; // Polymorphism: Circle → Shape
    }
}

public abstraction SquareFactory contracts ShapeFactory {
    public Shape createShape() {
        with_compat Square square = new SquareImpl();
        return square; // Polymorphism: Square → Shape
    }
}

// 8) Client agent
public agent DrawingAgent {
    private with_compat ShapeFactory factory;

    public DrawingAgent(with_compat ShapeFactory factory) {
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
public worker_agent FactoryDemo {
    public static void main(String[] args) {
        with_compat ShapeFactory circleFactory = new CircleFactory();
        with_compat ShapeFactory squareFactory = new SquareFactory();

        DrawingAgent drawer1 = new DrawingAgent(circleFactory);
        DrawingAgent drawer2 = new DrawingAgent(squareFactory);

        drawer1.render(); // Drawing a red Circle of size 5
        drawer2.render(); // Drawing a red Square of size 5
    }
}
```

---

## 🔎 Why this is clear in Clprolf

* `version_inh abstraction Shape` → defines the **nature of product**.
* `Circle` and `Square` → specialized versions (`nature Shape`).
* `ShapeImpl` → factors common attributes and logic.
* `CircleImpl`, `SquareImpl` → extend the base, add specific workers.
* `compat_interf_version abstraction ShapeFactory` → declares a **contract for creation**, not a nature hierarchy.
* Workers → handle the technical rendering.
* The **client agent** → depends only on the factory contract.

---

## 🎯 Key takeaway

In Clprolf, the Factory Method pattern is just:

> **Use `version_inh` for product natures, factor common logic in a base implementation, and `compat_interf_version` for the factory role.**

This keeps the distinction clear:

* **Natures** define what the object *is*,
* **Factories** define how the object *is created*,
* **Base implementations** capture what is common.

---

## 💡 Innovation Note: Patterns Reveal Roles

Design patterns are more than technical tricks — they carry **implicit roles**.

* Adapter → an agent that adapts,
* Strategy → an abstraction of interchangeable rules,
* Observer → an agent that reacts,
* Factory → an abstraction of creation.

👉 In Clprolf, these roles are not hidden — they are **explicit keywords** (`agent`, `abstraction`, `worker_agent`, `version_inh`, `compat_interf_version`).
That’s why patterns feel simpler and more natural here.

---

✨ Familiar use case, clear Clprolf solution, and proof that roles make design patterns self-explaining.

---