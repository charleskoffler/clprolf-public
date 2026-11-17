# Clprolf — DSL Embarqué (EDSL) et Cadre Architectural

## Utilisations cibles et prérequis

**Clprolf** (*"Clear PROgramming Language and Framework"*) est un **DSL embarqué (EDSL — *Embedded Domain-Specific Language*) architectural**, léger et indépendant, conçu pour Java ou C# .NET.
Il étend directement le langage hôte grâce à un vocabulaire déclaratif d'annotations (ou d'attributs C#) vérifié statiquement lors de la compilation ou des tests. Le code source (annotations + checker) peut être copié sous licence M.I.T., et est visible sur GitHub dans des applications écrites par l'auteur.
Cependant, l'outillage complet, ainsi que sa documentation et ses exemples, sont principalement obtenus par des formations payantes de son auteur.

Il s'agit d'un EDSL spécialisé pour les situations suivantes :
* support pédagogique pour apprendre la POO, les interfaces, voire l'immuabilité.
* applications scientifiques
* simulations
* applications d'entreprise souhaitant intégrer Clprolf en complément des architectures classiques
* applications très complexes, avec un grand nombre de classes ou d'interfaces
* gros refactoring nécessaire sur une application existante volumineuse
* ou tout simplement pour ceux qui apprécient ce style de programmation guidé par un DSL architectural vérifié statiquement sur les classes et les interfaces.

Il a pour prérequis la connaissance des bases de la POO. Une familiarité avec les principes de conception OO peut être utile.

Cet EDSL a pour objectif d'assister la conception objet. Il offre une interprétation concrète des principes SOLID et facilite la mise en œuvre de l'architecture hexagonale ou du DDD.
Bien entendu, Clprolf propose sa propre approche pour atteindre ces principes objets connus et ces architectures de référence.
L'application obtenue reste du pur Java ou C# : elle peut redevenir totalement standard à tout moment en retirant les annotations ou en cessant de les interpréter.

## Introduction

Clprolf est un **DSL embarqué (EDSL)** qui repose sur une idée simple :

> Une classe doit clairement exprimer son rôle principal.

En tant que langage dédié intégré au code hôte et complété par un cadre architectural léger, il aide ainsi à :

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

### Fondements théoriques

Clprolf s'appuie sur une étude théorique qui apporte une assise formelle à son modèle architectural et aux principes structurels qui organisent l'utilisation des composants, des familles et des traits.

Les utilisateurs qui souhaitent approfondir les fondements conceptuels et formels du framework peuvent consulter l'article :

