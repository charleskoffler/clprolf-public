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
@Agent
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
@Worker
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

## II.1) `agent`

Représente une classe métier ou conceptuelle.

Un `agent` :

* contient la logique métier,
* orchestre les traitements,
* prend des décisions,
* évite le code technique lourd.

Exemple :

```java
@Agent
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

## II.2) `worker`

Représente une classe technique.

Un `worker` :

* réalise les tâches machine,
* gère l’infrastructure,
* contient le code technique.

Exemple :

```java
@Worker
public class OrderRepository {

    public void save(Order order) {

        // accès base de données

    }
}
```

---

## II.3) `indef_obj`

Objet sans rôle défini.

Utilisé :

* pendant le prototypage,
* lors du refactoring,
* quand le rôle n’est pas encore clair.

Exemple :

```java
@Indef_obj
public class TemporaryManager {
}
```

`@Indef_obj` permet une approche flexible proche de l’OOP classique.

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
@Agent
public class Animal {
}

@Agent
public class Dog extends Animal {
}
```
---

## Exemple déconseillé

```java
@Worker
public class ClientRepository {
}

@Agent
public class Dog extends ClientRepository {
}
```

Ici, les domaines sont incompatibles.
Il faut utiliser la composition.

Un forçage de l'héritage de classe est possible avec @Forc_inh, au-dessus de la classe.
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

family_interf  = interface principale d’une famille
trait_interf = trait, capacité commune entre familles
compat_interf = interface libre

Dans Clprolf, les interfaces ne sont pas vues comme de simples
contrats techniques.

Les relations `extends` et `implements` sont considérées comme
de véritables relations d’héritage conceptuel, d'où "family".

---

## V.1) `family_interf`

Interface représentant une famille abstraite.

Utilisée pour :

* le polymorphisme,
* le découplage,
* les variantes d’implémentation.

Les interfaces de famille possèdent également un rôle cible :

* `@Agent`
* ou `@Worker`
---

## Exemple

```java
@Agent
@Family_interf
public interface agent Animal {

    void manger(int quantite);

}
```

La hiérarchie des interfaces family_interf reflète naturellement la hiérarchie des classes concrètes.

```java
@Agent
@Family_interf
public class agent Horse extends Animal {

    void sauter(int hauteur);

}
```

Et aura

```java

@Agent
public class AnimalImpl implements Animal { (...) }

```

```java

@Agent
public class HorseImpl extends AnimalImpl implements Horse { (...) }

```

---

## V.2) `trait_interf`

Interface représentant une fonctionnalité commune entre plusieurs family_interf.
Les traits utilisent un rôle cible, tout comme les family_interf :

* @Agent
* @Worker

Remarque: une interface @Trait_interf peut être annotée à la fois @Agent et @Worker.
Cette exception est réservée aux traits véritablement transversaux,
utilisables aussi bien par des agents que par des workers.

```java
@Agent
@Worker
@Trait_interf
public interface Runnable {
    public void run();
}

---

## Exemple côté métier

```java

@Agent
@Trait_interf
public interface Payable {
    void pay();
}
```
---

## Exemple côté technique

```java

@Worker
@Trait_interf
public interface Launcher {
    void start();
}
```
---

## V.3) `@Compat_interf`

Interface générique sans rôle particulier.
Permet de rester flexible.

---

## Exemple

```java
@Compat_interf
public interface ExternalApi {
}
```
---

## V.4) Utilisation des interfaces

En Clprolf, les interfaces `family_interf` sont l’équivalent de classes abstraites pures.

Elles sont destinées à être implémentées par une ou plusieurs futures classes Clprolf.
Elles possèdent donc un rôle cible (`agent` ou `worker`).

Une classe ne peut implémenter qu’une seule `family_interf` principale à la fois, et le rôle de la classe doit correspondre au rôle cible de l’interface.
Clprolf utilise ainsi une implémentation simple des interfaces, de la même manière que Java utilise un héritage simple des classes. En effet, une `family_interf` est toujours le reflet structurel de son implémentation.Cela permet notamment d’obtenir un loose coupling systématique.
Toutefois, l’implémentation multiple n’est pas supprimée, mais déplacée vers la `family_interf` implémentée par la classe.
Cette interface de famille peut elle-même hériter de plusieurs interfaces `family_interf` ou `trait_interf`.
Ainsi, les interfaces qui auraient été implémentées directement par la classe sont regroupées au niveau de sa `family_interf` principale.
Clprolf conserve donc la richesse de l’héritage multiple des interfaces, tout en maintenant une structure simple et cohérente pour les classes concrètes.

---

Les interfaces `trait_interf` expriment une fonctionnalité commune entre plusieurs interfaces `family_interf`.

Un `trait_interf` représente donc un trait transversal partagé entre plusieurs familles.

Normalement, un `trait_interf` ne peut être hérité que par une interface `family_interf`, et non directement par une classe.

Cependant, en Clprolf, l’implémentation directe d’un `trait_interf` par une classe reste tolérée, bien que déconseillée.

```text
Classe concrète
    ↓ implémente
