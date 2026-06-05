# 🗂️ Clprolf Framework Directory Explorer

Everyone knows that exploring directories can quickly turn into a messy technical exercise:
loops, recursion, stacks, file filters… and code that loses all readability.

With **Clprolf framework**, clarity is built-in.
Let’s see how a simple *directory explorer* can become a beautifully structured program.

---

### 💡 1. The Concept

We want to:

* explore all subdirectories of a given folder,
* in **breadth-first** order (level by level),
* assign each directory a hierarchical ID,
* and display the results cleanly.

Instead of mixing logic, display, and data handling,
Clprolf invites us to split them into **roles**.

| Component                                       | Declension              | Responsibility                         |
| ----------------------------------------------- | ----------------------- | -------------------------------------- |
| `Launcher`                                      | `@ClWorker`             | Starts the exploration                 |
| `DirectoryExplorerImpl`                         | `@ClAgent`              | Performs the exploration               |
| `DirectoryExplorerWorkerImpl`                   | `@ClWorker`             | Displays results                       |
| `Directory`                                     | `@ClAgent`              | Represents one directory node          |
| `DirectoryExplorer`                             | `@ClAgent @ClFamily`    | The family of the agent                |
| `DirectoryExplorerWorker`                       | `@ClWorker @ClFamily`   | The family of the worker for the agent |
---

### ⚙️ 2. The Launcher

The entry point is as simple as it looks.
It prepares the environment and delegates the job to the proper agent.

```java
@ClWorker
public class Launcher {

    public static void main(String[] args) {
        Path path = Paths.get(args.length > 0 ? args[0] : System.getProperty("user.home"));
        try { path = path.toRealPath(LinkOption.NOFOLLOW_LINKS); } catch (Exception ignored) {}

        if (!Files.isDirectory(path)) {
            System.err.println("Not a directory: " + path);
            System.exit(1);
        }

        DirectoryExplorer explorer = new DirectoryExplorerImpl();
        explorer.breadthFirstFolders(path);
    }
}
```

It’s clear who does what:
this worker doesn’t explore — it simply *launches the agent*.

---

### 🧭 3. The Agent — `DirectoryExplorerImpl`

Here lies the real exploration logic.
The agent collaborates with a worker, manipulates a model, and manages a queue.

```java
@ClAgent
public class DirectoryExplorerImpl implements DirectoryExplorer {

    private DirectoryExplorerWorker worker;

    public DirectoryExplorerImpl() {
        this.worker = new DirectoryExplorerWorkerImpl();
    }

    public void breadthFirstFolders(Path directoryPath) {
        directoryPath = directoryPath.normalize().toAbsolutePath();

        List<Directory> foldersList = new ArrayList<>();
        Queue<Directory> directoryToExplore = new LinkedList<>();

        directoryToExplore.add(new Directory(directoryPath, List.of(0)));

        while (!directoryToExplore.isEmpty()) {
            Directory current = directoryToExplore.poll();
            foldersList.add(current);

            File[] files = current.getPath().toFile().listFiles();
            if (files != null) {
                int index = 0;
                for (File file : files) {
                    if (file.isDirectory()) {
                        List<Integer> newId = new ArrayList<>(current.getHierarchicalId());
                        newId.add(index);
                        directoryToExplore.add(new Directory(file.toPath().normalize().toAbsolutePath(), newId));
                        index++;
                    }
                }
            }
        }

        worker.displayResult(foldersList);
    }
}
```

Every part is crystal clear:

* The queue defines a **breadth-first traversal**.
* Each directory receives its own **hierarchical ID**.
* The **worker** takes care of presentation.

No recursion, no confusion.

---

### 👷 4. The Worker

Responsible for showing the result, not for computing it.
Again, we separate *doing* from *showing*.

```java
@ClWorker
public class DirectoryExplorerWorkerImpl implements DirectoryExplorerWorker {

    public void displayResult(List<Directory> foldersList) {
        for (Directory dir : foldersList) {
            String display = formatId(dir.getHierarchicalId()) + " : " + dir.getPath();
            System.out.println(display);
        }
    }

    private String formatId(List<Integer> id) {
        return "(" + String.join(", ", id.stream().map(String::valueOf).toArray(String[]::new)) + ")";
    }
}
```

Simple, explicit, human-readable.

---

### 📦 5. The Model

```java
@ClAgent
public class Directory {
    private Path path;
    private List<Integer> hierarchicalId;

    public Directory(Path path, List<Integer> id) {
        this.path = path;
        this.hierarchicalId = id;
    }

    public Path getPath() { return path; }
    public List<Integer> getHierarchicalId() { return hierarchicalId; }
}
```

---

### 🤝 6. The interfaces

```java
@ClAgent
@ClFamily
public interface DirectoryExplorer {
    void breadthFirstFolders(Path directoryPath);
}

@ClWorker
@ClFamily
public interface DirectoryExplorerWorker {
    void displayResult(List<Directory> foldersList);
}
```

---

### 🪶 7. Why it matters

Breadth-first exploration is only the example.
What matters here is **how naturally the architecture expresses itself**:

* The launcher launches.
* The agent explores.
* The worker displays.

---

### ✨ Final Thoughts

> In classical Java, you might have written a single class doing everything.
>
> In Clprolf, each role finds its natural place.
> The result is simple, explicit, and readable — even for someone who never wrote Java before.

Clprolf brings **clarity back into architecture**.

---