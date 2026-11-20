# Formalisation axiomatique d'une topologie logicielle orientée objet et vérification statique des domaines : le modèle Clprolf

Axiomatic Formalization of an Object-Oriented Software Topology and Static Domain Verification: The Clprolf Model

**Version :** v1.0 (Novembre 2026)  
**Domaine principal :** Software Design  
**Domaines secondaires :** Theory of computation — Logic and verification *(Logique et vérification formelle)* ; Software notations and tools — Domain-specific languages *(Langages dédiés / EDSL)*  
**Mots-clés :** Software Topology, Architectural Verification, Domain Invariance, First-Order Logic, Axiomatic Formalization, Static Analysis, Embedded DSL (EDSL), Domain-Driven Design (DDD), ArchUnit, Clprolf

---

## Résumé

Cet article présente la formalisation axiomatique en logique du premier ordre du cadre architectural **Clprolf** (*Clear PROgramming Language and Framework*), un DSL embarqué léger conçu pour les langages orientés objet (Java et C# .NET) et vérifié statiquement via ArchUnit et ArchUnitNET. Clprolf établit une séparation stricte entre les composants de logique métier/conceptuelle (`@ClAgent`, `@ClSystem`) et les services d'exécution technique (`@ClWorker`), tout en imposant la conservation du domaine conceptuel le long des hiérarchies d'héritage. Le modèle formel définit deux espaces duals : le Monde Concret des composants exécutables ($\mathcal{C}$) et le Monde Abstrait des contrats ($\mathcal{I}$), structuré sous forme de graphe orienté acyclique (DAG) distinguant les interfaces de famille (`@ClFamily`) des capacités transverses (`@ClTrait`). À travers une fonction d'ancrage asymétrique dite *Miroir*, nous démontrons plusieurs propriétés structurelles clés — notamment le plongement des graphes d'héritage, l'étanchéité des domaines métiers en présence d'héritage multiple et la neutralité sémantique du polymorphisme d'interfaces sur les composants d'exécution. Enfin, le papier explicite la frontière entre les invariants indécidables au sens de Turing (équivalence sémantique, appartenance domaniale) et leur approximation décidable par vérification statique en ingénierie logicielle.

## Abstract

This paper presents an axiomatic formalization in first-order logic of the **Clprolf** (*Clear PROgramming Language and Framework*) architectural framework, a lightweight embedded DSL designed for object-oriented languages (Java and C# .NET) and statically enforced via ArchUnit and ArchUnitNET. Clprolf establishes a strict separation between business/conceptual components (`@ClAgent`, `@ClSystem`) and technical execution services (`@ClWorker`), while enforcing conceptual domain preservation across inheritance hierarchies. The formal model defines two dual spaces: the Concrete World of executable components ($\mathcal{C}$) and the Abstract World of contracts ($\mathcal{I}$), structured as a Directed Acyclic Graph (DAG) distinguishing family interfaces (`@ClFamily`) from cross-cutting capabilities (`@ClTrait`). Using an asymmetric anchoring relation termed *Mirror*, we prove key structural properties—including inheritance graph embedding, domain confinement under multiple inheritance, and semantic neutrality of interface polymorphism over concrete components. Finally, the paper delineates the boundary between Turing-undecidable domain properties (semantic equivalence, domain membership) and their decidable static approximations in software engineering practice.

---


## I. Introduction

Clprolf (*Clear PROgramming Language and Framework*) est un DSL embarqué (EDSL) léger pour la pédagogie et les applications complexes. Il se présente sous la forme d'annotations (ou d'attributs) et d'un checker statique de conformité sémantique. Le langage hôte est Java ou C# .NET.
Le modèle formel Clprolf modélise la topologie logicielle statique d'une application d'automatisation orientée objet ou des bibliothèques qui lui servent de support.

**Domaines d'application et positionnement :** Clprolf agit comme une couche intermédiaire entre la programmation orientée objet classique et les architectures logicielles (Architecture Hexagonale, Domain-Driven Design, Clean Architecture). Il est particulièrement adapté aux systèmes complexes, aux applications scientifiques, aux simulations, aux grands chantiers de refactoring ainsi qu'à la pédagogie de la POO et des principes SOLID. L'application reste du pur Java ou C# : les annotations sont non intrusives et vérifiées statiquement lors de la phase de test.

**Illustration minimale (Java) :**

```java
@ClAgent @ClTrait public interface Payable { void pay(); }
@ClAgent @ClFamily public interface OrderService extends Payable { void process(Order order); }
@ClWorker @ClFamily public interface OrderRepository { void save(Order order); }

@ClAgent
public class OrderServiceImpl implements OrderService {
    private final OrderRepository repository; // Injection du miroir technique
    // Logique métier...
}
```

## I.b Portée du Framework et Articulation avec le Modèle Formel

Le modèle formel de Clprolf définit une topologie théorique complète (incluant les **Parties de l'application $\mathcal{P}_A$**, les **Atomes architecturaux $\mathcal{D}$**, ou la catégorisation fine des **Composants Autonomes**). La mise en œuvre pratique dans le framework repose sur la philosophie suivante :

* **Contrôles focalisés sur le décidable et la valeur ajoutée :** Le checker statique (ArchUnit / ArchUnitNET) vérifie les contraintes fondamentales et décidables du modèle (séparation métier/technique, invariance du domaine par héritage, polarité des interfaces). Il ne cherche pas l'exhaustivité là où elle n'apporte aucun gain de qualité ou d'ingénierie.
* **Absence de sur-contrainte et de bruit visuel :**
  * Aucun contrôle n'est effectué par le checker sur d'éventuels composants métiers situés dans une partie mais hors d'un atome.
  * Le modèle formel caractérise les composants métiers autonomes comme résidant hors de toute partie, mais n'interdit pas les formes hybrides.
  * Aucun artefact ou annotation supplémentaire (`@ClAtom`, `@ClPart`) n'est imposé dans le code. Les atomes et sous-systèmes émergent naturellement de la structuration standard du projet (injection `@ClAgent` / `@ClWorker`, packages/namespaces).
* **Adoption progressive et intégration transparente :** Le checker permet une adoption en deux étapes (séparation des classes `@ClAgent`/`@ClWorker`/`@ClSystem` d'abord, puis activation optionnelle du mode strict sur les interfaces `@ClFamily`/`@ClTrait`). Il supporte également les alias de vocabulaire (ex. `@ClDomain`/`@ClInfrastructure`) ainsi que la détection automatique d'annotations natives d'écosystèmes tiers (ex. `@RestController` ou `[ApiController]` reconnus nativement comme `@ClSystem`).

Cette frontière garantit une adoption fluide en entreprise et préserve la liberté du développeur, tout en verrouillant 100 % des règles d'architecture indispensables.

---

## II. Synthèse du Modèle Formel : Clprolf

### 1. L'Espace des Familles et l'Espace des Composants

* **Le Monde Abstrait ($F$) :** Un espace de contrats, de traits et de familles, autorisant la composition riche et l'héritage multiple (DAG). C'est le lieu de la définition conceptuelle pure.
* **Le Monde Concret ($C$) :** L'espace des composants exécutables.

### 2. La Fonction Miroir et son Asymétrie

La relation entre les deux mondes est portée par une fonction d'ancrage, dite **Miroir** :

* **Totalité à gauche :** $\forall c \in C, \quad \exists! f' \in F, \quad \text{Miroir}(c) = f'$
*(Tout composant concret possède obligatoirement une famille miroir unique).*
* **Partialité à droite :** $\forall f \in F$, il n'existe pas nécessairement de composant $c$ associé. Mais si un tel composant existe, il est par définition le miroir de cette famille.

### 3. L'Équivalence Sémantique

Pour tout composant $c$ et son miroir $f'$ :


$$c \equiv_{\text{sem}} f' \quad (\text{ou } \mathcal{S}(c) = \mathcal{S}(f'))$$


Le composant et sa famille partagent strictement la même identité sémantique. L'un exécute, l'autre qualifie, mais le sens est rigoureusement unifié.

### 4. Le Confinement du Domaine et l'Héritage Multiple (DAG)

* **Invariance par l'héritage :** Toute relation d'héritage (directe ou au sein du DAG des familles) respecte l'invariant de domaine :

$$\text{HeriteStar}(f_1, f_2) \implies \text{MemeDomaine}(f_1, f_2)$$


* **Universalité :** Même en cas d'héritage multiple, tous les parents d'une famille doivent converger vers le même domaine, garantissant l'étanchéité totale du système.

### 5. L'Interdiction du Boilerplate

Les composants concrets n'implémentent **jamais** directement d'interfaces techniques (pas d'interfaces parasites type `Serializable`). Tout contrat passe exclusivement par la hiérarchie des familles et le mécanisme de miroir.

---

## III. Dictionnaires du système

### 1. Dictionnaire des Ensembles (Univers du Discours)

* **$\mathcal{A}_{\text{aut}}$** : L'ensemble des applications d'automatisation.
* **$\mathcal{H}$** : L'ensemble des travaux humains à automatiser.
* **$\mathcal{G}$** : L'ensemble des buts / objectifs fonctionnels humains.

* **$\mathcal{M}_{\text{global}}$** : L'ensemble universel de tous les domaines métiers possibles pour l'ensemble des travaux $h \in \mathcal{H}$ (ensemble d'ensembles) :

$$\mathcal{M}_{\text{global}} = \{ \mathcal{M}_h \mid h \in \mathcal{H} \}$$
* **$\mathcal{M}_h \in \mathcal{M}_{\text{global}}$** : Le domaine métier transversal spécifique, **unique** et invariant pour un travail humain $h$ donné (il regroupe l'ensemble des règles métiers mobilisées, quel que soit le nombre de départements ou de métiers impliqués).

* **$\mathcal{SE}$** : L'ensemble des supports d'exécution physiques ou organisationnels.
* **$\mathcal{P}(\mathcal{M}_h)$** : L'ensemble des parties de $\mathcal{M}_h$. C'est l'ensemble de tous les sous-domaines $m \subseteq \mathcal{M}_h$ possibles.  $m \in \mathcal{P}(\mathcal{M}_h)$.
* **$\mathcal{P}_{A}$** : L'ensemble des parties / sous-systèmes logiques de l'application $A$. Chaque partie $p \in \mathcal{P}_{A}$ simule $m \in \mathcal{P}(\mathcal{M}_h)$.
* **$\mathcal{D}$** : L'ensemble des atomes de domaine $d = (c, t)$.
* **$\mathcal{C}$** : L'ensemble des composants abstraits ($\mathcal{C} = \mathcal{C}_{\text{metier}} \cup \mathcal{C}_{\text{tech}} \cup \mathcal{C}_{\text{syst}}$).
* **$\mathcal{I}$** : L'ensemble des interfaces ($\mathcal{I} = F \cup T$).
* **$\mathcal{C}_{\text{poo}}$** : L'ensemble des classes concrètes (Java / C#).
* **$\mathcal{I}_{\text{poo}}$** : L'ensemble des interfaces POO
* **$\mathcal{S}_{\text{poo}}$** : L'ensemble des sous-systèmes du framework

*Note de périmètre : Le modèle formel ci-dessous formalise le noyau strict de Clprolf. Les éléments de souplesse du framework — tels que les classes en cours de prototypage (`@ClDraft`), les interfaces libres ou natives du langage hôte (`@ClFree`), ainsi que les mécanismes explicites de dérogation (`@ClBypass`, `@ClInterfaceBypass`) — sont volontairement exclus du noyau axiomatique strict.*

---

### 2. Dictionnaire des Prédicats

#### a. Prédicats Atomiques

* $\text{AppAutom}(A)$ : $A \in \mathcal{A}_{\text{aut}}$ est une application d'automatisation.
* $\text{TravailHumain}(h)$ : $h \in \mathcal{H}$ est un travail humain formalisé.
* $\text{ComposantMetier}(c)$ : $c$ est un composant de logique métier pure (`@ClAgent`).
* $\text{ComposantTech}(t)$ : $t$ est un service technique au service d'un agent (`@ClWorker`).
* $\text{AgentSysteme}(s)$ : $s$ est une abstraction système orientée domaine (`@ClSystem`).
* $\text{Atome}(d)$ : $d \in \mathcal{D}$ est un atome architectural indivisible.
* $\text{ButHumain}(G)$ : $G \in \mathcal{G}$ est un but fonctionnel.
* $\text{DomaineMetier}(\mathcal{M})$ : $\mathcal{M} \in \mathcal{M}_{\text{global}}$ est un univers de règles et concepts métiers.
* $\text{SupportExecution}(se)$ : $se \in \mathcal{SE}$ représente le support d'exécution du travail humain.
* $\text{SubsystemPOO}(s)$ : $s \in \mathcal{S}_{\text{poo}}$ est un module / package POO.
* $\text{SupportTech}(t)$ : $t$ est un support technique d'infrastructure.
* $\text{ComposantMetierAutonome}(c)$ : $c$ est un ComposantMetier autonome, qui ne se trouve pas dans une partie p, et qui n'a pas de ComposantTech combinée dans un atome.
* $\text{ComposantTechniqueAutonome}(c)$ : $c$ est un ComposantTechnique autonome, qui n'est pas combinée à un atome. Il peut ou pas se trouver dans une partie $p$.
* $\text{AgentSystemeAutonome}(c)$ : $c$ est un AgentSysteme autonome, qui ne se trouve pas dans une partie p, et qui n'a pas de ComposantTech combinée dans un atome.
---

#### b. Prédicats de relations

* $\text{Partie}(p, A)$ : $p \in \mathcal{P}$ est un sous-système / module de l'application $A$.
* $\text{Automatise}(A, h)$ : L'application $A$ automatise le travail $h$.
* $\text{Associe}(t, p)$ : Le support $t$ soutient la partie $p$.
* $\text{Simule}(p, m)$ : La partie $p$ simule le sous-domaine métier $m \subseteq \mathcal{M}_h$ (avec $m \in \mathcal{P}(\mathcal{M}_h)$).
* $\text{Contient}(p, d)$ : La partie $p$ contient l'atome $d$.
* $\text{ContientDirect}(p, c)$ : La partie $p$ contient le composant $c$ directement, sans passer pas un atome $d$.
* $\text{Combine}(d, c, t)$ : L'atome $d$ lie de manière univoque le composant métier $c$ et le composant technique $t$.
* $\text{Poursuit}(h, G)$ : Le travail $h$ vise l'accomplissement du but $G$.
* $\text{APourDomaine}(h, \mathcal{M}_h)$ : Le travail $h$ s'inscrit dans l'unique domaine métier $\mathcal{M}_h$.
* $\text{SAppuieSur}(h, se)$ : Le travail $h$ repose sur le support d'exécution $se$.
* $\text{DelegueA}(t, s)$ : Le composant $t$ délègue son travail bas niveau à l'agent système $s$.
* $\text{HeriteDirectComposant}(c_1, c_2)$ : $c_1$ hérite directement de $c_2$.
* $\text{HeriteStarComposant}(c_1, c_2)$ : Fermeture réflexive-transitive de l'héritage entre composants ($\ge 0$ niveau). On peut donc conserver le MemeDomaine(c_1, c_2), qui est vrai à chaque niveau.
* $\text{MemeDomaine}(c_1, c_2)$ : $c_1$ et $c_2$ appartiennent au même domaine conceptuel. Prédicat non décidable au sens de Türing, et pouvant être remplacé par le test que les composants sont avec la même polarité.
* $\text{Annote}(elem, \alpha)$ : L'élément POO ($elem \in \mathcal{C}_{\text{poo}} \cup \mathcal{I}_{\text{poo}}$) porte l'annotation $\alpha$.
* $\text{Realise}(elem_{\text{poo}}, comp_{\text{formel}})$ : Le code POO concrétise le composant ou l'interface formelle.
* $\text{InjectDepend}(cl_1, cl_2)$ : La classe $cl_1$ injecte une dépendance $cl_2$.

---

#### c. Prédicats atomiques des Interfaces

* $\text{Famille}(i)$ : $i \in \mathcal{I}$ est une interface de famille (`@ClFamily`).
* $\text{Trait}(i)$ : $i \in \mathcal{I}$ est une interface de trait/capacité (`@ClTrait`).

---

#### d. Prédicats de relations des interfaces

* $\text{ImplementeDirect}(c, f)$ : Le composant/classe $c$ implémente l'interface $f$.
* $\text{TargetMetier}(i)$ / $\text{TargetTech}(i)$ / $\text{TargetAgentSyst}(i)$ : $i \in \mathcal{I}$. Indique si l'interface $i$ cible/est compatible avec le rôle Métier, Technique ou Système.
* $\text{MemePolarite}(f, c)$ : $f \in \mathcal{I}$, $c \in \mathcal{C}$. Équivalence exacte de polarité/rôle entre la famille $f$ et le composant $c$, et nécessité d'être une famille
* $\text{EquivSem}(c, f)$ : $c \in \mathcal{C}, f \in \mathcal{I}$. Prédicat booléen primitif (indécidable au sens de Turing) exprimant l'équivalence sémantique entre le composant concret et la famille, tranché par convention ou outillage d'architecture.
* $\text{MiroirDe}(f, c)$ : $f$ est l'interface miroir exacte du composant $c$.
* $\text{HeriteDirect}(i_1, i_2)$ : L'interface $i_1$ étend directement $i_2$.
* $\text{HeriteStar}(i_1, i_2)$ : Fermeture réflexive-transitive de l'héritage d'interfaces.  On peut donc conserver le MemeDomaineFamille(i_1, i_2), qui est vrai à chaque niveau.
* $\text{MemeDomaineFamille}(i_1, i_2)$ : Condition de famille, et conservation du domaine entre deux interfaces. Comme MemeDomaine, mais pour deux interfaces Famille.
* $\text{TypeStatique}(o, f)$ : Variable/objet $o$ manipulé sous le type statique de l'interface $f$.
* $\text{InstanceDe}(o, c)$ : L'objet $o$ est une instance dynamique réelle de la classe $c$.


## IV. Axiomes et théorèmes axés sur les composants

Le premier axiome propose une approche téléologique de l'automatisation.

### Axiome 1 - Applications d'automatisation
$$\forall A \; \Big(\text{AppAutom}(A) \implies \exists h \; \big(\text{TravailHumain}(h) \land \text{Automatise}(A,h) \big) \Big)$$

### Axiome 1b.1 - Le travail humain ($h$)

$$\begin{aligned} \forall h \; \Big( \text{TravailHumain}(h) &\implies \exists G, \exists! \mathcal{M}, \exists! se_h \; \big( \text{ButHumain}(G) \land \text{DomaineMetier}(\mathcal{M}) \land \text{SupportExecution}(se_h) \\ &\land \text{Poursuit}(h, G) \land \text{APourDomaine}(h, \mathcal{M}) \land \text{SAppuieSur}(h, se_h) \big) \Big) \end{aligned}$$

### Axiome 1b.2 - Réciproque du travail humain

$$\begin{aligned} \forall h, G, \mathcal{M}, se_h \; \Big( \big( &\text{ButHumain}(G) \land \text{DomaineMetier}(\mathcal{M}) \land \text{SupportExecution}(se_h) \land \text{Poursuit}(h, G) \\ &\land \text{APourDomaine}(h, \mathcal{M}) \land \text{SAppuieSur}(h, se_h) \big) \implies \text{TravailHumain}(h) \Big) \end{aligned}$$

### Axiome 2 — Décomposition Structurelle en parties

$$\begin{aligned} \forall A, p, h, \mathcal{M} \; \Big( \big( &\text{AppAutom}(A) \land \text{Automatise}(A, h) \land \text{APourDomaine}(h, \mathcal{M}) \land \text{Partie}(p, A) \big) \implies \\ &\exists! m, \exists! t \; \big( m \subseteq \mathcal{M} \land \text{SupportTech}(t) \land \text{Simule}(p, m) \land \text{Associe}(t, p) \big) \Big) \end{aligned}$$

### Axiome 3 - Décomposition atomique des parties et auto-similarité structurelle

$$\begin{aligned} \forall A, p, d \; \Big( &\text{AppAutom}(A) \land \text{Partie}(p, A) \land \text{Atome}(d) \land \text{Contient}(p, d) \implies \\  &\exists! c, \exists! t \; (\text{ComposantMetier}(c) \land \text{ComposantTech}(t) \land \text{Combine}(d, c, t)) \Big) \end{aligned}$$

### Axiome 3b — Exclusivité du composant métier

$$\begin{aligned} \forall d_1, d_2, c, t_1, t_2 \; &\Big( \big( \text{Atome}(d_1) \land \text{Atome}(d_2) \land \text{ComposantMetier}(c)  \land \text{ComposantTech}(t_1) \land \text{ComposantTech}(t_2) \land \\ &\text{Combine}(d_2, c, t_2) \land \text{Combine}(d_1, c, t_1) \big) \implies d_1 = d_2 \Big)\end{aligned}$$

### Axiome 3c — Principe de délégation récursive

Un composant technique peut déléguer à un atome système, les tâches de complexité lourde. L'atome système est lui-même défini comme un atome de domaine

$$\forall t, s \; \Big( \text{ComposantTech}(t) \land \text{DelegueA}(t, s) \implies \exists a, w \; \big( \text{Atome}(s) \land \text{AgentSysteme}(a) \land \text{ComposantTech}(w) \land \text{Combine}(s, a, w) \big) \Big)$$

### Axiome 4 - Unicité de la qualification des composants

$$\begin{aligned} \forall c \; &\Big( \text{Composant}(c) \implies \big(  \text{ComposantMetier}(c) \land \neg \text{ComposantTech}(c) \land \neg \text{AgentSysteme}(c) \big) \lor \\ &\big(  \neg \text{ComposantMetier}(c) \land \text{ComposantTech}(c) \land \neg \text{AgentSysteme}(c) \big) \lor \\ &\big( \neg \text{ComposantMetier}(c) \land \neg \text{ComposantTech}(c) \land \text{AgentSysteme}(c) \big) \Big) \end{aligned}$$

### Axiome 5 - Invariance du domaine par héritage

La relation d'héritage entre deux composants implique l'appartenance au même domaine conceptuel

$$\forall c, p \; \Big( \big( \text{Composant}(c) \land \text{Composant}(p) \land \text{HeriteDirectComposant}(c, p) \big) \implies \text{MemeDomaine}(c, p) \Big)$$


### Définitions

#### Définition - Composants métiers autonomes

$$\begin{aligned} \forall c \in \mathcal{C}, \quad &\text{ComposantMetierAutonome}(c) \iff \Big[ \text{ComposantMetier}(c) \;\;\land\;\; \big( \forall t \in \mathcal{C}, \forall d \in \mathcal{D}, \; \neg \text{Combine}(d, c, t) \big) \;\;\land \\ &\big( \forall p \in \mathcal{P}, \; \neg \text{ContientDirect}(p, c) \big) \Big] \end{aligned}$$

#### Définition - Composants techniques autonomes

$$\forall t \in \mathcal{C}, \quad \text{ComposantTechAutonome}(t) \iff \Big( \text{ComposantTech}(t) \;\;\land\;\; \big( \forall c \in \mathcal{C}, \forall d \in \mathcal{D}, \; \neg \text{Combine}(d, c, t) \big) \Big) $$

#### Définition - Agents systèmes autonomes

$$\begin{aligned} \forall a \in \mathcal{C}, \quad &\text{AgentSystemeAutonome}(a) \iff \Big[ \text{AgentSysteme}(a) \;\;\land\;\; \big( \forall t \in \mathcal{C}, \forall d \in \mathcal{D}, \; \neg \text{Combine}(d, a, t) \big) \;\;\land \\ &\big( \forall p \in \mathcal{P}, \; \neg \text{ContientDirect}(p, a) \big) \Big] \end{aligned}$$

### Axiome COMPS-VERS-CLPROLF

1. **Composant métier**

$$\forall d, c, t \; \Big( \text{Atome}(d) \land \text{Combine}(d, c, t) \land \text{ComposantMetier}(c) \implies \exists! classe \in \mathcal{C}_{\text{poo}} \; \big( \text{Annote}(classe, @ClAgent) \land \text{Realise}(classe, c) \big) \Big)$$

2. **Composant technique**

$$\forall d, c, t \; \Big(  \text{Atome}(d) \land \text{Combine}(d, c, t) \land \text{ComposantTech}(t) \implies \exists! classe \in \mathcal{C}_{\text{poo}} \; \big( \text{Annote}(classe, @ClWorker) \land \text{Realise}( classe, t) \big) \Big)$$

3. **Composant système**

$$\forall d, c, t \; \Big(  \text{Atome}(d) \land \text{Combine}(d, c, t) \land \text{AgentSysteme}(c) \implies \exists! classe \in \mathcal{C}_{\text{poo}} \; \big( \text{Annote}(classe, @ClSystem) \land \text{Realise}( classe, c) \big) \Big)$$

4. **Composants métiers autonomes**

$$\forall c \in \mathcal{C} \Big( \text{ComposantMetierAutonome}(c) \implies \exists! classe \in \mathcal{C}_{poo} \big( \text{Annote}(classe, @ClAgent) \land \text{Realise}(classe, c) \big) \Big) $$

5. **Composants techniques autonomes**

$$\forall c \in \mathcal{C} \Big( \text{ComposantTechAutonome}(c) \implies \exists! classe \in \mathcal{C}_{poo} \big( \text{Annote}(classe, @ClWorker) \land \text{Realise}(classe, c) \big) \Big) $$

6. **Composants systèmes autonomes**

$$\forall c \in \mathcal{C} \Big( \text{AgentSystemeAutonome}(c) \implies \exists! classe \in \mathcal{C}_{poo} \big( \text{Annote}(classe, @ClSystem) \land \text{Realise}(classe, c) \big) \Big) $$

### Axiome ATOMES-VERS-CLPROLF

$$\forall d, c, t, cl_c, cl_t \; \Big( \big( \text{Atome}(d) \land \text{Combine}(d, c, t) \land \text{Realise}(cl_c, c) \land \text{Realise}(cl_t, t) \big) \implies \text{InjectDepend}(cl_c, cl_t) \Big)$$

### Axiome PARTIES-VERS-CLPROLF

$$\forall A, p \; \Big( \text{AppAutom}(A) \land \text{Partie}(p, A) \implies \exists! s \in \mathcal{S}_{\text{poo}} \; \big( \text{SubsystemPOO}(s) \land \text{Realise}(s, p) \big) \Big)$$

### Axiome RECIPR-ATOME-METIER

$$\begin{aligned} \forall cl_c, cl_t \in \mathcal{C}_{\text{poo}} \; &\Big( \big( \text{Annote}(cl_c, \text{@ClAgent}) \land \text{Annote}(cl_t, \text{@ClWorker}) \land \text{InjectDepend}(cl_c, cl_t) \big) \implies \\ &\exists! d, \exists! c, \exists! t \; \big( \text{Atome}(d) \land \text{Combine}(d, c, t) \land \text{ComposantMetier}(c) \land \text{ComposantTech}(t) \land \\ &\text{Realise}(cl_c, c) \land \text{Realise}(cl_t, t) \big) \Big) \end{aligned}$$

### Axiome RECIPR-ATOME-SYSTEME

$$\begin{aligned} \forall cl_c, cl_t \in \mathcal{C}_{\text{poo}} \; &\Big( \big( \text{Annote}(cl_c, \text{@ClSystem}) \land \text{Annote}(cl_t, \text{@ClWorker}) \land \text{InjectDepend}(cl_c, cl_t) \big) \implies \\ &\exists! d, \exists! a, \exists! t \; \big( \text{Atome}(d) \land \text{Combine}(d, a, t) \land \text{AgentSysteme}(a) \land \text{ComposantTech}(t) \land \\ &\text{Realise}(cl_c, a) \land \text{Realise}(cl_t, t) \big) \Big) \end{aligned}$$

### Axiome RECIPR-COMPOSANT-METIER

$$\forall cl \in \mathcal{C}_{\text{poo}} \; \Big( \text{Annote}(cl, \text{@ClAgent}) \implies \exists! c \; \big( \text{ComposantMetier}(c) \land \text{Realise}(cl, c) \big) \Big)$$

### Axiome RECIPR-COMPOSANT-TECHN

$$\forall cl \in \mathcal{C}_{\text{poo}} \; \Big( \text{Annote}(cl, \text{@ClWorker}) \implies \exists! t \; \big( \text{ComposantTech}(t) \land \text{Realise}(cl, t) \big) \Big)$$

### Axiome RECIPR-COMPOSANT-SYST

$$\forall cl \in \mathcal{C}_{\text{poo}} \; \Big( \text{Annote}(cl, \text{@ClSystem}) \implies \exists! s \; \big( \text{AgentSysteme}(s) \land \text{Realise}(cl, s) \big) \Big)$$

### Axiome RECIPR-SOUS-SYST

$$\forall s \in \mathcal{S}_{\text{poo}} \; \Big( \text{SubsystemPOO}(s) \implies \exists! p, \exists A \; \big( \text{Partie}(p, A) \land \text{Realise}(s, p) \big) \Big)$$


## V. Axiomes et théorèmes sur les interfaces 

### Définitions

$$\text{TargetMetier}(i) \triangleq \begin{cases} \text{PolariteMetier}(i) & \text{si } i \in \mathcal{I} \land \text{Famille}(i) \quad (\text{Famille : polarité exclusive}) \\ \text{EstCompatibleMetier}(i) & \text{si } i \in \mathcal{I} \land \text{Trait}(i) \quad (\text{Trait : compatibilité dépendante du contexte}) \end{cases}$$

$$\text{TargetTech}(i) \triangleq \begin{cases} \text{PolariteTech}(i) & \text{si } i \in \mathcal{I} \land \text{Famille}(i) \quad (\text{Famille : polarité exclusive}) \\ \text{EstCompatibleTech}(i) & \text{si } i \in \mathcal{I} \land \text{Trait}(i) \quad (\text{Trait : compatibilité dépendante du contexte}) \end{cases}$$

$$\text{TargetAgentSyst}(i) \triangleq \begin{cases} \text{PolariteAgentSyst}(i) & \text{si } i \in \mathcal{I} \land \text{Famille}(i) \quad (\text{Famille : polarité exclusive}) \\ \text{EstCompatibleAgentSyst}(i) & \text{si } i \in \mathcal{I} \land \text{Trait}(i) \quad (\text{Trait : compatibilité dépendante du contexte}) \end{cases}$$

$$\begin{aligned} \text{MemePolarite}(f, c) \iff & \text{Famille}(f) \land \Big( \big( \text{ComposantMetier}(c) \iff \text{TargetMetier}(f) \big) \land \\ & \big( \text{ComposantTech}(c) \iff \text{TargetTech}(f) \big) \land \\ & \big( \text{AgentSysteme}(c) \iff \text{TargetAgentSyst}(f) \big) \Big) \end{aligned}$$

$$\text{MiroirDe}(f, c) \iff \big( \text{Famille}(f) \land \text{ImplementeDirect}(c, f) \land \text{MemePolarite}(f, c) \land \text{EquivSem}(c, f) \big)$$

### Axiome 1 - Polarité stricte des familles

$$\begin{aligned} \forall f \in \mathcal{I} \; &\Big( \text{Famille}(f) \implies \big( (\text{TargetMetier}(f) \land \neg \text{TargetTech}(f) \land \neg \text{TargetAgentSyst}(f)) \lor \\ &(\neg \text{TargetMetier}(f) \land \text{TargetTech}(f) \land \neg \text{TargetAgentSyst}(f)) \lor \\ &(\neg \text{TargetMetier}(f) \land \neg \text{TargetTech}(f) \land \text{TargetAgentSyst}(f)) \big) \Big) \end{aligned}$$

### Axiome 2 - Présence de la famille du composant, et interdiction des traits directs

$$\begin{aligned} \forall c \in \mathcal{C} \; &\Big( \Big( \exists! f \in \mathcal{I} \; \big( \text{Famille}(f) \land \text{ImplementeDirect}(c, f) \big) \Big) \land \\ &\Big( \forall t \in \mathcal{I} \; \big( \text{Trait}(t) \implies \neg \text{ImplementeDirect}(c, t) \big) \Big) \Big) \end{aligned}$$

### Axiome 3 - Miroir de la famille lors de l'implémentation

$$\forall c \in \mathcal{C}, \forall f \in \mathcal{I} \quad \Big( \text{Famille}(f) \land \text{ImplementeDirect}(c, f) \implies \text{MiroirDe}(f, c) \Big)$$

### Axiome 4 - Héritage d'un trait par une famille

$$\begin{aligned} \forall f, t \in \mathcal{I} \; &\Big( \text{Famille}(f) \land \text{Trait}(t) \land  \text{HeriteDirect}(f, t) \implies \big( (\text{TargetMetier}(f) \implies \text{TargetMetier}(t)) \land \\ &(\text{TargetTech}(f) \implies \text{TargetTech}(t)) \land (\text{TargetAgentSyst}(f) \implies \text{TargetAgentSyst}(t)) \big) \Big) \end{aligned}$$

### Axiome 5 — héritage des traits

$$\forall t, i \in \mathcal{I} \; \Big( \text{Trait}(t) \land \text{HeriteDirect}(t, i) \implies \text{Trait}(i) \Big)$$

### Axiome 6 - Invariance du domaine lors de l'héritage des familles entre elles

$$\forall f_1, f_2 \in \mathcal{I} \; \Big( \text{HeriteDirect}(f_1, f_2) \land \text{Famille}(f_1) \land \text{Famille}(f_2) \implies \text{MemeDomaineFamille}(f_1, f_2) \Big)$$

### Axiome 7 - Transmission de l'héritage direct à la famille miroir

$$\begin{aligned} \forall c, p \in \mathcal{C}, &\forall f_c, f_p \in \mathcal{I} \; \Big( \big( \text{HeriteDirectComposant}(c, p) \land \text{Famille}(f_c) \land \text{Famille}(f_p) \land \text{MiroirDe}(f_c, c) \land \\&\text{MiroirDe}(f_p, p) \big) \implies \text{HeriteDirect}(f_c, f_p) \Big) \end{aligned}$$

### Axiome INTERFS-VERS-CLPROLF

1. **Interface de Famille**

$$\forall f \in \mathcal{I} \; \Big( \text{Famille}(f) \implies \exists! interfacePoo \in \mathcal{I}_{\text{poo}} \; \big( \text{Annote}(interfacePoo, @ClFamily) \land \text{Realise}(interfacePoo, f) \big) \Big)$$

2. **Interface de Trait**

$$\forall t \in \mathcal{I} \; \Big( \text{Trait}(t) \implies \exists! interfacePoo \in \mathcal{I}_{\text{poo}} \; \big( \text{Annote}(interfacePoo, @ClTrait) \land \text{Realise}(interfacePoo, t) \big) \Big)$$

### Axiome RECIPR-INTERF-FAM

$$\forall int \in \mathcal{I}_{\text{poo}} \; \Big( \text{Annote}(int, \text{@ClFamily}) \implies \exists! f \in \mathcal{I} \; \big( \text{Famille}(f) \land \text{Realise}(int, f) \big) \Big)$$

### Axiome RECIPR-INTERF-TRAIT

$$\forall int \in \mathcal{I}_{\text{poo}} \; \Big( \text{Annote}(int, \text{@ClTrait}) \implies \exists! t \in \mathcal{I} \; \big( \text{Trait}(t) \land \text{Realise}(int, t) \big) \Big)$$


### Lemmes de structuration et de plongement


#### Lemme 1 — Clôture et propagation des traits

Pour toute famille $f$ et tout trait $t$, si $f$ hérite de $t$ (directement ou par transitivité), alors la polarité / cible du trait $t$ est nécessairement compatible avec la polarité de la famille $f$.

$$\begin{aligned} \forall f, t \in \mathcal{I} \; &\Big( \text{Famille}(f) \land \text{Trait}(t) \land \text{HeriteStar}(f, t) \implies \big( \text{TargetMetier}(f) \implies \text{TargetMetier}(t) \big) \land \\  &\big( \text{TargetTech}(f) \implies \text{TargetTech}(t) \big) \land \big( \text{TargetAgentSyst}(f) \implies \text{TargetAgentSyst}(t) \big) \Big) \end{aligned}$$

*Démonstration déductive (Récurrence structurelle) :*

$$\begin{array}{rll} 1. & \text{Famille}(f) \land \text{Trait}(t) \land \text{HeriteStar}(f, t) & \text{[Hypothèse]} \\ 2. & \text{Cas de base } (k=1) : \text{HeriteDirect}(f, t) \implies \text{Target}(f) \implies \text{Target}(t) & \text{[Application directe de l'Axiome 4]} \\ 3. & \text{Pas récursif } (k+1) : \exists i \in \mathcal{I} \; (\text{HeriteStar}(f, i) \land \text{HeriteDirect}(i, t)) & \text{[Décomposition de la chaîne d'interfaces]} \\ 4. & \text{Trait}(i) \land \text{Trait}(t) & \text{[Axiome 5 — Propagation du statut de trait]} \\ 5. & \text{HeriteStar}(f, i) \implies \text{Target}(f) \implies \text{Target}(i) & \text{[Hypothèse de récurrence]} \\ 6. & \text{HeriteDirect}(i, t) \implies \text{Target}(i) \implies \text{Target}(t) & \text{[Axiome 4 appliqué sur } i \to t \text{]} \\ 7. & \text{Target}(f) \implies \text{Target}(t) & \text{[Transitivité de l'implication]} \\ \hline \therefore & \text{HeriteStar}(f, t) \implies \big( \text{TargetMetier}(f) \implies \text{TargetMetier}(t) \big) \land \dots & \blacksquare \end{array}$$

---

#### Lemme 2 — Plongement des graphes de même domaine conceptuel, des composants vers les familles

Pour tous composants en relation d'héritage transitive, il existe de façon unique leurs interfaces de famille miroirs, et ces dernières conservent exactement la même relation d'héritage transitive. Le graphe des composants de même domaine conceptuel, se trouve dans le graphe des familles miroirs de même domaine.

$$\begin{aligned} \forall c_1, c_2 \in \mathcal{C} \; &\Big( \text{HeriteStarComposant}(c_1, c_2) \implies \exists! f_1, \exists! f_2 \in \mathcal{I} \; \big( \text{Famille}(f_1) \land \text{Famille}(f_2) \land \\ &\text{MiroirDe}(f_1, c_1) \land \text{MiroirDe}(f_2, c_2) \land \text{HeriteStar}(f_1, f_2) \big) \Big) \end{aligned}$$

*Démonstration déductive :*

$$\begin{aligned} \begin{array}{rll} 1. & \text{HeriteStarComposant}(c_1, c_2) & \text{[Hypothèse : relation d'héritage transitive dans } \mathcal{C} \text{]} \\  2. & \text{MiroirDe}(f_1, c_1) \land \text{MiroirDe}(f_2, c_2) & \text{[Axiomes 2 et 3 — Ancrage miroir (1:1)]} \\  3. & \text{Cas de base } (k=0) : c_1 = c_2 \implies f_1 = f_2 \implies \text{HeriteStar}(f_1, f_1) & \text{[Réflexivité de l'héritage]} \\  4. & \text{Pas récursif } (k+1) : \exists p \in \mathcal{C} \; (\text{HeriteStarComposant}(c_1, p) \land \\&\text{HeriteDirectComposant}(p, c_2)) & \text{[Décomposition de la chaîne]} \\  5. & \exists! f_p \in \mathcal{I} \quad \text{Famille}(f_p) \land \text{MiroirDe}(f_p, p) & \text{[Axiomes 2 et 3 — Ancrage miroir]} \\  6. & \text{HeriteStar}(f_1, f_p) & \text{[Hypothèse de récurrence]} \\  7. & \text{HeriteDirect}(f_p, f_2) & \text{[Axiome 7 — Propagation de l'héritage direct]} \\  8. & \text{HeriteStar}(f_1, f_2) & \text{[Transitivité de l'héritage dans } \mathcal{F} \text{]} \\  \hline \therefore & \text{HeriteStar}(f_1, f_2) & \blacksquare \end{array} \end{aligned} $$

---

#### Lemme 3 — Filiation fermée des familles d'accès

Pour tout objet $o$ manipulé sous un type statique de famille $f$, la famille $f$ appartient nécessairement au cône ascendant d'héritage ($\text{HeriteStar}$) de la famille miroir unique $f'$ associée à la classe concrète $c$ de $o$.

$$\begin{aligned} \forall o \in \mathcal{O}, \forall f \in \mathcal{I} \; \Big( \text{TypeStatique}(o, f) \land \text{Famille}(f) \implies \exists! c \in \mathcal{C}, \exists! f' \in \mathcal{I} \; \Big( & \text{InstanceDe}(o, c) \land \text{Famille}(f') \land \\ & \text{MiroirDe}(f', c) \land \text{HeriteStar}(f', f) \Big) \end{aligned}$$

*Démonstration déductive :*

$$\begin{aligned} \begin{array}{rll} 1. & \text{TypeStatique}(o, f) \land \text{Famille}(f) & \text{[Hypothèse]} \\  2. & \exists! c \in \mathcal{C} \quad \text{InstanceDe}(o, c) & \text{[Instance POO dynamique unique]} \\  3. & \exists! f' \in \mathcal{I} \quad \text{Famille}(f') \land \text{MiroirDe}(f', c) & \text{[Axiomes 2 \& 3 — Unicité de la famille miroir]} \\  4. & \forall f'' \in \mathcal{I} \quad (\text{Famille}(f'') \land \text{ImplementeDirect}(c, f'') \implies f'' = f') & \text{[Axiome 2 — Ancrage unique du composant]} \\  5. & \text{HeriteStar}(f', f) & \text{[Résolution POO : } f \text{ est nécessairement satisfait via } f' \text{]} \\  \hline \therefore & \exists! c \in \mathcal{C}, \exists! f' \in \mathcal{I} \; \Big( \text{InstanceDe}(o, c) \land \text{Famille}(f') \land \text{MiroirDe}(f', c) \land \\&\text{HeriteStar}(f', f) \Big) & \blacksquare \end{array} \end{aligned}$$

---

#### Lemme 4 — Confinement hiérarchique des traits

Pour tout objet $o$ manipulé sous un type statique de trait $t$, le trait $t$ se retrouve nécessairement dans la relation d'héritage d'une famille pivot $f$ ($\text{HeriteStar}(f, t)$) située dans le cône ascendant de la hiérarchie miroir $f'$ de la classe concrète $c$ de $o$.

$$\begin{aligned} \forall o \in \mathcal{O}, \forall t \in \mathcal{I} \; \Big( \text{TypeStatique}(o, t) \land \text{Trait}(t) \implies \exists! c \in \mathcal{C}, \exists! f' \in \mathcal{I}, \exists f \in \mathcal{I} \; \Big( & \text{InstanceDe}(o, c) \land \text{Famille}(f') \land \text{MiroirDe}(f', c) \land \\ & \text{Famille}(f) \land \text{HeriteStar}(f', f) \land \text{HeriteStar}(f, t) \Big) \Big) \end{aligned}$$

