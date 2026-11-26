# Axiomatic Formalization of an Object-Oriented Software Topology and Static Domain Verification: The Clprolf Model

**Version:** v1.2 (November 2026)

**Primary Domain:** Software Design

**Secondary Domains:** Theory of computation — Logic and verification; Software notations and tools — Domain-specific languages

**Keywords:** Software Topology, Architectural Verification, Domain Invariance, First-Order Logic, Axiomatic Formalization, Static Analysis, Embedded DSL (EDSL), Domain-Driven Design (DDD), ArchUnit, Clprolf

---

## Abstract

This paper presents an axiomatic formalization in first-order logic of the **Clprolf** (*Clear PROgramming Language and Framework*) architectural framework, a lightweight embedded DSL designed for object-oriented languages (Java and C# .NET) and statically enforced via ArchUnit and ArchUnitNET. Clprolf establishes a strict separation between business/conceptual components (`@ClAgent`, `@ClSystem`) and technical execution services (`@ClWorker`), while enforcing conceptual domain preservation across inheritance hierarchies. The formal model defines two dual spaces: the Concrete World of executable components ($\mathcal{C}$) and the Abstract World of contracts ($\mathcal{I}$), structured as a Directed Acyclic Graph (DAG) distinguishing family interfaces (`@ClFamily`) from cross-cutting capabilities (`@ClTrait`). Using an asymmetric anchoring relation termed *Mirror*, we prove key structural properties—including inheritance graph embedding, domain confinement under multiple inheritance, and semantic neutrality of interface polymorphism over concrete components. Finally, the paper delineates the boundary between Turing-undecidable domain properties (semantic equivalence, domain membership) and their decidable static approximations in software engineering practice.

---

## I. Introduction

Clprolf (*Clear PROgramming Language and Framework*) is a lightweight embedded DSL (EDSL) for pedagogy and complex applications. It takes the form of annotations (or attributes) and a static semantic compliance checker. The host language is Java or C# .NET.
The Clprolf formal model models the static software topology of an object-oriented automation application.

**Application domains and positioning:** Clprolf acts as an intermediate layer between classical object-oriented programming and software architectures (Hexagonal Architecture, Domain-Driven Design, Clean Architecture). It is particularly well suited to complex systems, scientific applications, simulations, large-scale refactoring initiatives, as well as teaching OOP and SOLID principles. The application remains pure Java or C#: annotations are non-intrusive and statically verified during the test phase.

**Minimal illustration (Java):**

```java
@ClAgent @ClTrait public interface Payable { void pay(); }
@ClAgent @ClFamily public interface OrderService extends Payable { void process(Order order); }
@ClWorker @ClFamily public interface OrderRepository { void save(Order order); }

@ClAgent
public class OrderServiceImpl implements OrderService {
    private final OrderRepository repository; // Injection of the technical mirror
    // Business logic...
}
```

## I.b Framework Scope and Alignment with the Formal Model

> The Clprolf formal model defines a complete theoretical topology (including **Application Parts $\mathcal{P}_A$**, **Architectural Atoms $\mathcal{D}$**, and the categorization of **Standalone Components**). Practical implementation in the framework relies on the following philosophy:
> * **Checks focused on what is decidable and adds value:** The static checker (ArchUnit / ArchUnitNET) verifies the fundamental and decidable constraints of the model (business/technical separation, domain invariance under inheritance, interface polarity). It does not pursue exhaustiveness where it brings no quality or engineering benefit.
> * **No over-constraint or visual noise:** No extra artifact or annotation (`@ClAtom`, `@ClPart`) is imposed in the code. Atoms, standalone components within parts, and subsystems emerge naturally from standard project structure (`@ClAgent` / `@ClWorker` / `@ClSystem` injection, packages/namespaces).
> * **Progressive adoption and seamless integration:** The checker supports two-step adoption (separating `@ClAgent`/`@ClWorker`/`@ClSystem` classes first, then optionally enabling strict mode on `@ClFamily`/`@ClTrait` interfaces). It also supports vocabulary aliases (e.g., `@ClDomain`/`@ClInfrastructure`) as well as automatic detection of native annotations from third-party ecosystems (e.g., `@RestController` or `[ApiController]` natively recognized as `@ClSystem`).

This boundary ensures smooth adoption in enterprise environments and preserves developer freedom, while locking down 100% of essential architectural rules.

---

## I.c Illustrative Example: Home Automation Thermal Regulation

To concretely illustrate the ternary topology (`@ClAgent`, `@ClSystem`, `@ClWorker`), the direction of dependencies across layers, and the Mirror anchoring mechanism before formalizing the axioms, consider a home automation subsystem (package) dedicated to heating control.

### 1. The Abstract World ($\mathcal{I}$): Mirror Families and "Interfaces of Interfaces"

In the Abstract World, contracts are strictly partitioned between architectural identities (`@ClFamily`) and cross-cutting capabilities (`@ClTrait`), qualified directly by target role annotations (`@ClAgent`, `@ClSystem`, `@ClWorker`). A trait never targets concrete classes directly: it is attached to a compatible mirror family.

```java
// Cross-cutting capability allowed only for @ClAgent and @ClSystem roles
@ClTrait @ClAgent @ClSystem
public interface MonitorableTrait {
    String getDiagnosticStatus();
}

// Mirror family of the regulation business agent (target @ClAgent)
@ClFamily @ClAgent
public interface ThermostatFamily extends MonitorableTrait {
    void evaluateTargetTemperature(double currentTemp);
}

// Mirror family of the agent's technical actuator (target @ClWorker)
@ClFamily @ClWorker
public interface HeaterActuatorFamily {
    void sendPowerCommand(boolean activate);
}

// Mirror family of the hardware bus system agent (target @ClSystem)
@ClFamily @ClSystem
public interface HardwareBusSystemFamily extends MonitorableTrait {
    void publishFrame(String topic, String payload);
}

// Mirror family of the low-level worker associated with the system atom (target @ClWorker)
@ClFamily @ClWorker
public interface SocketDriverFamily {
    void writeRawBytes(byte[] frame);
}
```

### 2. The Concrete World ($\mathcal{C}$): Business Atom and System Atom

In the Concrete World, each atom associates a driver (`@ClAgent` or `@ClSystem`) with its associated technical `@ClWorker`. In accordance with Clprolf's topological hierarchy, a `@ClSystem` never calls a business `@ClAgent`: instead, `@ClWorker` components (and other `@ClSystem` components) invoke `@ClSystem` components as a technical substrate.

```java
// --- SYSTEM ATOM: HardwareBusSystem (@ClSystem) + SocketDriverWorker (@ClWorker) ---

// Low-level worker associated with the system atom (purely operational, no functional state)
@ClWorker
public class SocketDriverWorker implements SocketDriverFamily {
    @Override
    public void writeRawBytes(byte[] frame) {
        System.out.println("[SOCKET] Raw send of " + frame.length + " bytes.");
    }
}

// System agent governing access to the hardware bus (used only by @ClWorker or @ClSystem)
@ClSystem
public class HardwareBusSystem implements HardwareBusSystemFamily {
    private final SocketDriverFamily socketDriver; // Delegation to its associated worker
    private boolean busReady = true;

    public HardwareBusSystem(SocketDriverFamily socketDriver) {
        this.socketDriver = socketDriver;
    }

    @Override
    public void publishFrame(String topic, String payload) {
        if (busReady) {
            socketDriver.writeRawBytes((topic + ":" + payload).getBytes());
        }
    }

    @Override
    public String getDiagnosticStatus() {
        return busReady ? "BUS_READY" : "BUS_DOWN";
    }
}

// --- BUSINESS ATOM: ThermostatAgent (@ClAgent) + MqttHeaterWorker (@ClWorker) ---

// Technical executor of the business atom: translates the business command and relies on @ClSystem
@ClWorker
public class MqttHeaterWorker implements HeaterActuatorFamily {
    private final HardwareBusSystemFamily hardwareBus; // A @ClWorker can use a @ClSystem

    public MqttHeaterWorker(HardwareBusSystemFamily hardwareBus) {
        this.hardwareBus = hardwareBus;
    }

    @Override
    public void sendPowerCommand(boolean activate) {
        hardwareBus.publishFrame("home/heater/cmd", activate ? "ON" : "OFF");
    }
}

// Regulation business agent (carries the decision and functional state of the domain)
@ClAgent
public class ThermostatAgent implements ThermostatFamily {
    private final HeaterActuatorFamily actuator; // Delegation to its mirror worker
    private final double targetTemp = 20.5;
    private double lastMeasuredTemp = 20.5;

    public ThermostatAgent(HeaterActuatorFamily actuator) {
        this.actuator = actuator;
    }

    @Override
    public void evaluateTargetTemperature(double currentTemp) {
        this.lastMeasuredTemp = currentTemp;
        actuator.sendPowerCommand(currentTemp < targetTemp);
    }

    @Override
    public String getDiagnosticStatus() {
        return "THERMOSTAT_ACTIVE (T=" + lastMeasuredTemp + "°C)";
    }
}
```

### 3. What the Static Checker Blocks (Invariance Guarantee)

Without any ad hoc rule on package names, the semantic checker immediately rejects any violation of the topological invariants:
* **Error 1 (`IllegalSystem`):** Violates **Axiom 2 (Section V)**, which forbids a component from directly implementing a `@ClTrait` ($\neg \text{DirectImplement}(c, t)$) and requires anchoring onto a mirror `@ClFamily` interface.
* **Error 2 (`IllegalWorker`):** Violates the definition of `SamePolarity` and **Axiom 3 (Section V)** (*Mirror of the family upon implementation*), because the technical role (`@ClWorker`) of the class does not match the target role (`@ClAgent`) of `ThermostatFamily`.
* **Error 3 (`IllegalWorkerFamily`):** Violates **Axiom 4 (Section V)** (*Inheritance of a trait by an interface*), because the family `IllegalWorkerFamily` with technical polarity (`TargetTech`) extends the trait `MonitorableTrait`, which is not compatible with the technical role ($\neg \text{TargetTech}(t)$).

```java
// ERROR 1 (Axiom 2 - Section V): Direct implementation of a @ClTrait without going through a mirror @ClFamily
@ClSystem
public class IllegalSystem implements MonitorableTrait {
    @Override
    public String getDiagnosticStatus() { return "ILLEGAL"; }
}

// ERROR 2 (SamePolarity & Axiom 3 - Section V): Polarity break (a @ClWorker adopts a family targeting @ClAgent)
@ClWorker
public class IllegalWorker implements ThermostatFamily {
    @Override
    public void evaluateTargetTemperature(double currentTemp) { }

    @Override
    public String getDiagnosticStatus() { return "ILLEGAL"; }
}

// ERROR 3 (Axiom 4 - Section V): Attaching a @ClTrait to a @ClFamily whose target role is incompatible
@ClFamily @ClWorker
public interface IllegalWorkerFamily extends MonitorableTrait { // MonitorableTrait only allows @ClAgent and @ClSystem
    void executeRawTask();
}
```

## II. Summary of the Formal Model: Clprolf

### 1. The Family Space and the Component Space

* **The Abstract World ($F$):** A space of contracts, traits, and families, allowing rich composition and multiple inheritance (DAG). It is the locus of pure conceptual definition.
* **The Concrete World ($C$):** The space of executable components.

### 2. The Mirror Function and Its Asymmetry

The relation between the two worlds is carried by an anchoring function, called **Mirror**:
* **Left totality:** $\forall c \in C, \quad \exists! f' \in F, \quad \text{Mirror}(c) = f'$
*(Every concrete component necessarily has a unique mirror family).*
* **Right partiality:** $\forall f \in F$, there is not necessarily an associated component $c$. But if such a component exists, it is by definition the mirror of this family.

### 3. Semantic Equivalence

For any component $c$ and its mirror $f'$:

$$c \equiv_{\text{sem}} f' \quad (\text{or } \mathcal{S}(c) = \mathcal{S}(f'))$$

