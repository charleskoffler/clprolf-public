package org.clprolf;


import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.*;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

import org.clprolf.framework.*;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@AnalyzeClasses(
        packages = "org.clprolf.examples.animal_app",
        importOptions = ImportOption.DoNotIncludeTests.class
)
public class ClprolfArchTest {

    @ArchTest
    static final ArchRule clprolf_classes_must_not_mix_agent_and_worker =
            classes()
                    .that().areNotInterfaces()
                    .should(new ArchCondition<JavaClass>("not mix @Agent and @Worker") {
                        @Override
                        public void check(JavaClass clazz, ConditionEvents events) {
                            if (clazz.isAnnotatedWith(Indef_obj.class)) {
                                return;
                            }
                            if (!clazz.isAnnotatedWith(Agent.class) // Java class
                                    && !clazz.isAnnotatedWith(Worker.class)) {
                                return;
                            }

                            boolean hasAgent = clazz.isAnnotatedWith(Agent.class);
                            boolean hasWorker = clazz.isAnnotatedWith(Worker.class);

                            boolean ok = !(hasAgent && hasWorker);

                            events.add(new SimpleConditionEvent(
                                    clazz,
                                    ok,
                                    clazz.getName()
                                            + " cannot be both @Agent and @Worker"
                            ));
                        }
                    });

    @ArchTest
    static final ArchRule agent_worker_inheritance_must_not_mix =
            classes()
                    .that().areAnnotatedWith(Agent.class)
                    .or().areAnnotatedWith(Worker.class)
                    .should(new ArchCondition<JavaClass>("inherit only from same Clprolf role unless @Forc_inh") {

                        public void check(JavaClass clazz, ConditionEvents events) {
                            if (clazz.isAnnotatedWith(Indef_obj.class)) {
                                return;
                            }
                            if (!clazz.isAnnotatedWith(Agent.class) // Java class
                                    && !clazz.isAnnotatedWith(Worker.class)) {
                                return;
                            }

                            if (clazz.isAnnotatedWith(Forc_inh.class)) return;

                            clazz.getRawSuperclass().ifPresent(parent -> {
                                if (parent.getName().equals("java.lang.Object")) return;

                                boolean childAgent = clazz.isAnnotatedWith(Agent.class);
                                boolean childWorker = clazz.isAnnotatedWith(Worker.class);
                                boolean parentAgent = parent.isAnnotatedWith(Agent.class);
                                boolean parentWorker = parent.isAnnotatedWith(Worker.class);

                                boolean ok =
                                        (childAgent && parentAgent) ||
                                                (childWorker && parentWorker) ||
                                                parent.isAnnotatedWith(Indef_obj.class) ||
                                                // Java class
                                                (!parentAgent && !parentWorker);

                                events.add(new SimpleConditionEvent(
                                        clazz,
                                        ok,
                                        clazz.getName() + " cannot extend " + parent.getName()
                                                + " because Clprolf inheritance must preserve the role"
                                ));
                            });
                        }
                    });

    @ArchTest
    static final ArchRule family_interface_role_must_match_implementation =
            classes()
                    .that().areNotInterfaces()
                    .should(new ArchCondition<JavaClass>("implement only matching @Family_interf role") {
                        @Override
                        public void check(JavaClass clazz, ConditionEvents events) {
                            if (clazz.isAnnotatedWith(Indef_obj.class)) {
                                return;
                            }

                            if (!clazz.isAnnotatedWith(Agent.class) // Java class
                                    && !clazz.isAnnotatedWith(Worker.class)) {
                                return;
                            }

                            for (JavaClass interf : clazz.getRawInterfaces()) {
                                if (!interf.isAnnotatedWith(Family_interf.class)) continue;

                                boolean ok =
                                        clazz.isAnnotatedWith(Forc_inh.class) ||
                                                (clazz.isAnnotatedWith(Agent.class) && interf.isAnnotatedWith(Agent.class)) ||
                                                (clazz.isAnnotatedWith(Worker.class) && interf.isAnnotatedWith(Worker.class));

                                events.add(new SimpleConditionEvent(
                                        clazz,
                                        ok,
                                        clazz.getName() + " implements " + interf.getName()
                                                + " but class role and @Family_interf target role do not match"
                                ));
                            }
                        }
                    });

    @ArchTest
    static final ArchRule trait_interfaces_must_extend_only_trait_interfaces =
            classes()
                    .that().areInterfaces()
                    .and().areAnnotatedWith(Trait_interf.class)
                    .should(new ArchCondition<JavaClass>("extend only @Trait_interf") {

                        @Override
                        public void check(JavaClass trait, ConditionEvents events) {
                            for (JavaClass parent : trait.getRawInterfaces()) {

                                boolean ok =
                                        parent.isAnnotatedWith(Trait_interf.class)
                                                || (parent.isAnnotatedWith(Compat_interf.class))
                                                || (!parent.isAnnotatedWith(Family_interf.class) && !parent.isAnnotatedWith(Trait_interf.class)) // Java interface
                                                || trait.isAnnotatedWith(Forc_int_inh.class);

                                events.add(new SimpleConditionEvent(
                                        trait,
                                        ok,
                                        trait.getName()
                                                + " extends "
                                                + parent.getName()
                                                + " but a @Trait_interf may extend only another @Trait_interf"
                                ));
                            }
                        }
                    });

