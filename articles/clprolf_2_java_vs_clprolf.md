# Clprolf Docs #2 — QuickSort: Thinking in Agents and Workers

One of the easiest ways to understand Clprolf is to compare a simple program written in traditional Java and with the Clprolf framework.
The goal is not to change the algorithm.

The goal is to change how responsibilities are expressed.

Both examples below produce exactly the same result.

What changes is the architectural interpretation.

---

## The Problem

In traditional object-oriented programming, it is common to place multiple responsibilities inside a single class.

A sorting class may:

* implement the sorting algorithm,
* print results,
* launch the application,
* manage technical details.

For small programs, this is often acceptable.

As systems grow, however, responsibilities tend to accumulate and architectural intent becomes harder to identify.

---

## Traditional Java Version

```java
public class QuickSort {

    public void sort(int[] array) {
        quickSort(array, 0, array.length - 1);
    }

    private void quickSort(int[] array, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(array, low, high);
            quickSort(array, low, pivotIndex - 1);
            quickSort(array, pivotIndex + 1, high);
        }
    }

    private int partition(int[] array, int low, int high) {
        ...
    }

    public void printArray(int[] array) {
        ...
    }

    public static void main(String[] args) {
        ...
    }
}
```

This class contains several concerns:

* the QuickSort algorithm,
* output formatting,
* application startup.

The code works perfectly.

However, the different responsibilities remain mixed together.

---

## Identifying the Domain

Before writing Clprolf framework code, we ask a simple question:

> What is the main subject of this class?

In this example, the answer is:

```text
QuickSort
```

The class represents a sorting algorithm.

It has a clear domain.

Therefore, it naturally becomes an `agent`.

---

## Clprolf Version

### The Agent

```java
@ClAgent
public class QuickSort {

    private QuickSortSupport worker;

    public QuickSort(QuickSortSupport worker) {
        this.worker = worker;
    }

    public void sort(int[] array) {

        worker.printArray("Unsorted array:", array);

        quickSort(array, 0, array.length - 1);

        worker.printArray("Sorted array:", array);
    }

    private void quickSort(int[] array, int low, int high) {
        ...
    }

    private int partition(int[] array, int low, int high) {
        ...
    }
}
```

The `QuickSort` class represents the sorting domain.

Its identity comes from the algorithm it implements.

It is therefore modeled as an `agent`.

---

### The Worker

```java
@ClWorker
public class QuickSortSupport {

    public void printArray(String message, int[] array) {

        System.out.println(message);

        for (int value : array) {
            System.out.print(value + " ");
        }

        System.out.println();
    }
}
```

QuickSortSupport does not represent the main subject of the system.

Its purpose is to provide a technical service to the agent.

It is therefore modeled as a `worker`.

---

### The Launcher

```java
@ClWorker
public class Launcher {

    public static void main(String[] args) {

        QuickSortSupport worker = new QuickSortSupport();

        QuickSort sorter = new QuickSort(worker);

        int[] array = {34, 7, 23, 32, 5, 62, 32, 6};

        sorter.sort(array);
    }
}
```

The launcher exists solely to start the program.

It does not represent a domain.

Its role is technical support.

It is therefore naturally a `worker`.

---

## Execution Result

```
Unsorted array:
34 7 23 32 5 62 32 6

Sorted array:
5 6 7 23 32 32 34 62
```

The result is exactly the same as in the Java version.

Only the architectural expression changes.

---

## What Changed?

The algorithm did not change.

The runtime behavior did not change.

The execution result did not change.

What changed is the visibility of responsibilities.

```text
QuickSort          → agent
QuickSortSupport   → worker
Launcher           → worker
```

Each component now explicitly declares its role.

---

## Why QuickSort Is an Agent

Some developers initially associate agents with business concepts.

In Clprolf framework, the notion is broader.

An agent is any class organized around a coherent domain.

Examples include:

```text
Animal
File
Connection
Parser
Scheduler
Controller
PaymentService
QuickSort
```

All of these classes represent identifiable subjects.

They are therefore natural candidates for the `agent` role.

---

## Why QuickSortSupport and Launcher Are Workers

Workers exist primarily to support agents.

They often:

* launch execution,
* interact with the operating system,
* provide rendering,
* perform infrastructure tasks on behalf of agent classes,
* execute technical procedures.

Examples include:

```text
QuickSortSupport
AnimalWorker
ApplicationLauncher
ProcessLauncher
SystemExecutor
OperatingSystemWorker
```

Their purpose is not to represent a domain but to provide technical support.

---

## Conclusion

The Java version and the Clprolf version perform exactly the same work.

The difference lies in how responsibilities are expressed.

Traditional OOP leaves these distinctions largely to convention.

Clprolf makes them explicit.

```text
agent  → represents a domain
worker → supports a domain
```

This simple distinction helps maintain architectural clarity as systems grow, while remaining fully compatible with classical object-oriented design principles.
