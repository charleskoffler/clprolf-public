<!-- Clprolf formalisation v1.1 18/10/2026 Corrections typos: \text partout et \implies partout. MODIF du travail humain (axiome 1), pour lier le support d'exécution et le but , au travail humain et chgt de nom pour la relation "Viser(M,G)", "Sert". -->

### Axiome 1 - Applications d'automatisation
$$\forall A \; \Big(\text{AppAutom}(A) \implies \exists h \; \big(\text{TravailHumain}(h) \land \text{Automatise}(A,h) \big) \Big)$$

#### Le travail humain ($h$)

$$\forall h \; \Big( \text{TravailHumain}(h) \iff \exists G, \mathcal{M}, t_h \; \big( \text{ButHumain}(G) \land \text{DomaineMetier}(\mathcal{M}) \land \text{SupportExecution}(t_h) \land \text{Poursuit}(h, G) \land \text{APourDomaine}(h, \mathcal{M}) \land \text{SAppuieSur}(h, t_h) \land \text{Sert}(\mathcal{M}, G) \big) \Big)$$

### Axiome 2 - Décomposition en parties

$$\forall A \; \Big( \text{AppAutom}(A) \implies \exists \mathcal{M} \; \big( \text{DomaineMetier}(\mathcal{M}) \land \forall p \; ( \text{Partie}(p,A) \implies \exists m, t \; ( m \subseteq \mathcal{M} \land \text{SupportTech}(t) \land \text{Simule}(p,m) \land \text{Associe}(t, p) ) ) \big) \Big)$$

### Axiome 3 - Décomposition atomique des parties auto-similarité structurelle

$$\forall A \; \Big( \text{AppAutom}(A) \implies \forall p \;  \big( \text{Partie}(p,A) \implies \forall d \; ( \text{Contient}(p,d) \implies \text{Atome}(d) \land \exists c \, \exists t \; (\text{ComposantMetier}(c) \land \text{ComposantTech}(t) \land \text{Combine}(d, c, t)) ) \big) \Big)$$

#### Principe de délégation récursive ####

Un composant technique peut déléguer à un atome système, les tâches de complexité lourde. L'atome système est lui-même défini comme un atome de domaine

$$\forall t \; \Big( \text{ComposantTech}(t) \implies \forall s \; \big( \text{DelegueA}(t, s) \implies \exists a, w \; ( \text{Combine}(s, a, w) \land \text{Atome}(s) \land \text{AgentSysteme}(a) \land \text{Worker}(w) ) \big) \Big)$$