    @ArchTest
    static final ArchRule clprolf_interfaces_must_have_target_role =
            classes()
                    .that().areInterfaces()
                    .and().areAnnotatedWith(Family_interf.class)
                    .or().areAnnotatedWith(Trait_interf.class)
                    .should(new ArchCondition<JavaClass>(
                            "have a valid target role @Agent and/or @Worker"
                    ) {
                        @Override
                        public void check(JavaClass interf, ConditionEvents events) {

                            boolean isFamily = interf.isAnnotatedWith(Family_interf.class);
                            boolean isTrait = interf.isAnnotatedWith(Trait_interf.class);

                            if (!isFamily && !isTrait) { //Java interface or Compat_interf
                                return;
                            }

                            boolean hasAgent = interf.isAnnotatedWith(Agent.class);
                            boolean hasWorker = interf.isAnnotatedWith(Worker.class);

                            boolean ok;

                            if (isFamily) {
                                ok = hasAgent ^ hasWorker;
                            } else {
                                ok = hasAgent || hasWorker;
                            }

                            events.add(new SimpleConditionEvent(
                                    interf,
                                    ok,
                                    interf.getName()
                                            + " must have a valid target role: "
                                            + "@Family_interf requires exactly one of @Agent or @Worker; "
                                            + "@Trait_interf requires at least one of @Agent or @Worker"
                            ));
                        }
                    });

    @ArchTest
    static final ArchRule inheriting_interface_role_must_match_trait_interface_target_role =
            classes()
                    .that().areInterfaces()
                    .and().areAnnotatedWith(Family_interf.class)
                    .or().areAnnotatedWith(Trait_interf.class)
                    .should(new ArchCondition<JavaClass>(
                            "inherit only trait interfaces with compatible target roles"
                    ) {
                        @Override
                        public void check(JavaClass interf, ConditionEvents events) {

                            boolean inheritorIsAgent = interf.isAnnotatedWith(Agent.class);
                            boolean inheritorIsWorker = interf.isAnnotatedWith(Worker.class);

                            for (JavaClass parent : interf.getRawInterfaces()) {

                                if (!parent.isAnnotatedWith(Trait_interf.class)) {
                                    continue;
                                }

                                boolean parentIsAgent = parent.isAnnotatedWith(Agent.class);
                                boolean parentIsWorker = parent.isAnnotatedWith(Worker.class);

                                boolean compatible =
                                        interf.isAnnotatedWith(Forc_inh.class)
                                                || (inheritorIsAgent && parentIsAgent)
                                                || (inheritorIsWorker && parentIsWorker);

                                events.add(new SimpleConditionEvent(
                                        interf,
                                        compatible,
                                        interf.getName()
                                                + " cannot inherit trait interface "
                                                + parent.getName()
                                                + " because their target roles are incompatible"
                                ));
                            }
                        }
                    });

    @ArchTest
    static final ArchRule family_interface_target_role_must_match_inherited_family_interface =
            classes()
                    .that().areInterfaces()
                    .and().areAnnotatedWith(Family_interf.class)
                    .should(new ArchCondition<JavaClass>(
                            "inherit only family interfaces with compatible target roles"
                    ) {
                        @Override
                        public void check(JavaClass interf, ConditionEvents events) {

                            boolean childIsAgent = interf.isAnnotatedWith(Agent.class);
                            boolean childIsWorker = interf.isAnnotatedWith(Worker.class);

                            for (JavaClass parent : interf.getRawInterfaces()) {

                                if (!parent.isAnnotatedWith(Family_interf.class)) {
                                    continue;
                                }

                                boolean parentIsAgent = parent.isAnnotatedWith(Agent.class);
                                boolean parentIsWorker = parent.isAnnotatedWith(Worker.class);

                                boolean compatible =
                                        interf.isAnnotatedWith(Forc_inh.class)
                                                || (childIsAgent && parentIsAgent)
                                                || (childIsWorker && parentIsWorker);

                                events.add(new SimpleConditionEvent(
                                        interf,
                                        compatible,
                                        interf.getName()
                                                + " cannot inherit family interface "
                                                + parent.getName()
                                                + " because their target roles are incompatible"
                                ));
                            }
                        }
                    });

    /**
     * Only for non-strict mode.
     */
    @ArchTest
    static final ArchRule trait_interface_role_must_match_direct_implementation =
            classes()
                    .that().areNotInterfaces()
                    .should(new ArchCondition<JavaClass>("implement only matching @Trait_interf role") {
                        @Override
                        public void check(JavaClass clazz, ConditionEvents events) {
                            if (clazz.isAnnotatedWith(Indef_obj.class)) {
                                return;
                            }
                            if (!clazz.isAnnotatedWith(Agent.class) // Java class
                                    && !clazz.isAnnotatedWith(Worker.class)) {
                                return;
                            }

                            for (JavaClass interf : clazz.getRawInterfaces()) {
                                if (!interf.isAnnotatedWith(Trait_interf.class)) {
                                    continue;
                                }

                                boolean ok =
                                        clazz.isAnnotatedWith(Forc_inh.class)
                                                || (clazz.isAnnotatedWith(Agent.class)
                                                && interf.isAnnotatedWith(Agent.class))
                                                || (clazz.isAnnotatedWith(Worker.class)
                                                && interf.isAnnotatedWith(Worker.class));

                                events.add(new SimpleConditionEvent(
                                        clazz,
                                        ok,
                                        clazz.getName()
                                                + " implements "
                                                + interf.getName()
                                                + " but class role and @Trait_interf target role do not match"
                                ));
                            }
                        }
                    });
}