The component and its family share strictly the same semantic identity. One executes, the other qualifies, yet the meaning is rigorously unified.

### 4. Domain Confinement and Multiple Inheritance (DAG)

* **Invariance under inheritance:** Every inheritance relation (direct or within the DAG of families) respects the domain invariant:

$$\text{InheritsStar}(f_1, f_2) \implies \text{SameDomain}(f_1, f_2)$$

* **Universality:** Even under multiple inheritance, all parents of a family must converge toward the same domain, guaranteeing total confinement of the system.

### 5. Prohibition of Boilerplate

Concrete components **never** directly implement technical interfaces (no parasitic interfaces such as `Serializable`). Every contract passes exclusively through the family hierarchy and the mirror mechanism.

---

## III. System Dictionaries

### 1. Set Dictionary (Universe of Discourse)

* **$\mathcal{A}_{\text{aut}}$**: The set of automation applications.
* **$\mathcal{H}$**: The set of human tasks to be automated.
* **$\mathcal{G}$**: The set of human functional goals / objectives.
* **$\mathcal{M}_{\text{global}}$**: The universal set of all possible business domains across all tasks $h \in \mathcal{H}$ (a set of sets):
  $$\mathcal{M}_{\text{global}} = \{ \mathcal{M}_h \mid h \in \mathcal{H} \}$$
