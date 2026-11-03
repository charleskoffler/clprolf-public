<!-- Clprolf formalisation v2.5 (20261102).  -->

# Formalisation axiomatique de Clprolf

## I. Introduction

Clprolf est un DSL embarqué (EDSL) pour la pédagogie et les applications complexes. Il se présente sous la forme d'annotations et d'un checker statique de conformité sémantique. Le langage hôte est Java ou C#.
Le modèle formel Clprolf modélise la topologie logicielle statique d'une application d'automatisation orientée objet ou des bibliothèques qui lui servent de support. 


## I.b Portée du Framework et Articulation avec le Modèle Formel
 

 Le modèle formel de Clprolf définit une topologie théorique complète (incluant les **Parties de l'application $\mathcal{P}_A$**, les **Atomes architecturaux $\mathcal{D}$**, ou la catégorisation fine des **Composants Autonomes**). La mise en œuvre pratique dans le framework repose sur la philosophie suivante :
 

 * **Contrôles focalisés sur le décidable et la valeur ajoutée :** Le checker statique (ArchUnit / ArchUnitNET) vérifie les contraintes fondamentales et décidables du modèle (séparation métier/technique, invariance du domaine par héritage, polarité des interfaces). Il ne cherche pas l'exhaustivité là où elle n'apporte aucun gain de qualité ou d'ingénierie.


 * **Absence de sur-contrainte et de bruit visuel :**
 * Aucun contrôle n'est effectué par le checker sur d'éventuels composants métiers situés dans une partie mais hors d'un atome.
 

 * Le modèle formel caractérise les composants métiers autonomes comme résidant hors de toute partie, mais n'interdit pas les formes hybrides.
 
 
 * Aucun artefact ou annotation supplémentaire (`@ClAtom`, `@ClPart`) n'est imposé dans le code. Les atomes et sous-systèmes émergent naturellement de la structuration standard du projet (injection `@ClAgent` / `@ClWorker`, packages/namespaces).

 Cette frontière garantit une adoption fluide en entreprise et préserve la liberté du développeur, tout en verrouillant 100 % des règles d'architecture indispensables.

---

## II. Dictionnaires du système

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
* $\text{HeriteStarComposant}(c_1, c_2)$ : Fermeture réflexive-transitive de l'héritage entre composants ($\ge 0$ niveau).
* $\text{MemeDomaine}(c_1, c_2)$ : $c_1$ et $c_2$ appartiennent au même domaine conceptuel.
* $\text{Annote}(elem, \alpha)$ : L'élément POO ($elem \in \mathcal{C}_{\text{poo}} \cup \mathcal{I}_{\text{poo}}$) porte l'annotation $\alpha$.
* $\text{Realise}(elem_{\text{poo}}, comp_{\text{formel}})$ : Le code POO concrétise le composant ou l'interface formelle.
* $\text{InjectDepend}(cl_1, cl_2)$ : La classe $cl_1$ injecte une dépendance $cl_2$.

---

#### c. Prédicats atomiques des Interfaces

* $\text{Famille}(i)$ : $i \in \mathcal{I}$ est une interface de famille (`@ClFamily`).
* $\text{Trait}(i)$ : $i \in \mathcal{I}$ est une interface de trait/capacité (`@ClTrait`).

---

#### d. Prédicats de relations des interfaces

* $\text{Implemente}(c, f)$ : Le composant/classe $c$ implémente l'interface $f$.
* $\text{TargetMetier}(i)$ / $\text{TargetTech}(i)$ / $\text{TargetAgentSyst}(i)$ : $i \in \mathcal{I}$. Indique si l'interface $i$ cible/est compatible avec le rôle Métier, Technique ou Système.
* $\text{MemePolarite}(f, c)$ : $f \in \mathcal{I}$, $c \in \mathcal{C}$. Équivalence exacte de polarité/rôle entre la famille $f$ et le composant $c$, et nécessité d'être une famille
* $\text{MiroirDe}(f, c)$ : $f$ est l'interface miroir exacte du composant $c$.
* $\text{HeriteDirect}(i_1, i_2)$ : L'interface $i_1$ étend directement $i_2$.
* $\text{HeriteStar}(i_1, i_2)$ : Fermeture réflexive-transitive de l'héritage d'interfaces.
* $\text{MemeDomaineFamille}(i_1, i_2)$ : Condition de famille, et conservation du domaine entre deux interfaces.
* $\text{TypeStatique}(o, f)$ : Variable/objet $o$ manipulé sous le type statique de l'interface $f$.
* $\text{InstanceDe}(o, c)$ : L'objet $o$ est une instance dynamique réelle de la classe $c$.


## III. Axiomes et théorèmes axés sur les composants

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

$$\forall c \in \mathcal{C}, \quad \text{ComposantMetierAutonome}(c) \iff \Big[ \text{ComposantMetier}(c) \;\;\land\;\; \big( \forall t \in \mathcal{C}, \forall d \in \mathcal{D}, \; \neg \text{Combine}(d, c, t) \big) \;\;\land\;\; \big( \forall p \in \mathcal{P}, \; \neg \text{ContientDirect}(p, c) \big) \Big]$$

#### Définition - Composants techniques autonomes

$$\forall t \in \mathcal{C}, \quad \text{ComposantTechAutonome}(t) \iff \Big( \text{ComposantTech}(t) \;\;\land\;\; \big( \forall c \in \mathcal{C}, \forall d \in \mathcal{D}, \; \neg \text{Combine}(d, c, t) \big) \Big) $$

#### Définition - Agents systèmes autonomes

$$\forall a \in \mathcal{C}, \quad \text{AgentSystemeAutonome}(a) \iff \Big[ \text{AgentSysteme}(a) \;\;\land\;\; \big( \forall t \in \mathcal{C}, \forall d \in \mathcal{D}, \; \neg \text{Combine}(d, a, t) \big) \;\;\land\;\; \big( \forall p \in \mathcal{P}, \; \neg \text{ContientDirect}(p, a) \big) \Big]$$

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

$$\begin{aligned} \forall cl_c, cl_t \in \mathcal{C}_{\text{poo}} \; &\Big( \big( \text{Annote}(cl_c, \text{@ClAgent}) \land \text{Annote}(cl_t, \text{@ClWorker}) \land \text{InjectDepend}(cl_c, cl_t) \big) \implies \\ &\exists! d, \exists! c, \exists! t \; \big( \text{Atome}(d) \land \text{Combine}(d, c, t) \land \text{ComposantMetier}(c) \land \text{ComposantTech}(t) \land \text{Realise}(cl_c, c) \land \text{Realise}(cl_t, t) \big) \Big) \end{aligned}$$

### Axiome RECIPR-ATOME-SYSTEME

$$\begin{aligned} \forall cl_c, cl_t \in \mathcal{C}_{\text{poo}} \; &\Big( \big( \text{Annote}(cl_c, \text{@ClSystem}) \land \text{Annote}(cl_t, \text{@ClWorker}) \land \text{InjectDepend}(cl_c, cl_t) \big) \implies \\ &\exists! d, \exists! c, \exists! t \; \big( \text{Atome}(d) \land \text{Combine}(d, c, t) \land \text{AgentSysteme}(c) \land \text{ComposantTech}(t) \land \text{Realise}(cl_c, c) \land \text{Realise}(cl_t, t) \big) \Big) \end{aligned}$$

### Axiome RECIPR-COMPOSANT-METIER

$$\forall cl \in \mathcal{C}_{\text{poo}} \; \Big( \text{Annote}(cl, \text{@ClAgent}) \implies \exists! c \; \big( \text{ComposantMetier}(c) \land \text{Realise}(cl, c) \big) \Big)$$

### Axiome RECIPR-COMPOSANT-TECHN

$$\forall cl \in \mathcal{C}_{\text{poo}} \; \Big( \text{Annote}(cl, \text{@ClWorker}) \implies \exists! t \; \big( \text{ComposantTech}(t) \land \text{Realise}(cl, t) \big) \Big)$$

### Axiome RECIPR-COMPOSANT-SYST

$$\forall cl \in \mathcal{C}_{\text{poo}} \; \Big( \text{Annote}(cl, \text{@ClSystem}) \implies \exists! s \; \big( \text{AgentSysteme}(s) \land \text{Realise}(cl, s) \big) \Big)$$

### Axiome RECIPR-SOUS-SYST

$$\forall s \in \mathcal{S}_{\text{poo}} \; \Big( \text{SubsystemPOO}(s) \implies \exists! p, \exists A \; \big( \text{Partie}(p, A) \land \text{Realise}(s, p) \big) \Big)$$


## IV. Axiomes et théorèmes sur les interfaces 

### Définitions

$$\text{TargetMetier}(i) \triangleq \begin{cases} \text{PolariteMetier}(i) & \text{si } i \in \mathcal{I} \land \text{Famille}(i) \quad (\text{Famille : polarité exclusive}) \\ \text{EstCompatibleMetier}(i) & \text{si } i \in \mathcal{I} \land \text{Trait}(i) \quad (\text{Trait : compatibilité dépendante du contexte}) \end{cases}$$

$$\text{TargetTech}(i) \triangleq \begin{cases} \text{PolariteTech}(i) & \text{si } i \in \mathcal{I} \land \text{Famille}(i) \quad (\text{Famille : polarité exclusive}) \\ \text{EstCompatibleTech}(i) & \text{si } i \in \mathcal{I} \land \text{Trait}(i) \quad (\text{Trait : compatibilité dépendante du contexte}) \end{cases}$$

$$\text{TargetAgentSyst}(i) \triangleq \begin{cases} \text{PolariteAgentSyst}(i) & \text{si } i \in \mathcal{I} \land \text{Famille}(i) \quad (\text{Famille : polarité exclusive}) \\ \text{EstCompatibleAgentSyst}(i) & \text{si } i \in \mathcal{I} \land \text{Trait}(i) \quad (\text{Trait : compatibilité dépendante du contexte}) \end{cases}$$

$$\begin{aligned} \text{MemePolarite}(f, c) \iff & \text{Famille}(f) \land \Big( \big( \text{ComposantMetier}(c) \iff \text{TargetMetier}(f) \big) \land \\ & \big( \text{ComposantTech}(c) \iff \text{TargetTech}(f) \big) \land \\ & \big( \text{AgentSysteme}(c) \iff \text{TargetAgentSyst}(f) \big) \Big) \end{aligned}$$

$$\text{MiroirDe}(f, c) \iff \big( \text{Famille}(f) \land \text{Implemente}(c, f) \land \text{MemePolarite}(f, c) \big)$$

### Axiome 1 - Polarité stricte des familles

$$\begin{aligned} \forall f \in \mathcal{I} \; &\Big( \text{Famille}(f) \implies \big( (\text{TargetMetier}(f) \land \neg \text{TargetTech}(f) \land \neg \text{TargetAgentSyst}(f)) \lor \\ &(\neg \text{TargetMetier}(f) \land \text{TargetTech}(f) \land \neg \text{TargetAgentSyst}(f)) \lor \\ &(\neg \text{TargetMetier}(f) \land \neg \text{TargetTech}(f) \land \text{TargetAgentSyst}(f)) \big) \Big) \end{aligned}$$

### Axiome 2 - Présence de la famille du composant, et interdiction des traits directs

$$\begin{aligned} \forall c \in \mathcal{C} \; &\Big( \Big( \exists! f \in \mathcal{I} \; \big( \text{Famille}(f) \land \text{Implemente}(c, f) \big) \Big) \land \\ &\Big( \forall t \in \mathcal{I} \; \big( \text{Trait}(t) \implies \neg \text{Implemente}(c, t) \big) \Big) \Big) \end{aligned}$$

### Axiome 3 - Miroir de la famille lors de l'implémentation

$$\forall c \in \mathcal{C}, \forall f \in \mathcal{I} \quad \Big( \text{Famille}(f) \land \text{Implemente}(c, f) \implies \text{MiroirDe}(f, c) \Big)$$

### Axiome 4 - Héritage d'un trait par une famille

$$\begin{aligned} \forall f, t \in \mathcal{I} \; &\Big( \text{Famille}(f) \land \text{Trait}(t) \land  \text{HeriteDirect}(f, t) \implies \big( (\text{TargetMetier}(f) \implies \text{TargetMetier}(t)) \land \\ &(\text{TargetTech}(f) \implies \text{TargetTech}(t)) \land (\text{TargetAgentSyst}(f) \implies \text{TargetAgentSyst}(t)) \big) \Big) \end{aligned}$$

### Axiome 5 — héritage des traits

$$\forall t, i \in \mathcal{I} \; \Big( \text{Trait}(t) \land \text{HeriteDirect}(t, i) \implies \text{Trait}(i) \Big)$$

### Théorème - Clôture réflexive et transitive de l'héritage des traits

Par clôture réflexive et transitive de l'héritage direct des traits

$$\begin{aligned}  \forall f, t \in \mathcal{I} \; &\Big( \text{Famille}(f) \land \text{Trait}(t) \land \text{HeriteStar}(f, t) \implies \big( \text{TargetMetier}(f) \implies \text{TargetMetier}(t) \big) \land \\ &\big( \text{TargetTech}(f) \implies \text{TargetTech}(t) \big) \land \big( \text{TargetAgentSyst}(f) \implies \text{TargetAgentSyst}(t) \big) \Big) \end{aligned} $$

### Axiome 7 - Invariance du domaine lors de l'héritage des familles entre elles

$$\forall f_1, f_2 \in \mathcal{I} \; \Big( \text{HeriteDirect}(f_1, f_2) \land \text{Famille}(f_1) \land \text{Famille}(f_2) \implies \text{MemeDomaineFamille}(f_1, f_2) \Big)$$

### Axiome 8 - Transmission de l'héritage direct à la famille miroir

$$\forall c, p \in \mathcal{C}, \forall f_c, f_p \in \mathcal{I} \; \Big( \big( \text{HeriteDirectComposant}(c, p) \land \text{Famille}(f_c) \land \text{Famille}(f_p) \land \text{MiroirDe}(f_c, c) \land \text{MiroirDe}(f_p, p) \big) \implies \text{HeriteDirect}(f_c, f_p) \Big)$$

### Axiome INTERFS-VERS-CLPROLF

1. **Interface de Famille**

$$\forall f \in \mathcal{I} \; \Big( \text{Famille}(f) \implies \exists! interfacePoo \in \mathcal{I}_{\text{poo}} \; \big( \text{Annote}(interfacePoo, @ClFamily) \land \text{Realise}(interfacePoo, f) \big) \Big)$$

2. **Interface de Trait**

$$\forall t \in \mathcal{I} \; \Big( \text{Trait}(t) \implies \exists! interfacePoo \in \mathcal{I}_{\text{poo}} \; \big( \text{Annote}(interfacePoo, @ClTrait) \land \text{Realise}(interfacePoo, t) \big) \Big)$$

### Axiome RECIPR-INTERF-FAM

$$\forall int \in \mathcal{I}_{\text{poo}} \; \Big( \text{Annote}(int, \text{@ClFamily}) \implies \exists! f \in \mathcal{I} \; \big( \text{Famille}(f) \land \text{Realise}(int, f) \big) \Big)$$

### Axiome RECIPR-INTERF-TRAIT

$$\forall int \in \mathcal{I}_{\text{poo}} \; \Big( \text{Annote}(int, \text{@ClTrait}) \implies \exists! t \in \mathcal{I} \; \big( \text{Trait}(t) \land \text{Realise}(int, t) \big) \Big)$$

### Théorème 1 - Neutralité du polymorphisme des familles

$$\begin{aligned} \forall o\in \mathcal{O}, f \in \mathcal{I}, \quad \text{TypeStatique}(o, f) \land &\text{Famille}(f) \implies \exists! f' \in \mathcal{I}, \exists! c \in \mathcal{C} \; \Big( \text{InstanceDe}(o, c) \land \text{Famille}(f') \land \\&\text{MiroirDe}(f', c) \land \text{HeriteStar}(f', f) \Big) \end{aligned}$$