*Démonstration déductive :*

$$\begin{aligned} \begin{array}{rll} 
1. & \text{TypeStatique}(o, t) \land \text{Trait}(t) & \text{[Hypothèse]} \\ 
2. & \exists! c \in \mathcal{C} \quad \text{InstanceDe}(o, c) & \text{[Instance POO dynamique unique]} \\ 
3. & \exists! f' \in \mathcal{I} \quad \text{Famille}(f') \land \text{MiroirDe}(f', c) & \text{[Axiomes 2 \& 3 — Unicité de la famille miroir]} \\ 
4. & \forall t' \in \mathcal{I} \quad (\text{Trait}(t') \implies \neg \text{ImplementeDirect}(c, t')) & \text{[Axiome 2 — Interdiction des traits directs]} \\ 
5. & \exists f \in \mathcal{I} \quad \text{Famille}(f) \land \text{HeriteStar}(f', f) \land \text{HeriteStar}(f, t) & \text{[Résolution POO \& Lemme 1 } \text{]} \\ 
\hline 
\therefore & \exists! c \in \mathcal{C}, \exists! f' \in \mathcal{I}, \exists f \in \mathcal{I} \; \Big( \text{InstanceDe}(o, c) \land \text{Famille}(f') \land \text{MiroirDe}(f', c) \land \\ &\text{Famille}(f) \land \text{HeriteStar}(f', f) \land \text{HeriteStar}(f, t) \Big) & \blacksquare 
\end{array} \end{aligned}$$