* **$\mathcal{M}_h \in \mathcal{M}_{\text{global}}$**: The specific, **unique**, and invariant cross-cutting business domain for a given human task $h$ (it gathers all business rules mobilized, regardless of the number of departments or roles involved).
* **$\mathcal{SE}$**: The set of physical or organizational execution media.
* **$\mathcal{P}(\mathcal{M}_h)$**: The power set of $\mathcal{M}_h$. It is the set of all possible subdomains $m \subseteq \mathcal{M}_h$. $m \in \mathcal{P}(\mathcal{M}_h)$.
* **$\mathcal{P}_{A}$**: The set of parts / logical subsystems of application $A$. Each part $p \in \mathcal{P}_{A}$ simulates $m \in \mathcal{P}(\mathcal{M}_h)$.
* **$\mathcal{P}$**: The set of all parts / logical subsystems across all applications.
* **$\mathcal{D}$**: The set of domain atoms $d = (c, t)$.
* **$\mathcal{C}$**: The set of abstract components ($\mathcal{C} = \mathcal{C}_{\text{business}} \cup \mathcal{C}_{\text{tech}} \cup \mathcal{C}_{\text{syst}}$).
* **$\mathcal{I}$**: The set of interfaces ($\mathcal{I} = F \cup T$).
* **$\mathcal{C}_{\text{oop}}$**: The set of concrete classes (Java / C#).
* **$\mathcal{I}_{\text{oop}}$**: The set of OOP interfaces.
* **$\mathcal{S}_{\text{oop}}$**: The set of framework subsystems.

*Scope note: The formal model below formalizes the strict core of Clprolf. Flexibility elements of the framework—such as classes under prototyping (`@ClDraft`), free or native host-language interfaces (`@ClFree`), as well as explicit bypass mechanisms (`@ClBypass`, `@ClInterfaceBypass`)—are intentionally excluded from the strict axiomatic core.*

---

### 2. Predicate Dictionary

#### a. Atomic Predicates

* $\text{AutomApp}(A)$: $A \in \mathcal{A}_{\text{aut}}$ is an automation application.
* $\text{HumanWork}(h)$: $h \in \mathcal{H}$ is a formalized human task.
* $\text{BusinessComponent}(c)$: $c$ is a pure business logic component (`@ClAgent`).
* $\text{TechComponent}(t)$: $t$ is a technical service serving an agent (`@ClWorker`).
* $\text{SystemAgent}(s)$: $s$ is a domain-oriented system abstraction (`@ClSystem`).
* $\text{Atom}(d)$: $d \in \mathcal{D}$ is an indivisible architectural atom.
* $\text{HumanGoal}(G)$: $G \in \mathcal{G}$ is a functional goal.
* $\text{BusinessDomain}(\mathcal{M})$: $\mathcal{M} \in \mathcal{M}_{\text{global}}$ is a universe of business rules and concepts.
* $\text{ExecutionMedium}(se)$: $se \in \mathcal{SE}$ represents the execution medium of the human task.
* $\text{OOPSubsystem}(s)$: $s \in \mathcal{S}_{\text{oop}}$ is an OOP module / package.
* $\text{TechSupport}(t)$: $t$ is technical infrastructure support.
* $\text{StandaloneBusinessComponent}(c)$: $c$ is a standalone `BusinessComponent` located in a part $p$, with no `TechComponent` combined in an atom.
* $\text{StandaloneTechComponent}(c)$: $c$ is a standalone `TechComponent` located in a part $p$, not combined in an atom.
* $\text{StandaloneSystemAgent}(c)$: $c$ is a standalone `SystemAgent` located in a part $p$, with no `TechComponent` combined in an atom.

---

#### b. Relation Predicates

* $\text{Part}(p, A)$: $p \in \mathcal{P}$ is a subsystem / module of application $A$.
* $\text{Automates}(A, h)$: Application $A$ automates task $h$.
* $\text{Associates}(t, p)$: Support $t$ sustains part $p$.
* $\text{Simulates}(p, m)$: Part $p$ simulates business subdomain $m \subseteq \mathcal{M}_h$ (with $m \in \mathcal{P}(\mathcal{M}_h)$).
* $\text{Contains}(p, d)$: Part $p$ contains atom $d$.
* $\text{DirectlyContains}(p, c)$: Part $p$ directly contains component $c$ (without going through an atom $d$).
* $\text{Combines}(d, c, t)$: Atom $d$ uniquely binds pilot component (business or system) $c$ and technical component $t$.
* $\text{Pursues}(h, G)$: Task $h$ aims to achieve goal $G$.
* $\text{HasDomain}(h, \mathcal{M}_h)$: Task $h$ falls within the unique business domain $\mathcal{M}_h$.
* $\text{ReliesOn}(h, se)$: Task $h$ relies on execution medium $se$.
* $\text{DelegatesTo}(t, s)$: Component $t$ delegates its low-level work to system agent $s$.
* $\text{DirectInheritComponent}(c_1, c_2)$: $c_1$ directly inherits from $c_2$.
* $\text{InheritStarComponent}(c_1, c_2)$: Reflexive-transitive closure of inheritance between components ($\ge 0$ levels). Thus `SameDomain(c_1, c_2)`, which holds at every level, can be preserved.
* $\text{SameDomain}(c_1, c_2)$: $c_1$ and $c_2$ belong to the same conceptual domain. A Turing-undecidable predicate, which can be replaced by testing that the components share the same polarity.
* $\text{Annotates}(elem, \alpha)$: OOP element ($elem \in \mathcal{C}_{\text{oop}} \cup \mathcal{I}_{\text{oop}}$) carries annotation $\alpha$.
* $\text{Realizes}(elem_{\text{oop}}, comp_{\text{formal}})$: OOP code concretizes the formal component or interface.
* $\text{InjectsDep}(cl_1, cl_2)$: Class $cl_1$ injects a dependency $cl_2$.

---

#### c. Atomic Predicates for Interfaces

* $\text{Family}(i)$: $i \in \mathcal{I}$ is a family interface (`@ClFamily`).
* $\text{Trait}(i)$: $i \in \mathcal{I}$ is a trait/capability interface (`@ClTrait`).

---

#### d. Relation Predicates for Interfaces

* $\text{DirectImplement}(c, f)$: Component/class $c$ implements interface $f$.
* $\text{TargetBusiness}(i)$ / $\text{TargetTech}(i)$ / $\text{TargetSystemAgent}(i)$: $i \in \mathcal{I}$. Indicates whether interface $i$ targets / is compatible with the Business, Technical, or System role.
* $\text{SamePolarity}(f, c)$: $f \in \mathcal{I}$, $c \in \mathcal{C}$. Exact equivalence of polarity/role between family $f$ and component $c$, and requirement to be a family.
* $\text{SemEquiv}(c, f)$: $c \in \mathcal{C}, f \in \mathcal{I}$. Primitive boolean predicate (Turing-undecidable) expressing semantic equivalence between the concrete component and the family, settled by convention or architectural tooling.
* $\text{MirrorOf}(f, c)$: $f$ is the exact mirror interface of component $c$.
* $\text{DirectInherit}(i_1, i_2)$: Interface $i_1$ directly extends $i_2$.
* $\text{InheritsStar}(i_1, i_2)$: Reflexive-transitive closure of interface inheritance. Thus `SameDomainFamily(i_1, i_2)`, which holds at every level, can be preserved.
* $\text{SameDomainFamily}(i_1, i_2)$: Family condition, and domain preservation between two interfaces. Like `SameDomain`, but for two Family interfaces.
* $\text{StaticType}(o, f)$: Variable/object $o$ manipulated under static type of interface $f$.
* $\text{InstanceOf}(o, c)$: Object $o$ is an actual dynamic instance of class $c$.

---

## IV. Component-Focused Axioms and Theorems

The first axiom proposes a teleological approach to automation.

### Axiom 1 - Automation Applications
$$\forall A \; \Big(\text{AutomApp}(A) \implies \exists h \; \big(\text{HumanWork}(h) \land \text{Automates}(A,h) \big) \Big)$$

### Axiom 1b.1 - Human Work ($h$)

$$\begin{aligned} \forall h \; \Big( \text{HumanWork}(h) &\implies \exists G, \exists! \mathcal{M}, \exists! se_h \; \big( \text{HumanGoal}(G) \land \text{BusinessDomain}(\mathcal{M}) \land \text{ExecutionMedium}(se_h) \\ &\land \text{Pursues}(h, G) \land \text{HasDomain}(h, \mathcal{M}) \land \text{ReliesOn}(h, se_h) \big) \Big) \end{aligned}$$

### Axiom 1b.2 - Converse of Human Work

$$\begin{aligned} \forall h, G, \mathcal{M}, se_h \; \Big( \big( &\text{HumanGoal}(G) \land \text{BusinessDomain}(\mathcal{M}) \land \text{ExecutionMedium}(se_h) \land \text{Pursues}(h, G) \\ &\land \text{HasDomain}(h, \mathcal{M}) \land \text{ReliesOn}(h, se_h) \big) \implies \text{HumanWork}(h) \Big) \end{aligned}$$

### Axiom 2 — Structural Decomposition into Parts

$$\begin{aligned} \forall A, p, h, \mathcal{M} \; \Big( \big( &\text{AutomApp}(A) \land \text{Automates}(A, h) \land \text{HasDomain}(h, \mathcal{M}) \land \text{Part}(p, A) \big) \implies \\ &\exists! m, \exists! t \; \big( m \subseteq \mathcal{M} \land \text{TechSupport}(t) \land \text{Simulates}(p, m) \land \text{Associates}(t, p) \big) \Big) \end{aligned}$$

> **Case of reusable libraries:** A software library (providing, for example, generic structures such as collections, system agents, or technical components) may define parts whose own business subdomain is empty ($m = \emptyset \subseteq \mathcal{M}$), awaiting use by the business parts of a future application.

### Axiom 3 - Atomic Decomposition of Parts and Structural Self-Similarity

$$\begin{aligned} \forall A, p, d \; \Big( &\text{AutomApp}(A) \land \text{Part}(p, A) \land \text{Atom}(d) \land \text{Contains}(p, d) \implies \\  &\exists! c, \exists! t \; ((\text{BusinessComponent}(c) \lor \text{SystemAgent}(c)) \land \text{TechComponent}(t) \land \text{Combines}(d, c, t)) \Big) \end{aligned}$$

### Axiom 3b — Exclusivity of the Pilot Component per Application

Within the same application $A$, a pilot component (`BusinessComponent` or `SystemAgent`) can define only a single architectural atom (while remaining reusable in other applications):

$$\begin{aligned} \forall A, p_1, p_2, d_1, d_2, c, t_1, t_2 \; &\Big( \big( \text{AutomApp}(A) \land \text{Part}(p_1, A) \land \text{Part}(p_2, A) \land \text{Atom}(d_1) \land \text{Atom}(d_2) \land \\ &\text{Contains}(p_1, d_1) \land \text{Contains}(p_2, d_2) \land (\text{BusinessComponent}(c) \lor \text{SystemAgent}(c)) \land \\ &\text{TechComponent}(t_1) \land \text{TechComponent}(t_2) \land \text{Combines}(d_1, c, t_1) \land \text{Combines}(d_2, c, t_2) \big) \implies d_1 = d_2 \Big)\end{aligned}$$

### Axiom 3c — Principle of Recursive Delegation

A technical component can delegate heavy-complexity tasks to a system atom. The system atom is itself defined as a domain atom.

$$\forall t, s \; \Big( \text{TechComponent}(t) \land \text{DelegatesTo}(t, s) \implies \exists a, w \; \big( \text{Atom}(s) \land \text{SystemAgent}(a) \land \text{TechComponent}(w) \land \text{Combines}(s, a, w) \big) \Big)$$

### Axiom 4 - Uniqueness of Component Qualification

$$\begin{aligned} \forall c \; &\Big( \text{Component}(c) \implies \big(  \text{BusinessComponent}(c) \land \neg \text{TechComponent}(c) \land \neg \text{SystemAgent}(c) \big) \lor \\ &\big(  \neg \text{BusinessComponent}(c) \land \text{TechComponent}(c) \land \neg \text{SystemAgent}(c) \big) \lor \\ &\big( \neg \text{BusinessComponent}(c) \land \neg \text{TechComponent}(c) \land \text{SystemAgent}(c) \big) \Big) \end{aligned}$$

### Axiom 5 - Domain Invariance under Inheritance

The inheritance relation between two components implies belonging to the same conceptual domain.

$$\forall c, p \; \Big( \big( \text{Component}(c) \land \text{Component}(p) \land \text{DirectInheritComponent}(c, p) \big) \implies \text{SameDomain}(c, p) \Big)$$

#### Definition - Standalone Business Components

$$\begin{aligned} \forall c \in \mathcal{C}, \quad &\text{StandaloneBusinessComponent}(c) \iff \Big[ \text{BusinessComponent}(c) \;\;\land\;\; \big( \exists p \in \mathcal{P}, \; \text{DirectlyContains}(p, c) \big) \;\;\land \\ &\big( \forall t \in \mathcal{C}, \forall d \in \mathcal{D}, \; \neg \text{Combines}(d, c, t) \big) \Big] \end{aligned}$$

#### Definition - Standalone Technical Components

$$\begin{aligned} \forall t \in \mathcal{C}, \quad &\text{StandaloneTechComponent}(t) \iff \Big[ \text{TechComponent}(t) \;\;\land\;\; \big( \exists p \in \mathcal{P}, \; \text{DirectlyContains}(p, t) \big) \;\;\land \\ &\big( \forall c \in \mathcal{C}, \forall d \in \mathcal{D}, \; \neg \text{Combines}(d, c, t) \big) \Big] \end{aligned}$$

#### Definition - Standalone System Agents

$$\begin{aligned} \forall a \in \mathcal{C}, \quad &\text{StandaloneSystemAgent}(a) \iff \Big[ \text{SystemAgent}(a) \;\;\land\;\; \big( \exists p \in \mathcal{P}, \; \text{DirectlyContains}(p, a) \big) \;\;\land \\ &\big( \forall t \in \mathcal{C}, \forall d \in \mathcal{D}, \; \neg \text{Combines}(d, a, t) \big) \Big] \end{aligned}$$

### Axiom COMPS-TO-CLPROLF

1. **Business component (within an atom)**

$$\forall d, c, t \; \Big( \text{Atom}(d) \land \text{Combines}(d, c, t) \land \text{BusinessComponent}(c) \implies \exists! class \in \mathcal{C}_{\text{oop}} \; \big( \text{Annotates}(class, @ClAgent) \land \text{Realizes}(class, c) \big) \Big)$$

2. **Technical component (within an atom)**

$$\forall d, c, t \; \Big(  \text{Atom}(d) \land \text{Combines}(d, c, t) \land \text{TechComponent}(t) \implies \exists! class \in \mathcal{C}_{\text{oop}} \; \big( \text{Annotates}(class, @ClWorker) \land \text{Realizes}( class, t) \big) \Big)$$

3. **System component (within an atom)**

$$\forall d, c, t \; \Big(  \text{Atom}(d) \land \text{Combines}(d, c, t) \land \text{SystemAgent}(c) \implies \exists! class \in \mathcal{C}_{\text{oop}} \; \big( \text{Annotates}(class, @ClSystem) \land \text{Realizes}( class, c) \big) \Big)$$

4. **Standalone business components**

$$\forall c \in \mathcal{C} \Big( \text{StandaloneBusinessComponent}(c) \implies \exists! class \in \mathcal{C}_{oop} \big( \text{Annotates}(class, @ClAgent) \land \text{Realizes}(class, c) \big) \Big)$$

5. **Standalone technical components**

$$\forall c \in \mathcal{C} \Big( \text{StandaloneTechComponent}(c) \implies \exists! class \in \mathcal{C}_{oop} \big( \text{Annotates}(class, @ClWorker) \land \text{Realizes}(class, c) \big) \Big)$$

6. **Standalone system agents**

$$\forall c \in \mathcal{C} \Big( \text{StandaloneSystemAgent}(c) \implies \exists! class \in \mathcal{C}_{oop} \big( \text{Annotates}(class, @ClSystem) \land \text{Realizes}(class, c) \big) \Big)$$

### Axiom ATOMS-TO-CLPROLF

$$\forall d, c, t, cl_c, cl_t \; \Big( \big( \text{Atom}(d) \land \text{Combines}(d, c, t) \land \text{Realizes}(cl_c, c) \land \text{Realizes}(cl_t, t) \big) \implies \text{InjectsDep}(cl_c, cl_t) \Big)$$

### Axiom PARTS-TO-CLPROLF

$$\forall A, p \; \Big( \text{AutomApp}(A) \land \text{Part}(p, A) \implies \exists! s \in \mathcal{S}_{\text{oop}} \; \big( \text{OOPSubsystem}(s) \land \text{Realizes}(s, p) \big) \Big)$$

### Axiom CONVERSE-BUSINESS-ATOM

$$\begin{aligned} \forall cl_c, cl_t \in \mathcal{C}_{\text{oop}} \; &\Big( \big( \text{Annotates}(cl_c, \text{@ClAgent}) \land \text{Annotates}(cl_t, \text{@ClWorker}) \land \text{InjectsDep}(cl_c, cl_t) \big) \implies \\ &\exists! d, \exists! c, \exists! t \; \big( \text{Atom}(d) \land \text{Combines}(d, c, t) \land \text{BusinessComponent}(c) \land \text{TechComponent}(t) \land \\ &\text{Realizes}(cl_c, c) \land \text{Realizes}(cl_t, t) \big) \Big) \end{aligned}$$

### Axiom CONVERSE-SYSTEM-ATOM

$$\begin{aligned} \forall cl_c, cl_t \in \mathcal{C}_{\text{oop}} \; &\Big( \big( \text{Annotates}(cl_c, \text{@ClSystem}) \land \text{Annotates}(cl_t, \text{@ClWorker}) \land \text{InjectsDep}(cl_c, cl_t) \big) \implies \\ &\exists! d, \exists! a, \exists! t \; \big( \text{Atom}(d) \land \text{Combines}(d, a, t) \land \text{SystemAgent}(a) \land \text{TechComponent}(t) \land \\ &\text{Realizes}(cl_c, a) \land \text{Realizes}(cl_t, t) \big) \Big) \end{aligned}$$

> **Note on `CONVERSE-BUSINESS-ATOM` and `CONVERSE-SYSTEM-ATOM`:** These two axioms push the formalization to its completion to show that an unambiguous reconstruction of the formal atom from code observation (`@ClAgent` / `@ClSystem`, `@ClWorker`, and dependency injection) is theoretically possible in a consistent model. They pertain to the completeness of the formalization and do not correspond to a check performed by the static *checker* on atoms.

### Axiom CONVERSE-BUSINESS-COMPONENT

$$\forall cl \in \mathcal{C}_{\text{oop}} \; \Big( \text{Annotates}(cl, \text{@ClAgent}) \implies \exists! c \; \big( \text{BusinessComponent}(c) \land \text{Realizes}(cl, c) \big) \Big)$$

### Axiom CONVERSE-TECH-COMPONENT

$$\forall cl \in \mathcal{C}_{\text{oop}} \; \Big( \text{Annotates}(cl, \text{@ClWorker}) \implies \exists! t \; \big( \text{TechComponent}(t) \land \text{Realizes}(cl, t) \big) \Big)$$

### Axiom CONVERSE-SYSTEM-COMPONENT

$$\forall cl \in \mathcal{C}_{\text{oop}} \; \Big( \text{Annotates}(cl, \text{@ClSystem}) \implies \exists! a \; \big( \text{SystemAgent}(a) \land \text{Realizes}(cl, a) \big) \Big)$$

### Axiom CONVERSE-SUBSYSTEM

$$\forall s \in \mathcal{S}_{\text{oop}} \; \Big( \text{OOPSubsystem}(s) \implies \exists! p, \exists A \; \big( \text{Part}(p, A) \land \text{Realizes}(s, p) \big) \Big)$$

---

## V. Interface Axioms and Theorems

### Definitions

$$\text{TargetBusiness}(i) \triangleq \begin{cases} \text{BusinessPolarity}(i) & \text{if } i \in \mathcal{I} \land \text{Family}(i) \quad (\text{Family: exclusive polarity}) \\ \text{IsBusinessCompatible}(i) & \text{if } i \in \mathcal{I} \land \text{Trait}(i) \quad (\text{Trait: context-dependent compatibility}) \end{cases}$$

$$\text{TargetTech}(i) \triangleq \begin{cases} \text{TechPolarity}(i) & \text{if } i \in \mathcal{I} \land \text{Family}(i) \quad (\text{Family: exclusive polarity}) \\ \text{IsTechCompatible}(i) & \text{if } i \in \mathcal{I} \land \text{Trait}(i) \quad (\text{Trait: context-dependent compatibility}) \end{cases}$$

$$\text{TargetSystemAgent}(i) \triangleq \begin{cases} \text{SystemAgentPolarity}(i) & \text{if } i \in \mathcal{I} \land \text{Family}(i) \quad (\text{Family: exclusive polarity}) \\ \text{IsSystemAgentCompatible}(i) & \text{if } i \in \mathcal{I} \land \text{Trait}(i) \quad (\text{Trait: context-dependent compatibility}) \end{cases}$$

$$\begin{aligned} \text{SamePolarity}(f, c) \iff & \text{Family}(f) \land \Big( \big( \text{BusinessComponent}(c) \iff \text{TargetBusiness}(f) \big) \land \\ & \big( \text{TechComponent}(c) \iff \text{TargetTech}(f) \big) \land \\ & \big( \text{SystemAgent}(c) \iff \text{TargetSystemAgent}(f) \big) \Big) \end{aligned}$$

$$\text{MirrorOf}(f, c) \iff \big( \text{Family}(f) \land \text{DirectImplement}(c, f) \land \text{SamePolarity}(f, c) \land \text{SemEquiv}(c, f) \big)$$

### Axiom 1 - Strict Polarity of Families

$$\begin{aligned} \forall f \in \mathcal{I} \; &\Big( \text{Family}(f) \implies \big( (\text{TargetBusiness}(f) \land \neg \text{TargetTech}(f) \land \neg \text{TargetSystemAgent}(f)) \lor \\ &(\neg \text{TargetBusiness}(f) \land \text{TargetTech}(f) \land \neg \text{TargetSystemAgent}(f)) \lor \\ &(\neg \text{TargetBusiness}(f) \land \neg \text{TargetTech}(f) \land \text{TargetSystemAgent}(f)) \big) \Big) \end{aligned}$$

### Axiom 2 - Presence of the Component's Family, and Prohibition of Direct Traits

$$\begin{aligned} \forall c \in \mathcal{C} \; &\Big( \Big( \exists! f \in \mathcal{I} \; \big( \text{Family}(f) \land \text{DirectImplement}(c, f) \big) \Big) \land \\ &\Big( \forall t \in \mathcal{I} \; \big( \text{Trait}(t) \implies \neg \text{DirectImplement}(c, t) \big) \Big) \Big) \end{aligned}$$

### Axiom 3 - Mirror of the Family upon Implementation

$$\forall c \in \mathcal{C}, \forall f \in \mathcal{I} \quad \Big( \text{Family}(f) \land \text{DirectImplement}(c, f) \implies \text{MirrorOf}(f, c) \Big)$$

### Axiom 4 - Inheritance of a Trait by an Interface

$$\begin{aligned} \forall i, t \in \mathcal{I} \; &\Big( \text{Trait}(t) \land \text{DirectInherit}(i, t) \implies \big( (\text{TargetBusiness}(i) \implies \text{TargetBusiness}(t)) \land \\ &(\text{TargetTech}(i) \implies \text{TargetTech}(t)) \land (\text{TargetSystemAgent}(i) \implies \text{TargetSystemAgent}(t)) \big) \Big) \end{aligned}$$

### Axiom 5 — Inheritance of Traits

$$\forall t, i \in \mathcal{I} \; \Big( \text{Trait}(t) \land \text{DirectInherit}(t, i) \implies \text{Trait}(i) \Big)$$

### Axiom 6 - Domain Invariance During Family-to-Family Inheritance

$$\forall f_1, f_2 \in \mathcal{I} \; \Big( \text{DirectInherit}(f_1, f_2) \land \text{Family}(f_1) \land \text{Family}(f_2) \implies \text{SameDomainFamily}(f_1, f_2) \Big)$$

### Axiom 7 - Transmission of Direct Inheritance to the Mirror Family

$$\begin{aligned} \forall c, p \in \mathcal{C}, &\forall f_c, f_p \in \mathcal{I} \; \Big( \big( \text{DirectInheritComponent}(c, p) \land \text{Family}(f_c) \land \text{Family}(f_p) \land \text{MirrorOf}(f_c, c) \land \\&\text{MirrorOf}(f_p, p) \big) \implies \text{DirectInherit}(f_c, f_p) \Big) \end{aligned}$$

### Axiom INTERFS-TO-CLPROLF

1. **Family Interface**

$$\forall f \in \mathcal{I} \; \Big( \text{Family}(f) \implies \exists! oopInterface \in \mathcal{I}_{\text{oop}} \; \big( \text{Annotates}(oopInterface, @ClFamily) \land \text{Realizes}(oopInterface, f) \big) \Big)$$

2. **Trait Interface**

$$\forall t \in \mathcal{I} \; \Big( \text{Trait}(t) \implies \exists! oopInterface \in \mathcal{I}_{\text{oop}} \; \big( \text{Annotates}(oopInterface, @ClTrait) \land \text{Realizes}(oopInterface, t) \big) \Big)$$

### Axiom CONVERSE-FAMILY-INTERF

$$\forall int \in \mathcal{I}_{\text{oop}} \; \Big( \text{Annotates}(int, \text{@ClFamily}) \implies \exists! f \in \mathcal{I} \; \big( \text{Family}(f) \land \text{Realizes}(int, f) \big) \Big)$$

### Axiom CONVERSE-TRAIT-INTERF

$$\forall int \in \mathcal{I}_{\text{oop}} \; \Big( \text{Annotates}(int, \text{@ClTrait}) \implies \exists! t \in \mathcal{I} \; \big( \text{Trait}(t) \land \text{Realizes}(int, t) \big) \Big)$$

---

### Structuring and Embedding Lemmas

#### Lemma 1 — Closure and Propagation of Traits

For any family $f$ and any trait $t$, if $f$ inherits from $t$ (directly or transitively), then the polarity / target of trait $t$ is necessarily compatible with the polarity of family $f$.

$$\begin{aligned} \forall f, t \in \mathcal{I} \; &\Big( \text{Family}(f) \land \text{Trait}(t) \land \text{InheritsStar}(f, t) \implies \big( \text{TargetBusiness}(f) \implies \text{TargetBusiness}(t) \big) \land \\  &\big( \text{TargetTech}(f) \implies \text{TargetTech}(t) \big) \land \big( \text{TargetSystemAgent}(f) \implies \text{TargetSystemAgent}(t) \big) \Big) \end{aligned}$$

*Deductive proof (Structural induction):*

$$\begin{array}{rll} 1. & \text{Family}(f) \land \text{Trait}(t) \land \text{InheritsStar}(f, t) & \text{[Hypothesis]} \\ 2. & \text{Base case } (k=1) : \text{DirectInherit}(f, t) \implies \text{Target}(f) \implies \text{Target}(t) & \text{[Direct application of Axiom 4]} \\ 3. & \text{Inductive step } (k+1) : \exists i \in \mathcal{I} \; (\text{InheritsStar}(f, i) \land \text{DirectInherit}(i, t)) & \text{[Decomposition of interface chain]} \\ 4. & \text{Trait}(i) \land \text{Trait}(t) & \text{[Axiom 5 — Propagation of trait status]} \\ 5. & \text{InheritsStar}(f, i) \implies \text{Target}(f) \implies \text{Target}(i) & \text{[Induction hypothesis]} \\ 6. & \text{DirectInherit}(i, t) \implies \text{Target}(i) \implies \text{Target}(t) & \text{[Axiom 4 applied to } i \to t \text{]} \\ 7. & \text{Target}(f) \implies \text{Target}(t) & \text{[Transitivity of implication]} \\ \hline \therefore & \text{InheritsStar}(f, t) \implies \big( \text{TargetBusiness}(f) \implies \text{TargetBusiness}(t) \big) \land \dots & \blacksquare \end{array}$$

---

#### Lemma 2 — Embedding of Same-Conceptual-Domain Graphs, from Components to Families

For any components in a transitive inheritance relation, their mirror family interfaces exist uniquely, and these preserve the exact same transitive inheritance relation. The graph of components in the same conceptual domain lies within the graph of mirror families in the same domain.

$$\begin{aligned} \forall c_1, c_2 \in \mathcal{C} \; &\Big( \text{InheritStarComponent}(c_1, c_2) \implies \exists! f_1, \exists! f_2 \in \mathcal{I} \; \big( \text{Family}(f_1) \land \text{Family}(f_2) \land \\ &\text{MirrorOf}(f_1, c_1) \land \text{MirrorOf}(f_2, c_2) \land \text{InheritsStar}(f_1, f_2) \big) \Big) \end{aligned}$$

*Deductive proof:*

$$\begin{aligned} \begin{array}{rll} 1. & \text{InheritStarComponent}(c_1, c_2) & \text{[Hypothesis: transitive inheritance relation in } \mathcal{C} \text{]} \\  2. & \text{MirrorOf}(f_1, c_1) \land \text{MirrorOf}(f_2, c_2) & \text{[Axioms 2 and 3 — Mirror anchoring (1:1)]} \\  3. & \text{Base case } (k=0) : c_1 = c_2 \implies f_1 = f_2 \implies \text{InheritsStar}(f_1, f_1) & \text{[Reflexivity of inheritance]} \\  4. & \text{Inductive step } (k+1) : \exists p \in \mathcal{C} \; (\text{InheritStarComponent}(c_1, p) \land \\&\text{DirectInheritComponent}(p, c_2)) & \text{[Decomposition of chain]} \\  5. & \exists! f_p \in \mathcal{I} \quad \text{Family}(f_p) \land \text{MirrorOf}(f_p, p) & \text{[Axioms 2 and 3 — Mirror anchoring]} \\  6. & \text{InheritsStar}(f_1, f_p) & \text{[Induction hypothesis]} \\  7. & \text{DirectInherit}(f_p, f_2) & \text{[Axiom 7 — Propagation of direct inheritance]} \\  8. & \text{InheritsStar}(f_1, f_2) & \text{[Transitivity of inheritance in } \mathcal{F} \text{]} \\  \hline \therefore & \text{InheritsStar}(f_1, f_2) & \blacksquare \end{array} \end{aligned} $$

---

#### Lemma 3 — Closed Filiation of Access Families

For any object $o$ manipulated under a static family type $f$, family $f$ necessarily belongs to the upward inheritance cone ($\text{InheritsStar}$) of the unique mirror family $f'$ associated with the concrete class $c$ of $o$.

$$\begin{aligned} \forall o \in \mathcal{O}, \forall f \in \mathcal{I} \; \Big( \text{StaticType}(o, f) \land \text{Family}(f) \implies \exists! c \in \mathcal{C}, \exists! f' \in \mathcal{I} \; \Big( & \text{InstanceOf}(o, c) \land \text{Family}(f') \land \\ & \text{MirrorOf}(f', c) \land \text{InheritsStar}(f', f) \Big) \end{aligned}$$

