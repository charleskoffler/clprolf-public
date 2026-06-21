# Securing Your Spring Boot Architecture: A Pragmatic Approach with Clprolf

## Introduction

Spring Boot is an incredible framework. It gives developers immense freedom to build and scale applications quickly. However, in large teams or complex environments, this absolute freedom can sometimes lead to architectural drift. Spring cares about making your application work, but it doesn't enforce code symmetry or strict layering.

How can we ensure our architecture remains clean, maintainable, and predictable over time?

In this article, we'll share a real-world example of how we achieved **psychological safety** for our development team by pairing Spring Boot with **Clprolf**, a lightweight semantic framework designed to enforce architectural rules through automated tests.

---

## The Core Concept: Architectural Psychological Safety

When a project grows, the mental load on tech leads and developers increases. You worry about whether a junior developer accidentally bypassed a service layer, or if a database entity is leaking into a controller.

Instead of relying solely on code reviews, we can use a declarative approach. By using semantic annotations, we tell our framework *what* a class is supposed to be, and automated tests verify that the rules are never broken.

---

## A Practical Example: The Weather Observation API

Let’s look at a concrete implementation: a clean, batch-optimized Weather API. We organized our components using Clprolf's core concepts: `@ClAgent` (for functional components), `@ClFamily` (for contracts/interfaces), and `@ClWorker` (for specialized execution units).

### 1. The Pure Contract (Interface)

Our controller interface is completely decoupled from the Web framework. It expresses pure business intent and is tagged as a framework family contract.

```java
package org.clprolf.weatherapp.controllers;

import org.clprolf.framework.ClAgent;
import org.clprolf.framework.ClFamily;
import org.clprolf.weatherapp.entities.Observation;
import org.springframework.http.ResponseEntity;
import java.util.List;

@ClAgent
@ClFamily
public interface ClprolfWeatherController {
    ResponseEntity<List<Observation>> getObsByStation(String station);
    ResponseEntity<Observation> createObservation(Observation observation);
    ResponseEntity<List<Observation>> getAllObs();
}

```

### 2. The Spring Boot Implementation

The implementation handles the HTTP plumbing. Notice how clean it remains, focusing only on routing and database interaction via the repository (`ObservationDao`).

```java
package org.clprolf.weatherapp.controllers.impl;

import jakarta.validation.Valid;
import org.clprolf.framework.ClAgent;
import org.clprolf.weatherapp.controllers.ClprolfWeatherController;
import org.clprolf.weatherapp.entities.Observation;
import org.clprolf.weatherapp.repos.ObservationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@ClAgent
@RestController
@RequestMapping("/weatherapi/observations")
public class ClprolfWeatherControllerImpl implements ClprolfWeatherController {

    @Autowired
    private ObservationDao obsDao;

    @GetMapping("get/station/{station}")
    public ResponseEntity<List<Observation>> getObsByStation(@PathVariable String station) {
        List<Observation> observations = obsDao.findByIdStation(station);
        return observations.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(observations);
    }

    @PostMapping("create")
    @Override
    public ResponseEntity<Observation> createObservation(@Valid @RequestBody Observation observation) {
        Observation savedObservation = obsDao.save(observation);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedObservation);
    }

    @GetMapping("all")
    @Override
    public ResponseEntity<List<Observation>> getAllObs() {
        List<Observation> allObservations = obsDao.findAll();
        return allObservations.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(allObservations);
    }
}

```

> **💡 A Note on the Service Layer & Clprolf**
> 
> For brevity, this CRUD example connects the Controller directly to the Worker (DAO). In a real-world application, you would naturally introduce a Service layer to handle specific business rules. 
> 
> In the Clprolf ecosystem, this Service would simply be another `@ClAgent`. It is the core Domain Agent executing its specific functional job, sitting between the API entrypoint (the Controller) and the infrastructure worker (the DAO).

---

### 3. The Data Access Layer & Composite Identity

To keep our database layer optimized for high-volume weather data, we separated the identity from the entity. Here is our immutable composite ID (`ObservationId`) and its corresponding data access object (`ObservationDao`).

Notice how Clprolf's annotations (`@ClAgent`, `@ClWorker`) live alongside standard JPA annotations, cleanly documenting the role of each class.

```java
package org.clprolf.weatherapp.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.clprolf.framework.ClAgent;
import java.time.OffsetDateTime;
import java.util.Objects;

@ClAgent
@Embeddable
public class ObservationId {

    @Column(length = 8)
    private String station;
    private OffsetDateTime dayUTC; // Leveraging Java 8+ time types for strict UTC handling

    public String getStation() { return station; }
    public OffsetDateTime getDayUTC() { return dayUTC; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ObservationId that = (ObservationId) o;
        return Objects.equals(station, that.station) && Objects.equals(dayUTC, that.dayUTC);
    }

    @Override
    public int hashCode() {
        return Objects.hash(station, dayUTC);
    }
}

```

And the Data Access Object (DAO) interface, tagged as a framework worker component:

```java
package org.clprolf.weatherapp.repos;

import java.util.List;
import org.clprolf.framework.ClFamily;
import org.clprolf.framework.ClWorker;
import org.clprolf.weatherapp.entities.Observation;
import org.clprolf.weatherapp.entities.ObservationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@ClWorker
@ClFamily
@Repository
public interface ObservationDao extends JpaRepository<Observation, ObservationId> {
    List<Observation> findByIdStation(String station);
}

```

---

## Automated Enforcement in the CI/CD Pipeline

The real magic happens behind the scenes. We use ArchUnit rules integrated into our test suite to ensure that no one violates the design patterns.

For instance, we enforce that specific trait interfaces extend only other trait interfaces. If a rule cannot be evaluated yet (e.g., if a project doesn't use traits on day one), we safely allow it to bypass without breaking the CI build using `.allowEmptyShould(true)`.

Here is a glimpse of how strict structural checks are written:

```java
@ArchTest
static final ArchRule trait_interfaces_must_extend_only_trait_interfaces =
        classes()
                .that().areInterfaces()
                .and().areAnnotatedWith(ClTrait.class)
                .should(new ArchCondition<JavaClass>("extend only @ClTrait") {
                    @Override
                    public void check(JavaClass trait, ConditionEvents events) {
                        for (JavaClass parent : trait.getRawInterfaces()) {
                            boolean ok = parent.isAnnotatedWith(ClTrait.class)
                                      || parent.isAnnotatedWith(ClFree.class)
                                      || (!parent.isAnnotatedWith(ClFamily.class) && !parent.isAnnotatedWith(ClTrait.class));
                            
                            events.add(new SimpleConditionEvent(trait, ok, trait.getName() + " extends " + parent.getName()));
                        }
                    }
                })
                .allowEmptyShould(true); // Prevents blocking the CI pipeline prematurely

```

---

## Conclusion

By combining the runtime power of Spring Boot with the architectural guardrails of Clprolf, we created a safe environment for our team. Senior developers can rest assured that the design pattern is respected, and junior developers receive immediate, automated feedback on their code structure before it even reaches a human code review.

It’s not about restricting creativity; it’s about removing the anxiety of architectural erosion.

---
