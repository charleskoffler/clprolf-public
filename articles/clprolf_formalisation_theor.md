<!-- Clprolf formalisation v1.0 18/10/2026 On a les 3 premiers axiomes relus et corrigés -->

### Axiome 1 - Applications d'automatisation
$$\forall A \; \Big(AppAutom(A) \implies \exists h \; \big(TravailHumain(h) \land Automatise(A,h) \big) \Big)$$

#### Le travail humain ($h$)

$$\forall h \; \Big( TravailHumain(h) \iff \exists G \, \exists \mathcal{M} \, \exists t_h \; \big( ButHumain(G) \land DomaineMetier(\mathcal{M}) \land SupportExecution(t_h) \land Viser(\mathcal{M}, G) \big) \Big)$$

### Axiome 2 - Décomposition en parties

$$\forall A \; \Big( \text{AppAutom}(A) \implies \exists \mathcal{M} \; \big( \text{DomaineMetier}(\mathcal{M}) \land \forall p \; ( \text{Partie}(p,A) \implies \exists m, t \; ( m \subseteq \mathcal{M} \land \text{SupportTech}(t) \land \text{Simule}(p,m) \land \text{Associe}(t, p) ) ) \big) \Big)$$

### Axiome 3 - Décomposition atomique des parties auto-similarité structurelle

$$\forall A \; \Big( AppAutom(A) \Rightarrow \forall p \;  \big( Partie(p,A) \Rightarrow \forall d \; ( Contient(p,d) \Rightarrow Atome(d) \land \exists c \, \exists t \; (ComposantMetier(c) \land ComposantTech(t) \land Combine(d, c, t)) ) \big) \Big)$$

#### Principe de délégation récursive ####

Un composant technique peut déléguer à un atome système, les tâches de complexité lourde. L'atome système est lui-même défini comme un atome de domaine

$$\forall t \; \Big( \text{ComposantTech}(t) \Rightarrow \forall s \; \big( \text{DelegueA}(t, s) \Rightarrow \exists a, w \; ( \text{Combine}(s, a, w) \land \text{Atome}(s) \land \text{AgentSysteme}(a) \land \text{Worker}(w) ) \big) \Big)$$