*Deductive proof:*

$$\begin{aligned} \begin{array}{rll} 1. & \text{StaticType}(o, f) \land \text{Family}(f) & \text{[Hypothesis]} \\  2. & \exists! c \in \mathcal{C} \quad \text{InstanceOf}(o, c) & \text{[Unique dynamic OOP instance]} \\  3. & \exists! f' \in \mathcal{I} \quad \text{Family}(f') \land \text{MirrorOf}(f', c) & \text{[Axioms 2 \& 3 — Uniqueness of mirror family]} \\  4. & \forall f'' \in \mathcal{I} \quad (\text{Family}(f'') \land \text{DirectImplement}(c, f'') \implies f'' = f') & \text{[Axiom 2 — Unique anchoring of component]} \\  5. & \text{InheritsStar}(f', f) & \text{[OOP resolution: } f \text{ is necessarily satisfied via } f' \text{]} \\  \hline \therefore & \exists! c \in \mathcal{C}, \exists! f' \in \mathcal{I} \; \Big( \text{InstanceOf}(o, c) \land \text{Family}(f') \land \text{MirrorOf}(f', c) \land \\&\text{InheritsStar}(f', f) \Big) & \blacksquare \end{array} \end{aligned}$$

---

#### Lemma 4 — Hierarchical Confinement of Traits

