
# Introduction

Clprolf est un DSL embarqué (EDSL) pour la pédagogie et les applications complexes. Il se présente sous la forme d'annotations et d'un checker statique de conformité sémantique. Le langage hôte est Java ou C#.

# Formalisation en système formel

## Axiomes et théorèmes axés sur les composants

Le premier axiome propose une approche téléologique de l'automatisation.

### Axiome 1 - Applications d'automatisation
$$\forall A \; \Big(\text{AppAutom}(A) \implies \exists! h \; \big(\text{TravailHumain}(h) \land \text{Automatise}(A,h) \big) \Big)$$

### Axiome 1b - Le travail humain ($h$)

$$\forall h \; \Big( \text{TravailHumain}(h) \iff \exists! G, \exists! \mathcal{M}, \exists! se_h \; \big( \text{ButHumain}(G) \land \text{DomaineMetier}(\mathcal{M}) \land \text{SupportExecution}(se_h) \land \text{Poursuit}(h, G) \land \text{APourDomaine}(h, \mathcal{M}) \land \text{SAppuieSur}(h, se_h) \big) \Big)$$

### Axiome 2 — Décomposition Structurelle en parties

$$\forall A, p, h, \mathcal{M} \; \Big( \big( \text{AppAutom}(A) \land \text{Automatise}(A, h) \land \text{APourDomaine}(h, \mathcal{M}) \land \text{Partie}(p, A) \big) \implies \exists! m, \exists! t \; \big( m \subseteq \mathcal{M} \land \text{SupportTech}(t) \land \text{Simule}(p, m) \land \text{Associe}(t, p) \big) \Big)$$

### Axiome 3 - Décomposition atomique des parties et auto-similarité structurelle

$$\forall A, p, d \; \Big( \text{AppAutom}(A) \land \text{Partie}(p, A) \land \text{Atome}(d) \land \text{Contient}(p, d) \implies \exists! c, \exists! t \; (\text{ComposantMetier}(c) \land \text{ComposantTech}(t) \land \text{Combine}(d, c, t)) \Big)$$

### Axiome 3b — Exclusivité du composant métier

$$\forall d_1, d_2, c, t_1, t_2 \; \Big( \big( \text{Combine}(d_1, c, t_1) \land \text{Combine}(d_2, c, t_2) \big) \implies d_1 = d_2 \Big)$$

### Axiome 3c — Principe de délégation récursive

Un composant technique peut déléguer à un atome système, les tâches de complexité lourde. L'atome système est lui-même défini comme un atome de domaine

$$\forall t, s \; \Big( \text{ComposantTech}(t) \land \text{DelegueA}(t, s) \implies \exists a, w \; \big( \text{Atome}(s) \land \text{AgentSysteme}(a) \land \text{ComposantTech}(w) \land \text{Combine}(s, a, w) \big) \Big)$$

### Axiome 4 - Unicité de la qualification des composants

$$\forall c \; \Big( \text{Composant}(c) \implies \big(  \text{ComposantMetier}(c) \land \neg \text{ComposantTech}(c) \land \neg \text{AgentSysteme}(c) \big) \lor \big(  \neg \text{ComposantMetier}(c) \land \text{ComposantTech}(c) \land \neg \text{AgentSysteme}(c) \big) \lor \big( \neg \text{ComposantMetier}(c) \land \neg \text{ComposantTech}(c) \land \text{AgentSysteme}(c) \big) \Big)$$

### Axiome 5 - Invariance du domaine par héritage

La relation d'héritage entre deux composants implique l'appartenance au même domaine conceptuel

$$\forall c, p \; \Big( \big( \text{Composant}(c) \land \text{Composant}(p) \land \text{HeriteDirectComposant}(c, p) \big) \implies \text{MemeDomaine}(c, p) \Big)$$

### Axiome GEN-POO-1 - Génération des composants concrets de l'atome $c \in C$

1. **Composant métier**

$$\forall d, c, t \; \Big( \text{Atome}(d) \land \text{Combine}(d, c, t) \land \text{ComposantMetier}(c) \implies \exists! classe \in \mathcal{C}_{\text{poo}} \; \big( \text{Realise}(classe, c) \big) \Big)$$

2. **Composant technique**

$$\forall d, c, t \; \Big(  \text{Atome}(d) \land \text{Combine}(d, c, t) \land \text{ComposantTech}(c) \implies \exists! classe \in \mathcal{C}_{\text{poo}} \; \big( \text{Realise}( classe, c) \big) \Big)$$

3. **Composant système**

$$\forall d, c, t \; \Big(  \text{Atome}(d) \land \text{Combine}(d, c, t) \land \text{AgentSysteme}(c) \implies \exists! classe \in \mathcal{C}_{\text{poo}} \; \big( \text{Realise}( classe, c) \big) \Big)$$


### Axiome GEN-POO-2 - Génération des interfaces Clprolf ($\mathcal{I} = F \cup T$)

1. **Interface de Famille**

$$\forall f \; \Big( \text{Famille}(f) \implies \exists! interfacePoo \in \mathcal{I}_{\text{poo}} \; \big( \land \text{Realise}(interfacePoo, f) \big) \Big)$$

2. **Interface de Trait**

$$\forall t \; \Big( \text{Trait}(t) \implies \exists! interfacePoo \in \mathcal{I}_{\text{poo}} \; \big( \land \text{Realise}(interfacePoo, t) \big) \Big)$$

### Axiome GEN-POO-3 - Génération du lien de composition entre les composants d'un atome

$$\forall d, c, t, cl_c, cl_t \; \Big( \big( \text{Atome}(d) \land \text{Combine}(d, c, t) \land \text{Realise}(cl_c, c) \land \text{Realise}(cl_t, t) \big) \implies \text{CompositionPOO}(cl_c, cl_t) \Big)$$

### Axiome GEN-POO-4 - Génération des sous-systèmes des parties $p \in \mathcal{P}$

$$\forall A, p \; \Big( \text{AppAutom}(A) \land \text{Partie}(p, A) \implies \exists! s \in \mathcal{S}_{\text{poo}} \; \big( \text{SubsystemPOO}(s) \land \text{Realise}(s, p) \big) \Big)$$

## Axiomes et théorèmes sur les interfaces 