---

#### Lemme 5 — Propagation du domaine par héritage de composants

La fermeture réflexive-transitive de l'héritage entre composants préserve par transitivité l'appartenance au même domaine conceptuel.

$$\forall c_1, c_2 \in \mathcal{C}, \quad \text{HeriteStarComposant}(c_1, c_2) \implies \text{MemeDomaine}(c_1, c_2)$$

*Démonstration (par induction structurelle sur la chaîne d'héritage direct) :*
Le cas de base ($c_1 = c_2$) est réflexif. Pour tout pas d'héritage direct $\text{HeriteDirectComposant}(a, b)$, l'Axiome 5 garantit $\text{MemeDomaine}(a, b)$. Par transitivité de la relation d'équivalence de domaine le long de la chaîne $\text{HeriteStarComposant}$, la propriété se propage à l'ensemble du cône d'héritage. $\blacksquare$

---

#### Lemme 6 — Propagation du domaine par héritage de familles et structuration du graphe

De même, l'héritage étendu entre interfaces de familles garantit la conservation stricte du domaine de proche en proche. Il s'ensuit que l'ensemble des familles associées à un même domaine conceptuel forme un graphe orienté (DAG) hiérarchique et strictement étanche.

$$\begin{aligned} \forall f_1, f_2 \in \mathcal{I}, \quad \big( \text{Famille}(f_1) \land \text{Famille}(f_2) \land \text{HeriteStar}(f_1, f_2) \big) \implies \text{MemeDomaineFamille}(f_1, f_2) \end{aligned}$$

*Démonstration :*
Conséquence directe de l'Axiome 6 ($\text{HeriteDirect} \implies \text{MemeDomaineFamille}$) liftée par la fermeture transitive $\text{HeriteStar}$. $\blacksquare$

---

### Théorèmes de polymorphisme et d'ancrage


#### Théorème 1 — Neutralité sur les composants du polymorphisme des familles

Pour tout objet $o$ qualifié statiquement par une famille $f$, il existe une unique classe concrète $c$ dont $o$ est l'instance directe, et une unique famille miroir $f'$ ancrée à $c$, telle que $f'$ hérite de $f$, appartient au même domaine conceptuel que $f$, et garantit l'équivalence sémantique avec le composant concret $c$.

$$\begin{aligned} \forall o \in \mathcal{O}, \, f \in \mathcal{F}, \quad & \Big( \text{TypeStatique}(o, f) \land \text{Famille}(f) \Big) \implies \\  & \exists! c \in \mathcal{C}, \, \exists! f' \in \mathcal{F} \; \Big( \text{InstanceDe}(o, c) \land \text{MiroirDe}(f', c) \land \text{EquivSem}(c, f') \land \\ & \quad \text{HeriteStar}(f', f) \land \text{MemeDomaineFamille}(f', f) \Big) \end{aligned}$$

*Démonstration déductive :*

$$\begin{aligned} \begin{array}{rll} 1. & \text{TypeStatique}(o, f) \land \text{Famille}(f) & \text{[Hypothèse ]} \\  2. & \exists! c \in \mathcal{C} \quad \text{InstanceDe}(o, c) & \text{[Axiome d'instanciation concrète dans } \mathcal{C} \text{]} \\  3. & \exists! f' \in \mathcal{F} \quad \text{MiroirDe}(f', c) & \text{[Axiomes 2 et 3 — Ancrage miroir strict 1:1]} \\  4. & \text{EquivSem}(c, f') & \text{[Axiome 4 — Ancrage sémantique miroir sur } f' \text{]} \\  5. & \text{HeriteStar}(f', f) & \text{[Lemme 3 — Compatibilité de typage dans } \mathcal{F} \text{]} \\  6. & \text{MemeDomaineFamille}(f', f) & \text{[Lemme 6 ]} \\  \hline \therefore & \exists! c \in \mathcal{C}, \, \exists! f' \in \mathcal{F} \; \Big( \text{InstanceDe}(o, c) \land \text{MiroirDe}(f', c) \land & \\  & \quad \text{EquivSem}(c, f') \land \text{HeriteStar}(f', f) \land \text{MemeDomaineFamille}(f', f) \Big) & \blacksquare \end{array} \end{aligned}$$

---


#### Corollaire 1.1 — Neutralité sur les composants du polymorphisme des traits

Pour tout objet $o$ qualifié statiquement par un trait $t$, il existe une unique classe concrète $c$ dont $o$ est l'instance directe, une unique famille miroir $f'$ ancrée à $c$, et une famille pivot $f$ dérivant de $t$, telles que $f'$ garantit l'équivalence sémantique avec $c$, dérive de $f$ et appartient au même domaine conceptuel que $f$.

$$\begin{aligned} \forall o \in \mathcal{O}, \, t \in \mathcal{F}, \quad & \Big( \text{TypeStatique}(o, t) \land \text{Trait}(t) \Big) \implies \\  & \exists! c \in \mathcal{C}, \, \exists! f' \in \mathcal{F}, \, \exists f \in \mathcal{F} \; \Big( \text{InstanceDe}(o, c) \land \text{Famille}(f') \land \text{MiroirDe}(f', c) \land \\  & \quad \text{EquivSem}(c, f') \land \text{Famille}(f) \land \text{HeriteStar}(f', f) \land \\  & \quad \text{MemeDomaineFamille}(f', f) \land \text{HeriteStar}(f, t) \Big) \end{aligned}$$

*Démonstration déductive :*

$$\begin{aligned} \begin{array}{rll} 1. & \text{TypeStatique}(o, t) \land \text{Trait}(t) & \text{[Hypothèse : qualification par une interface de trait]} \\  2. & \exists! c \in \mathcal{C}, \exists! f' \in \mathcal{F}, \exists f \in \mathcal{F} \quad \text{InstanceDe}(o, c) \land & \\  & \quad \text{Famille}(f') \land \text{MiroirDe}(f', c) \land \text{Famille}(f) \land & \\  & \quad \text{HeriteStar}(f', f) \land \text{HeriteStar}(f, t) & \text{[Application directe du Lemme 4]} \\  3. & \text{EquivSem}(c, f') & \text{[Axiome 4 — Ancrage sémantique miroir sur } f' \text{]} \\  4. & \text{MemeDomaineFamille}(f', f) & \text{[Lemme 6 sur } \text{HeriteStar}(f', f) \text{]} \\  \hline \therefore & \exists! c \in \mathcal{C}, \exists! f' \in \mathcal{F}, \exists f \in \mathcal{F} \; \Big( \text{InstanceDe}(o, c) \land & \\  & \quad \text{Famille}(f') \land \text{MiroirDe}(f', c) \land \text{EquivSem}(c, f') \land & \\  & \quad \text{Famille}(f) \land \text{HeriteStar}(f', f) \land \text{MemeDomaineFamille}(f', f) \land & \\  & \quad \text{HeriteStar}(f, t) \Big) & \blacksquare \end{array} \end{aligned}$$