For any object $o$ manipulated under a static trait type $t$, trait $t$ is necessarily found in the inheritance relation of a pivot family $f$ ($\text{InheritsStar}(f, t)$) located in the upward cone of the mirror hierarchy $f'$ of the concrete class $c$ of $o$.

$$\begin{aligned} \forall o \in \mathcal{O}, \forall t \in \mathcal{I} \; \Big( \text{StaticType}(o, t) \land \text{Trait}(t) \implies \exists! c \in \mathcal{C}, \exists! f' \in \mathcal{I}, \exists f \in \mathcal{I} \; \Big( & \text{InstanceOf}(o, c) \land \text{Family}(f') \land \text{MirrorOf}(f', c) \land \\ & \text{Family}(f) \land \text{InheritsStar}(f', f) \land \text{InheritsStar}(f, t) \Big) \Big) \end{aligned}$$

*Deductive proof:*

$$\begin{aligned} \begin{array}{rll} 
1. & \text{StaticType}(o, t) \land \text{Trait}(t) & \text{[Hypothesis]} \\ 
2. & \exists! c \in \mathcal{C} \quad \text{InstanceOf}(o, c) & \text{[Unique dynamic OOP instance]} \\ 
3. & \exists! f' \in \mathcal{I} \quad \text{Family}(f') \land \text{MirrorOf}(f', c) & \text{[Axioms 2 \& 3 — Uniqueness of mirror family]} \\ 
4. & \forall t' \in \mathcal{I} \quad (\text{Trait}(t') \implies \neg \text{DirectImplement}(c, t')) & \text{[Axiom 2 — Prohibition of direct traits]} \\ 
5. & \exists f \in \mathcal{I} \quad \text{Family}(f) \land \text{InheritsStar}(f', f) \land \text{InheritsStar}(f, t) & \text{[OOP resolution \& Lemma 1]} \\ 
\hline 
\therefore & \exists! c \in \mathcal{C}, \exists! f' \in \mathcal{I}, \exists f \in \mathcal{I} \; \Big( \text{InstanceOf}(o, c) \land \text{Family}(f') \land \text{MirrorOf}(f', c) \land \\ &\text{Family}(f) \land \text{InheritsStar}(f', f) \land \text{InheritsStar}(f, t) \Big) & \blacksquare 
\end{array} \end{aligned}$$

