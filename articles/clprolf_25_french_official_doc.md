# Framework Clprolf - Documentation officielle

## Introduction

**Clprolf**("Clear PROgramming Language and Framework") est un framework pour Java et C# .net. Sa devise est "Ne l'utilisez pas - Vous n'en avez pas besoin!".

Son objectif est de rendre explicites certaines bonnes pratiques de la programmation orientée objet, sans introduire une architecture lourde ni une courbe d’apprentissage importante.
Ainsi, le framework aide à respecter les principes SOLID bien connus.

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
---

### Monde technique

La classe effectue un travail technique, effectué par le système :

* accès base de données (par l'intermédiaire d'agents systèmes),
* réseau (par utilisation d'agents bas niveaux souvent),
* fichiers (le plus souvent avec des agents orientés systèmes),
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

# II) Les Types de Classes

Clprolf possède seulement trois types de classes.

---

## II.1) `ClAgent`

Représente une classe métier ou conceptuelle.

Un `agent` :

* contient la logique métier ou conceptuelle,
* orchestre les traitements,
* prend des décisions,
* évite le code technique lourd, qui est souvent délégué à un worker associé
* peut être orienté système comme Connection ou Socket

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

Remarque: les entity sont considérées agent, car ayant un domaine, même sans méthodes.

---

## II.2) `ClWorker`

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

## II.5) Un framework "opinionated" (d'opinion) pour le choix agent/worker

Certaines responsabilités peuvent être interprétées de différentes manières selon la vision architecturale adoptée.

Par exemple, une connexion peut être représentée :

* comme un `agent`, si elle est vue comme une abstraction fonctionnelle ;
* ou comme un `worker`, si elle est considérée comme un mécanisme purement technique.

Cependant, dans ces cas, le framework Clprolf impose d'utiliser un agent. Par exemple, pour une classe Connection:

* un `agent` pour représenter la connexion,
* et déléguer le code technique à un ou plusieurs `worker`.

C'est pour cela que Clprolf peut être qualifié de framework "opinionated" (framework d'opinion).
Dès qu'il est possible de voir un domaine, il doit être choisi à la place de la vision worker. Ce choix est argumenté par le fait que les agents et abstractions sont plus faciles à manipuler et facilitent la conception.
Mais dire que la classe Connection est `ClAgent` n'exclut pas qu'elle puisse posséder un worker pour ses propres besoins.

### Exemple de Java qui confirme la vision Clprolf: java.io.File

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

CONCEPT CLPROLF                   CODE SOURCE JAVA (OpenJDK)
┌──────────────────────────┐            ┌──────────────────────────┐
│        @ClAgent          │            │      java.io.File        │
│    (Agent Système)       │            │                          │
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

Remarque: java.io.UnixFileSystem, ainsi que WinNTFileSystem, contiennent beaucoup de méthodes `native`.

---

### II.5.2) Le rôle optionnel `ClSystem`