family_interf
    ↓ hérite de
trait_interf
```

Note : une interface `family_interf` peut hériter de plusieurs interfaces `family_interf` ou `trait_interf`.

Une interface `trait_interf` ne peut hériter que d’autres `trait_interf`, car un trait reste un trait.

Un forçage de l’héritage d’interfaces reste possible avec `@Forc_int_inh` au-dessus de l’interface (ou `@Forc_inh` pour forcer l’héritage entre rôles cibles différents).

---

## V.5) Remarque sur Clprolf et l'Interface Segregation Principle (ISP)

Clprolf respecte l'ISP, il suffit d'adapter le design des classes et interfaces avec les bonnes familles et traits:

```java
@Agent
@Trait_interf
public interface Scanner {
	void scan(Document doc);
}

@Agent
@Trait_interf
public interface Fax {
	void fax(Document doc);
}

@Agent
@Trait_interf
public interface Printer {
	void print(Document doc);
}

@Agent
@Family_interf
public interface OldPrinter extends Printer  {
	
}

@Agent
@Family_interf
public interface ModernPrinter extends OldPrinter, Scanner, Fax {
}

@Agent
public class OldPrinterImpl implements OldPrinter {
//(…)
}

@Agent
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
Une classe ne peut pas être annotée à la fois @Agent et @Worker

### agent_worker_inheritance_must_not_mix
Une classe @Worker ne peut pas hériter d'une classe @Agent, et inversement.

### family_interface_role_must_match_implementation
Le rôle cible d'une interface @Family_interf, doit être similaire au rôle de la classe d'implémentation(@Agent ou @Worker). Forçage possible avec @Forc_inh.

### (mode non strict)trait_interface_role_must_match_direct_implementation
Une classe qui implémente directement un trait doit avoir un rôle compatible (sauf si forçage avec @Forc_inh). Interdit en mode strict.

### trait_interfaces_must_extend_only_trait_interfaces
Les interfaces @Trait_interf ne peuvent hériter que d'autres @Trait_interf. Forçage possible avec @Forc_int_inh.

### clprolf_interfaces_must_have_target_role
Les interfaces @Family_interf doivent avoir un seul rôle cible : @Agent ou @Worker.
Les interfaces @Trait_interf doivent avoir au moins un rôle cible : @Agent, @Worker,
ou exceptionnellement les deux.

### inheriting_interface_role_must_match_trait_interface_target_role
Les interfaces (family ou trait) qui héritent d'un trait doivent avoir un rôle compatible avec le trait (sauf si forçage avec @Forc_inh).

### family_interface_target_role_must_match_inherited_family_interface
Les interfaces family dont hérite une interface family doivent avoir un rôle compatible, à moins d'utiliser @Forc_inh.

Règles plus stricts:

### optional_all_classes_should_have_clprolf_role 
Toutes les classes doivent avoir un rôle Clprolf (@Agent, @Worker ou @Indef_obj)

### optional_all_interfaces_should_have_clprolf_role 
Toutes les interfaces doivent avoir un rôle Clprolf(@Family_interf, @Trait_interf ou @Compat_interf)

### optional_class_should_not_implement_trait_directly
Une classe ne peut pas implémenter directement une interface @Trait_interf (à moins d'utiliser @Forc_int_inh)

### optional_class_must_implement_only_one_family_interface (OPTIONNELLE)
Une classe Clprolf ne peut implémenter qu'une interface @Family_interf. Forçage possible avec @Forc_int_inh


# X) Clprolf et les architectures existantes

Clprolf est compatible avec les architectures DDD, MVC, Clean architecture, architecture hexagonale, etc. C'est une couche entre la POO et les architectures, qui vient compléter et sécuriser les architectures connues.


# XI) Résumé

Clprolf ajoute très peu de concepts.
---

## Classes

```text
@Agent
@Worker
@Indef_obj
```
---

## Interfaces

```text
@Family_interf
@Trait_interf
@Compat_interf
```
---

## Deux règles fondamentales

```text
1. Séparer métier et technique.
2. Hériter seulement dans le même domaine.
```
---
