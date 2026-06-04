v2.7, 4/6/2026

Rajout de ``` manquant à V.5
ClAgent ClWorker ClDraft ClFamily ClTrait ClFree ClBypass ClInterfaceBypass
=================================================================================================
# Clprolf — Documentation Officielle

## Introduction

**Clprolf** est un framework pour Java (et C#).

Son objectif est de rendre explicites certaines bonnes pratiques de la programmation orientée objet, sans introduire une architecture lourde ni une courbe d’apprentissage importante.

Clprolf repose sur une idée simple :

> Une classe doit clairement exprimer son rôle principal.

Le framework aide ainsi à :

* séparer la logique métier du code technique,
* limiter la dérive architecturale,
* rendre l’héritage plus cohérent,
* améliorer la lisibilité des systèmes.

---

# I) Les Deux Principes Fondamentaux

Clprolf repose sur deux principes centraux.

---

## 1. Une classe est soit métier, soit technique

Chaque classe appartient à l’un des deux mondes suivants :

### Monde métier / domaine

La classe représente une responsabilité métier ou conceptuelle.

Exemples :

* gestion de commandes,
* logique métier,
* simulation,
* orchestration fonctionnelle.

Ces classes sont déclarées avec :

```java
@ClAgent
```
---

### Monde technique

La classe effectue un travail technique :

* accès base de données,
* réseau,
* fichiers,
* affichage,
* infrastructure.

Ces classes sont déclarées avec :

```java
@ClWorker
```
---

## 2. L’héritage doit préserver le domaine

Une classe ne doit hériter que d’une classe appartenant au même domaine conceptuel.

Sinon :

> on utilise la composition.

Ce principe évite les hiérarchies incohérentes et les mélanges de responsabilités.

---

# II) Les Types de Classes

Clprolf possède seulement trois types de classes.

---

## II.1) `ClAgent`

Représente une classe métier ou conceptuelle.

Un `agent` :

* contient la logique métier,
* orchestre les traitements,
* prend des décisions,
* évite le code technique lourd.

Exemple :

```java
@ClAgent
public class OrderProcessor {

    private OrderRepository repository;

    public void process(Order order) {
        if(order.total() <= 0) {
            throw Error;
        }
        repository.save(order);
    }
}
```

Remarque: les entity, ou DTOs, sont agent, car les données sont métiers.

---

## II.2) `ClWorker`

Représente une classe technique.

Un `worker` :

* réalise les tâches machine,
* gère l’infrastructure,
* contient le code technique.

Exemple :

```java
@ClWorker
public class OrderRepository {

    public void save(Order order) {

        // accès base de données

    }
}
```

---

## II.3) `ClDraft`

Objet sans rôle défini.

Utilisé :

* pendant le prototypage,
* lors du refactoring,
* quand le rôle n’est pas encore clair.

Exemple :

```java
@ClDraft
public class TemporaryManager {
}
```

`@ClDraft` permet une approche flexible proche de l’OOP classique.

---

## II.4) Domaine principal et code technique

Clprolf encourage à déplacer autant que possible le code technique des classes agent vers des classes worker.

Cependant, un agent peut contenir une quantité raisonnable de code technique lorsque cela améliore la simplicité ou la lisibilité du système.

Un agent possède toujours un domaine principal représentant sa responsabilité centrale.

Des responsabilités secondaires peuvent exister tant qu’elles restent cohérentes avec ce domaine principal.

## II.5) Liberté d’interprétation et recommandations du framework

Le choix entre `agent` et `worker` reste laissé au développeur.

Certaines responsabilités peuvent être interprétées de différentes manières selon la vision architecturale adoptée.

Par exemple, une connexion peut être représentée :

* comme un `agent`, si elle est vue comme une abstraction fonctionnelle ;
* ou comme un `worker`, si elle est considérée comme un mécanisme purement technique.

Cependant, dans ces cas, Clprolf recommande d'utiliser un agent. Par exemple, pour une classe Connection:

* un `agent` pour représenter la connexion,
* et déléguer le code technique à un ou plusieurs `worker`.

---

# III) Héritage

> une classe hérite seulement d’une classe du même domaine.
---

## Exemple valide

```java
@ClAgent
public class Animal {
}

@ClAgent
public class Dog extends Animal {
}
```
---

## Exemple déconseillé

```java
@ClWorker
public class ClientRepository {
}

