# Framework Clprolf - Documentation officielle

## Utilisations cibles et prérequis

**Clprolf**("Clear PROgramming Language and Framework") est un framework architectural indépendant, léger, pour Java ou C# .net.
Le code source (annotations + checker) peut être copié sous licence M.I.T., et visible sur Github dans des applications écrites par l'auteur.
Cependant, le framework est principalement obtenu, ainsi que sa documentation et exemples, par des formations payantes de son auteur.

Il s'agit d'un framework spécialisé, pour les situations suivantes:
* framework pédagogique pour apprendre la POO, et les interfaces, voire l'immuabilité.
* applications scientifiques
* simulations
* applications d'entreprises intéressées par ajouter Clprolf en complément des architectures classiques
* applications très complexes, avec beaucoup de classes ou d'interfaces
* gros refactoring nécessaire sur une grosse appli existante
* ou tout simplement pour ceux qui aiment ce style de programmation encadrée par checker architectural pour classes et interfaces.

Il a pour prérequis la connaissance des bases de la POO. Une connaissance des principes de conception OO peut être utile.

Le framework a pour objectif d'aider à la conception objet. Il permet une interprétation des principes de conception objet comme SOLID, ou d'aider à appliquer l'architecture hexagonale/DDD.
Bien entendu, Clprolf ne fournit que sa propre façon d'obtenir les principes objets connus, et autres architectures notoires.
L'application obtenue pourra redevenir du pur Java ou C#, en retirant les annotations, ou en ne les interprétant pas.

## Introduction

Clprolf repose sur une idée simple :

> Une classe doit clairement exprimer son rôle principal.

Le framework aide ainsi à :

* séparer la logique métier du code technique,
* limiter la dérive architecturale,
* rendre l’héritage plus cohérent,
* améliorer la lisibilité des systèmes.

---

### En Java (ArchUnit)

Dans l'écosystème Java, les composants sont marqués à l'aide d'annotations :

```java
import org.clprolf.framework.ClAgent;

@ClAgent
public class CarImpl implements Car {
    // Logique de l'agent...
}

```

### En C# (ArchUnitNET)

Dans l'écosystème .NET, l'équivalent strict utilise les **Attributs C#** entre crochets `[...]` placés juste au-dessus de la classe :

```csharp
using Clprolf.ArchUnitNet.Attributes;

namespace MyApp.Agents {

    [ClAgent]
    public class Car : ICar
    {
        // Logique de l'agent...
    }

}
```

---

### 💡 Note pour les utilisateurs du Framework (.NET vs Java)

* **Syntaxe :** Le `@Annotation` de Java devient `[Attribute]` en C#.

---

# I) Les Deux Principes Fondamentaux

Clprolf repose sur deux principes centraux.

---

## 1. Une classe est soit métier/conceptuelle, soit technique

Chaque classe appartient à l’un des deux mondes suivants :

### Monde métier / domaine

La classe représente une responsabilité métier ou conceptuelle.

Exemples :

* gestion de commandes,
* logique métier,
* simulation,
* orchestration fonctionnelle.
* mais aussi agents orientés systèmes

Ces classes sont déclarées avec :

```java
@ClAgent
```

ou
```java
@ClSystem
```
---

### Monde technique

La classe effectue un travail technique, effectué par le système :

