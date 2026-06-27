# 🐍 Revisiting Snake in Java with Clprolf Framework

What if writing a small game could prove that architecture can be both **clean** and **alive**?
Let’s revisit the classic **Snake** game — but built with **Clprolf framework**.

---

### 🧠 1. From OOP to Clarity-Oriented Programming

Clprolf is a framework that builds **architectural meaning** into Java itself.

| Annotation      | Role                                              |
| --------------- | ------------------------------------------------- |
| `@ClAgent`        | Domain logic                                      |
| `@ClWorker`       | Technical performer (I/O, UI, OS, rendering)      |

When you read Clprolf code, you see **intent**, not just syntax.

---

### ⚙️ 2. The Architecture of the Snake Game

The game has five layers — all explicit:

```
SnakeGameScene (ClAgent)
 ├── SnakeImpl (ClAgent)
 ├── FoodExpertImpl (ClAgent)
 ├── SnakeGameSceneRendererImpl (ClWorker)
 ├── SnakeWindowImpl (ClSystem)(Could have been ClAgent)
 └── SnakeGamePanelImpl (ClSystem, Swing-based UI component)(Could have been ClAgent)
```

Each one knows **exactly what it should know**, and **nothing more**.

For instance, the `FoodExpert` agent handles food positions —
but knows nothing about the UI, keyboard, or rendering:

```java
@ClAgent
public class FoodExpertImpl implements FoodExpert {
    private SnakeGameScene scene;

    public void positionFood() {
        Random random = new Random();
        ...
        food.setType(random.nextBoolean() ? FoodType.APPLE : FoodType.ORANGE);
        foodList.add(food);
    }
}
```

Meanwhile, the **Worker** handles technical events and visual updates:

```java
@ClWorker
public class SnakeGameSceneRendererImpl implements SnakeGameSceneRenderer {
    private SnakeGameScene scene;

    public SnakeGameSceneRendererImpl( SnakeGameScene scene) {
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
protected void continueSliding() {
    if (this.lastSlidingType == SlidingType.STOPPED) return;

    SnakeLink newHeadLink = computeHeadLink(this.links.get(0));
    checkCollisionsForNewHead(newHeadLink);

    Food foodAtNewHeadPlace = this.scene.getFoodExpert().getFoodAt(newHeadLink.x, newHeadLink.y);
    updateBodyForSliding(foodAtNewHeadPlace != null, newHeadLink);
}
```

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
without custom game threads or complex scheduling.

---

### 💡 5. What We Learn from This Example

* You can build a **complete game architecture** without losing clarity.
* **Workers** act as true performers, keeping the domain clean.
* The **render loop** stays synchronous, transparent, and elegant.

> The result? A Snake that’s both fun and architecturally explicit.

---

### 🏁 Conclusion

We did not just implement Snake. We made its responsibilities visible: the scene owns the game state, agents express domain behavior, and workers perform technical execution.

---