---

### Corollaire 1.1 — Neutralité du polymorphisme des traits

$$\begin{aligned} \forall o \in \mathcal{O}, \forall t \in \mathcal{I}, \quad &\text{TypeStatique}(o, t) \land \text{Trait}(t) \implies \exists f, f' \in \mathcal{I}, \exists! c \in \mathcal{C} \; \Big( \text{InstanceDe}(o, c) \land \text{HeriteStar}(f, t) \land \text{Famille}(f) \land \text{Famille}(f') \\ \land &\text{MiroirDe}(f', c) \land \text{HeriteStar}(f', f) \Big) \end{aligned}$$

---

### Lemme 1.0 — Isomorphisme de miroir et d'héritage entre composants et familles

#### Énoncé

Pour tous composants en relation d'héritage transitive, il existe de façon unique leurs interfaces de famille miroirs, et ces dernières conservent exactement la même relation d'héritage transitive. 

$$\begin{aligned} \forall c_1, c_2 \in \mathcal{C} \; &\Big( \text{HeriteStarComposant}(c_1, c_2) \implies \exists! f_1, \exists! f_2 \in \mathcal{I} \; \big( \text{Famille}(f_1) \land \text{Famille}(f_2) \land \\ &\text{MiroirDe}(f_1, c_1) \land \text{MiroirDe}(f_2, c_2) \land \text{HeriteStar}(f_1, f_2) \big) \Big) \end{aligned} $$