---

#### Lemma 5 — Domain Propagation via Component Inheritance

The reflexive-transitive closure of inheritance between components preserves belonging to the same conceptual domain by transitivity.

$$\forall c_1, c_2 \in \mathcal{C}, \quad \text{InheritStarComponent}(c_1, c_2) \implies \text{SameDomain}(c_1, c_2)$$

*Proof (by structural induction on the direct inheritance chain):*
The base case ($c_1 = c_2$) is reflexive. For every direct inheritance step $\text{DirectInheritComponent}(a, b)$, Axiom 5 guarantees $\text{SameDomain}(a, b)$. By transitivity of the domain equivalence relation along the $\text{InheritStarComponent}$ chain, the property propagates across the entire inheritance cone. $\blacksquare$

---

#### Lemma 6 — Domain Propagation via Family Inheritance and Graph Structuring

Likewise, extended inheritance between family interfaces guarantees strict step-by-step domain preservation. It follows that the set of families associated with the same conceptual domain forms a hierarchical and strictly confined directed graph (DAG).

$$\begin{aligned} \forall f_1, f_2 \in \mathcal{I}, \quad \big( \text{Family}(f_1) \land \text{Family}(f_2) \land \text{InheritsStar}(f_1, f_2) \big) \implies \text{SameDomainFamily}(f_1, f_2) \end{aligned}$$

*Proof:*
Direct consequence of Axiom 6 ($\text{DirectInherit} \implies \text{SameDomainFamily}$) lifted by the transitive closure $\text{InheritsStar}$. $\blacksquare$

---

### Polymorphism and Anchoring Theorems

#### Theorem 1 — Neutrality over Components of Family Polymorphism

For any object $o$ statically typed by a family $f$, there exists a unique concrete class $c$ of which $o$ is the direct instance, and a unique mirror family $f'$ anchored to $c$, such that $f'$ inherits from $f$, belongs to the same conceptual domain as $f$, and guarantees semantic equivalence with the concrete component $c$.

$$\begin{aligned} \forall o \in \mathcal{O}, \, f \in \mathcal{F}, \quad & \Big( \text{StaticType}(o, f) \land \text{Family}(f) \Big) \implies \\  & \exists! c \in \mathcal{C}, \, \exists! f' \in \mathcal{F} \; \Big( \text{InstanceOf}(o, c) \land \text{MirrorOf}(f', c) \land \text{SemEquiv}(c, f') \land \\ & \quad \text{InheritsStar}(f', f) \land \text{SameDomainFamily}(f', f) \Big) \end{aligned}$$

*Deductive proof:*

$$\begin{aligned} \begin{array}{rll} 1. & \text{StaticType}(o, f) \land \text{Family}(f) & \text{[Hypothesis]} \\  2. & \exists! c \in \mathcal{C} \quad \text{InstanceOf}(o, c) & \text{[Concrete instantiation axiom in } \mathcal{C} \text{]} \\  3. & \exists! f' \in \mathcal{F} \quad \text{MirrorOf}(f', c) & \text{[Axioms 2 and 3 — Strict 1:1 mirror anchoring]} \\  4. & \text{SemEquiv}(c, f') & \text{[Definition of MirrorOf on } f' \text{]} \\  5. & \text{InheritsStar}(f', f) & \text{[Lemma 3 — Typing compatibility in } \mathcal{F} \text{]} \\  6. & \text{SameDomainFamily}(f', f) & \text{[Lemma 6]} \\  \hline \therefore & \exists! c \in \mathcal{C}, \, \exists! f' \in \mathcal{F} \; \Big( \text{InstanceOf}(o, c) \land \text{MirrorOf}(f', c) \land & \\  & \quad \text{SemEquiv}(c, f') \land \text{InheritsStar}(f', f) \land \text{SameDomainFamily}(f', f) \Big) & \blacksquare \end{array} \end{aligned}$$

---

#### Corollary 1.1 — Neutrality over Components of Trait Polymorphism

For any object $o$ statically typed by a trait $t$, there exists a unique concrete class $c$ of which $o$ is the direct instance, a unique mirror family $f'$ anchored to $c$, and a pivot family $f$ deriving from $t$, such that $f'$ guarantees semantic equivalence with $c$, derives from $f$, and belongs to the same conceptual domain as $f$.

$$\begin{aligned} \forall o \in \mathcal{O}, \, t \in \mathcal{I}, \quad & \Big( \text{StaticType}(o, t) \land \text{Trait}(t) \Big) \implies \\  & \exists! c \in \mathcal{C}, \, \exists! f' \in \mathcal{F}, \, \exists f \in \mathcal{F} \; \Big( \text{InstanceOf}(o, c) \land \text{Family}(f') \land \text{MirrorOf}(f', c) \land \\  & \quad \text{SemEquiv}(c, f') \land \text{Family}(f) \land \text{InheritsStar}(f', f) \land \\  & \quad \text{SameDomainFamily}(f', f) \land \text{InheritsStar}(f, t) \Big) \end{aligned}$$

*Deductive proof:*

$$\begin{aligned} \begin{array}{rll} 1. & \text{StaticType}(o, t) \land \text{Trait}(t) & \text{[Hypothesis: qualification by a trait interface]} \\  2. & \exists! c \in \mathcal{C}, \exists! f' \in \mathcal{F}, \exists f \in \mathcal{F} \quad \text{InstanceOf}(o, c) \land & \\  & \quad \text{Family}(f') \land \text{MirrorOf}(f', c) \land \text{Family}(f) \land & \\  & \quad \text{InheritsStar}(f', f) \land \text{InheritsStar}(f, t) & \text{[Direct application of Lemma 4]} \\  3. & \text{SemEquiv}(c, f') & \text{[Definition of MirrorOf on } f' \text{]} \\  4. & \text{SameDomainFamily}(f', f) & \text{[Lemma 6 on } \text{InheritsStar}(f', f) \text{]} \\  \hline \therefore & \exists! c \in \mathcal{C}, \exists! f' \in \mathcal{F}, \exists f \in \mathcal{F} \; \Big( \text{InstanceOf}(o, c) \land & \\  & \quad \text{Family}(f') \land \text{MirrorOf}(f', c) \land \text{SemEquiv}(c, f') \land & \\  & \quad \text{Family}(f) \land \text{InheritsStar}(f', f) \land \text{SameDomainFamily}(f', f) \land & \\  & \quad \text{InheritsStar}(f, t) \Big) & \blacksquare \end{array} \end{aligned}$$