---


### Théorème 2 — Structure et invariance du graphe des familles

Le Monde Abstrait $\mathcal{F}$ forme un graphe orienté d'interfaces de familles. Toute relation d'héritage étendu ($\text{HeriteStar}$) entre deux familles établit une dérivation hiérarchique directe tout en garantissant la stricte invariance du domaine conceptuel.

$$\begin{aligned} \forall f_1, f_2 \in \mathcal{F}, \quad \Big( \text{Famille}(f_1) \land \text{Famille}(f_2) \land \text{HeriteStar}(f_1, f_2) \Big) \implies \text{MemeDomaineFamille}(f_1, f_2) \end{aligned}$$

*Démonstration déductive :*

$$\begin{aligned} \begin{array}{rll} 1. & \text{Famille}(f_1) \land \text{Famille}(f_2) \land \text{HeriteStar}(f_1, f_2) & \text{[Hypothèse : relation dans le graphe } \mathcal{F} \text{]} \\  2. & \text{MemeDomaineFamille}(f_1, f_2) & \text{[Application directe du Lemme 6]} \\  \hline \therefore & \text{MemeDomaineFamille}(f_1, f_2) & \blacksquare \end{array} \end{aligned}$$

---


### Synthèse — La neutralité des interfaces sur les composants

> Le théorème 1 et le corollaire 1.1 établissent la conclusion suivante : **le polymorphisme d'interfaces — qu'il s'exprime par des familles ou par des traits — ne constitue en aucun cas une trahison sémantique ou domaniale du code concret.** Le système formel des composants, atomes, parties s'en trouve ainsi préservé.
> 
> 1. **La fidélité sémantique entre les interfaces et les composants est inviolable :** Le lien d'ancrage (`MiroirDe`) couplé à l'équivalence (`EquivSem`) garantit que le composant d'exécution concrétise rigoureusement le sens qualifié par le contrat.
> 2. **L'invariance du domaine est préservée :** La propagation transitive de proche en proche (`HeriteStar` et `HeriteStarComposant`) assure qu'aucune fuite conceptuelle ni hybridation illégitime ne vient corrompre le territoire métier initial.