#### Démonstration déductive (*Forward Proof*)

$$\begin{array}{rll} 1. & \text{HeriteStarComposant}(c_1, c_2) & \text{[Hypothèse : relation d'héritage transitive dans } \mathcal{C} \text{]} \\ 2. & \text{MiroirDe}(f_1, c_1) \land \text{MiroirDe}(f_2, c_2) & \text{[Axiomes 2 et 3 — Ancrage miroir (1:1)]} \\ 3. & \text{Cas de base } (k=0) : c_1 = c_2 \implies f_1 = f_2 \implies \text{HeriteStar}(f_1, f_1) & \text{[Réflexivité de l'héritage]} \\ 4. & \text{Pas récursif } (k+1) : \exists p \in \mathcal{C} \; (\text{HeriteStarComposant}(c_1, p) \land \text{HeriteDirectComposant}(p, c_2)) & \text{[Décomposition de la chaîne d'héritage]} \\ 5. & \exists! f_p \in \mathcal{I} \quad \text{Famille}(f_p) \land \text{MiroirDe}(f_p, p) & \text{[Axiomes 2 et 3 — Ancrage miroir]} \\ 6. & \text{HeriteStar}(f_1, f_p) & \text{[Hypothèse de récurrence]} \\ 7. & \text{HeriteDirect}(f_p, f_2) & \text{[Axiome 8 — Propagation de l'héritage direct]} \\ 8. & \text{HeriteStar}(f_1, f_2) & \text{[Transitivité de l'héritage dans } \mathcal{F} \text{]} \\ \hline \therefore & \text{HeriteStar}(f_1, f_2) & \blacksquare \end{array}$$


### 1. La situation de départ (Lignes 1 et 2)

On prend un composant enfant $c_1$ qui hérite (directement ou sur plusieurs niveaux) d'un composant parent $c_2$. Par construction du système (Axiomes 2 et 3), le composant enfant a sa propre interface miroir $f_1$, et le composant parent a la sienne, $f_2$.

On veut prouver que l'interface enfant $f_1$ hérite obligatoirement de l'interface parente $f_2$.

---

### 2. Le cas de base — $0$ niveau d'écart (Ligne 3)

Si le composant enfant et le composant parent sont en fait le même ($c_1 = c_2$), leurs interfaces miroirs sont identiques ($f_1 = f_2$). La relation d'héritage est vraie par simple **réflexivité** (une interface « hérite » d'elle-même).

---

### 3. Le pas récursif — La chaîne d'héritage (Lignes 4 à 8)

Si $c_1$ hérite de $c_2$ à travers une chaîne de plusieurs classes :

* **Lignes 4 & 5 :** On isole le composant intermédiaire $p$ situé juste en dessous de $c_2$ dans la chaîne ($p$ hérite directement de $c_2$). Ce composant $p$ possède de manière unique sa propre interface miroir $f_p$.
* **Ligne 6 (Hypothèse de récurrence) :** On admet que la propriété est vraie pour le sous-trajet de $c_1$ à $p$ : l'interface miroir $f_1$ hérite de l'interface $f_p$ ($\text{HeriteStar}(f_1, f_p)$).
* **Ligne 7 (Axiome 8) :** Puisque $p$ hérite directement de $c_2$, l'Axiome 8 propage cette liaison aux miroirs : l'interface $f_p$ hérite directement de $f_2$ ($\text{HeriteDirect}(f_p, f_2)$).
* **Ligne 8 (Transitivité) :** Si $f_1$ hérite de $f_p$, et que $f_p$ hérite de $f_2$, alors par transitivité de l'héritage dans $\mathcal{F}$, $f_1$ hérite de $f_2$.


### Démonstration du Théorème 1

#### Énoncé du Théorème 1

$$\begin{aligned} \forall o\in \mathcal{O}, f \in \mathcal{I}, \quad \text{TypeStatique}(o, f) \land &\text{Famille}(f) \implies \exists! f' \in \mathcal{I}, \exists! c \in \mathcal{C} \; \Big( \text{InstanceDe}(o, c) \land \text{Famille}(f') \land \\&\text{MiroirDe}(f', c) \land \text{HeriteStar}(f', f) \Big) \end{aligned}$$