@ClAgent
public class Dog extends ClientRepository {
}
```

Ici, les domaines sont incompatibles.
Il faut utiliser la composition.

Un forçage de l'héritage de classe est possible avec @ClBypass, au-dessus de la classe.
---

# IV) Flexibilité

Clprolf est flexible.

Le développeur garde donc sa liberté :

* mélange possible si nécessaire,
* migration progressive,
* compatibilité avec du code existant.
* mais on a toujours un domaine principal

Le framework agit surtout comme :

> un guide structurel.
---

# V) Interfaces

Dans Clprolf, les interfaces sont vues comme :

> des formes abstraites d’héritage.

Elles participent donc à la continuité structurelle du système.

ClFamily  = interface principale d’une famille
ClTrait = trait, capacité commune entre familles
ClFree = interface libre

Dans Clprolf, les interfaces ne sont pas vues comme de simples
contrats techniques.

Les relations `extends` et `implements` sont considérées comme
de véritables relations d’héritage conceptuel, d'où "family".

---

## V.1) `ClFamily`

Interface représentant une famille abstraite.

Utilisée pour :

* le polymorphisme,
* le découplage,
* les variantes d’implémentation.

Les interfaces de famille possèdent également un rôle cible :

* `@ClAgent`
* ou `@ClWorker`
---

## Exemple

```java
@ClAgent
@ClFamily
public interface agent Animal {

    void manger(int quantite);

}
```

La hiérarchie des interfaces ClFamily reflète naturellement la hiérarchie des classes concrètes.

```java
@ClAgent
@ClFamily
public class agent Horse extends Animal {

    void sauter(int hauteur);

}
```

Et aura

```java

@ClAgent
public class AnimalImpl implements Animal { (...) }

```

```java

@ClAgent
public class HorseImpl extends AnimalImpl implements Horse { (...) }

```

---

## V.2) `ClTrait`

Interface représentant une fonctionnalité commune entre plusieurs ClFamily.
Les traits utilisent un rôle cible, tout comme les ClFamily :

* @ClAgent
* @ClWorker

Remarque: une interface @ClTrait peut être annotée à la fois @ClAgent et @ClWorker.
Cette exception est réservée aux traits véritablement transversaux,
utilisables aussi bien par des agents que par des workers.

```java
@ClAgent
@ClWorker
@ClTrait
public interface Runnable {
    public void run();
}

---

## Exemple côté métier

```java

@ClAgent
@ClTrait
public interface Payable {
    void pay();
}
```
---

## Exemple côté technique

```java

@ClWorker
@ClTrait
public interface Launcher {
    void start();
}
```
---

## V.3) `@ClFree`

Interface générique sans rôle particulier.
Permet de rester flexible.

---

## Exemple

```java
@ClFree
public interface ExternalApi {
}
```
---

## V.4) Utilisation des interfaces

En Clprolf, les interfaces `Family` sont l’équivalent de classes abstraites pures.

Elles sont destinées à être implémentées par une ou plusieurs futures classes Clprolf.
Elles possèdent donc un rôle cible (`agent` ou `worker`).

Une classe ne peut implémenter qu’une seule `Family` principale à la fois, et le rôle de la classe doit correspondre au rôle cible de l’interface.
Clprolf utilise ainsi une implémentation simple des interfaces, de la même manière que Java utilise un héritage simple des classes. En effet, une `Family` est toujours le reflet structurel de son implémentation.Cela permet notamment d’obtenir un loose coupling systématique.
Toutefois, l’implémentation multiple n’est pas supprimée, mais déplacée vers la `Family` implémentée par la classe.
Cette interface de famille peut elle-même hériter de plusieurs interfaces `Family` ou `Trait`.
Ainsi, les interfaces qui auraient été implémentées directement par la classe sont regroupées au niveau de sa `Family` principale.
Clprolf conserve donc la richesse de l’héritage multiple des interfaces, tout en maintenant une structure simple et cohérente pour les classes concrètes.

---

Les interfaces `Trait` expriment une fonctionnalité commune entre plusieurs interfaces `Family`.

Un `Trait` représente donc un trait transversal partagé entre plusieurs familles.

Normalement, un `Trait` ne peut être hérité que par une interface `Family`, et non directement par une classe.

Cependant, en Clprolf, l’implémentation directe d’un `Trait` par une classe reste tolérée, bien que déconseillée.