---


#### Théorème 3 — Concrétisation d'une famille (0 ou plusieurs composants)

Pour toute interface de famille $f$, soit il n'existe aucun composant concret dont $f$ est la famille miroir (spécification pure), soit il existe un ou plusieurs composants concrets $c$ dont $f$ est la famille miroir.

$$\begin{aligned} \forall f \in \mathcal{F}, \quad \text{Famille}(f) \implies & \Big( \neg \exists c \in \mathcal{C} \; \text{MiroirDe}(f, c) \Big) \;\lor\; \Big( \exists c \in \mathcal{C} \; \text{MiroirDe}(f, c) \Big) \end{aligned}$$

*Démonstration déductive :*

$$\begin{aligned} \begin{array}{rll} 1. & \text{Famille}(f) & \text{[Hypothèse]} \\  2. & \forall c \in \mathcal{C}, \quad \exists! f'_c \in \mathcal{F} \quad \text{MiroirDe}(f'_c, c) & \text{[Axiomes 2 et 3 — Injectivité à gauche du miroir]} \\  3. & \text{Soit } C_f = \{ c \in \mathcal{C} \mid \text{MiroirDe}(f, c) \} & \text{[Ensemble des composants miroirs de } f \text{]} \\  4. & \text{Cas } C_f = \emptyset \implies \neg \exists c \in \mathcal{C} \; \text{MiroirDe}(f, c) & \text{[Spécification pure : 0 composant]} \\  5. & \text{Cas } C_f \neq \emptyset \implies \exists c \in \mathcal{C} \; \text{MiroirDe}(f, c) & \text{[Concrétisation : 1 à } N \text{ composants]} \\  \hline \therefore & \text{Une famille correspond à 0 ou plusieurs composants miroirs} & \blacksquare \end{array} \end{aligned}$$