---

### Theorem 2 — Structure and Invariance of the Family Graph

The Abstract World $\mathcal{F}$ forms a directed graph of family interfaces. Every extended inheritance relation ($\text{InheritsStar}$) between two families establishes a direct hierarchical derivation while guaranteeing strict invariance of the conceptual domain.

$$\begin{aligned} \forall f_1, f_2 \in \mathcal{F}, \quad \Big( \text{Family}(f_1) \land \text{Family}(f_2) \land \text{InheritsStar}(f_1, f_2) \Big) \implies \text{SameDomainFamily}(f_1, f_2) \end{aligned}$$

*Deductive proof:*

$$\begin{aligned} \begin{array}{rll} 1. & \text{Family}(f_1) \land \text{Family}(f_2) \land \text{InheritsStar}(f_1, f_2) & \text{[Hypothesis: relation in graph } \mathcal{F} \text{]} \\  2. & \text{SameDomainFamily}(f_1, f_2) & \text{[Direct application of Lemma 6]} \\  \hline \therefore & \text{SameDomainFamily}(f_1, f_2) & \blacksquare \end{array} \end{aligned}$$

---

### Synthesis — Neutrality of Interfaces over Components

> Theorem 1 and Corollary 1.1 establish the following conclusion: **interface polymorphism—whether expressed through families or traits—never constitutes a semantic or domain betrayal of the concrete code.** The formal system of components, atoms, and parts is thereby preserved.
> 
> 1. **Semantic fidelity between interfaces and components is inviolable:** The anchoring link (`MirrorOf`) coupled with equivalence (`SemEquiv`) guarantees that the execution component rigorously concretizes the meaning qualified by the contract.
> 2. **Domain invariance is preserved:** Step-by-step transitive propagation (`InheritsStar` and `InheritStarComponent`) ensures that no conceptual leak or illegitimate hybridization corrupts the initial business territory.

---

#### Theorem 3 — Concretization of a Family (0 or More Components)

For any family interface $f$, either there exists no concrete component for which $f$ is the mirror family (pure specification), or there exist one or more concrete components $c$ for which $f$ is the mirror family.

$$\begin{aligned} \forall f \in \mathcal{F}, \quad \text{Family}(f) \implies & \Big( \neg \exists c \in \mathcal{C} \; \text{MirrorOf}(f, c) \Big) \;\lor\; \Big( \exists c \in \mathcal{C} \; \text{MirrorOf}(f, c) \Big) \end{aligned}$$

*Deductive proof:*

$$\begin{aligned} \begin{array}{rll} 1. & \text{Family}(f) & \text{[Hypothesis]} \\  2. & \forall c \in \mathcal{C}, \quad \exists! f'_c \in \mathcal{F} \quad \text{MirrorOf}(f'_c, c) & \text{[Axioms 2 and 3 — Left injectivity of mirror]} \\  3. & \text{Let } C_f = \{ c \in \mathcal{C} \mid \text{MirrorOf}(f, c) \} & \text{[Set of mirror components of } f \text{]} \\  4. & \text{Case } C_f = \emptyset \implies \neg \exists c \in \mathcal{C} \; \text{MirrorOf}(f, c) & \text{[Pure specification: 0 component]} \\  5. & \text{Case } C_f \neq \emptyset \implies \exists c \in \mathcal{C} \; \text{MirrorOf}(f, c) & \text{[Concretization: 1 to } N \text{ components]} \\  \hline \therefore & \text{A family corresponds to 0 or more mirror components} & \blacksquare \end{array} \end{aligned}$$

---

#### Theorem 4 — DAG Structure and Multiple Inheritance of Families

The Abstract World $\mathcal{F}$ admits multiple inheritance: a family interface $f \in \mathcal{F}$ can derive directly from several distinct parent families $f_1, \dots, f_k \in \mathcal{F}$ ($k \ge 0$). This Directed Acyclic Graph (DAG) structure preserves strict business domain invariance on every branch.

$$\begin{aligned} \forall f \in \mathcal{F}, \quad \text{Family}(f) \implies & \Big( \exists k \ge 0, \exists f_1, \dots, f_k \in \mathcal{F} \quad \big( \big(\bigwedge_{i=1}^k \text{Family}(f_i) \land \text{DirectInherit}(f, f_i)\big) \land \\ & \big(\bigwedge_{i=1}^k \text{SameDomainFamily}(f, f_i)\big) \big) \Big) \end{aligned}$$

*Deductive proof:*

$$\begin{aligned} \begin{array}{rll} 1. & \text{Family}(f) & \text{[Hypothesis]} \\  2. & \text{Let } f_1, \dots, f_k \in \mathcal{F} \text{ such that } \text{Family}(f_i) \land \text{DirectInherit}(f, f_i) & \text{[Direct parents of } f \text{ in } \mathcal{F} \text{]} \\  3. & \forall i \in \{1, \dots, k\}, \quad \text{SameDomainFamily}(f, f_i) & \text{[Direct application of Axiom 6 on each edge]} \\  \hline \therefore & \text{Multiple inheritance in } \mathcal{F} \text{ preserves the business domain on } \\ &\text{all its branches} & \blacksquare \end{array} \end{aligned}$$

---

#### Theorem 5 — Factorization and Requalification of OOP Polymorphism

For any homogeneous OOP class $cl$ directly implementing a set of OOP interfaces $\{int_1, \dots, int_k\}$, there exists a domain requalification function $\psi : \mathcal{I}_{\text{oop}} \to (\mathcal{F} \cup \mathcal{T})$, a unique formal component $c \in \mathcal{C}$, and a unique pivot mirror family $f_{\text{pivot}} \in \mathcal{F}$ such that:
1. $cl$ concretizes component $c \in \mathcal{C}$ ($\text{Realizes}(cl, c)$).
2. The unique direct mirror family of component $c$ is $f_{\text{pivot}}$ ($\text{MirrorOf}(f_{\text{pivot}}, c)$).
3. Each OOP interface $int_j$ is requalified by $\psi$ either as a **Family** ($\psi(int_j) \in \mathcal{F}$) or as a **Trait** ($\psi(int_j) \in \mathcal{T}$).
4. $f_{\text{pivot}}$ gathers and inherits in the Abstract World $\mathcal{I}$ from the set of requalified abstractions $\psi(int_j)$.
5. For every interface requalified as a family ($\psi(int_j) \in \mathcal{F}$), business domain invariance is strictly preserved ($\text{SameDomainFamily}(f_{\text{pivot}}, \psi(int_j))$).

$$\begin{aligned} \forall cl \in \mathcal{C}_{\text{oop}}, \, \forall \{int_1, \dots, int_k\} \subseteq \mathcal{I}_{\text{oop}}, \quad & \Big( \big(\bigwedge_{j=1}^k \text{ImplementsOOP}(cl, int_j)\big) \Big) \implies \\  & \exists! c \in \mathcal{C}, \, \exists! f_{\text{pivot}} \in \mathcal{F} \; \Big( \text{Realizes}(cl, c) \;\land\; \text{MirrorOf}(f_{\text{pivot}}, c) \;\land\; \\ & \big(\bigwedge_{j=1}^k \text{InheritsStar}(f_{\text{pivot}}, \psi(int_j))\big) \Big) \end{aligned}$$

---

## VI. State of the Art and Positioning

The Clprolf model sits at the convergence of three research and software engineering currents: static architectural verification, component- or trait-oriented languages and type systems, and topological structuring of domain-centered and automation architectures.

### 1. Static Architectural Verification and Dependency Analysis

