# Introduction

Clprolf est un DSL embarqué (EDSL) pour la pédagogie et les applications complexes. Il se présente sous la forme d'annotations et d'un checker statique de conformité sémantique. Le langage hôte est Java ou C#.

# Formalisation en système formel

## Axiomes et théorèmes axés sur les composants

Le premier axiome propose une approche téléologique de l'automatisation.

### Axiome 1 - Applications d'automatisation
$$\forall A \; \Big(\text{AppAutom}(A) \implies \exists! h \; \big(\text{TravailHumain}(h) \land \text{Automatise}(A,h) \big) \Big)$$

### Axiome 1b.1 - Le travail humain ($h$)

$$\forall h \; \Big( \text{TravailHumain}(h) \implies \exists G, \exists! \mathcal{M}, \exists! se_h \; \big( \text{ButHumain}(G) \land \text{DomaineMetier}(\mathcal{M}) \land \text{SupportExecution}(se_h) \land \text{Poursuit}(h, G) \land \text{APourDomaine}(h, \mathcal{M}) \land \text{SAppuieSur}(h, se_h) \big) \Big)$$

### Axiome 1b.2 - Réciproque du travail humain

$$\forall h, G, \mathcal{M}, se_h \; \Big( \big( \text{ButHumain}(G) \land \text{DomaineMetier}(\mathcal{M}) \land \text{SupportExecution}(se_h) \land \text{Poursuit}(h, G) \land \text{APourDomaine}(h, \mathcal{M}) \land \text{SAppuieSur}(h, se_h) \big) \implies \text{TravailHumain}(h) \Big)$$

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

### Axiome COMPS-VERS-CLPROLF

1. **Composant métier**

$$\forall d, c, t \; \Big( \text{Atome}(d) \land \text{Combine}(d, c, t) \land \text{ComposantMetier}(c) \implies \exists! classe \in \mathcal{C}_{\text{poo}} \; \big( \text{Annote}(classe, @ClAgent) \land \text{Realise}(classe, c) \big) \Big)$$

2. **Composant technique**

$$\forall d, c, t \; \Big(  \text{Atome}(d) \land \text{Combine}(d, c, t) \land \text{ComposantTech}(t) \implies \exists! classe \in \mathcal{C}_{\text{poo}} \; \big( \text{Annote}(classe, @ClWorker) \land \text{Realise}( classe, t) \big) \Big)$$

3. **Composant système**

$$\forall d, c, t \; \Big(  \text{Atome}(d) \land \text{Combine}(d, c, t) \land \text{AgentSysteme}(c) \implies \exists! classe \in \mathcal{C}_{\text{poo}} \; \big( \text{Annote}(classe, @ClSystem) \land \text{Realise}( classe, c) \big) \Big)$$

### Axiome ATOMES-VERS-CLPROLF

$$\forall d, c, t, cl_c, cl_t \; \Big( \big( \text{Atome}(d) \land \text{Combine}(d, c, t) \land \text{Realise}(cl_c, c) \land \text{Realise}(cl_t, t) \big) \implies \text{CompositionPOO}(cl_c, cl_t) \Big)$$

### Axiome PARTIES-VERS-CLPROLF

$$\forall A, p \; \Big( \text{AppAutom}(A) \land \text{Partie}(p, A) \implies \exists! s \in \mathcal{S}_{\text{poo}} \; \big( \text{SubsystemPOO}(s) \land \text{Realise}(s, p) \big) \Big)$$

### Axiome RECIPR-ATOME-METIER

$$\forall cl_c, cl_t \in \mathcal{C}_{\text{poo}} \; \Big( \big( \text{Annote}(cl_c, \text{@ClAgent}) \land \text{Annote}(cl_t, \text{@ClWorker}) \land \text{CompositionPOO}(cl_c, cl_t) \big) \implies \exists! d, \exists! c, \exists! t \; \big( \text{Atome}(d) \land \text{Combine}(d, c, t) \land \text{ComposantMetier}(c) \land \text{ComposantTech}(t) \land \text{Realise}(cl_c, c) \land \text{Realise}(cl_t, t) \big) \Big)$$

### Axiome RECIPR-ATOME-SYSTEME