---


#### Théorème 4 — Structure en DAG et héritage multiple des familles

Le Monde Abstrait $\mathcal{F}$ admet l'héritage multiple : une interface de famille $f \in \mathcal{F}$ peut dériver directement de plusieurs familles parentes distinctes $f_1, \dots, f_k \in \mathcal{F}$ ($k \ge 0$). Cette structure en graphe orienté acyclique (DAG) préserve l'invariance stricte du domaine métier sur chaque branche.

$$\begin{aligned} \forall f \in \mathcal{F}, \quad \text{Famille}(f) \implies & \Big( \exists k \ge 0, \exists f_1, \dots, f_k \in \mathcal{F} \quad \big( \big(\bigwedge_{i=1}^k \text{Famille}(f_i) \land \text{HeriteDirect}(f, f_i)\big) \land \\ & \big(\bigwedge_{i=1}^k \text{MemeDomaineFamille}(f, f_i)\big) \big) \Big) \end{aligned}$$

*Démonstration déductive :*

$$\begin{aligned} \begin{array}{rll} 1. & \text{Famille}(f) & \text{[Hypothèse]} \\  2. & \text{Soient } f_1, \dots, f_k \in \mathcal{F} \text{ telles que } \text{Famille}(f_i) \land \text{HeriteDirect}(f, f_i) & \text{[Parentes directes de } f \text{ dans } \mathcal{F} \text{]} \\  3. & \forall i \in \{1, \dots, k\}, \quad \text{MemeDomaineFamille}(f, f_i) & \text{[Application directe de l'Axiome 6 sur chaque arc]} \\  \hline \therefore & \text{L'héritage multiple dans } \mathcal{F} \text{ conserve le domaine métier sur } \\ &\text{toutes ses branches} & \blacksquare \end{array} \end{aligned}$$

---

#### Théorème 5 — Factorisation et requalification du polymorphisme POO

Pour toute classe POO $cl$ homogène implémentant directement un ensemble d'interfaces POO $\{int_1, \dots, int_k\}$, il existe une fonction de requalification domaniale $\psi : \mathcal{I}_{\text{poo}} \to (\mathcal{F} \cup \mathcal{T})$, un unique composant formel $c \in \mathcal{C}$ et une unique famille miroir pivot $f_{\text{pivot}} \in \mathcal{F}$ tels que :

1. $cl$ concrétise le composant $c \in \mathcal{C}$ ($\text{Realise}(cl, c)$).
2. L'unique famille miroir directe du composant $c$ est $f_{\text{pivot}}$ ($\text{MiroirDe}(f_{\text{pivot}}, c)$).
3. Chaque interface POO $int_j$ est requalifiée par $\psi$ soit en une **Famille** ($\psi(int_j) \in \mathcal{F}$), soit en un **Trait** ($\psi(int_j) \in \mathcal{T}$).
4. $f_{\text{pivot}}$ réunit et hérite dans le Monde Abstrait $\mathcal{I}$ de l'ensemble des abstractions requalifiées $\psi(int_j)$.
5. Pour toute interface requalifiée en famille ($\psi(int_j) \in \mathcal{F}$), l'invariance du domaine métier est strictement conservée ($\text{MemeDomaineFamille}(f_{\text{pivot}}, \psi(int_j))$).

$$\begin{aligned} \forall cl \in \mathcal{C}_{\text{poo}}, \, \forall \{int_1, \dots, int_k\} \subseteq \mathcal{I}_{\text{poo}}, \quad & \Big( \big(\bigwedge_{j=1}^k \text{ImplémentePOO}(cl, int_j)\big) \Big) \implies \\  & \exists! c \in \mathcal{C}, \, \exists! f_{\text{pivot}} \in \mathcal{F} \; \Big( \text{Realise}(cl, c) \;\land\; \text{MiroirDe}(f_{\text{pivot}}, c) \;\land\; \\ & \big(\bigwedge_{j=1}^k \text{HeriteStar}(f_{\text{pivot}}, \psi(int_j))\big) \Big) \end{aligned}$$

---


## VI. Implémentation et Ressources

* **Dépôt principal Clprolf (documentation, exemples et code source) :** [https://github.com/charleskoffler/clprolf-public](https://github.com/charleskoffler/clprolf-public)
* **Checker statique Java (basé sur ArchUnit) :** [https://github.com/charleskoffler/clprolf-public/tree/main/clprolf_checker](https://github.com/charleskoffler/clprolf-public/tree/main/clprolf_checker)
* **Checker statique C# .NET (basé sur ArchUnitNET) :** [https://github.com/charleskoffler/clprolf-public/tree/main/Clprolf.ArchUnitNet](https://github.com/charleskoffler/clprolf-public/tree/main/Clprolf.ArchUnitNet)