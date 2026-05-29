# Clprolf Docs #3 — Understanding Class Roles Through Java Wrappers

## Introduction

One of the easiest ways to understand Clprolf class roles is to reinterpret familiar Java classes through the Clprolf lens.

By creating wrappers around well-known classes such as `Socket`, `Scanner`, `String`, or `System`, we can explore a simple question:

> What does this class fundamentally represent?

The answer often determines whether the class naturally becomes an `@Agent` or a `@Worker`.

Wrappers therefore serve not only as integration tools but also as educational examples of Clprolf's architectural philosophy.

---

## The Question Behind Every Role

When assigning a role in Clprolf, we first ask:

> Does this class represent a coherent domain?

If the answer is yes, the class will usually become an `@Agent`.

If the class primarily exists to provide technical support, execution facilities, infrastructure access, or operating-system interaction, it will usually become a `@Worker`.

In simplified form:

```text
Agent  → represents a domain
Worker → supports a domain
```

---

## ClpSocket

A socket represents a well-defined subject:

```text
network communication endpoint
```

It has its own identity, state, and behavior.

For this reason, it naturally becomes an `@Agent`.

```java
@Agent
public class ClpSocket extends Socket {

}
```

The socket is not merely a technical helper.

It is the subject being manipulated.

---

## ClpSocketServer

A socket server also represents a coherent domain:

```text
accepting and managing network connections
```

Its purpose is not to assist another component.

It represents a system concept in its own right.

```java
@Agent
public class ClpSocketServer extends ServerSocket {

    public ClpSocketServer() throws IOException {
        super();
    }

    public ClpSocketServer(int port) throws IOException {
        super(port);
    }

    public ClpSocketServer(int port, int backlog) throws IOException {
        super(port, backlog);
    }

    public ClpSocketServer(int port, int backlog, InetAddress bindAddr)
            throws IOException {
        super(port, backlog, bindAddr);
    }
}
```

---

## ClpJButton

A button is more than a utility function.

It represents a user-interface component with its own state and behavior.

Its domain is:

```text
graphical user interaction
```

Therefore, it can naturally be modeled as an `@Agent`.

```java
@Agent
public class ClpJButton extends JButton {

}
```

---

## ClpScanner

The scanner is organized around a clear domain:

```text
input scanning
```

It performs a coherent responsibility and remains meaningful when considered independently.

```java
@Agent
public final class ClpScanner {

    private final Scanner internal;

    public ClpScanner(Scanner javaScanner) {
        this.internal = javaScanner;
    }

    public String nextLine() {
        return internal.nextLine();
    }
}
```

For this reason, it is naturally modeled as an `@Agent`.

---

## ClpString

A string represents textual content.

Its domain is:

```text
textual data
```

Even though Java declares `String` as `final`, the Clprolf interpretation remains the same.

```java
@Agent
public final class ClpString {

    private final String internal;

    public ClpString(String internalString) {
        this.internal = internalString;
    }

    public String getInternal() {
        return internal;
    }

    public static String valueOf(int i) {
        return String.valueOf(i);
    }
}
```

The wrapper therefore uses composition while preserving the role of the original concept.

---

## ClpSystem

`System` is different.

Unlike the previous examples, it does not primarily represent a domain object.

Instead, it exposes technical services such as:

* standard input,
* standard output,
* error streams,
* console access.

Its role is to support other components.

For this reason, it is naturally modeled as a `@Worker`.

```java
@Worker
public final class ClpSystem {

    public static PrintStream getOut() {
        return System.out;
    }

    public static InputStream getIn() {
        return System.in;
    }

    public static PrintStream getErr() {
        return System.err;
    }

    public static Console console() {
        return System.console();
    }
}
```

---

## What These Examples Teach Us

These wrappers reveal an important aspect of Clprolf:

The distinction is not primarily:

```text
business vs technical
```

Instead, it is closer to:

```text
represents a subject
vs
supports a subject
```

Examples:

```text
Socket         → Agent
SocketServer   → Agent
Scanner        → Agent
JButton        → Agent
String         → Agent
System         → Worker
```

An agent may represent a business concept, a system concept, a technical concept, or a scientific concept.

What matters is that it possesses a coherent domain.

A worker exists primarily to assist such agents.

---

## Conclusion

Java wrappers provide a practical way to understand Clprolf roles.

They demonstrate how familiar classes can be interpreted through the concept of class domain.

When creating a wrapper, a useful question is:

> Does this class represent a coherent subject?

If the answer is yes, it will usually become an `@Agent`.

If it primarily provides technical support to other components, it will usually become a `@Worker`.

This simple principle helps make architectural intent explicit while remaining compatible with the Java ecosystem.
