# Proxy Pattern in Clprolf — Example with an Image

In traditional OOP, the **Proxy** is used to control access to another object. The proxy has the same interface as the real subject and delegates calls, while possibly adding **lazy loading, security checks, caching, or network indirection**.

In **Clprolf**, the Proxy becomes clearer and safer:

* Both the proxy and the real subject are **siblings in the same abstraction family** (`version_inh`).
* Technical work is delegated to a **`worker_agent`**, so business and technical responsibilities remain separated.
* `with_compat` enforces loose coupling.

---

## Example: Lazy Loading an Image

Imagine we want to work with a very large image file. Loading it into memory is expensive, so we want to delay that cost until the moment we actually need to display the image.

---

### The Version (Abstraction)

```java
public version_inh abstraction IImage {
    void show();
}
```

---

### The Real Subject (Abstraction)

```java
public abstraction BigImage contracts IImage {
    private WorkerImageHandler handler;
    private String displayMode; // business attribute: "fullscreen", "thumbnail", etc.

    public BigImage(String filename, String displayMode){
        this.handler = new WorkerImageHandler(filename); 
        this.displayMode = displayMode;
        handler.loadFromDisk();
    }

    public void show(){
        handler.display(displayMode); // delegates to the worker
    }
}
```

---

### The Worker Agent (Technical Responsibility)

```java
public worker_agent WorkerImageHandler {
    private String filename;

    public WorkerImageHandler(String filename){
        this.filename = filename;
    }

    public void loadFromDisk(){
        System.out.println("Loading " + filename + " from disk...");
    }

    public void display(String mode){
        System.out.println("Displaying " + filename + " in mode: " + mode);
    }
}
```

---

### The Proxy (Sibling Abstraction)

```java
public abstraction ProxyImage contracts IImage {
    private BigImage realImage;
    private String filename;
    private String displayMode;

    public ProxyImage(String filename, String displayMode){
        this.filename = filename;
        this.displayMode = displayMode;
    }

    public void show(){
        if(realImage == null){
            realImage = new BigImage(filename, displayMode); // lazy instantiation
        }
        realImage.show();
    }
}
```

---

### The Launcher

```java
public worker_agent Launcher {
    public static void main(String[] args){
        with_compat IImage image = new ProxyImage("big_photo.jpg", "fullscreen");

        System.out.println("Proxy created, but image not loaded yet.");
        image.show();  // only here, BigImage + WorkerImageHandler are instantiated
    }
}
```

---

## Console Output

```
Proxy created, but image not loaded yet.
Loading big_photo.jpg from disk...
Displaying big_photo.jpg in mode: fullscreen
```

---

## Why Proxy Is Clearer in Clprolf

* `IImage` → **`version_inh abstraction`**, defines the family of Image abstractions.
* `BigImage` → **abstraction**, a concrete member of that family.
* `ProxyImage` → **abstraction**, another member of the same family, acting as a clone with a different behavior (lazy loading).
* `WorkerImageHandler` → **worker_agent**, handling the technical responsibility of file loading and display.

In traditional OOP, `Image` often mixes both business and technical code. In Clprolf, the split is natural, and the Proxy fits in seamlessly.

---

## Inheritance vs Proxy

One might ask: *“Why not subclass `BigImage`?”*

The difference is crucial:

| **Inheritance (`nature`)**                                                 | **Proxy (`version_inh abstraction`)**                                 |
| -------------------------------------------------------------------------- | --------------------------------------------------------------------- |
| Creates a **child class**                                                  | Creates another **sibling in the same family**                        |
| Parent’s constructor is **always called** → heavy load happens immediately | The real subject is created **only when needed**                      |
| Used to **specialize business logic**                                      | Used to **control access** or **defer creation**                      |
| Example: `CountingChatGPT nature ChatGPT` (adds business rules)            | Example: `ProxyImage` (adds lazy loading, caching, or access control) |

👉 With inheritance, the parent *lives inside you*: its constructor always runs.
👉 With a proxy, it’s like a *clone of the same abstraction*: same family, but free to change behavior.

---

## Why `version_inh` matters for Proxy in Clprolf

In traditional OOP, the Proxy is described as “an object with the same interface as another.”
In Clprolf, this intent becomes **visible in the code** because we use **`version_inh`** to define the family of abstractions.

* The real subject (`BigImage`) and the proxy (`ProxyImage`) are **siblings** in the same family.
* They are not subclasses of each other, but two different implementations of the same abstraction.
* This expresses exactly what a Proxy is: **a clone of the abstraction, rectifying its behavior** (by delaying creation, controlling access, or adding a cache).

With Clprolf, we also see immediately if the proxy’s rectification is:

* **business logic** → then inheritance with `nature` might be more appropriate,
* **technical access or optimization** → then a Proxy sibling is the right solution.

---

## Two Faces of Proxy in Clprolf

In Clprolf, the Proxy can be understood in two complementary ways:

| **Type of Proxy**   | **What it rectifies**                            | **Example in Clprolf**                                                                                                                 | **Level**             |
| ------------------- | ------------------------------------------------ | -------------------------------------------------------------------------------------------------------------------------------------- | --------------------- |
| **Technical Proxy** | How and when **workers** are called              | Lazy loading (shift worker call), Cache (add memory worker), Logging (add logging worker), Security check (extra worker before access) | **Workers**           |
| **Business Proxy**  | How the **abstraction** is used (domain control) | Limit number of calls (`max 3 displays`), License check before use, Restrict access by user role                                       | **Abstraction usage** |

✨ Clprolf makes this distinction explicit:

* Both `BigImage` and `ProxyImage` are **siblings in the same abstraction family**.
* But you can immediately see whether the Proxy rectifies the **technical side** (workers) or the **business side** (usage control).

---

## Variation: Shifting the Load Logic

In our first version, `BigImage` loads the file immediately in its constructor.
But we could also decide that the **real subject itself** only loads at display time:

```java
public abstraction BigImage contracts IImage {
    private WorkerImageHandler handler;
    private String displayMode;

    public BigImage(String filename, String displayMode){
        this.handler = new WorkerImageHandler(filename); 
        this.displayMode = displayMode;
    }

    public void show(){
        handler.loadFromDisk();           // load only at show time
        handler.display(displayMode);     // delegates to the worker
    }
}
```

This makes the distinction even clearer:

* **`BigImage`** always does the full job (load + display).
* **`ProxyImage`** rectifies the timing: it creates the `BigImage` only when needed, delaying both the construction and the worker call.

---

## Conclusion

The **Proxy Pattern in Clprolf** becomes simpler and more consistent:

* Proxy and real subject are **siblings in the same abstraction family** (`version_inh abstraction`).
* Technical details are isolated in a **worker_agent**.
* The Proxy acts as a **clone of the abstraction**, rectifying what it wants — whether by shifting when workers are called (*lazy loading*), or by adding workers for logging, caching, or security.

With Clprolf, we also see that Proxy has **two faces**:

* a **technical proxy**, which rectifies how workers are used,
* or a **business proxy**, which rectifies how the abstraction itself is used (domain rules, usage control).

Even subtle variations, like moving the worker call from the constructor to the `show()` method, are made explicit.
The intent is no longer hidden — it’s visible in the grammar.

✨ In the end, Clprolf doesn’t reinvent design patterns.
It **clarifies them**, makes their business and technical intent **explicit**, and prevents them from turning into blind dogma.

---