```text
Classe concrète
    ↓ implémente
ClFamily
    ↓ hérite de
ClTrait
```

Note : une interface `Family` peut hériter de plusieurs interfaces `Family` ou `Trait`.

Une interface `Trait` ne peut hériter que d’autres `Trait`, car un trait reste un trait.

Un forçage de l’héritage d’interfaces reste possible avec `@ClInterfaceBypass` au-dessus de l’interface (ou `@ClBypass` pour forcer l’héritage entre rôles cibles différents).

---

## V.5) Remarque sur Clprolf et l'Interface Segregation Principle (ISP)

Clprolf respecte l'ISP, il suffit d'adapter le design des classes et interfaces avec les bonnes familles et traits:

```java
@ClAgent
@ClTrait
public interface Scanner {
	void scan(Document doc);
}

@ClAgent
@ClTrait
public interface Fax {
	void fax(Document doc);
}

@ClAgent
@ClTrait
public interface Printer {
	void print(Document doc);
}

@ClAgent
@ClFamily
public interface OldPrinter extends Printer  {
	
}

@ClAgent
@ClFamily
public interface ModernPrinter extends OldPrinter, Scanner, Fax {
}

@ClAgent
public class OldPrinterImpl implements OldPrinter {
//(…)
}

@ClAgent
public class ModernPrinterImpl implements ModernPrinter {
//(…)
}
```

# VI) Architecture Générale

Clprolf encourage naturellement une architecture simple.

```text
agent
    ↓ délègue vers
worker
```

Les `agents` contiennent :

* les règles métier,
* les décisions,
* l’orchestration.

Les `worker` réalisent :

* le travail technique,
* les accès systèmes,
* les opérations machine.

Un agent délègue à un ou plusieurs worker, le code technique. Il peut en exécuter, mais en appelant une méthode d'un worker.
Le worker est au service de l'agent.

---

# VIII) Objectif du Framework

Clprolf ne cherche pas à remplacer l’OOP classique.

Il cherche à rendre explicites certaines distinctions importantes :

* métier vs technique,
* héritage cohérent vs composition,
* responsabilité principale d’une classe.
---

# IX) Checker ArchUnit

Un checker basé sur ArchUnit est disponible pour le Framework Clprolf, sur Github. Il est open-source et composé de deux classes ClprolfArchTest, et ClprolfStrictArchTest. Il valide les règles sémantiques.
Les règles de ClprolfStrictArchTest sont optionnelles. De même, il est facile de changer le nom des annotations, si on préfère un autre vocabulaire.

### clprolf_classes_must_not_mix_agent_and_worker:
Une classe ne peut pas être annotée à la fois @ClAgent et @ClWorker

### agent_worker_inheritance_must_not_mix
Une classe @ClWorker ne peut pas hériter d'une classe @ClAgent, et inversement.

### clfamily_role_must_match_implementation
Le rôle cible d'une interface @ClFamily, doit être similaire au rôle de la classe d'implémentation(@ClAgent ou @ClWorker). Forçage possible avec @ClBypass.

### (mode non strict)trait_interface_role_must_match_direct_implementation
Une classe qui implémente directement un trait doit avoir un rôle compatible (sauf si forçage avec @ClBypass). Interdit en mode strict.

### trait_interfaces_must_extend_only_trait_interfaces
Les interfaces @ClTrait ne peuvent hériter que d'autres @ClTrait. Forçage possible avec @ClInterfaceBypass.

### clprolf_interfaces_must_have_target_role
Les interfaces @ClFamily doivent avoir un seul rôle cible : @ClAgent ou @ClWorker.
Les interfaces @ClTrait doivent avoir au moins un rôle cible : @ClAgent, @ClWorker,
ou exceptionnellement les deux.

### inheriting_interface_role_must_match_trait_interface_target_role
Les interfaces (family ou trait) qui héritent d'un trait doivent avoir un rôle compatible avec le trait (sauf si forçage avec @ClBypass).

### family_interface_target_role_must_match_inherited_family_interface
Les interfaces family dont hérite une interface family doivent avoir un rôle compatible, à moins d'utiliser @ClBypass.

Règles plus stricts:

### optional_all_classes_should_have_clprolf_role 
Toutes les classes doivent avoir un rôle Clprolf (@ClAgent, @ClWorker ou @ClDraft)

### optional_all_interfaces_should_have_clprolf_role 
Toutes les interfaces doivent avoir un rôle Clprolf(@ClFamily, @ClTrait ou @ClFree)