$$\forall cl_c, cl_t \in \mathcal{C}_{\text{poo}} \; \Big( \big( \text{Annote}(cl_c, \text{@ClSystem}) \land \text{Annote}(cl_t, \text{@ClWorker}) \land \text{CompositionPOO}(cl_c, cl_t) \big) \implies \exists! d, \exists! c, \exists! t \; \big( \text{Atome}(d) \land \text{Combine}(d, c, t) \land \text{AgentSysteme}(c) \land \text{ComposantTech}(t) \land \text{Realise}(cl_c, c) \land \text{Realise}(cl_t, t) \big) \Big)$$

### Axiome RECIPR-COMPOSANT-METIER

$$\forall cl \in \mathcal{C}_{\text{poo}} \; \Big( \text{Annote}(cl, \text{@ClAgent}) \implies \exists! c \; \big( \text{ComposantMetier}(c) \land \text{Realise}(cl, c) \big) \Big)$$

### Axiome RECIPR-COMPOSANT-TECHN

$$\forall cl \in \mathcal{C}_{\text{poo}} \; \Big( \text{Annote}(cl, \text{@ClWorker}) \implies \exists! t \; \big( \text{ComposantTech}(t) \land \text{Realise}(cl, t) \big) \Big)$$

### Axiome RECIPR-COMPOSANT-SYST

$$\forall cl \in \mathcal{C}_{\text{poo}} \; \Big( \text{Annote}(cl, \text{@ClSystem}) \implies \exists! s \; \big( \text{AgentSysteme}(s) \land \text{Realise}(cl, s) \big) \Big)$$

### Axiome RECIPR-SOUS-SYST

$$\forall s \in \mathcal{S}_{\text{poo}} \; \Big( \text{SubsystemPOO}(s) \implies \exists! p, \exists A \; \big( \text{Partie}(p, A) \land \text{Realise}(s, p) \big) \Big)$$


## Axiomes et théorèmes sur les interfaces 

### Définitions

$$\text{TargetMetier}(i) \triangleq \begin{cases} \text{PolariteMetier}(i) & \text{si } i \in F \quad (\text{Famille : polarité exclusive}) \\ \text{EstCompatibleMetier}(i) & \text{si } i \in T \quad (\text{Trait : compatibilité dépendante du contexte}) \end{cases}$$

$$\text{TargetTech}(i) \triangleq \begin{cases} \text{PolariteTech}(i) & \text{si } i \in F \quad (\text{Famille : polarité exclusive}) \\ \text{EstCompatibleTech}(i) & \text{si } i \in T \quad (\text{Trait : compatibilité dépendante du contexte}) \end{cases}$$

$$\text{TargetAgentSyst}(i) \triangleq \begin{cases} \text{PolariteAgentSyst}(i) & \text{si } i \in F \quad (\text{Famille : polarité exclusive}) \\ \text{EstCompatibleAgentSyst}(i) & \text{si } i \in T \quad (\text{Trait : compatibilité dépendante du contexte}) \end{cases}$$

$$\begin{aligned} \text{MemePolarite}(f, c) \iff & \big( \text{ComposantMetier}(c) \iff \text{TargetMetier}(f) \big) \land \\ & \big( \text{ComposantTech}(c) \iff \text{TargetTech}(f) \big) \land \\ & \big( \text{AgentSysteme}(c) \iff \text{TargetAgentSyst}(f) \big) \end{aligned}$$

$$\text{MiroirDe}(f, c) \iff \big( \text{Famille}(f) \land \text{Implemente}(c, f) \land \text{MemePolarite}(f, c) \big)$$

### Axiome 1 - Polarité stricte des familles

$$\forall f \in F \; \Big( (\text{TargetMetier}(f) \land \neg \text{TargetTech}(f) \land \neg \text{TargetAgentSyst}(f)) \lor (\neg \text{TargetMetier}(f) \land \text{TargetTech}(f) \land \neg \text{TargetAgentSyst}(f)) \lor (\neg \text{TargetMetier}(f) \land \neg \text{TargetTech}(f) \land \text{TargetAgentSyst}(f)) \Big)$$

### Axiome 2 - Présence de la famille implémentée, et interdiction des traits directs

$$\forall c \in C, \forall t \in T \; \Big( \big( \exists! f \in F \; \text{Implemente}(c, f) \big) \land \neg \text{Implemente}(c, t) \Big)$$