Il est possible d'utiliser l'annotation `@ClSystem` (ou l'attribut `[ClSystem]` en C#), pour les agents orientés système. Mais ce n'est pas obligatoire, pour ne pas compliquer trop le framework.
Si on s'en sert, on ne peut plus mixer l'héritage agent et agent orienté système. Ils sont alors considérés, par le checker, comme un rôle indépendant.
Les personnes préférant annoncer et contrôler finement les agents orientés systèmes (comme `File`), pourront les annoter `ClSystem` au lieu de `ClAgent`.

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

## V.3) Illustration du parallèle interfaces familles / implémentations

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

## V.4) `@ClFree`

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

## V.5) Utilisation des interfaces

En Clprolf, les interfaces `Family` se rapprochent des classes abstraites pures.

Elles sont destinées à être implémentées par une ou plusieurs futures classes Clprolf.
Elles possèdent donc un rôle cible (`agent` ou `worker`).

Une classe ne peut implémenter qu’une seule `Family` principale à la fois, et le rôle de la classe doit correspondre au rôle cible de l’interface.
Clprolf utilise ainsi une implémentation simple des interfaces, de la même manière que Java utilise un héritage simple des classes. En effet, une `Family` est toujours le reflet structurel de son implémentation.Cela permet notamment d’obtenir un loose coupling systématique.
Toutefois, l’implémentation multiple n’est pas supprimée, mais déplacée vers la `Family` implémentée par la classe.
Cette interface de famille peut elle-même hériter de plusieurs interfaces `Family` ou `Trait`.
Ainsi, les interfaces qui auraient été implémentées directement par la classe sont regroupées au niveau de sa `Family` principale.
Clprolf conserve donc la richesse de l’héritage multiple des interfaces, tout en maintenant une structure simple et cohérente pour les classes concrètes.

Remarque: en mode non strict, il est possible pour une classe d'implémenter plusieurs interfaces ClFamily, pour rester plus proche des habitudes en Java et C#.
---

Les interfaces `Trait` expriment une fonctionnalité commune entre plusieurs interfaces `Family`.

Un `Trait` représente donc un trait transversal partagé entre plusieurs familles.

Normalement, un `Trait` ne peut être hérité que par une interface `Family`, et non directement par une classe.

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

Remarque: en mode non strict, l’implémentation directe d’un `Trait` par une classe est autorisée.

---

## V.6) Remarque sur Clprolf et l'Interface Segregation Principle (ISP)

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
│                  AGENT (SYSTÈME)                   │
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

## 1. Version Java (ArchUnit)

Disponible sur GitHub, le checker Java est open-source et s'articule autour de deux classes de tests principales :
* **`ClprolfArchTest`** : Valide les règles sémantiques standard et fondamentales du framework.
* **`ClprolfStrictArchTest`** : Regroupe des contraintes optionnelles et plus rigides pour les projets exigeants (comme l'interdiction pour une classe d'implémenter directement un `ClTrait`).
Les checkers (Java et .Net) contiennent aussi les définitions des annotations (ou attributs) Clprolf.
Pour simplifier la documentation, l'annotation optionnelle `ClSystem` ne figure pas dans la description des règles. Elle est simplement gérée comme un rôle indépendant, par le checker.
---

## 2. Version C# .NET (ArchUnitNET)

Le portage de l'extension .NET est disponible et publié sur GitHub.

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

# X) Clprolf et les principes SOLID

## **S** — Principe de responsabilité unique (SRP)
Le Framework naturellement procure le principe de responsabilité unique(SRP). En effet, chaque classe possède son domaine conceptuel ou métier, qui est préservé lors de l'héritage. 

## **O** — Principe Ouvert/Fermé (OCP, Open-Closed Principle)
Ce principe nous incite à prévoir les futures évolutions comme des extensions et pas comme des corrections du code. Le Framework Clprolf facile les extensions, de par la séparation stricte des domaines conceptuels, et les workers. Les interfaces ClFamily poussent à une bonne visibilité des interfaces utilisées dans les classes, et à bien penser les fonctionnalités. Les interfaces ClTrait nous permettent d'avoir des ClFamily encore plus simples et pures, et à mutualiser les traits.

## **L** — Principe de substitution de Liskov (LSP)
Clprolf impose un héritage qui reste dans le même domaine conceptuel, en plus de la séparation ClAgent et ClWorker. Ainsi, le principe LSP de Liskov est naturellement pris en compte, car une classe Carre n'est pas du même domaine conceptuel que Rectangle.
En effet, une classe Girafe appartient au même domaine qu'un Animal dont elle hérite les comportements naturels. À l'inverse, une classe Carre n'a pas la nature d'un Rectangle (elle ne peut pas avoir une longueur et une largeur indépendantes). Dans Clprolf, ils n'appartiennent donc pas au même domaine conceptuel, ce qui empêche un héritage abusif et prévient les violations du LSP.

## **I** — Principe de ségrégation des interfaces (ISP)
Ce principe conseille qu'un client ne doit pas devoir implémenter des méthodes qu'ils n'utilisent pas. Avec Clprolf, l'interface est taillée sur mesure pour le client, et les traits sont précis. De plus, les héritages entre interfaces sont contrôlés.

## **D** — Injection des dépendances (Dependency Injection, DI)

L'injection des dépendances suppose le loose coupling, le couplage faible avec les implémentations. Ce couplage faible est encouragé et facilité, avec les interfaces ClFamily qui sont intimement liéées aux classes.

## "Composition plutôt qu'héritage" ("Favoring composition over inheritance")
Le framework Clprolf permet de n'utiliser l'héritage qu'avec parcimonie et précision, et de se servir de la composition sinon.

---

# XI) Clprolf et les architectures existantes

Clprolf est compatible avec les architectures DDD, MVC, Clean architecture, architecture hexagonale, etc. C'est une couche entre la POO et les architectures, qui vient compléter et sécuriser les architectures connues.
Et Clprolf ne sert pas que pour l'informatique d'entreprise, mais pour tout type d'applications, y compris simulations, et applications scientifiques.

# XII) Résumé

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