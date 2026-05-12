# 🐍 Revisiting Snake in Java with Clprolf — From Clear Code to Clear Game

What if writing a small game could prove that architecture can be both **clean** and **alive**?
Let’s revisit the classic **Snake** game — but built with **Clprolf**,
a paradigm that turns *clarity* into a coding language.

---

### 🧠 1. From OOP to Clarity-Oriented Programming

Clprolf isn’t a framework. It’s a **language layer + methodology**
that builds **architectural meaning** into Java itself.

| Annotation      | Role                                              |
| --------------- | ------------------------------------------------- |
| `@Agent`        | Domain logic — active and autonomous components   |
| `@Worker_agent` | Technical performer (I/O, UI, OS, rendering)      |
| `@Abstraction`  | Conceptual contract or system-level interface     |
| `@Model`        | Passive structure, pure data                      |
| `@Underst`      | A “thinking” method — where reasoning matters     |
| `@Long_action`  | A time-based or continuous process                |
| `@With_compat`  | Declares a safe compatibility link between agents |

When you read Clprolf code, you see **intent**, not just syntax.

---

### ⚙️ 2. The Architecture of the Snake Game

The game has five layers — all explicit:

```
SnakeGameScene (Abstraction)
 ├── SnakeImpl (Agent)
 ├── FoodExpertImpl (Agent)
 ├── SnakeGameSceneRendererImpl (Worker_agent)
 ├── SnakeWindowImpl (Abstraction)
 └── SnakeGamePanelImpl (Abstraction + Swing Nature)
```

Each one knows **exactly what it should know**, and **nothing more**.

For instance, the `FoodExpert` agent handles food positions —
but knows nothing about the UI, keyboard, or rendering:

```java
@Agent(Gender.EXPERT_COMPONENT)
public class FoodExpertImpl implements @Contracts FoodExpert {
    private @With_compat SnakeGameScene scene;

    public void positionFood() {
        Random random = new Random();
        ...
        food.setType(random.nextBoolean() ? FoodType.APPLE : FoodType.ORANGE);
        foodList.add(food);
    }
}
```

Meanwhile, the **Worker Agent** handles technical events and visual updates:

```java
@Worker_agent
public class SnakeGameSceneRendererImpl implements @Contracts SnakeGameSceneRenderer {
    private @With_compat SnakeGameScene scene;

    public SnakeGameSceneRendererImpl(@With_compat SnakeGameScene scene) {
        this.scene = scene;
        EventQueue.invokeLater(this); // Executed in AWT thread
    }

    @Override
    public void run() {
        window = new SnakeWindowImpl(this); // creates and starts rendering
    }
}
```

---

### 🔁 3. Understanding the Flow of Life — The Snake Logic

The magic happens in `SnakeImpl`.
It shows how **Clprolf models living behavior**:
sliding, growing, interacting — without spaghetti code.

```java
@Underst
@Long_action
protected void continueSliding() {
    if (this.lastSlidingType == SlidingType.STOPPED) return;

    SnakeLink newHeadLink = computeHeadLink(this.links.get(0));
    checkCollisionsForNewHead(newHeadLink);

    Food foodAtNewHeadPlace = this.scene.getFoodExpert().getFoodAt(newHeadLink.x, newHeadLink.y);
    updateBodyForSliding(foodAtNewHeadPlace != null, newHeadLink);
}
```

The `@Underst` annotation reminds that the logic involves **reasoning** —
not just a trivial mechanical step.
Each call represents an *intention* (“continue sliding”, “handle food”, “grow body”).
The structure itself communicates **meaning**.

---

### 🎨 4. Rendering Without Losing the Model

The UI never controls the game directly.
The window only refreshes every 20 ms —
and triggers the next logic step **inside the paint cycle**.

```java
@Override
public void paintComponent(Graphics g) {
    super.paintComponent(g);
    drawSnakes(g);
    drawFood(g);

    // End of long actions — one physics step every few frames
    this.gameWindow.getReal().getScene().getSnake().endLongActions();
    this.gameWindow.getReal().getScene().getSnake_two().endLongActions();
}
```

So the **render loop and game loop are naturally synchronized** —
without timers, threads, or complicated scheduling.

---

### 🪢 5. Loose Coupling by Design — Not by Effort

With `@With_compat`, compatibility is both explicit and enforced:

```java
public class SnakeImpl implements @Contracts Snake {
    protected @With_compat SnakeGameScene scene;
}
```

If a component tries to depend on something it shouldn’t —
Clprolf makes it visible, conceptually and syntactically.
You see the architecture *as you read the code.*

---

### 💡 6. What We Learn from This Example

* You can build a **complete game architecture** without losing clarity.
* **Long actions** and **reasoning methods** bring life-like modeling.
* **Workers** act as true performers, keeping the domain clean.
* **Compatibility links** replace DI frameworks and reflection.
* The **render loop** stays synchronous, transparent, and elegant.

> The result? A Snake that’s both fun and architecturally flawless.

---

### 🏁 Conclusion

Clprolf isn’t about writing fancy syntax.
It’s about making **clarity a property of the codebase itself.**

From **clear code** to **clear game**,
we didn’t just rewrite Snake — we **understood it**.

---
