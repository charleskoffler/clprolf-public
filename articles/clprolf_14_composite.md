# Composite Pattern in Clprolf framework — Example with Files and Folders

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

## In Clprolf framework

With **Clprolf**, roles and interfaces make the Composite **explicit**:

* `FileSystemComponent` is the family of all components.
* `File` is a **Leaf**: an `agent` with no extra methods.
* `Folder` is a **Composite**: an `agent` with `add()` / `remove()` methods for direct children.
* Implementations (`FileImpl`, `FolderImpl`) respect these roles when implementing.
* A `worker` launcher builds the tree and calls `done()`.

The result: we can read the **roles and hierarchy** directly in the interfaces — no ambiguity.

---

## Example

```java
@ClAgent
@ClFamily
public interface FileSystemComponent {
    void done();
}

@ClAgent
@ClFamily
public interface File extends FileSystemComponent { }

@ClAgent
@ClFamily
public interface Folder extends FileSystemComponent {
    void add(FileSystemComponent component);
    void remove(FileSystemComponent component);
}
```

### Leaf

```java
@ClAgent
public class FileImpl implements File {
    private String name;

    public FileImpl(String name) { this.name = name; }

    public void done() { System.out.println("Leaf File: " + name); }
}
```

### Composite

```java
@ClAgent
public class FolderImpl implements Folder {
    private String name;
    private List<FileSystemComponent> children = new ArrayList<>();

    public FolderImpl(String name) { this.name = name; }

    public void add(FileSystemComponent component) {
        children.add(component);
    }

    public void remove(FileSystemComponent component) {
        children.remove(component);
    }

    public void done() {
        System.out.println("Composite Folder: " + name);
        for ( FileSystemComponent c : children) c.done();
    }
}
```

### Launcher

```java
@ClWorker
public class CompositePatternLauncher {
    public static void main(String[] args) {
        Folder root = new FolderImpl("Root");
        Folder documents = new FolderImpl("Documents");
        File file1 = new FileImpl("File1.txt");

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

1. **Families show everything**

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
In **Clprolf  framework**, it becomes obvious:

* A **Leaf** is an `agent` without children.
* A **Composite** is an `agent` with children of type `FileSystemComponent`.

👉 The pattern is no longer something to memorize, but something you can **read directly in the interfaces**.

---