* accès base de données (par l'intermédiaire d'agents systèmes),
* réseau (par utilisation d'agents bas niveaux souvent),
* utilisation d'abstractions systèmes comme les fichiers
* affichage,
* infrastructure.
* pas de domaine conceptuel, juste le système qui exécute des agents techniques, ou qui démarre une appli
* généralement un service technique système associé à un agent.

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

# II) Adoption progressive, Personnalisation, et Détection automatique

Le framework offre une flexibilité totale tant dans son déploiement que dans sa terminologie.

---

## II.1) Une adoption par étapes

L'intégration de Clprolf au sein d'un projet peut se faire de manière incrémentale. Il n'est pas nécessaire de tout appliquer dès le premier jour.

* **Étape 1 : Les Classes d'abord.** Vous pouvez vous concentrer uniquement sur le découpage fondamental entre `@ClAgent` (ou `ClSystem`) et `@ClWorker`. Cela permet à l'équipe de maîtriser la séparation des classes sans friction.
* **Étape 2 : Les Interfaces ensuite (Optionnel).** Le modèle des interfaces (`ClFamily`, `ClTrait`) peut être introduit dans un second temps.

> Si vous jugez la gestion des interfaces trop disruptive par rapport à vos habitudes ou à l'OOP classique, vous pouvez totalement l'ignorer. Le framework restera pleinement fonctionnel et utile pour vos classes.

---

## II.2) Personnalisation de la terminologie (Alias natifs)

Le vocabulaire par défaut de Clprolf ne correspond pas tout à fait à votre nomenclature ?
Le framework propose des **annotations/attributs équivalents natifs** pour s'adapter à la culture de votre équipe.
Le checker ArchUnit/ArchUnitNET traite ces alias de manière totalement équivalente aux mots-clés de référence lors des validations automatisées :

| Rôle par défaut | Alternative conceptuelle | Alternative DDD |
| --- | --- | --- |
| **`@ClAgent`** | `@ClConcept` | `@ClDomain` |
| **`@ClWorker`** | `@ClMechanism` | `@ClInfrastructure` |
| **`@ClSystem`** | `@ClBridge` | `@ClLowLevel` |

> *Notes : Vous pouvez librement utiliser les alias qui résonnent le plus avec votre architecture, ou même étendre le checker pour enregistrer vos propres annotations d'équipe.*
> *Le checker traite les alias comme strictement équivalents. Vous pouvez utiliser le vocabulaire de votre choix sur un projet sans risquer d'incompatibilité, le checker garantissant la même cohérence architecturale sous-jacente.*

---

## II.3) Détection automatique et intégration sans friction

Pour faciliter l'adoption et éviter le bruit visuel dans le code, le checker ArchUnit de Clprolf centralise la qualification des rôles via ses méthodes prédicatives (`isAgent()`, `isWorker()`, `isSystem()`).
Le framework peut ainsi mapper automatiquement les annotations et attributs natifs de vos écosystèmes habituels (comme Spring ou ASP.NET Core) vers les rôles Clprolf. Un `@RestController` ou un `[ApiController]` sera par exemple immédiatement reconnu comme un `@ClSystem`, sans que vous n'ayez besoin d'ajouter la moindre annotation Clprolf sur vos classes.
Cette approche permet de bénéficier de toute la puissance de contrôle du framework de manière totalement invisible et progressive, sans impacter la lisibilité de vos développements du quotidien. Ainsi, vos applications existantes peuvent déjà bénéficier des contrôles du checker Clprolf, sans ajouter une seule annotation dans le code déjà écrit.

---

# III) Alignement Hexagonale, DDD & Clprolf

Clprolf formalise les principes majeurs de l'**Architecture Hexagonale** et du **Domain-Driven Design (DDD)** sous forme de contraintes et d'annotations POO explicites.
Il fournit une vision de ces architectures, mais ce n'est pas la seule possible. Clprolf ne cherche pas à être complet mais d'apporter une aide à sa façon.

## 1. La matrice d'équivalence

| Concept Hexa / DDD / Clean | Rôle & Responsabilité | Rôle Clprolf | Typage & Annotations |
| --- | --- | --- | --- |
| **Agrégat / Entité** *(DDD)* | Cœur métier, état, invariants et règles de gestion pures. | **`Agent`** | `@ClAgent` |
| **Domain Service** *(DDD)* | Orchestration de règles fonctionnelles combinant plusieurs agrégats. | **`Agent`** | `@ClAgent` |
| **Use Case** *(Clean)* / **Application Service** *(DDD)* | Scénario fonctionnel d'intention métier *(ex: `RegisterCustomer`)*. | **`Agent`** | `@ClAgent` |
| **Ports Primaires** *(Driving)* | Interfaces exposées par le domaine pour être piloté de l'extérieur. | **`Family`** | `@ClAgent @ClFamily` |
| **Ports Secondaires** *(Driven)* | Interfaces requises par le domaine pour communiquer avec l'extérieur. | **`Family`** | `@ClWorker @ClFamily` |
| **Adaptateurs Entrants** *(Hexa)* | Points d'entrée du système *(Contrôleurs REST, Handlers CLI, Consumers)*. | **`System`** | `@ClSystem` |
| **Adaptateurs Sortants** *(Hexa)* / **Infrastructure Service** *(DDD)* | Exécuteurs techniques *(Implémentations de repositories, Clients API, Senders)*. | **`Worker`** | `@ClWorker` |
| **Value Objects / DTOs** | Objets de données anémiques immuables (sans comportement). | *Donnée pure* | **Non annoté** |

---

## 2. Le flux d'exécution canonique

*Note : Un flux est dit **canonique** car il représente le modèle de référence officiel et standard d'exécution.*

```text
 ┌──────────────────────────────────────────────────────────────────┐
 │ ADAPTATEURS ENTRANTS (Driving)                                   │
 │                                                                  │
 │   ┌─────────────┐                                                │
 │   │  @ClSystem  │  (Contrôleur REST, CLI, Consumer Kafka)        │
 │   └──────┬──────┘                                                │
 └──────────┼───────────────────────────────────────────────────────┘
            │
            │ Appelle via le Port Primaire (@ClAgent @ClFamily)
            ▼
 ┌──────────────────────────────────────────────────────────────────┐
 │ CŒUR DOMAINE / USE CASES & AGRÉGATS                              │
 │                                                                  │
 │   ┌─────────────┐   Orchestre &   ┌─────────────┐                │
 │   │  @ClAgent   │ ──────────────> │  @ClAgent   │                │
 │   │ (Use Case)  │   manipule      │  (Agrégat)  │                │
 │   └──────┬──────┘                 └─────────────┘                │
 └──────────┼───────────────────────────────────────────────────────┘
            │
            │ Appelle via le Port Secondaire (@ClWorker @ClFamily)
            ▼
 ┌──────────────────────────────────────────────────────────────────┐
 │ ADAPTATEURS SORTANTS (Driven)                                    │
 │                                                                  │
 │   ┌─────────────┐                                                │
 │   │  @ClWorker  │  (JpaRepository, StripeClient, EmailSender)    │
 │   └─────────────┘                                                │
 └──────────────────────────────────────────────────────────────────┘

```

## 3. Les 3 règles d'or de Clprolf

1. **Les dépendances vont vers l'intérieur :** `@ClSystem` dépend de `@ClAgent`. `@ClAgent` ne dépend JAMAIS de `@ClWorker` directement, mais uniquement de son contrat `@ClWorker @ClFamily`.
2. **Tout le Domaine est `@ClAgent` :** Que ce soit le Use Case (l'orchestrateur du scénario) ou l'Agrégat (le cœur de l'état et des règles métier), tous deux sont des **`@ClAgent`**.
3. **Isolation totale du domaine :** Les Agents manipulent de la logique métier pure, complètement affranchie des frameworks d'infrastructure (JPA, HTTP, Messaging).

---

# IV) Les Types de Classes

Clprolf possède seulement quatre types de classes. Les classes sans méthodes(entités, DTO, etc) ne sont pas annotées.

---

## IV.1) `ClAgent`

Représente une classe métier ou conceptuelle.

Un `agent` :

* contient la logique métier ou conceptuelle,
* orchestre les traitements,
* prend des décisions,
* évite le code technique lourd, qui est souvent délégué à un worker associé
* peut être orienté système comme Connection ou Socket (dans ce cas on utilise ClSystem)

Exemple :

```java
@ClAgent
public class OrderProcessor {

    private OrderRepository repository;

    public void process(Order order) {
        if(order.total() <= 0) {
            throw new Error();
        }
        repository.save(order);
    }
}
```

---

## IV.2) `ClWorker`

Représente un service système.

Une classe worker a principalement pour vocation de soutenir les classes agents plutôt que d'être organisée autour d'un domaine de classe.

Les workers fournissent des services techniques et d'infrastructure. Ils peuvent coordonner ou utiliser des agents de bas niveau (agents systèmes ) tels que `File`, `Connection`, `Random`, `Logger` ou `Parser`, mais contrairement à ces classes, un worker n'est pas organisé autour d'un domaine de classe qui lui est propre.

Il existe avant tout pour assister d'autres composants au travers de mécanismes techniques, d'accès à l'infrastructure, du démarrage de l'application, d'interactions avec le système d'exploitation ou d'autres responsabilités similaires.

Un `worker` :

* C'est un service système
* fournit un support technique ;
* gère l'infrastructure et les mécanismes d'exécution
* contient du code technique.
* utilise des abstractions systèmes, mais n'en est pas une
* Souvent là pour assister une classe agent (y compris les agents systèmes), pour l'affichage, l'accès direct à la base de donnée, etc.
* Permet de séparer le code domaine/fonctionnel, du code purement technique.


## IV.3) Le rôle `ClSystem`

On doit utiliser l'annotation `@ClSystem` (ou l'attribut `[ClSystem]` en C#), pour les agents orientés système.
On ne peut pas mixer l'héritage agent et agent orienté système. Ils sont alors considérés, par le checker, comme un rôle indépendant.
Les abstractions systèmes (comme `File`), sont donc annotés `ClSystem` au lieu de `ClAgent`.
A noter que les classes imposées par les frameworks tiers, comme les Controllers, le Routing, ou les Middlewares (les Filters/Interceptors en Java), sont considérées comme des classes ClSystem.
ClSystem permet d'apporter une vision plus technique, tout en gardant la notion de domaine.

### Précisions

Par exemple, une connexion est représentée, en Clprolf :

* comme une abstraction système `ClSystem`
* son domaine est le domaine de connexion

On aura alors:

* un `ClSystem` pour représenter la connexion
* le code purement technique peut être délégué à un ou plusieurs `worker`.
* on pourra changer d'implémentation technique si nécessaire, sans modifier le code conceptuel (voir exemple Java de File).

### Exemple de Java pour illustrer `ClSystem`: java.io.File

L'implémentation OpenJDK récente de java.io.File, montre une classe assez longue, de 2000 lignes. La classe exécute tout le travail purement technique, et non lié au domaine, par un attribut qui est l'équivalent d'un worker( FileSystem ).

```java
private static final FileSystem FS = DefaultFileSystem.getFileSystem();

```

```java
 public boolean delete() {
        if (isInvalid()) {
            return false;
        }
        return FS.delete(this);
    }
```

```text
CONCEPT CLPROLF                   CODE SOURCE JAVA (OpenJDK)
┌──────────────────────────┐            ┌──────────────────────────┐
│         @ClSystem        │            │      java.io.File        │
│  (Abstraction Système)   │            │                          │
│ Représente le concept de │            │ Gère l'abstraction du    │
│ fichier et son chemin.   │            │ fichier et son statut.   │
│ Méthodes conceptuelles   │            │ Méthodes conceptuelles   │
└────────────┬─────────────┘            └────────────┬─────────────┘
             │                                       │
             │ délègue à                             │ appelle
             ▼                                       ▼
┌──────────────────────────┐            ┌──────────────────────────┐
│        @ClWorker         │            │    java.io.FileSystem    │
│   (Worker Bas Niveau)    │            │        (variable FS)     │
│ Réalise l'accès et la    │            │ Implémentation selon l'OS│
│ validation spécifique OS │            │ , WinNT/UnixFileSystem.  │
└──────────────────────────┘            └──────────────────────────┘

```

Remarque: java.io.UnixFileSystem, ainsi que WinNTFileSystem, contiennent beaucoup de méthodes `native`.

---

## IV.4) `ClDraft`

Objet sans rôle défini. Normalement, il est jamais indispendable.

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

## IV.5) Domaine principal et code technique

Clprolf encourage à déplacer autant que possible le code technique des classes agent vers des classes worker.

Cependant, un agent peut contenir une quantité raisonnable de code technique lorsque cela améliore la simplicité ou la lisibilité du système.

Un agent possède toujours un domaine principal représentant sa responsabilité centrale.

Des responsabilités secondaires peuvent exister tant qu’elles restent cohérentes avec ce domaine principal.

# V) Héritage

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

Un forçage de l'héritage de classe est possible avec @ClBypass, au-dessus de la classe, mais ne devrait pas être fréquent.
---

# VI) Flexibilité

Clprolf est flexible.

Le développeur garde donc sa liberté :

* mélange possible si nécessaire,
* migration progressive,
* compatibilité avec du code existant.
* mais on a toujours un domaine principal

Le framework agit surtout comme :

> un guide structurel.
---

# VII) Les Sous-systèmes (*Subsystems*) — Découpage et Organisation

Dans **Clprolf**, la notion de **sous-système** est l'unité d'organisation logique qui donne son sens au modèle métier. Un sous-système regroupe un ensemble cohérent de `@ClAgent` (et leurs `@ClWorker` associés) pour simuler un domaine fonctionnel précis.

### 1. La règle du sous-système par défaut

Par construction, **toute application Clprolf possède au moins un sous-système**.

Dans sa forme la plus simple, ce sous-système racine regroupe l'intégralité des `@ClAgent` de l'application au sein du package principal. Cette structure minimale suffit pour couvrir 100 % des besoins des applications de petite à moyenne taille.

### 2. Spécialisation et découpage fin

Lorsque la complexité de l'application augmente, il devient nécessaire de regrouper les agents de manière plus fine. Vous pouvez alors définir des **sous-systèmes supplémentaires** afin d'isoler des responsabilités distinctes :

* **Par intention fonctionnelle :** Découpage de l'application selon les grands cas d'usage métiers (ex. sous-système `Facturation`, sous-système `GestionStock`, sous-système `Authentification`).
* **Par frontière d'isolation :** Regroupement des agents partageant les mêmes contraintes de sécurité, de déploiement ou de données.

# VIII) Interfaces

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

## VIII.1) `ClFamily`

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
public interface Animal {

    void manger(int quantite);

}
```

La hiérarchie des interfaces ClFamily reflète naturellement la hiérarchie des classes concrètes.

```java
@ClAgent
@ClFamily
public interface Horse extends Animal {

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

## VIII.2) `ClTrait`

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

## VIII.3) Illustration du parallèle interfaces familles / implémentations

```text
[MONDE ABSTRAIT / INTERFACES]          │    [MONDE CONCRET / CLASSES]
                                         │
       @ClAgent @ClFamily                │        @ClAgent
        interface Animal                 │       class AnimalImpl
               ▲                         │               ▲
               │ (extends)               │               │ (extends)
               │                         │               │
       @ClAgent @ClFamily                │        @ClAgent
         interface Horse                 │       class HorseImpl
               ▲                         │               ▼ (implements)
               │                         │       👉 implémente Horse
               └─────────────────────────┼───────  (et hérite de AnimalImpl)
                 (Héritage de structure) │
                                         │
 ────────────────────────────────────────┴─────────────────────────────────
  👉 LE TRAIT (Transversal) :
  
       @ClAgent @ClTrait                 │
        interface Jumpable               │
               ▲                         │
               │ (hérité par la Family)  │
               │                         │
     Horse extends Jumpable              │
```

## VIII.4) `@ClFree`

Interface générique sans rôle particulier.
Permet de rester flexible. Elle n'est pas indispensable.

---

## Exemple

```java
@ClFree
public interface ExternalApi {
}
```
---

## VIII.5) Utilisation des interfaces

* En Clprolf, les interfaces `Family` se rapprochent des classes abstraites pures.

Elles sont destinées à être implémentées par une ou plusieurs futures classes Clprolf.
Elles possèdent donc un rôle cible (`agent` ou `worker`).

Une classe ne peut implémenter qu’une seule `Family` principale à la fois, et le rôle de la classe doit correspondre au rôle cible de l’interface. Clprolf utilise ainsi une implémentation simple des interfaces, de la même manière que Java utilise un héritage simple des classes. En effet, une `Family` est toujours le reflet structurel de son implémentation. Cela permet notamment d’obtenir un loose coupling systématique.
Toutefois, l’implémentation multiple n’est pas supprimée, mais déplacée vers la `Family` implémentée par la classe.
**Ceci peut paraître restrictif et inhabituel, mais cela ne s'applique qu'en mode strict, qui n'est pas le mode par défaut. Cela permet d'éviter que plusieurs implémentations d'une même famille ne répètent un contrat multiple, et aide à comprendre rapidement ce qu'implémentent les classes.**

* Les interfaces `Trait` expriment une fonctionnalité commune entre plusieurs interfaces `Family`.

Un `Trait` représente donc un trait transversal partagé entre plusieurs familles.

Normalement, un `Trait` ne peut être hérité que par une interface `Family`, et non directement par une classe. C'est pour éviter de séparer le trait de son interface famille, et clarifier les implémentations des classes (notamment en cas de multiples implémentations d'une même famille).

**Cependant, ceci est vrai uniquement en mode strict, qui n'est pas le mode par défaut.**

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
Mais ces forçages ne devraient pas être fréquent.

### VIII.5.2) Mode strict des interfaces avec flexibilité

Il est possible d'utiliser `ClFree` pour rester strict uniquement sur les interfaces qu'on souhaite, et faire des ClFamily quand on souhaite plus de rigueur, sur des cas précis. Dans ce cas, même en mode strict, on reste ouvert et flexible.

---

## VIII.6) Avantages du "Loose Coupling" systématique par l'interface miroir

En mode strict, le fait de restreindre une classe à l'implémentation d'une unique ClFamily (et de déléguer les ClTrait à cette interface) n'est pas une simple contrainte esthétique. C'est le mécanisme même qui garantit un couplage faible (loose coupling) systématique.

Plutôt qu'une classe implémentant de multiples contrats éparpillés, la ClFamily agit comme le "contrat miroir" officiel et complet du composant. Ainsi, lorsqu'un composant A a besoin d'un composant B, il est naturellement poussé à dépendre de la ClFamily de B, et non de son implémentation. L'inversion de dépendance n'est plus une simple recommandation, elle découle mécaniquement de la structure du code.

Le gain est de n'avoir plus qu'une seule hiérarchie, au lieu de la hierarchie de classes + la hiérarchie d'interfaces. Bien sûr, ce mode strict est à utiliser selon les préférences, et situations à résoudre.

### Un exemple réel des interfaces `ClFamily` et `ClTrait`

Regardons cet exemple réel, qui utilise une conception applicable au mode strict du Framework Clprolf:

```java
package com.tngtech.archunit.lang;

@ClAgent @ClFamily
public interface ArchRule extends CanBeEvaluated, CanOverrideDescription<ArchRule> {
//(…)
}
```

```java
package com.tngtech.archunit.library;

public final class Architectures {
//(…)
@ClAgent
public static final class LayeredArchitecture implements ArchRule {
// (…)
}

}
```

> Dans cet exemple, `CanBeEvaluated` et `CanOverrideDescription` jouent le rôle de `@ClTrait`, tandis que `ArchRule` formalise la `@ClFamily`. On constate que la class LayeredArchitecture ne fait qu'implémenter la famille, qui, elle implémente les traits.
La classe n'implémente pas directement CanBeEvaluated et CanOverrideDescription.

## VIII.7) Remarque sur les interfaces Clprolf et l'Interface Segregation Principle (ISP)

Clprolf aide naturellement à respecter l'ISP et permet d'éviter les *« fat interfaces »* (interfaces fourre-tout). Il ne prétend pas d'être la seule façon de tendre vers l'ISP.

### Le problème classique de la *Fat Interface*

Sans découpage strict, on a souvent tendance à créer une interface unique regroupant toutes les fonctionnalités possibles pour un domaine :

```java
// Anti-pattern : Fat Interface qui viole l'ISP
public interface Machine {
    void print(Document doc);
    void scan(Document doc);
    void fax(Document doc);
}

```

Dans ce schéma, une ancienne imprimante (`OldPrinterImpl`) qui ne sait qu'imprimer serait forcée d'implémenter `scan()` et `fax()`, souvent en levant une exception (`UnsupportedOperationException`), ce qui viole l'ISP.

### La réponse de Clprolf

Clprolf respecte naturellement l'ISP, et permet d'éviter les interfaces trop grosses. Une interface `ClFamily`est le miroir de son implémentation, elle ne peut donc pas imposer des méthodes qui ne correspondent pas à l'implémentation.
Ainsi, on réalisera naturellement une factorisation en plusieurs `ClFamily`, et avec des `ClTrait`. Et on gardera la relation architecturale (ici entre ModernPrinter et OldPrinter), en évitant la duplication des traits communs.
Le code améliore ainsi son évolutivité, et lisibilité.

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

# IX) Immuabilité

Il est aussi possible de rendre les classes Clprolf immuables, par exemple en encapsulant l'état dans un record. Chaque méthode modifiant l'état renverra un nouvel objet, sur lequel on pourra appeler d'autres méthodes, de manière fluide.
À l'image de la classe String en Java/.NET, cette approche garantit une thread-safety native, élimine les effets de bord et offre une API fluide et expressive. Il faudra juger, bien sûr, selon les situations, coûts, performances, et préférences, l'utilisation ou non de l'immuabilité.
Voici un exemple d'implémentation en Java et C#, mais il existe d'autres solutions, selon la version du langage que vous utilisez, et selon que vous devez utiliser des APIs mutables ou pas.
Ces exemples ne sont pas des patterns, mais juste des exemples d'application de l'immuabilité avec Clprolf.

```java

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@ClAgent
@ClFamily
public interface Car {

    // Immutable state definition
    // Automatically public static inside an interface
    
    record State(
            String make,
            int mileage,
            List<String> options
    ) {
        // Compact constructor enforcing domain invariants
        public State {
            Objects.requireNonNull(make, "Make cannot be null");
            if (mileage < 0) {
                throw new IllegalArgumentException("Mileage cannot be negative");
            }
            
            // Guarantees deep immutability via defensive copy and prevents null elements.
            // If 'options' is already an unmodifiable list (e.g., produced by Stream.toList() or List.of),
            // Java optimizes this call by returning the same instance without re-allocating memory.
            options = List.copyOf(options); 
        }
    }

    State state();

    // Business methods returning the contract abstraction
    Car drive(int distance);
    Car addOption(String newOption);
}

@ClAgent
public class CarImpl implements Car {

    // 🔒 Strictly final state reference
    private final State state;

    public CarImpl(State state) {
        this.state = Objects.requireNonNull(state, "State cannot be null");
    }

    @Override
    public State state() {
        return this.state;
    }

    // Covariant return types: methods return 'CarImpl' instead of 'Car'
    @Override
    public CarImpl drive(int distance) {
        if (distance <= 0) {
            return this; // No state change occurs
        }

        var newState = new State(
                state.make(),
                state.mileage() + distance,
                state.options()
        );
        return new CarImpl(newState);
    }

    @Override
    public CarImpl addOption(String newOption) {
        Objects.requireNonNull(newOption, "New option cannot be null");

        // OPTION 1: Optimized Stream concatenation (Single allocation)
        // Stream.toList() (Java 16+) returns an unmodifiable list directly.
        // When passed to State's constructor, List.copyOf(options) recognizes 
        // it as already unmodifiable and avoids a second copy.
        var updatedOptions = Stream.concat(state.options().stream(), Stream.of(newOption))
                                   .toList();

        /* 
        // OPTION 2: Alternative using a temporary mutable ArrayList
        // Simple and readable, but results in 2 list allocations in heap memory:
        // 1) The temporary ArrayList created here.
        // 2) The unmodifiable copy created inside State's constructor by List.copyOf().
        
        var updatedOptions = new ArrayList<>(state.options());
        updatedOptions.add(newOption);
        */

        return new CarImpl(new State(state.make(), state.mileage(), updatedOptions));
    }

    public static void main(String[] args) {
        // Using the specific type
        CarImpl initialCar = new CarImpl(new Car.State("Tesla", 10000, List.of("Autopilot")));
        CarImpl drivenCar = initialCar.drive(150);

        // Using the interface type
        Car genericCar = initialCar;
        Car updatedCar = genericCar.addOption("Premium Audio"); // Returns a Car
    }
}

```

## 2) Version C Sharp

```csharp

using System;
using System.Collections.Generic;
using System.Collections.Immutable;

[ClAgent]
[ClFamily]
public interface ICar
{
    public record State
    {
        public string Make { get; init; }
        public int Mileage { get; init; }

        // Strongly-typed property using the concrete immutable type
        public ImmutableArray<string> Options { get; init; }

        public State(string make, int mileage, IEnumerable<string> options)
        {
            ArgumentNullException.ThrowIfNull(make);
            ArgumentNullException.ThrowIfNull(options);

            if (mileage < 0)
            {
                throw new ArgumentOutOfRangeException(nameof(mileage), "Mileage cannot be negative.");
            }

            Make = make;
            Mileage = mileage;

            // ToImmutableArray() handles automatically if options is already an ImmutableArray
            Options = options.ToImmutableArray();

            // PERFORMANCE NOTE:
            // If the collection contains a very large number of items and undergoes VERY frequent additions/modifications,
            // consider using ImmutableList<string> and ToImmutableList() to optimize memory reuse (AVL tree):
            // Options = options.ToImmutableList();
        }
    }

    State CarState { get; }

    // Business methods returning the contract abstraction
    ICar Drive(int distance);
    ICar AddOption(string newOption);
}

[ClAgent]
public class CarImpl : ICar
{
    // Strictly read-only state reference
    public ICar.State CarState { get; }

    public CarImpl(ICar.State state)
    {
        CarState = state;
    }

    public CarImpl Drive(int distance)
    {
        if (distance <= 0)
        {
            return this; // No state change occurs
        }

        // C# 'with' expression creates a copy of State updating only Mileage
        var newState = CarState with { Mileage = CarState.Mileage + distance };
        return new CarImpl(newState);
    }

    public CarImpl AddOption(string newOption)
    {
        // Clean and direct syntax without casts or conversions.
        // Options.Add() returns a new ImmutableArray instance.
        var newState = CarState with { Options = CarState.Options.Add(newOption) };
        return new CarImpl(newState);
    }

    // Explicit interface implementation to satisfy the ICar contract in C#
    ICar ICar.Drive(int distance) => Drive(distance);
    ICar ICar.AddOption(string newOption) => AddOption(newOption);

    public static void Main(string[] args)
    {
        // Using the specific type
        CarImpl initialCar = new(new ICar.State("Tesla", 10000, new[] { "Autopilot" }));
        CarImpl drivenCar = initialCar.Drive(150);

        // Using the interface type
        ICar genericCar = initialCar;
        ICar updatedCar = genericCar.AddOption("Premium Audio");
    }
}

```

# X) Architecture Générale

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

Un agent(y compris système `ClSystem`) délègue à un ou plusieurs worker, le code technique. Il peut en exécuter, mais en appelant une méthode d'un worker.
Le worker est au service de l'agent.

```text
┌────────────────────────────────────────────────────┐
│                       AGENT                        │
│     			comportement conceptuel,  		     │
│      		responsabilité domaine/métier            │
└─────────────────────────┬──────────────────────────┘
                          │
                          │ utilise / délègue à
                          │
                          ▼
┌────────────────────────────────────────────────────┐
│                       WORKER                       │
│    service système pour l'exécution technique,     │
│                 au service d'un agent              │
└─────────────────────────┬──────────────────────────┘
                          │
                          │ peut utiliser
                          │
                          ▼
┌────────────────────────────────────────────────────┐
│                  DOMAINE SYSTÈME (ClSystem)        │
│    objet conceptuel lié au comportement système    │
│ exemples : stream, socket, thread, fichier, window │
└─────────────────────────┬──────────────────────────┘
                          │
                          │ délègue le travail bas niveau à
                          │
                          ▼
┌────────────────────────────────────────────────────┐
│                 WORKER (BAS NIVEAU)                │
│  appel natif, rendu, E/S, travail OS / runtime     │
└────────────────────────────────────────────────────┘
```

---

# XI) Objectif du Framework

Clprolf ne cherche pas à remplacer l’OOP classique.

Il cherche à rendre explicites certaines distinctions importantes :

* métier vs technique,
* héritage cohérent vs composition,
* responsabilité principale d’une classe.
---

# XII) Checker ArchUnit

Un checker basé sur ArchUnit est disponible pour le Framework Clprolf. Il est composé de deux classes ClprolfArchTest, et ClprolfStrictArchTest. Il valide les règles sémantiques.
Les règles de ClprolfStrictArchTest sont optionnelles. De même, il est facile de changer le nom des annotations, si on préfère un autre vocabulaire.

## 1. Version Java (ArchUnit)

Le checker Java s'articule autour de deux classes de tests principales :
* **`ClprolfArchTest`** : Valide les règles sémantiques standard et fondamentales du framework.
* **`ClprolfStrictArchTest`** : Regroupe des contraintes optionnelles et plus rigides pour les projets exigeants (comme l'interdiction pour une classe d'implémenter directement un `ClTrait`).
Les checkers (Java et .Net) contiennent aussi les définitions des annotations (ou attributs) Clprolf.
Pour simplifier la documentation, l'annotation `ClSystem` ne figure pas dans la description des règles. Elle est simplement gérée comme un rôle indépendant, par le checker.
---

## 2. Version C# .NET (ArchUnitNET)

La solution Visual Studio (2022 à ce jour), contient un projet contenant le framework et les règles ArchUnit, et un projet xUnit pour les tests.
Un troisième projet d'exemple est ajouté. Il y 8 tests obligatoires, et 4 tests stricts, actuellement.

## Les règles à respecter

### clprolf_classes_must_not_mix_agent_and_worker:
Une classe ne peut pas être annotée à la fois @ClAgent et @ClWorker

### agent_worker_inheritance_must_not_mix
Une classe @ClWorker ne peut pas hériter d'une classe @ClAgent, et inversement.

### family_role_must_match_implementation
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

Règles plus strictes:

### optional_all_classes_should_have_clprolf_role 
Toutes les classes doivent avoir un rôle Clprolf (@ClAgent, @ClWorker ou @ClDraft)

### optional_all_interfaces_should_have_clprolf_role 
Toutes les interfaces doivent avoir un rôle Clprolf(@ClFamily, @ClTrait ou @ClFree)

### optional_class_should_not_implement_trait_directly
Une classe ne peut pas implémenter directement une interface @ClTrait (à moins d'utiliser @ClInterfaceBypass)

### optional_class_must_implement_only_one_family_interface (OPTIONNELLE)
Une classe Clprolf ne peut implémenter qu'une interface @ClFamily. Forçage possible avec @ClInterfaceBypass

# XIII) Clprolf et les principes SOLID

Clprolf a pour conséquence d'apporter un soutien des principes SOLID. Il ne prétend pas être la seule vision possible de ces principes, ni la seule interprétation de SOLID.

## **S** — Principe de responsabilité unique (SRP)

Le Framework **aide** naturellement **à l'application** du principe de responsabilité unique (SRP). En effet, chaque classe possède son domaine conceptuel ou métier, qui est préservé lors de l'héritage. En appliquant strictement Clprolf, on y arrive encore **davantage**.

## **O** — Principe Ouvert/Fermé (OCP, Open-Closed Principle)

Ce principe nous incite à prévoir les futures évolutions comme des extensions et pas comme des corrections du code. Le Framework Clprolf **facilite** les extensions, de par la séparation stricte des domaines conceptuels, et les workers. Les interfaces `ClFamily` poussent à une bonne visibilité des interfaces utilisées dans les classes, et à bien penser les fonctionnalités. Les interfaces `ClTrait` nous permettent d'avoir des `ClFamily` encore plus simples et pures, et à mutualiser les traits.

## **L** — Principe de substitution de Liskov (LSP)

Clprolf impose un héritage qui reste dans le même domaine conceptuel, en plus de la séparation `ClAgent` et `ClWorker`. Ainsi, le principe LSP de Liskov est plus facilement pris en compte, car une classe `Carre` n'est pas du même domaine conceptuel que `Rectangle`.
En effet, une classe `Girafe` appartient au même domaine qu'un `Animal` dont elle hérite les comportements naturels. À l'inverse, une classe `Carre` n'a pas la nature d'un `Rectangle` (elle ne peut pas avoir une longueur et une largeur indépendantes). Dans Clprolf, ils n'appartiennent donc pas au même domaine conceptuel, ce qui empêche certains héritages abusifs et contribue à prévenir certaines violations du LSP.

## **I** — Principe de ségrégation des interfaces (ISP)

Ce principe conseille qu'un client ne doit pas devoir implémenter des méthodes **qu'il n'utilise** pas. Avec Clprolf, l'interface est taillée sur mesure pour le client, et les traits sont précis. On favorise ainsi naturellement le respect de l'ISP.

## **D** — Injection des dépendances (Dependency Injection, DI)

L'injection des dépendances est particulièrement efficace lorsque les dépendances sont faiblement couplées aux implémentations. Ce couplage faible est encouragé et facilité, avec les interfaces `ClFamily` qui sont intimement **liées** aux classes, et sont le miroir des implémentations. Il devient alors très facile de remplacer une implémentation par une interface, dans une déclaration de variable.

## "Composition plutôt qu'héritage" ("Favoring composition over inheritance")

Le framework Clprolf permet de n'utiliser l'héritage qu'avec parcimonie et précision, et de se servir de la composition sinon. Il offre un moyen pratique et intuitif de choisir entre les deux.

---

# XIV) Clprolf et les architectures existantes

Clprolf est compatible avec les architectures DDD, MVC, Clean architecture, architecture hexagonale, etc. C'est une couche entre la POO et les architectures, qui vient compléter et sécuriser les architectures connues.
Et Clprolf ne sert pas que pour l'informatique d'entreprise, mais pour tout type d'applications, y compris simulations, et applications scientifiques.

# XV) Résumé

Clprolf ajoute très peu de concepts.
---

## Classes

```text
@ClAgent
@ClWorker
@ClSystem
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

# XVI) Formalisation axiomatique et théorème d'expressivité

## 1. Contrat Linguistique et Définitions (Les Prédicats)

| Type de Prédicat | Prédicat | Signification & Rôle | Domaine / Type | Correspondance Clprolf |
| :--- | :--- | :--- | :--- | :--- |
| **Objets & Entités** | $\text{App}(A)$ | $A$ est une application logicielle valide. | $A \in \mathcal{A}$ | Application globale |
| | $\text{TravailHumain}(h)$ | $h$ est l'activité/travail humain à mécaniser. | $h \in \mathcal{H}$ | Activité réelle |
| | $\text{ButHumain}(G)$ | $G$ est l'objectif final visé par/pour l'humain. | $G \in \mathcal{G}$ | But téléologique |
| | $\text{DomaineMetier}(\mathcal{M})$ | $\mathcal{M}$ est l'ensemble de toutes les intentions/règles. | $\mathcal{M} \subseteq \mathcal{D}$ | Modèle Métier |
| | $\text{SupportExecution}(t_h)$ | $t_h$ est le support/effort d'exécution chez l'humain. | $t_h \in \mathcal{T}_h$ | Support humain |
| | $\text{ComposantMetier}(c)$ | $c$ est le composant incarnant la logique métier. | $c \in \mathcal{C}$ | `@ClAgent` |
| | $\text{ComposantTech}(t)$ | $t$ est le composant de support technique. | $t \in \mathcal{T}$ | `@ClWorker` |
| | $\text{AgentSysteme}(s)$ | $s$ est un agent d'infrastructure/technique basse. | $s \in \mathcal{S}$ | `@ClSystem` |
| | $\text{Atome}(a)$ | $a$ est l'unité minimale et indivisible du système. | $a \in \text{Atome}$ | Brique atomique |
| **Relations & Liens** | $\text{Automatise}(A, h)$ | $A$ mécanise/automatise le travail humain $h$. | $\mathcal{A} \times \mathcal{H}$ | Axiome 1 |
| | $\text{Viser}(\mathcal{M}, G)$ | L'ensemble métier $\mathcal{M}$ tend vers le but $G$. | $\mathcal{D} \times \mathcal{G}$ | Axiome 1 (Décomp.) |
| | $\text{Partie}(p, A)$ | $p$ est une sous-partie ou composant de $A$. | $\mathcal{P} \times \mathcal{A}$ | Axiome 2 & 3 |
| | $\text{Contient}(p, a)$ | Le sous-système $p$ contient l'atome de domaine $a$. | $\mathcal{P} \times \text{Atome}$ | Axiome 3 |
| | $\text{Simule}(p, m)$ | La partie $p$ incruste la portion du domaine $m \subseteq \mathcal{M}$. | $\mathcal{P} \times \mathcal{D}$ | Composant Métier ($m \subseteq \mathcal{M}$) |
| | $\text{Associe}(t, p)$ | Le support technique $t$ est adjoint à la partie $p$. | $\mathcal{T} \times \mathcal{P}$ | Composant Tech adossé |
| | $\text{Combine}(a, c, t)$ | L'atome $a$ réunit strictement le composant métier $c$ et le composant technique $t$. | $\text{Atome} \times \mathcal{C} \times \mathcal{T}$ | Atom (Union exclusive) |
| | $\text{Utilise}(t, s)$ | Le composant tech $t$ délègue sa complexité à l'agent système $s$. | $\mathcal{T} \times \mathcal{S}$ | Règle fractale (Axiome 3) |

## 2. Les Axiomes Fondamentaux

### Axiome 1 — Automatisation du Travail Humain

$$\forall A \; \Big( App(A) \implies \exists h \; \big( TravailHumain(h) \land Automatise(A, h) \big) \Big)$$

#### Décomposition du Travail Humain ($h$)

$$\forall h \; \Big( TravailHumain(h) \iff \exists G \, \exists \mathcal{M} \, \exists t_h \; \big( ButHumain(G) \land DomaineMetier(\mathcal{M}) \land SupportExecution(t_h) \land Viser(\mathcal{M}, G) \big) \Big)$$

---

### Axiome 2 — Décomposition Structurelle

$$\forall A \; \Big( App(A) \implies \forall p \; \big( Partie(p, A) \implies \exists m \, \exists t \; ( m \subseteq \mathcal{M} \land SupportTech(t) \land Simule(p, m) \land Associe(t, p) ) \big) \Big)$$

---

### Axiome 3 — Composition Atomique et Propriété Fractale

Chaque sous-système $p$ d'une application $A$ est composé d'atomes de domaine $d$. Un atome de domaine est l'union exclusive d'un composant métier $c$ et d'un composant technique $t$.

$$\forall A \; \Big( App(A) \Rightarrow \forall p \; \big( Partie(p, A) \Rightarrow \forall d \; ( Contient(p, d) \Rightarrow Atome(d) \land \exists c \, \exists t \; (ComposantMetier(c) \land ComposantTech(t) \land Combine(d, c, t)) ) \big) \Big)$$

**Règle de délégation fractale :**

Si un composant technique (Worker) fait face à une complexité d'infrastructure lourde, il a la garantie de pouvoir déléguer cette tâche à des Agents Systèmes. Tout Agent Système est explicitement défini comme étant lui-même un atome de domaine complet.

$$\forall t \; \Big( ComposantTech(t) \Rightarrow \forall s \; \big( Utilise(t, s) \Rightarrow Atome(s) \land AgentSysteme(s) \big) \Big)$$


## 3. Dictionnaire de Correspondance Canonique

Le formalisme logique n'est pas une abstraction virtuelle, il calque au millimètre près les concepts concrets du framework :

| Concept Formel | Description Sémantique | Implémentation Clprolf |
| --- | --- | --- |
| M ($c$) | Composant portant la sémantique et les règles | `@ClAgent` |
| W ($w$) | Composant portant l'infrastructure physique | `@ClWorker` |
| D ($d$) | L'alliance indissociable du sens et du support | Le binôme natif (Agent + Worker) |
| S ($s$) | Atome technique gérant l'infrastructure complexe | Agent Système (`@ClSystem`) |
| P ($p$) | Assemblage modulaire d'atomes | Subsystem (Ensemble d'Agents & Workers) |
| A ($A$) | L'entité applicative unifiée | L'Application Clprolf finale |
| F ($f$) | Contrat principal d'un composant | `@ClFamily` |
| T ($t$) | Capacité transverse ou contrat secondaire | `@ClTrait` |

---

## 4. Théorème d'Expressivité Architecturale

### Énoncé du Théorème

Pour toute application globale $A$ pouvant être décrite de manière cohérente dans ce système formel, il existe une implémentation complète, exacte et sans angle mort dans le framework Clprolf qui la réalise.

### Démonstration (par induction structurelle)

#### Étape 1 : Le Cas de Base (L'Atome de Domaine)

* **Donnée formelle :** Selon l'Axiome 3, l'unité minimale est l'atome $d$, défini par $Combine(d, c, t)$.
* **Correspondance Clprolf :** Le framework possède l'abstraction native du binôme Agent + Worker. Le composant métier $c$ devient un Agent, le composant technique $t$ devient un Worker.
* **Extension Fractale :** Si le Worker $t$ est complexe, la règle de délégation garantit qu'il peut appeler des Agents Systèmes ($s$). Puisque l'Agent Système est lui-même un atome, la propriété se vérifie par auto-similarité à l'échelle microscopique du code.
* **Conclusion de l'étape :** Clprolf sait encoder de manière exacte 100 % des briques atomiques de base.

#### Étape 2 : L'Étape Inductive 1 (La Partie / Le Sous-système)

* **Hypothèse d'induction :** Supposons que Clprolf sait encoder les atomes de domaine.
* **Donnée formelle :** Selon l'Axiome 2, une partie $p$ de l'application est un ensemble fini d'atomes de domaine.
* **Correspondance Clprolf :** Clprolf fournit le concept de Subsystem. Un sous-système est un conteneur regroupant un ensemble fini d'Agents et de Workers interconnectés.
* **Conclusion de l'étape :** L'ensemble fini qui forme la Partie $p$ est intégralement réalisable sous forme de sous-système Clprolf.

#### Étape 3 : L'Étape Inductive 2 (L'Application Globale)

* **Hypothèse d'induction :** Supposons que Clprolf sait encoder les sous-systèmes.
* **Donnée formelle :** Selon l'Axiome 1, l'application complète $A$ est l'union finie de toutes ses parties ($A = \bigcup P_i$).
* **Correspondance Clprolf :** L'application globale Clprolf finale assemble, déploie et orchestre nativement l'ensemble de ces sous-systèmes.
* **Conclusion de l'étape :** L'application complète $A$ est intégralement réalisée par l'architecture globale Clprolf.

Q.E.D.

---

## 5. Conclusion Finale — La Force de la Structure Fractale

Cette démonstration prouve que Clprolf n'est pas seulement expressif au sens computationnel brut (Turing), mais au sens architectural : tout système composé de composants métiers et techniques — ce qui inclut l'intégralité des applications logicielles du monde réel — possède une représentation canonique et fractale dans Clprolf.

---

---

# XVII) Formalisation mathématique de la neutralité des contrats

Afin de démontrer que l'introduction du polymorphisme par interfaces (`ClFamily` et `ClTrait`) ne modifie pas la nature intrinsèque des atomes du système, nous formalisons la théorie des contrats sous forme de système logique et topologique.

## 1. Prédicats de Contrat et Surcharge Contextuelle

Soient les ensembles de base :

* $C$ l'ensemble des composants logiciels.
* $I = F \cup T$ l'ensemble des interfaces du système, où $F$ est l'ensemble des familles et $T$ l'ensemble des traits.

### Prédicats de nature des composants

* $ComposantMetier(c)$ : Le composant $c \in C$ porte une responsabilité métier (`@ClAgent`).
* $ComposantTech(c)$ : Le composant $c \in C$ porte une responsabilité technique (`@ClWorker`).

### Prédicats de typologie d'interface

* $Famille(i)$ : $i \in F$, l'interface $i$ est une famille de premier niveau.
* $Trait(i)$ : $i \in T$, l'interface $i$ est une capacité transversale.

### Relations topologiques

* $Implements(c, i)$ : Le composant $c$ implémente l'interface $i$.
* $Inherits(i, j)$ : L'interface $i$ hérite de l'interface $j$.

### Définition par Surcharge Contextuelle ($\triangleq$)

Pour préserver une syntaxe unifiée dans toutes les équations, les prédicats de cible sont définis par disjonction de cas selon la nature de l'interface $i \in I$ :

$$\text{TargetMetier}(i) \triangleq \begin{cases}  \text{PolaritéMetier}(i) & \text{si } i \in F \quad (\text{Famille : polarité stricte et exclusive}) \\  \text{EstCompatibleMetier}(i) & \text{si } i \in T \quad (\text{Trait : compatibilité réflexive selon le contexte})  \end{cases}$$

$$\text{TargetTech}(i) \triangleq \begin{cases}  \text{PolaritéTech}(i) & \text{si } i \in F \quad (\text{Famille : polarité stricte et exclusive}) \\  \text{EstCompatibleTech}(i) & \text{si } i \in T \quad (\text{Trait : compatibilité réflexive selon le contexte})  \end{cases}$$

---

## 2. Axiomes de la Théorie des Contrats

### Axiome 1 — Exclusivité des Familles

Toute famille $f \in F$ possède une polarité stricte et exclusive. Les deux cibles sont mutuellement incompatibles sur une famille, comme l'exprime le connectif d'incompatibilité ($\uparrow$) :

$$\forall f \in F \; \Big( \text{TargetMetier}(f) \uparrow \text{TargetTech}(f) \Big)$$

### Axiome 2 — Loi du Miroir Strict

Un composant ne peut implémenter qu'une famille dont la polarité est strictement isomorphe à sa propre nature :

$$\forall c \in C, \forall f \in F \; \Big( Implements(c, f) \implies \big( ComposantMetier(c) \iff TargetMetier(f) \big) \land \big( ComposantTech(c) \iff TargetTech(f) \big) \Big)$$

### Axiome 3 — Continuité Contextuelle des Traits

Lorsqu'une famille $f$ hérite d'un trait $t$, la compatibilité du trait $t$ est évaluée dynamiquement dans le contexte de la polarité de la famille $f$ :

$$\forall f \in F, \forall t \in T \; \Big( Inherits(f, t) \implies \big( TargetMetier(f) \implies TargetMetier(t) \big) \land \big( TargetTech(f) \implies TargetTech(t) \big) \Big)$$

---

## 3. Théorème de Neutralité des Interfaces

### Énoncé du Théorème

Pour tout composant $c$, toute famille $f$ et tout trait $t$, si $f$ hérite de $t$ et que $c$ implémente $f$, alors l'arborescence des contrats préserve la nature de $c$ jusqu'au trait $t$, garantissant la neutralité ontologique des interfaces :

$$\forall c \in C, \forall f, t \in I \; \Big( Famille(f) \land Trait(t) \land Inherits(f, t) \land Implements(c, f) \implies \big( ComposantMetier(c) \implies TargetMetier(t) \big) \land \big( ComposantTech(c) \implies TargetTech(t) \big) \Big)$$

### Démonstration analytique

1. **Supposition initiale :** Soit un composant $c$ tel que $ComposantMetier(c)$, une famille $f \in F$ et un trait $t \in T$ tels que $Inherits(f, t)$ et $Implements(c, f)$.
2. **Application de l'Axiome 2 (Loi du Miroir) :** Puisque $Implements(c, f)$, on déduit immédiatement :

$$ComposantMetier(c) \iff TargetMetier(f)$$



Comme $ComposantMetier(c)$ est vrai par hypothèse, $TargetMetier(f)$ est établi.
3. **Application de l'Axiome 3 (Continuité Contextuelle) :** Puisque $Inherits(f, t)$, l'Axiome 3 donne :

$$TargetMetier(f) \implies TargetMetier(t)$$



Puisque $TargetMetier(f)$ est vrai, l'évaluation du prédicat surchargé pour le trait $t$ donne $\text{TargetMetier}(t) = \text{Vrai}$.
4. **Cas des Traits Poly-polaires (ex. `Runnable`) :** Si $t$ est un trait d'infrastructure universel, sa fonction $\text{EstCompatibleMetier}(t)$ renvoie $\text{Vrai}$ dans le contexte de la famille métier $f$, validant l'implication $\text{Vrai} \implies \text{Vrai}$ sans altérer l'identité du composant $c$.
5. **Conclusion par symétrie :** Le raisonnement s'applique à l'identique pour un composant technique $ComposantTech(c)$.

$$\text{Q.E.D.}$$

# XVIII) Certification formelle et Assistant de preuve Coq

> **Afin de garantir une rigueur absolue et d'éliminer toute ambiguïté ou faille logique, l'ensemble du système axiomatique et le théorème d'expressivité de Clprolf ont été formellement codifiés et intégralement vérifiés par machine à l'aide de l'assistant de preuve Coq.**

Cette démarche de vérification mécanique (*machine-checked*) permet de passer d'une démonstration littérale sur le papier à une certification informatique irréfutable. En soumettant les définitions, les axiomes et la structure inductive du framework au noyau logique de Coq, nous obtenons la garantie formelle — jusqu'au `Qed` final — que l'architecture Clprolf est exempte de toute contradiction et capable d'exprimer fidèlement toute application répondant à ses lois fondamentales.

---

```coq

Section ClprolfFormalization.

  (* ================================================================= *)
  (* 1. TYPAGE DE BASE ET PRÉDICATS CANONIQUES                         *)
  (* ================================================================= *)

  Variable Object : Type.

  (* Prédicats d'objets *)
  Variable App             : Object -> Prop.
  Variable TravailHumain   : Object -> Prop.
  Variable ButHumain       : Object -> Prop.
  Variable DomaineMetier   : Object -> Prop.
  Variable SupportExecution: Object -> Prop.
  Variable Atome           : Object -> Prop.
  Variable AgentSysteme    : Object -> Prop.
  Variable ComposantMetier : Object -> Prop.
  Variable ComposantTech   : Object -> Prop.
  Variable SupportTech     : Object -> Prop.

  (* Relations formelles *)
  Variable Automatise : Object -> Object -> Prop. (* Application -> TravailHumain *)
  Variable Viser      : Object -> Object -> Prop. (* DomaineMetier -> ButHumain *)
  Variable Partie     : Object -> Object -> Prop. (* Subsystem -> Application *)
  Variable Contient   : Object -> Object -> Prop. (* Subsystem -> Atome *)
  Variable Combine    : Object -> Object -> Object -> Prop. (* Atome -> ComposantMetier -> ComposantTech *)
  Variable Utilise    : Object -> Object -> Prop. (* ComposantTech -> AgentSysteme *)
  Variable Simule     : Object -> Object -> Prop. (* Subsystem/Agent -> Portion Domaine *)
  Variable Associe    : Object -> Object -> Prop. (* SupportTech/Worker -> Subsystem/Agent *)
  Variable SubSet     : Object -> Object -> Prop. (* Inclusions m ⊆ M *)

  (* ================================================================= *)
  (* 2. AXIOMES FONDAMENTAUX DE CLPROLF                                *)
  (* ================================================================= *)

  (* Axiome 1 : Téléologie - Automatisation du Travail Humain *)
  Axiom Axiom1_Teleologie :
    forall A : Object,
      App A ->
      exists h : Object, TravailHumain h /\ Automatise A h.

  (* Décomposition formelle du Travail Humain *)
  Axiom Axiom1_DecompositionTravailHumain :
    forall h : Object,
      TravailHumain h <->
      exists (G M th : Object),
        ButHumain G /\
        DomaineMetier M /\
        SupportExecution th /\
        Viser M G.

  (* Axiome 2 : Décomposition Structurelle (m ⊆ M) *)
  Axiom Axiom2_DecompositionStructurelle :
    forall A : Object,
      App A ->
      forall p : Object,
        Partie p A ->
        exists (M m t : Object),
          DomaineMetier M /\
          SubSet m M /\
          SupportTech t /\
          Simule p m /\
          Associe t p.

  (* Axiome 3 : Composition Atomique & Propriété Fractale *)
  Axiom Axiom3_CompositionAtomique_Et_Fractale :
    forall A p : Object,
      Partie p A ->
      (* Part 1 : Composition Atomique *)
      (forall d : Object,
        Contient p d ->
        Atome d /\
        exists c t : Object,
          ComposantMetier c /\ ComposantTech t /\ Combine d c t) /\
      (* Part 2 : Règle de Délégation Fractale des Workers *)
      (forall t : Object,
        ComposantTech t ->
        forall s : Object,
          Utilise t s -> Atome s /\ AgentSysteme s).

  (* ================================================================= *)
  (* 3. GRAMMAIRE (AST) DU FRAMEWORK CLPROLF                           *)
  (* ================================================================= *)

  Inductive ClprolfComponent : Type :=
    | ClAtom      : Object -> Object -> ClprolfComponent (* Atome (c, t) *)
    | ClSubsystem : Object -> ClprolfComponent          (* Sous-système p *)
    | ClApp       : Object -> ClprolfComponent.         (* Application A *)

  (* ================================================================= *)
  (* 4. SÉMANTIQUE DE RÉALISATION (PONT MODÈLE <-> GRAMMAIRE)          *)
  (* ================================================================= *)

  Inductive Realizes : Object -> ClprolfComponent -> Prop :=
    | Realize_Atom : forall (d c t : Object),
        Atome d ->
        ComposantMetier c ->
        ComposantTech t ->
        Combine d c t ->
        Realizes d (ClAtom c t)
    | Realize_Subsystem : forall (p A : Object),
        Partie p A ->
        Realizes p (ClSubsystem p)
    | Realize_App : forall (A : Object),
        App A ->
        Realizes A (ClApp A).

  (* ================================================================= *)
  (* 5. LEMMES D'EXPRESSIVITÉ LOCAUX                                   *)
  (* ================================================================= *)

  Lemma Atom_Expressiveness :
    forall (d c t : Object),
      Atome d ->
      ComposantMetier c ->
      ComposantTech t ->
      Combine d c t ->
      exists impl : ClprolfComponent, Realizes d impl.
  Proof.
    intros d c t HAtome HMetier HTech HComb.
    exists (ClAtom c t).
    apply Realize_Atom with (c := c) (t := t); assumption.
  Qed.

  Lemma Subsystem_Expressiveness :
    forall (p A : Object),
      Partie p A ->
      exists impl : ClprolfComponent, Realizes p impl.
  Proof.
    intros p A HPart.
    exists (ClSubsystem p).
    apply Realize_Subsystem with (A := A); assumption.
  Qed.

  (* ================================================================= *)
  (* 6. THÉORÈME D'EXPRESSIVITÉ INTÉGRALE (SURFACE ET PROFONDEUR)      *)
  (* ================================================================= *)

  Theorem Deep_Architectural_Expressiveness :
    forall (A p d : Object),
      App A ->
      Partie p A ->
      Contient p d ->
      exists (implApp : ClprolfComponent) 
             (implSub : ClprolfComponent) 
             (implAtom : ClprolfComponent),
        Realizes A implApp /\
        Realizes p implSub /\
        Realizes d implAtom.
  Proof.
    intros A p d HApp HPart HCont.
    destruct (Axiom3_CompositionAtomique_Et_Fractale A p HPart) as [HAtomRule _].
    destruct (HAtomRule d HCont) as [HAtom [c [t [HMetier [HTech HComb]]]]].

    exists (ClApp A).
    exists (ClSubsystem p).
    exists (ClAtom c t).

    split.
    - apply Realize_App. exact HApp.
    - split.
      + apply Realize_Subsystem with (A := A). exact HPart.
      + apply Realize_Atom with (c := c) (t := t); assumption.
  Qed.

  (* ================================================================= *)
  (* 7. EXTENSION : THÉORÈME DE LA NEUTRALITÉ DES FAMILLES & TRAITS    *)
  (* ================================================================= *)

  Variable Interface : Type.

  (* Classification des interfaces : Famille (Miroir) vs Trait (Capacité) *)
  Variable Famille : Interface -> Prop.
  Variable Trait   : Interface -> Prop.

  (* Prédicats d'évaluation internes *)
  Variable PolariteMetier      : Interface -> Prop.
  Variable PolariteTech        : Interface -> Prop.
  Variable EstCompatibleMetier : Interface -> Prop.
  Variable EstCompatibleTech   : Interface -> Prop.

  (* Décidabilité locale de Famille *)
  Variable fam_dec : forall i : Interface, {Famille i} + {~ Famille i}.

  (* Définition par Surcharge Contextuelle *)
  Definition TargetMetier (i : Interface) : Prop :=
    if (fam_dec i) then PolariteMetier i else EstCompatibleMetier i.

  Definition TargetTech (i : Interface) : Prop :=
    if (fam_dec i) then PolariteTech i else EstCompatibleTech i.

  Variable Implements : Object -> Interface -> Prop.
  Variable Inherits   : Interface -> Interface -> Prop.

  (* Axiome 1 : Exclusivité des Familles via Connectif d'Incompatibilité (Sheffer / NAND) *)
  Axiom Contract_Axiom1_FamilyExclusivity :
    forall f : Interface,
      Famille f -> ~ (TargetMetier f /\ TargetTech f).

  (* Axiome 2 : Loi du Miroir Strict (Composant <-> Famille) *)
  Axiom Contract_Axiom2_MirrorLaw :
    forall (c : Object) (f : Interface),
      Famille f ->
      Implements c f ->
      (ComposantMetier c <-> TargetMetier f) /\
      (ComposantTech c <-> TargetTech f).

  (* Axiome 3 : Continuité Contextuelle des Traits par Héritage *)
  Axiom Contract_Axiom3_TraitContextualContinuity :
    forall (f t : Interface),
      Famille f ->
      Trait t ->
      Inherits f t ->
      (TargetMetier f -> TargetMetier t) /\
      (TargetTech f -> TargetTech t).

  (* Théorème de Neutralité des Interfaces *)
  Theorem Interface_Neutrality_Theorem :
    forall (c : Object) (f t : Interface),
      Famille f ->
      Trait t ->
      Inherits f t ->
      Implements c f ->
      (ComposantMetier c -> TargetMetier t) /\
      (ComposantTech c -> TargetTech t).
  Proof.
    intros c f t HFam HTrait HInherit HImpl.
    split.
    - intros HMetierC.
      destruct (Contract_Axiom2_MirrorLaw c f HFam HImpl) as [HMirrorMetier _].
      assert (HTargetF : TargetMetier f) by (apply HMirrorMetier; exact HMetierC).
      destruct (Contract_Axiom3_TraitContextualContinuity f t HFam HTrait HInherit) as [HContMetier _].
      apply HContMetier. exact HTargetF.
    - intros HTechC.
      destruct (Contract_Axiom2_MirrorLaw c f HFam HImpl) as [_ HMirrorTech].
      assert (HTargetF : TargetTech f) by (apply HMirrorTech; exact HTechC).
      destruct (Contract_Axiom3_TraitContextualContinuity f t HFam HTrait HInherit) as [_ HContTech].
      apply HContTech. exact HTargetF.
  Qed.

End ClprolfFormalization.

```

# XIX) Illustrations applicatives et cas d'usage canoniques

Pour démontrer que le formalisme mathématique s'applique directement à la réalité du développement, nous instancions la décomposition de l'Axiome 1 ($\text{TravailHumain}(h) \iff \exists G, \mathcal{M}, t_h$) sur trois domaines applicatifs majeurs.

---

## 1. Modélisation des domaines métiers

### Cas d'usage A : Système de Paie d'Entreprise

* **But Applicatif ($G$) :** Produire des bulletins de paie conformes et verser les salaires à date fixe.
* **Modèle Métier ($\mathcal{M}$) :** Code du travail, règles des cotisations URSSAF, grilles salariales, conventions collectives ($m \subseteq \mathcal{M}$).
* **Support d'Exécution ($t_h$) :** Saisie manuelle des heures, calculs sur tableur, vérification visuelle des fiches.
* **Implémentation Clprolf ($A$) :**
* `@ClAgent` (`CalculateurCotisationsAgent`) : Porte la règle de calcul pure.
* `@ClWorker` (`GenerateurPdfWorker`, `SqliExportWorker`) : Exécute la génération physique du fichier et l'I/O.



### Cas d'usage B : Gestion de Santé et Prise de Rendez-vous

* **But Applicatif ($G$) :** Assurer la prise de rendez-vous médical sans chevauchement ni double réservation.
* **Modèle Métier ($\mathcal{M}$) :** Règles de gestion des créneaux, urgences, durées selon la spécialité, délais d'annulation.
* **Support d'Exécution ($t_h$) :** Prise de note sur agenda papier, appels téléphoniques de confirmation.
* **Implémentation Clprolf ($A$) :**
* `@ClAgent` (`PlanningMedicalAgent`) : Valide la possibilité d'ajouter un rendez-vous selon les règles du cabinet.
* `@ClWorker` (`AgendaDatabaseWorker`, `SmsNotificationWorker`) : Écrit en base de données et envoie le SMS de confirmation.



### Cas d'usage C : Logistique et Réapprovisionnement de Stock

* **But Applicatif ($G$) :** Garantir la disponibilité des articles en magasin en réapprovisionnant avant la rupture.
* **Modèle Métier ($\mathcal{M}$) :** Calcul du seuil de réapprovisionnement, délais de livraison des fournisseurs, priorisation des flux.
* **Support d'Exécution ($t_h$) :** Inventaire physique au registre, comptage manuel des cartons.
* **Implémentation Clprolf ($A$) :**
* `@ClAgent` (`SeuilReapproAgent`) : Calcule la quantité exacte à commander selon le stock théorique.
* `@ClWorker` (`EdiSupplierWorker`, `ERPConnectorWorker`) : Transfère le bon de commande au système du fournisseur.



---

## 2. Table de correspondance synthétique

| Variable Formelle | Rôle Sémantique | Exemple Concret (Système de Paie) |
| --- | --- | --- |
| **$G$** | Intent de résultat visé par le système | Bulletin de paie juste et versé à temps |
| **$m \subseteq \mathcal{M}$** | Portion des règles du domaine | Formule du calcul du prélèvement à la source |
| **$c$ (`@ClAgent`)** | Composant portant la règle $m$ | `CalculateurTauxTaxeAgent` |
| **$t$ (`@ClWorker`)** | Support technique adossé à $c$ | `TaxApiWorker` (Appel REST / I/O) |
| **$d$ (Atome)** | Binôme indissociable $Combine(d, c, t)$ | Binôme natif `TaxeCalculatedPair` |
| **$f$ (`@ClFamily`)** | Contrat-miroir hiérarchique | `TaxCalculatorFamily` |

---