* [**Formalisation axiomatique d'une topologie logicielle orientée objet et vérification statique des domaines : le modèle Clprolf**](https://doi.org/10.5281/zenodo.24310061) *(Preprint Zenodo)*

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

Dans **Clprolf**, la notion de **sous-système** est l'unité d'organisation logique qui donne son sens au modèle métier. Un sous-système regroupe un ensemble cohérent de `@ClAgent` (et leurs `@ClWorker` associés) pour simuler un domaine fonctionnel précis. En Java, il est implémenté par un package, ou un module. L'équivalent en .NET C# est un namespace ou une assembly.

### 1. La règle du sous-système par défaut

Par construction, **toute application Clprolf possède au moins un sous-système**.

Dans sa forme la plus simple, ce sous-système racine regroupe l'intégralité des `@ClAgent` de l'application au sein du package principal. Cette structure minimale suffit pour couvrir 100 % des besoins des applications de petite à moyenne taille.

### 2. Spécialisation et découpage fin

Lorsque la complexité de l'application augmente, il devient nécessaire de regrouper les agents de manière plus fine. Vous pouvez alors définir des **sous-systèmes supplémentaires** afin d'isoler des responsabilités distinctes :

* **Par intention fonctionnelle :** Découpage de l'application selon les grands cas d'usage métiers (ex. sous-système `Facturation`, sous-système `GestionStock`, sous-système `Authentification`).
* **Par frontière d'isolation :** Regroupement des agents partageant les mêmes contraintes de sécurité, de déploiement ou de données.

# VIII) Interfaces

Il y a en Clprolf deux types d'interfaces. Une seule est implémentable directement par une classe, ce sont les interfaces de famille (`ClFamily`).
Le contrat est plus exigeant que le contrat habituel. La classe doit être un composant de même type que le rôle cible demandé par le contrat(la "Target").
La classe est réduite à un seul contrat possible. En mode non strict, la classe peut implémenter plusieurs interfaces, dont des traits(`ClTrait`).
En pur Clprolf, les traits sont des exigences pour les interfaces familles, et ne peuvent pas être implémentées par les classes directement. Ce ne sont pas des contrats.
Ou alors on pourrait dire que ce sont des contrats pour les interfaces de famille. Les traits sont des fonctionnalités communes et transverses entre hiérarchies de familles({`ClFamily`).
Ils ne peuvent étendre que des interfaces family qui possèdent un des rôles cibles demandés par le trait.

Dans Clprolf, les interfaces sont vues comme :

> des formes abstraites d’héritage.

Elles participent donc à la continuité structurelle du système.

ClFamily  = interface principale d’une famille
ClTrait = trait, capacité commune entre familles
ClFree = interface libre

Les relations `extends` et `implements` sont considérées comme
de véritables relations d’héritage conceptuel, d'où "family".

---

## VIII.1) `ClFamily` et le contrat de miroir

Interface représentant une famille abstraite, et apportant un contrat miroir pour la future implémentation par une classe.

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

Interface représentant une fonctionnalité commune entre plusieurs ClFamily. Ce n'est pas un contrat pour une classe.
Les traits utilisent un rôle cible, tout comme les ClFamily :

* @ClAgent
* @ClWorker
* @ClSystem

Mais une interface @ClTrait peut être annotée par plusieurs rôles de composant, @ClAgent, @ClWorker ou @ClSystem.
Cette exception est réservée aux traits véritablement transversaux, utilisables aussi bien par plusieurs types de composant.
Cela signifie que l'interface famille héritante doit posséder un de ses rôles.
En général, un trait ne possède qu'un rôle cible.

```java
@ClAgent
@ClWorker
@ClTrait
public interface Runnable {
    public void run();
}
```

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

* En Clprolf, les interfaces `Family` permettent d'avoir un miroir des composants, mais sans implémentation.
Le contrat est plus exigeant qu'en Java ou C#, car la classe doit, en plus des méthodes, être un composant du type visé(`ClAgent`, `ClWorker`, ou `ClSystem`).
Enfin, le contrat est que la classe n'implémente que cette interface family, et pas d'autres, pour avoir un miroir entre les interfaces et leurs implémentations.
Par contre, plusieurs composants(classes) peuvent implémenter même interface `ClFamily`.

Une `Family` est toujours le reflet structurel de son implémentation. Cela permet notamment d’obtenir un loose coupling systématique.
L’implémentation de plusieurs interfaces family n’est pas supprimée, mais déplacée vers la `Family` implémentée par la classe. Tout reste donc possible.
**Cela ne s'applique que pour le checker en mode strict.**

* Les interfaces `Trait` expriment une fonctionnalité commune entre plusieurs interfaces `Family`.

Un `Trait` représente donc un trait transversal partagé entre plusieurs familles. Ce n'est pas un contrat avec une classe.

Un `Trait` ne peut être hérité que par une interface `Family`( ou étendre un autre `ClTrait`), et non directement par une classe.

**Ceci est vrai uniquement en mode strict du checker. En contrôles non strict, un trait peut être implémenté directement par une classe. Mais on perd alors certains invariants structurels**

```text
Classe concrète
    ↓ implémente
ClFamily
    ↓ hérite de
ClTrait
```

Note :
Un forçage de l’héritage d’interfaces reste possible avec `@ClInterfaceBypass` au-dessus de l’interface (ou `@ClBypass` pour forcer l’héritage entre rôles cibles différents).
Mais ces forçages ne devraient pas être fréquent.

### VIII.5.2) Mode strict des interfaces avec flexibilité

Il est possible d'utiliser `ClFree` pour rester strict uniquement sur les interfaces qu'on souhaite, et faire des ClFamily quand on souhaite plus de rigueur, sur des cas précis. Dans ce cas, même en mode strict, on reste ouvert et flexible.
Les interfaces natives Java ou C# sont acceptées et considérées comme `ClFree`.

---

## VIII.6) "Loose Coupling" systématique par l'interface miroir

Le contrat miroir, et la séparation des traits, permet d'obtenir un loose coupling d'implémentation de manière structurelle.

### Un exemple réel des interfaces `ClFamily` et `ClTrait`

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

Clprolf apporte des éléments pour respecter l'ISP et permet d'éviter les *« fat interfaces »* (interfaces fourre-tout).

### Le problème classique de la *Fat Interface*

On a souvent tendance à créer une interface unique regroupant toutes les fonctionnalités possibles pour un domaine :

```java
// Anti-pattern : Fat Interface qui viole l'ISP
public interface Machine {
    void print(Document doc);
    void scan(Document doc);
    void fax(Document doc);
}

```

Dans ce schéma, une ancienne imprimante (`OldPrinterImpl`) qui ne sait qu'imprimer serait forcée d'implémenter `scan()` et `fax()`, souvent en levant une exception (`UnsupportedOperationException`), ce qui viole l'ISP.

### Version avec Clprolf

Le code tend à améliorer l'évolutivité, et lisibilité.

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