### optional_class_should_not_implement_trait_directly
Une classe ne peut pas implémenter directement une interface @ClTrait (à moins d'utiliser @ClInterfaceBypass)

### optional_class_must_implement_only_one_family_interface (OPTIONNELLE)
Une classe Clprolf ne peut implémenter qu'une interface @ClFamily. Forçage possible avec @ClInterfaceBypass


# X) Clprolf et les architectures existantes

Clprolf est compatible avec les architectures DDD, MVC, Clean architecture, architecture hexagonale, etc. C'est une couche entre la POO et les architectures, qui vient compléter et sécuriser les architectures connues.


# XI) Résumé

Clprolf ajoute très peu de concepts.
---

## Classes

```text
@ClAgent
@ClWorker
@ClDraft
```
---

## Interfaces

```text
@ClFamily
@ClTrait
@ClFree
```
---

## Deux règles fondamentales

```text
1. Séparer métier et technique.
2. Hériter seulement dans le même domaine.
```
---

=====================================
TRADUCTION EN ANGLAIS
=========================================
# Clprolf — Official Documentation

## Introduction

**Clprolf** is Java (or C#) framework.

Its goal is to make certain object-oriented programming best practices explicit, without introducing heavy architecture or a steep learning curve.

Clprolf is based on a simple idea:

> A class should clearly express its primary role.

The framework helps to:

* separate business logic from technical code,
* limit architectural drift,
* make inheritance more coherent,
* improve system readability.

---

# I) The Two Fundamental Principles

Clprolf is built around two central principles.

---

## 1. A class is either business-oriented or technical

Every class belongs to one of the following worlds:

### Business / Domain World

The class represents a business or conceptual responsibility.

Examples:

* order management,
* business logic,
* simulation,
* functional orchestration.

These classes are declared using:

```java
agent
```

---

### Technical World

The class performs a technical task:

* database access,
* networking,
* file handling,
* display,
* infrastructure.

These classes are declared using:

```java
worker
```

---

## 2. Inheritance must preserve the domain

A class should only inherit from another class belonging to the same conceptual domain.

Otherwise:

> composition should be used.

This principle prevents incoherent hierarchies and mixed responsibilities.

---

# II) Class Types

Clprolf contains only three class types.

---

## II.1) `ClAgent`

Represents a business or conceptual class.

An `agent`:

* contains business logic,
* orchestrates processes,
* makes decisions,
* avoids heavy technical code.

Note: entities and DTOs are typically classified as agents, since they represent domain data.

Example:

```java
@ClAgent
public class OrderProcessor {

    private OrderRepository repository;

    public void process(Order order) {
        if(order.total() <= 0) {
            throw Error;
        }
        repository.save(order);
    }
}
```

---

## II.2) `ClWorker`

Represents a technical class.

A `worker`:

* performs machine-oriented tasks,
* manages infrastructure,
* contains technical code.

Example:

```java
@ClWorker
public class OrderRepository {

    public void save(Order order) {

        // database access

    }
}
```

---

## II.3) `ClDraft`

An object without a defined role.

Used:

* during prototyping,
* during refactoring,
* when the role is not yet clear.

Example:

```java
@ClDraft
public class TemporaryManager {
}
```

`ClDraft` enables a flexible approach similar to classical OOP.

---

## II.4) Primary Domain and Technical Code

Clprolf encourages moving as much technical code as possible from `agent` classes into `worker` classes.

However, an `agent` may contain a reasonable amount of technical code when doing so improves simplicity or readability.

An `agent` always has a primary domain representing its central responsibility.

Secondary responsibilities may exist as long as they remain consistent with that primary domain.

---

## II.5) Freedom of Interpretation and framework Recommendations

The choice between `agent` and `worker` is left to the developer.

Some responsibilities may be interpreted differently depending on the adopted architectural vision.

For example, a connection may be represented:

* as an `agent` if viewed as a functional abstraction;
* or as a `worker` if viewed as a purely technical mechanism.

However, in these cases, Clprolf recommends using an agent. For example, for a `Connection` class:

* an `agent` to represent the connection,
* and delegate technical code to one or more `worker` classes.

---

# III) Inheritance

> A class only inherits from another class within the same domain.

---

## Valid Example

```java
@ClAgent
public class Animal {
}

@ClAgent
public class Dog extends Animal {
}
```

---

## Discouraged Example

```java
@ClWorker
public class ClientRepository {
}

@ClAgent
public class Dog extends ClientRepository {
}
```

Here, the domains are incompatible.

Composition should be used instead.

Inheritance can be forced using `@ClBypass` above the class.

---

# IV) Flexibility

Clprolf is flexible.

The developer therefore keeps their freedom:

* mixing responsibilities when necessary,
* progressive migration,
* compatibility with existing code,
* while always maintaining a primary domain.

The framework mainly acts as:

> a structural guide.

---

# V) Interfaces

In Clprolf, interfaces are viewed as:

> abstract forms of inheritance.

They therefore participate in the structural continuity of the system.

```text
ClFamily = primary family interface
ClTrait  = trait, shared capability between families
ClDraft = unrestricted interface
```

In Clprolf, interfaces are not viewed as simple technical contracts.

Both `extends` and `implements` relationships are considered genuine conceptual inheritance relationships.

---

## V.1) `ClFamily`

An interface representing an abstract family.

Used for:

* polymorphism,
* decoupling,
* implementation variants.

Family interfaces also have a target role:

* `ClAgent`
* or `ClWorker`

---

## Example

```java
@ClAgent
@ClFamily
public interface Animal {

    void eat(int quantity);

}
```

The hierarchy of `ClFamily` interfaces naturally reflects the hierarchy of concrete classes.

```java
@ClAgent
@ClFamily
public interface Horse extends Animal {

    void jump(int height);

}
```

Which may lead to:

```java
@ClAgent
public class AnimalImpl implements Animal { (...) }
```

```java
@ClAgent
public class HorseImpl extends AnimalImpl implements Horse { (...) }
```

---

## V.2) `ClTrait`

An interface representing a shared capability across multiple `ClFamily`.

Traits also use a target role:

* `ClAgent`
* `ClWorker`

> **Note: a `@ClTrait` may be annotated with both `@ClAgent` and `@ClWorker`.**
>
> **This exception is reserved for genuinely cross-cutting traits that can be used by both agents and workers.**

---

### Business Example

```java
@ClAgent
@ClTrait
public interface agent Payable {
    void pay();
}
```

---

### Technical Example

```java
@ClWorker
@ClTrait
public interface worker Persistable {
    void save();
}
```

---

## V.3) `ClFree`

A generic interface without a specific role.

Allows flexibility.

---

### Example

```java
@ClFree
public interface ExternalApi {
}
```

---

## V.4) Interface Usage

In Clprolf, `ClFamily` interfaces are the equivalent of pure abstract classes.

They are intended to be implemented by one or more future Clprolf classes.

They therefore possess a target role (`agent` or `worker`).

A class may implement only one primary `ClFamily` at a time, and the role of the class must match the target role of the interface.

Clprolf therefore adopts a simple interface implementation model, in the same way that Java uses single class inheritance. Indeed, a `ClFamily` is always the structural reflection of its implementation. This notably enables systematic loose coupling.

However, multiple interface implementation is not removed; it is simply moved to the `Family` implemented by the class.

This family interface may itself inherit from multiple `Family` and/or `Trait`` interfaces.

As a result, interfaces that would otherwise have been implemented directly by the class are grouped at the level of its primary `Family`.

Clprolf therefore preserves the expressive power of multiple interface inheritance while maintaining a simple and coherent structure for concrete classes.

---

`Trait` interfaces express a capability shared among multiple `Family` interfaces.

A `Trait` therefore represents a cross-cutting trait shared by several families.

Normally, a `Trait` may only be inherited by a `Family`, not directly by a class.

However, direct implementation of a `Trait` by a class remains tolerated in Clprolf, although discouraged.

```text
Concrete Class
      ↓ implements
ClFamily
      ↓ inherits from
ClTrait
```

A `Family` may inherit from multiple `Family` and/or `Trait`.

A `Trait` may inherit only from other `Trait`, since a trait remains a trait.

Interface inheritance may still be forced using `@ClInterfaceBypass` (or `@ClBypass` when forcing inheritance across different target roles).

---

## V.5) Note on Clprolf and the Interface Segregation Principle (ISP)

Clprolf inherently respects the ISP; it is simply a matter of adapting the design of your classes and interfaces using the appropriate families and traits:

```java
@ClAgent
@ClTrait
public interface Scanner {
    void scan(Document doc);
}

@ClAgent
@ClTrait
public interface Fax {
    void fax(Document doc);
}

@ClAgent
@ClTrait
public interface Printer {
    void print(Document doc);
}

@ClAgent
@ClFamily
public interface OldPrinter extends Printer {
    
}

@ClAgent
@ClFamily
public interface ModernPrinter extends OldPrinter, Scanner, Fax {
}

@ClAgent
public class OldPrinterImpl implements OldPrinter {
    // (...)
}

@ClAgent
public class ModernPrinterImpl implements ModernPrinter {
    // (...)
}

# VI) General Architecture

Clprolf naturally encourages a simple architecture.

```text
agent
    ↓ delegates to
worker
```

`agent` classes contain:

* business rules,
* decisions,
* orchestration.

`worker` classes perform:

* technical work,
* system access,
* machine operations.

An `agent` delegates technical code to one or more `worker` classes.

A worker serves the agent.

---

# VIII) Purpose of the framework

Clprolf does not aim to replace classical OOP.

It aims to make certain important distinctions explicit:

* business vs technical,
* coherent inheritance vs composition,
* primary responsibility of a class.

---

# IX) ArchUnit Checker for the Clprolf Framework

An ArchUnit-based checker is available for the Clprolf Framework on GitHub.
It is open source and consists of two classes: ClprolfArchTest and ClprolfStrictArchTest.

It validates the semantic rules of Clprolf.

The rules contained in ClprolfStrictArchTest are optional.
Likewise, annotation names can easily be customized if another vocabulary is preferred.

### clprolf_classes_must_not_mix_agent_and_worker

A class cannot be annotated with both @ClAgent and @ClWorker.

### agent_worker_inheritance_must_not_mix

A @ClWorker class cannot inherit from an @ClAgent class, and vice versa.

### family_interface_role_must_match_implementation

The target role of a @ClFamily must match the role of its implementing class (@ClAgent or @ClWorker).

Can be overridden using @ClBypass.

### (non-strict mode) trait_interface_role_must_match_direct_implementation

A class that directly implements a trait must have a compatible role.

Can be overridden using @ClBypass.

Direct trait implementation is forbidden in strict mode.

### trait_interfaces_must_extend_only_trait_interfaces

A @ClTrait may only extend other @ClTrait interfaces.

Can be overridden using @ClInterfaceBypass.

### clprolf_interfaces_must_have_target_role

A @ClFamily must declare exactly one target role:
@ClAgent or @ClWorker.

A @ClTrait must declare at least one target role:
@ClAgent, @ClWorker, or exceptionally both.

### inheriting_interface_role_must_match_trait_interface_target_role

Any interface (family or trait) inheriting from a trait must have a role compatible with that trait.

Can be overridden using @ClBypass.

### family_interface_target_role_must_match_inherited_family_interface

Family interfaces inherited by another family interface must have a compatible target role, unless overridden with @ClBypass.


## Stricter Rules

### optional_all_classes_should_have_clprolf_role

Every class should declare a Clprolf role:
@ClAgent, @ClWorker, or @ClDraft.

### optional_all_interfaces_should_have_clprolf_role

Every interface should declare a Clprolf interface role:
@ClFamily, @ClTrait, or @ClFree.

### optional_class_should_not_implement_trait_directly

A class should not directly implement a @ClTrait.

Use @ClInterfaceBypass if this behavior is intentionally required.

### optional_class_must_implement_only_one_family_interface

A Clprolf class should implement at most one @ClFamily.

Can be overridden using @ClInterfaceBypass.


# X) Clprolf and Existing Architectures

Clprolf is compatible with existing architectural approaches such as Domain-Driven Design (DDD), Model-View-Controller (MVC), Clean Architecture, Hexagonal Architecture, and others.

Rather than replacing these architectures, Clprolf acts as an additional layer between Object-Oriented Programming (OOP) and software architecture. Its purpose is to complement, clarify, and reinforce existing architectural principles by making class roles and inheritance relationships more explicit.

In this way, Clprolf helps improve architectural consistency while remaining fully compatible with established design practices.


# XI) Summary

Clprolf introduces very few concepts.

## Classes

```text
Agent
Worker
Draft
```

## Interfaces

```text
Family
Trait
Free
```

## Two Fundamental Rules

```text
1. Separate business and technical concerns.
2. Inherit only within the same domain.
```
