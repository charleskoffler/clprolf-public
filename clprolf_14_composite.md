# Composite Pattern in Clprolf — Example with Files and Folders

The **Composite pattern** solves a classic problem:
👉 *How to treat simple objects (“leaves”) and groups of objects (“composites”) in the same way?*

---

## In Traditional OOP

In OOP, we usually create:

* a **Component** interface,
* a **Leaf** class (e.g., a `File`) with no children,
* a **Composite** class (e.g., a `Folder`) that contains other components.

It works, but the design often feels implicit: we must *know* by convention that `File` is a leaf and `Folder` is a composite.

---

## In Clprolf

With **Clprolf**, roles and contracts make the Composite **explicit**:

* `FileSystemComponent` is the contract for all components.
* `File` is a **Leaf**: an `abstraction` with no extra methods.
* `Folder` is a **Composite**: an `abstraction` with `add()` / `remove()` methods for direct children.
* Implementations (`FileImpl`, `FolderImpl`) respect these roles with `contracts`.
* A `worker_agent` launcher builds the tree and calls `done()`.

The result: we can read the **roles and hierarchy** directly in the contracts — no ambiguity.

---

## Example

```java
public version_inh FileSystemComponent {
    void done();
}

public version_inh abstraction File nature FileSystemComponent { }

public  version_inh abstraction  Folder nature FileSystemComponent {
    void add(with_compat FileSystemComponent component);
    void remove(with_compat FileSystemComponent component);
}
```

### Leaf

```java
@Forced_pract_code
public abstraction FileImpl contracts File {
    private String name;

    public FileImpl(String name) { this.name = name; }

    public void done() { System.out.println("Leaf File: " + name); }
}
```

### Composite

```java
@Forced_pract_code
public abstraction FolderImpl contracts Folder {
    private String name;
    private List<FileSystemComponent> children = new ArrayList<>();

    public FolderImpl(String name) { this.name = name; }

    public void add(with_compat FileSystemComponent component) {
        children.add(component);
    }

    public void remove(with_compat FileSystemComponent component) {
        children.remove(component);
    }

    public void done() {
        System.out.println("Composite Folder: " + name);
        for (with_compat FileSystemComponent c : children) c.done();
    }
}
```

### Launcher

```java

public worker_agent CompositePatternLauncher {
    public static void main(String[] args) {
        FolderImpl root = new FolderImpl("Root");
        FolderImpl documents = new FolderImpl("Documents");
        FileImpl file1 = new FileImpl("File1.txt");

        documents.add(file1);
        root.add(documents);

        root.done();
    }
}
```

---

## Output

```
Composite Folder: Root
Composite Folder: Documents
Leaf File: File1.txt
```

---

## Why Clprolf Makes Composite Clear

1. **Contracts show everything**

   * `File` is a Leaf (no extra methods).
   * `Folder` is a Composite (can add/remove children).
   * Both are `FileSystemComponent`.

2. **Each composite is simple**
   A `Folder` only knows its **direct children**.
   The global hierarchy comes from recursion, not from extra complexity.

3. **Roles prevent ambiguity**
   Implementations respect exactly the declared role — no mix-up between leaf and composite.

---

## Conclusion

In OOP, the Composite is often taught as a trick with interfaces and polymorphism.
In **Clprolf**, it becomes obvious:

* A **Leaf** is an `abstraction` without children.
* A **Composite** is an `abstraction` with children of type `FileSystemComponent`.

👉 The pattern is no longer something to memorize, but something you can **read directly in the contracts**.

---