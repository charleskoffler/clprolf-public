# A Global Model of How Classes Relate to Each Other in Clprolf

Clprolf introduces simple declensions (Agent, Worker, Abstraction…) that clarify **the natural place of each class in a project**.
Many developers asked for a **global picture** showing how these classes interact, how they depend on each other, and how domain-level classes stay cleanly separated from technical ones.

This article presents **a conceptual model** that explains:

* how each **class type** fits into the architecture,
* how domain classes interact with technical classes,
* how pure abstractions differ from system abstractions,
* and where `@Forc_pract_code` is used when crossing boundaries.

This is not a compiler rule-set.
It is simply a **mental model** that makes Clprolf’s design easier to understand and that helps classify new classes intuitively.

Below is the complete model, with diagram and detailed explanations.

---

This document provides a conceptual overview of how the main Clprolf declensions relate to each other in practice.
It is **not a formal set of compiler rules**, but a conceptual guide that helps developers understand how each declension fits into a coherent architectural model.

The goal is simple:
**clarify the natural relationships between Agents, Abstractions, Workers, and System Abstractions**,
and indicate where exceptions must be explicitly acknowledged through `@Forc_pract_code`.

---

```
                         ┌──────────────────────────────────────────┐
                         │              PURE ABSTRACTION            │
                         │ (concepts usable by ALL other roles)     │
                         │   - no workers                           │
                         │   - no system calls                      │
                         └───────────────────────┬──────────────────┘
                                                 │  used by anyone
                                                 ▼
                  ┌──────────────────────────────────────────────────┐
                  │                       AGENT                      │
                  │        (domain-level behavior & logic)           │
                  │                                                  │
                  │      [Optionally: simu_real_obj available]       │
                  └───────────────────────────┬──────────────────────┘
                                              │
                                              │ uses
                                              ▼
                 ┌──────────────────────────────────────────────────┐
                 │                   WORKER_AGENT                   │
                 │   (technical realization serving an Agent)       │
                 │                                                  │
                 │                                                  │
                 └──────────────────────────┬───────────────────────┘
                                             │
                                             │ calls
                                             ▼
         ┌────────────────────────────────────────────────────────────────┐
         │                        SYSTEM ABSTRACTION                      │
         │      (agent-like, system domain; may call workers or           │
         │       other system abstractions; uses @Forc_pract_code as      │
         │       declaration when needed)                                 │
         └────────────────────────────────┬───────────────────────────────┘
                                          │
                                          │ calls
                                          ▼
                   ┌──────────────────────────────────────────────────┐
                   │                        WORKER                    │
                   │   (low-level technical or native operations)     │
                   └──────────────────────────────────────────────────┘
```

---

#### **1. Agent**

An **Agent** represents a meaningful domain object.
It carries intention, behavior, and domain responsibility.

##### **Agent rules**

* An Agent **may call its Worker** (its technical realization).
* An Agent **may call other Agents**.
* An Agent **may use pure Abstractions**.
* An Agent **must not directly use System Abstractions**.
  If a domain object needs access to a system feature, **its Worker handles it**.

---

#### **2. Worker**

A **Worker** performs technical tasks on behalf of its Agent.

Typical examples include:

* network senders/receivers,
* file or database handlers,
* UI adapters.

##### **Worker rules**

* A Worker **may call other Workers**.
* A Worker **may use System Abstractions**.
* A Worker **may use pure Abstractions**.
* A Worker **may call its Agent** to trigger domain operations (UI events, callbacks, etc.).
* A Worker **must not expose domain intelligence**: its responsibility is purely technical.

Workers are the only declension allowed to bridge the domain world and system-level operations.

---

#### **3. Pure Abstraction**

A **pure Abstraction** is a conceptual type with no system interaction.
Its behavior is fully defined by logic, data, or mathematical structure.

Examples: collections, geometry, colors, numeric models, conceptual structures.

##### **Pure Abstraction rules**

* It **may call other pure Abstractions**.
* It **may call Agents** (same declension family).
* It **must not use System Abstractions**.
* It **must not use Workers**.
* It normally **does not have a Worker**: memory itself provides the “technical realization”.

Pure Abstractions represent stable logic that does not depend on the environment.

---

#### **4. System Abstraction**

A **System Abstraction** is an abstraction that *simulates* a system resource or capability.

Examples:

* Streams
* Channels
* Sockets
* Threads
* File abstractions

These objects have conceptual methods (e.g., read, write, open) but **their internal realization requires system-level operations** provided by a Worker.

##### **System Abstraction rules**

* A System Abstraction **may use its Worker** for technical realization.
* A System Abstraction **may call other System Abstractions** only when they belong to the same underlying domain of functionality.
* A System Abstraction **may not call Agents**.
* Any usage of external system abstractions or utilities must be annotated with `@Forc_pract_code`.

The annotation confirms that the developer intentionally breaks the purity of the abstraction for practical reasons.

---

#### **5. Exceptions with `@Forc_pract_code`**

Whenever a class must call an element that normally belongs outside its allowed scope, the developer must explicitly annotate the class or method with:

```
@Forc_pract_code
```

This annotation expresses:

* “I acknowledge that I am breaking the usual boundaries.”
* “This is done for practical, controlled reasons.”
* “This code is still correct within the intended model.”

Typical cases:

* A System Abstraction relying on a helper System Abstraction indirectly.
* A System Abstraction using a utility normally reserved to another domain.
* Exceptional bridging between system-level abstractions.

---

#### **6. Summary Table**

Here is a simplified summary of allowed direct usage:

| From / To              | Agent         | Worker         | Pure Abstraction | System Abstraction |
| ---------------------- | ------------- | -------------- | ---------------- | ------------------ |
| **Agent**              | ✔             | ✔              | ✔                | ✘                  |
| **Worker**             | ✔ (its Agent) | ✔              | ✔                | ✔                  |
| **Pure Abstraction**   | ✔             | ✘              | ✔                | ✘                  |
| **System Abstraction** | ✘             | ✔ (its Worker) | ✔                | ✔ (same domain)    |

`@Forc_pract_code` may be added where exceptions are intentional and justified.

---

#### **7. Purpose of This Model**

This article does not add rules to the compiler.
It simply clarifies the **intuitive structure** that naturally emerges from Clprolf:

* Agents = meaning
* Workers = execution
* Pure Abstractions = logic
* System Abstractions = simulated system capabilities

This model helps developers:

* classify new classes correctly,
* avoid mixing concerns accidentally,
* navigate and design complex systems more safely,
* recognize patterns inside existing codebases (e.g., Java libraries),
* understand how system-oriented classes are structured internally.

---