Numerous tools and academic works aim to prevent architectural erosion by checking source code conformance against a target architecture. Approaches such as Dependency Structure Matrices (DSM) [Sangal et al., 2005], Sonargraph, Lattix, or more recently architectural testing libraries such as **ArchUnit** (Java) and **ArchUnitNET** (C#) [Gafert, 2017] make it possible to formulate rules on code structure.

However, traditional use of these tools relies essentially on **topological checks of call graphs or inter-namespace dependencies** (for example, forbidding a `domain` package from importing an `infrastructure` package). Such checks rely on naming conventions or ad hoc folder partitioning, lacking intrinsic semantics at the type level. Clprolf takes a radically different approach: it imposes no rigid control over inter-layer call graphs, instead using ArchUnit and ArchUnitNET as lightweight execution engines to verify a **semantic type system** (in the sense of *pluggable type systems* [Bracha, 2004]). Emphasis is placed on the intrinsic qualification of entities and the preservation of their domain across inheritance hierarchies, leaving higher-level software architectures to organize execution flows.

### 2. Architectural Languages, Traits, and Roles in Object-Oriented Programming

Integrating architectural concepts directly into object-oriented languages has been explored notably by **ArchJava** [Aldrich et al., 2002], which extends Java with dedicated constructs (`component`, `port`, `connect`) to statically guarantee connection integrity at compile time. Nevertheless, ArchJava has two major limitations compared to Clprolf: on the one hand, its concept of `component` remains **purely structural and semantic-role agnostic** (it does not distinguish pure business logic from technical or system support); on the other hand, it requires a modified compiler and proprietary syntax, hindering industrial adoption. Conversely, Clprolf remains a non-intrusive EDSL built on standard host-language annotations.

Regarding interface structuring, Clprolf draws inspiration from foundational work on **traits** [Schärli et al., 2003; Ducasse et al., 2006] and on **role** modeling in OOP [Steimann, 2000], which enable fine-grained horizontal composition of behaviors without the pitfalls of multiple state inheritance. Clprolf, however, transposes this duality to the architectural level: it strictly separates **families** (`@ClFamily`, carrying an exclusive polarity and domain identity) from **traits** (`@ClTrait`, genuine "interfaces of interfaces" expressing cross-cutting capabilities subject to role compatibility), while prohibiting concrete components from directly implementing traits without going through a mirror family.

### 3. Domain-Centered Architectures and Atomic Topology of Automation

Methodologically, *Domain-Driven Design* (DDD) [Evans, 2003], *Hexagonal Architecture* [Cockburn, 2005], and *Clean Architecture* [Martin, 2017] establish the core principle of isolating the business core from technical infrastructure details. However, these approaches remain informal conceptual frameworks: they provide neither a unified annotation language across classes and interfaces nor a formal proof that object polymorphism does not compromise domain confinement.

More fundamentally, whereas these architectures partition software into generic horizontal strata or concentric circles, Clprolf introduces an **original topological organization** designed for object-oriented automation applications. It structures the application around the **architectural atom**: a coherent unit associating a driver (`@ClAgent` for the business domain, or `@ClSystem` for system infrastructure atoms) with its technical executors (`@ClWorker`), organized within the parts (subsystems) of the application.

Likewise, at the macroscopic level, Clprolf subsystems (embodied by Java packages or C# namespaces) align naturally with the Bounded Context notion in Domain-Driven Design (DDD), the modules of a modular monolith, or the internal scope of a microservice. However, whereas those approaches govern external partitioning of domain boundaries and flows across contexts, Clprolf operates as an internal intermediate layer: it guarantees through static verification that the interior of each subsystem remains structured into semantically confined atoms, without relying on mere naming conventions.

By acting as an **intermediate layer** between classical object-oriented programming and macroscopic architectural styles, Clprolf unifies this ternary topology with a rigorous formalization: its asymmetric **Mirror** relation between the Concrete World ($\mathcal{C}$) and the Abstract World ($\mathcal{I}$) allows us to prove deductively in first-order logic that multiple inheritance of families and trait composition invariably preserve the semantic and domain purity of executable atoms.

### Comparative Summary

| Approach / Tool | Abstraction Level | Semantic Typing of Roles | Inheritance & Polymorphism Verification | Host-Language Intrusiveness | Formal Foundation |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **ArchUnit / ArchUnitNET** | Dependency graphs (packages/layers) | No (naming conventions) | Manual ad hoc rules | None (unit tests) | None (bytecode analysis) |
| **ArchJava** | Components, ports, and connectors | No (single structural component) | Port connection checking | High (syntax & compiler extension) | Dedicated type system |
| **Traits (Schärli et al.)** | Fine-grained behavioral reuse | No (domain-agnostic) | Horizontal composition of operations | Built into language (Smalltalk, Scala, Rust...) | Operational calculus / trait algebra |
| **DDD / Clean Architecture** | Domain-centered macroscopic strata | Conceptual (informal) | Not formalized | None (methodology) | Informal |
| **Clprolf (this work)** | **Intermediate layer & atomic topology (`Agent`–`Worker`–`System`)** | **Yes (`@ClAgent`, `@ClWorker`, `@ClSystem`)** | **Yes (domain invariance & Mirror anchoring)** | **None (standard Java / C# annotations)** | **Axiomatic in 1st-order logic**

---

## VII. Discussion and Perspectives

### 1. Boundary Between Conceptual Structuring and LLM Code Generation

Clprolf makes it possible to distinguish conceptual structuring from operational implementation, a challenge heightened by the use of Large Language Models (LLMs) in software development. Even on algorithmic or technical tasks, a generative model often produces flat code and does not spontaneously introduce structuring such as an intermediate `@ClAgent` or a distinct `@ClSystem` to isolate responsibilities.

By formalizing the expected structure via explicit roles (`@ClAgent`, `@ClSystem`, `@ClWorker`, `@ClFamily`, and *Mirror* relations) subject to static verification, the framework provides an anchor to guide automatically generated code. Generation can thus focus on local implementation—such as method bodies or certain `@ClWorker` components—while the checker flags deviations from the structural and domain-invariance constraints defined by the model.

---

### 2. Perspectives for Extending the Formal Model

Consistent with its positioning as an **intermediate layer** between object-oriented programming and macro-architectures (DDD, Hexagonal Architecture, Clean Architecture), Clprolf is not intended to constrain call graphs or directional dependencies between roles (for example, forbidding a `@ClAgent` from invoking a `@ClSystem`), as such policies belong to higher-level software architectures. Three avenues for extension nonetheless emerge from this formalization:
* **Complementarity with macro-architecture rules:** Although outside Clprolf's intermediate core, semantic qualifications (`@ClAgent`, `@ClWorker`, `@ClSystem`) and partitioning into parts ($\mathcal{P}_A$) provide a natural foundation on which a macro-architecture can build to express its own isolation or call-direction rules.
* **Diagnostic tooling and topological inspection:** Exploiting the structure of atoms ($\mathcal{D}$), parts ($\mathcal{P}_A$), and inheritance/family hierarchies opens the way to static analysis tools capable of mapping an application—inventorying agents, workers, and systems, visualizing inheritance, and identifying complete atoms or standalone components.
* **Extension to other object-oriented languages:** While currently anchored in Java and C# .NET, Clprolf's axiomatic core is host-language agnostic and can be transposed to other statically typed object-oriented ecosystems (such as TypeScript, Kotlin, or Scala).

---

## Bibliographic References

* **[Aldrich et al., 2002]** Aldrich, J., Chambers, C., & Notkin, D. (2002). *ArchJava: Connecting software architecture to implementation*. In Proceedings of the 24th International Conference on Software Engineering (ICSE '02), 187–197. ACM.
* **[Bracha, 2004]** Bracha, G. (2004). *Pluggable Type Systems*. In OOPSLA'04 Workshop on Revival of Dynamic Languages.
* **[Cockburn, 2005]** Cockburn, A. (2005). *Hexagonal architecture (Ports and Adapters)*. Alistair in the Hexagone. [https://cockburn.us](https://cockburn.us)
* **[Ducasse et al., 2006]** Ducasse, S., Nierstrasz, O., Schärli, N., Wuyts, R., & Black, A. P. (2006). *Traits: A mechanism for fine-grained reuse*. ACM Transactions on Programming Languages and Systems (TOPLAS), 28(2), 331–388.
* **[Evans, 2003]** Evans, E. (2003). *Domain-Driven Design: Tackling Complexity in the Heart of Software*. Addison-Wesley Professional.
* **[Gafert, 2017]** Gafert, P. (2017). *ArchUnit: Unit test your Java architecture* [Software]. TNG Technology Consulting. [https://archunit.org](https://archunit.org)
* **[Martin, 2017]** Martin, R. C. (2017). *Clean Architecture: A Craftsman's Guide to Software Structure and Design*. Prentice Hall.
* **[Sangal et al., 2005]** Sangal, N., Jordan, E., Sinha, V., & Jackson, D. (2005). *Using dependency models to manage complex software architecture*. In Proceedings of the 20th Annual ACM SIGPLAN Conference on Object-Oriented Programming, Systems, Languages, and Applications (OOPSLA '05), 167–176.
* **[Schärli et al., 2003]** Schärli, N., Ducasse, S., Nierstrasz, O., & Black, A. P. (2003). *Traits: Composable units of behaviour*. In European Conference on Object-Oriented Programming (ECOOP 2003), LNCS 2743, 248–274. Springer.
* **[Steimann, 2000]** Steimann, F. (2000). *On the representation of roles in object-oriented and conceptual modelling*. Data & Knowledge Engineering, 35(1), 83–106.

## VIII. Implementation and Resources

* **Main Clprolf repository (documentation, examples, and source code):** [https://github.com/charleskoffler/clprolf-public](https://github.com/charleskoffler/clprolf-public)
* **Java static checker (ArchUnit-based):** [https://github.com/charleskoffler/clprolf-public/tree/main/clprolf_checker](https://github.com/charleskoffler/clprolf-public/tree/main/clprolf_checker)
* **C# .NET static checker (ArchUnitNET-based):** [https://github.com/charleskoffler/clprolf-public/tree/main/Clprolf.ArchUnitNet](https://github.com/charleskoffler/clprolf-public/tree/main/Clprolf.ArchUnitNet)
