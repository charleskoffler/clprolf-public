# Introduction

Clprolf est un DSL embarqué (EDSL) pour la pédagogie et les applications complexes. Il se présente sous la forme d'annotations et d'un checker statique de conformité sémantique. Le langage hôte est Java ou C#.

# Formalisation en système formel

## Axiomes et théorèmes axés sur les composants

Le premier axiome propose une approche téléologique de l'automatisation.

### Axiome 1 - Applications d'automatisation
$$\forall A \; \Big(\text{AppAutom}(A) \implies \exists h \; \big(\text{TravailHumain}(h) \land \text{Automatise}(A,h) \big) \Big)$$

#### Le travail humain ($h$)

$$\forall h \; \Big( \text{TravailHumain}(h) \iff \exists G, \mathcal{M}, t_h \; \big( \text{ButHumain}(G) \land \text{DomaineMetier}(\mathcal{M}) \land \text{SupportExecution}(t_h) \land \text{Poursuit}(h, G) \land \text{APourDomaine}(h, \mathcal{M}) \land \text{SAppuieSur}(h, t_h) \land \text{Sert}(\mathcal{M}, G) \big) \Big)$$

### Axiome 2 — Décomposition Structurelle en parties

$$\forall A, p \; \Big( \text{AppAutom}(A) \land \text{Partie}(p,A) \implies \exists! \mathcal{M}, m, t \; \big( \text{DomaineMetier}(\mathcal{M}) \land m \subseteq \mathcal{M} \land \text{SupportTech}(t) \land \text{Simule}(p,m) \land \text{Associe}(t, p) \big) \Big)$$

### Axiome 3 - Décomposition atomique des parties et auto-similarité structurelle

$$\forall A, p, d \; \Big( \text{AppAutom}(A) \land \text{Partie}(p, A) \land \text{Contient}(p, d) \implies \text{Atome}(d) \land \exists c, t \; (\text{ComposantMetier}(c) \land \text{ComposantTech}(t) \land \text{Combine}(d, c, t)) \Big)$$

#### Principe de délégation récursive ####

Un composant technique peut déléguer à un atome système, les tâches de complexité lourde. L'atome système est lui-même défini comme un atome de domaine

$$\forall t, s \; \Big( \text{ComposantTech}(t) \land \text{DelegueA}(t, s) \implies \text{Atome}(s) \land \exists a, w \; \big( \text{AgentSysteme}(a) \land \text{ComposantTech}(w) \land \text{Combine}(s, a, w) \big) \Big)$$

### Axiome 4 - Unicité de la qualification des composants

$$\forall c \; \Big( \text{Composant}(c) \implies \big(  \text{ComposantMetier}(c) \land \neg \text{ComposantTech}(c) \land \neg \text{AgentSysteme}(c) \big) \lor \big(  \neg \text{ComposantMetier}(c) \land \text{ComposantTech}(c) \land \neg \text{AgentSysteme}(c) \big) \lor \big( \neg \text{ComposantMetier}(c) \land \neg \text{ComposantTech}(c) \land \text{AgentSysteme}(c) \big) \Big)$$

### Axiome 5 - Invariance du domaine par héritage

La relation d'héritage entre deux composants implique l'appartenance au même domaine conceptuel

$$\forall c, p \; \Big( \big( \text{Composant}(c) \land \text{Composant}(p) \land \text{HeriteDirectComposant}(c, p) \big) \implies \text{MemeDomaine}(c, p) \Big)$$

### Axiome GEN-POO-1 - Génération des composants concrets $c \in C$

1. **Composant métier**

$$\forall c \; \Big(  \text{ComposantMetier}(c) \implies \exists! classe \in \mathcal{C}_{\text{poo}} \; \big( \text{Annote}(classe, @ClAgent) \land \text{Realise}( classe, c) \big) \Big) $$

2. **Composant technique**

$$\forall c \; \Big(  \text{ComposantTech}(c) \implies \exists! classe \in \mathcal{C}_{\text{poo}} \; \big( \text{Annote}(classe, @ClWorker) \land \text{Realise}( classe, c) \big) \Big) $$

3. **Composant système**

$$\forall c \; \Big(  \text{AgentSysteme}(c) \implies \exists! classe \in \mathcal{C}_{\text{poo}} \; \big( \text{Annote}(classe, @ClSystem) \land \text{Realise}( classe, c) \big) \Big) $$

# Axiomes et théorèmes sur les interfaces