### Axiome 3 - Miroir de la famille lors de l'implémentation

$$\forall c \in C, \forall f \in F \quad \Big( \text{Famille}(f) \land \text{Implemente}(c, f) \implies \text{MiroirDe}(f, c) \Big)$$

### Axiome 4 - Héritage d'un trait par une famille

$$\forall f \in F, \forall t \in T \; \Big( \text{HeriteDirect}(f, t) \implies \big( (\text{TargetMetier}(f) \implies \text{TargetMetier}(t)) \land (\text{TargetTech}(f) \implies \text{TargetTech}(t)) \land (\text{TargetAgentSyst}(f) \implies \text{TargetAgentSyst}(t)) \big) \Big)$$

### Axiome 5 — héritage des traits

$$\forall t \in T, \forall i \in I \; \Big( \text{HeriteDirect}(t, i) \implies \text{Trait}(i) \Big)$$

### Axiome 6 - Clôture réflexive et transitive de l'héritage des traits

Par clôture réflexive et transitive de l'héritage direct des traits

$$\forall f \in F, \forall t \in T \; \Big( \text{HeriteStar}(f, t) \implies \big( \text{TargetMetier}(f) \implies \text{TargetMetier}(t) \big) \land \big( \text{TargetTech}(f) \implies \text{TargetTech}(t) \big) \land \big( \text{TargetAgentSyst}(f) \implies \text{TargetAgentSyst}(t) \big) \Big)$$

### Axiome 7 - Invariance du domaine lors de l'héritage des familles entre elles

$$\forall f_1, f_2 \in F \; \Big( \text{HeriteDirect}(f_1, f_2) \implies \text{MemeDomaineFamille}(f_1, f_2) \Big)$$

### Axiome 8 - Transmission de l'héritage direct à la famille miroir

$$\forall c, p \in C, \forall f_c, f_p \in F \; \Big( \big( \text{HeriteDirectComposant}(c, p) \land \text{MiroirDe}(f_c, c) \land \text{MiroirDe}(f_p, p) \big) \implies \text{HeriteDirect}(f_c, f_p) \Big)$$

### Axiome INTERFS-VERS-CLPROLF

1. **Interface de Famille**

$$\forall f \; \Big( \text{Famille}(f) \implies \exists! interfacePoo \in \mathcal{I}_{\text{poo}} \; \big( \text{Annote}(interfacePoo, @ClFamily) \land \text{Realise}(interfacePoo, f) \big) \Big)$$

2. **Interface de Trait**

$$\forall t \; \Big( \text{Trait}(t) \implies \exists! interfacePoo \in \mathcal{I}_{\text{poo}} \; \big( \text{Annote}(interfacePoo, @ClTrait) \land \text{Realise}(interfacePoo, t) \big) \Big)$$

### Axiome RECIPR-INTERF-FAM

$$\forall int \in \mathcal{I}_{\text{poo}} \; \Big( \text{Annote}(int, \text{@ClFamily}) \implies \exists! f \; \big( \text{Famille}(f) \land \text{Realise}(int, f) \big) \Big)$$

### Axiome RECIPR-INTERF-TRAIT

$$\forall int \in \mathcal{I}_{\text{poo}} \; \Big( \text{Annote}(int, \text{@ClTrait}) \implies \exists! t \; \big( \text{Trait}(t) \land \text{Realise}(int, t) \big) \Big)$$

### Théorème 1 - Neutralité du polymorphisme des familles

$$\forall o, \forall f \in F, \quad \text{TypeStatique}(o, f) \implies \exists! f' \in F, \exists! c \in C \; \Big( \text{InstanceDe}(o, c) \land \text{MiroirDe}(f', c) \land \text{HeriteStar}(f', f) \Big)$$

---

### Corollaire 1.1 — Neutralité du polymorphisme des traits

$$\forall o, \forall t \in T, \quad \text{TypeStatique}(o, t) \implies \exists f, f' \in F, \exists! c \in C \; \Big( \text{InstanceDe}(o, c) \land \text{HeriteStar}(f, t) \land \text{MiroirDe}(f', c) \land \text{HeriteStar}(f', f) \Big)$$

---