#### Démonstration déductive (*Forward Proof*)

$$\begin{array}{rll} 1. & \text{TypeStatique}(o, f) \land \text{Famille}(f) & \text{[Hypothèse : appel via la famille } f \text{]} \\ 2. & \exists c_f \in \mathcal{C} \quad \text{MiroirDe}(f, c_f) & \text{[Axiome 3 — Ancrage miroir du type/contrat } f \text{]} \\ 3. & \exists! c \in \mathcal{C} \quad \text{InstanceDe}(o, c) \land \text{HeriteStarComposant}(c, c_f) & \text{[Sécurité du typage POO : } o \text{ instancie } c \text{ qui dérive de } c_f \text{]} \\ 4. & \exists! f' \in \mathcal{I} \quad \text{Famille}(f') \land \text{MiroirDe}(f', c) & \text{[Axiome 3 — Ancrage miroir sur le composant réel } c \text{]} \\ 5. & \text{HeriteStar}(f', f) & \text{[Application du Lemme 1.0 sur } \text{HeriteStarComposant}(c, c_f) \text{]} \\ \hline \therefore & \exists! c \in \mathcal{C}, \exists! f' \in \mathcal{I} \; \Big( \text{InstanceDe}(o, c) \land \text{Famille}(f') \land \text{MiroirDe}(f', c) \land \text{HeriteStar}(f', f) \Big) & \blacksquare \end{array}$$

---

