# 🗂️ Clprolf Directory Explorer — When Breadth-First Becomes Intuitive

Everyone knows that exploring directories can quickly turn into a messy technical exercise:
loops, recursion, stacks, file filters… and code that loses all readability.

With **Clprolf**, clarity is built-in.
You don’t just write *code* — you design **agents**, **workers**, and **models** that reflect what really happens.
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

| Component                                       | Declension      | Responsibility                 |
| ----------------------------------------------- | --------------- | ------------------------------ |
| `Launcher`                                      | `@Worker_agent` | Starts the exploration         |
| `DirectoryExplorerImpl`                         | `@Agent`        | Performs the exploration       |
| `DirectoryExplorerWorkerImpl`                   | `@Worker_agent` | Displays results               |
| `Directory`                                     | `@Model`        | Represents one directory node  |
| `DirectoryExplorer` / `DirectoryExplorerWorker` | `@Version_inh`  | Define contracts between roles |

---

### ⚙️ 2. The Launcher

The entry point is as simple as it looks.
It prepares the environment and delegates the job to the proper agent.

```java
@Worker_agent
public class Launcher {

    public static void main(String[] args) {
        @With_compat Path path = Paths.get(args.length > 0 ? args[0] : System.getProperty("user.home"));
        try { path = path.toRealPath(LinkOption.NOFOLLOW_LINKS); } catch (Exception ignored) {}

        if (!Files.isDirectory(path)) {
            System.err.println("Not a directory: " + path);
            System.exit(1);
        }

        @With_compat DirectoryExplorer explorer = new DirectoryExplorerImpl();
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
@Agent
public class DirectoryExplorerImpl implements @Contracts DirectoryExplorer {

    private @With_compat DirectoryExplorerWorker worker;

    public DirectoryExplorerImpl() {
        this.worker = new DirectoryExplorerWorkerImpl();
    }

    public void breadthFirstFolders(@With_compat Path directoryPath) {
        directoryPath = directoryPath.normalize().toAbsolutePath();

        @With_compat List<Directory> foldersList = new ArrayList<>();
        @With_compat Queue<Directory> directoryToExplore = new LinkedList<>();

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

### 👷 4. The Worker Agent

Responsible for showing the result, not for computing it.
Again, we separate *doing* from *showing*.

```java
@Worker_agent
public class DirectoryExplorerWorkerImpl implements @Contracts DirectoryExplorerWorker {

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

A `@Model` in Clprolf is always clear:
it represents data, and nothing more.

```java
@Model
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

No logic, no side effects. Just structure.

---

### 🤝 6. The Contracts

```java
@Agent
@Version_inh
public interface DirectoryExplorer {
    void breadthFirstFolders(Path directoryPath);
}

@Worker_agent
@Version_inh
public interface DirectoryExplorerWorker {
    void displayResult(List<Directory> foldersList);
}
```

Contracts make the collaboration explicit.
No hidden dependencies, no tight coupling — just clear communication.

---

### 🪶 7. Why it matters

Breadth-first exploration is only the example.
What matters here is **how naturally the architecture expresses itself**:

* The launcher launches.
* The agent explores.
* The worker displays.
* The model represents.
* The contract binds.

Clprolf doesn’t just help you code — it helps you **think**.
The structure emerges from the intention.

---

### ✨ Final Thoughts

> In classical Java, you might have written a single class doing everything.
>
> In Clprolf, each role finds its natural place.
> The result is simple, explicit, and readable — even for someone who never wrote Java before.

Clprolf brings **clarity back into architecture**,
and even the smallest utilities become examples of well-designed software.

---