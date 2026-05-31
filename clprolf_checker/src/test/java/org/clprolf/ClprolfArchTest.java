package org.clprolf;


import org.clprolf.framework.*;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.*;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@AnalyzeClasses(
        packages = "org.clprolf.examples",
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
                            if (!clazz.isAnnotatedWith(Agent.class) // Java class
                                    && !clazz.isAnnotatedWith(Worker.class)) {
                                return;
                            }
                            if (clazz.isAnnotatedWith(Indef_obj.class)) {
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
                            if (!clazz.isAnnotatedWith(Agent.class) // Java class
                                    && !clazz.isAnnotatedWith(Worker.class)) {
                                return;
                            }
                            if (clazz.isAnnotatedWith(Indef_obj.class)) {
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
                                                parent.isAnnotatedWith(Indef_obj.class);

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
    static final ArchRule class_should_not_implement_trait_directly =
            classes()
                    .that().areNotInterfaces()
                    .should(new ArchCondition<JavaClass>("avoid direct implementation of @Trait_interf") {
                        @Override
                        public void check(JavaClass clazz, ConditionEvents events) {
                            if (!clazz.isAnnotatedWith(Agent.class)
                                    && !clazz.isAnnotatedWith(Worker.class)) {
                                return;
                            } // Java class
                            if (clazz.isAnnotatedWith(Indef_obj.class)) {
                                return;
                            }

                            for (JavaClass interf : clazz.getRawInterfaces()) {
                                boolean ok = !interf.isAnnotatedWith(Trait_interf.class)
                                        || clazz.isAnnotatedWith(Forc_int_inh.class);

                                events.add(new SimpleConditionEvent(
                                        clazz,
                                        ok,
                                        clazz.getName() + " directly implements trait "
                                                + interf.getName()
                                                + "; prefer family_interf -> trait_interf"
                                ));
                            }
                        }
                    });

    @ArchTest
    static final ArchRule class_must_implement_only_one_family_interface =
            classes()
                    .that().areNotInterfaces()
                    .should(new ArchCondition<JavaClass>("implement at most one @Family_interf") {
                        @Override
                        public void check(JavaClass clazz, ConditionEvents events) {
                            if (!clazz.isAnnotatedWith(Agent.class)
                                    && !clazz.isAnnotatedWith(Worker.class)) {
                                return;
                            } // Java class
                            if (clazz.isAnnotatedWith(Indef_obj.class)) {
                                return;
                            }

                            long count = clazz.getRawInterfaces()
                                    .stream()
                                    .filter(i -> i.isAnnotatedWith(Family_interf.class))
                                    .count();

                            boolean ok = count <= 1 || clazz.isAnnotatedWith(Forc_int_inh.class);

                            events.add(new SimpleConditionEvent(
                                    clazz,
                                    ok,
                                    clazz.getName()
                                            + " implements "
                                            + count
                                            + " @Family_interf interfaces, maximum is 1"
                            ));
                        }
                    });

    @ArchTest
    static final ArchRule family_interface_role_must_match_implementation =
            classes()
                    .that().areNotInterfaces()
                    .should(new ArchCondition<JavaClass>("implement only matching @Family_interf role") {
                        @Override
                        public void check(JavaClass clazz, ConditionEvents events) {
                            if (!clazz.isAnnotatedWith(Agent.class) // Java class
                                    && !clazz.isAnnotatedWith(Worker.class)) {
                                return;
                            }
                            if (clazz.isAnnotatedWith(Indef_obj.class)) {
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
                            if (!trait.isAnnotatedWith(Family_interf.class)
                                    && !trait.isAnnotatedWith(Trait_interf.class)) {
                                return;
                            } // Java interface
                            if (trait.isAnnotatedWith(Compat_interf.class)) {
                                return;
                            }

                            for (JavaClass parent : trait.getRawInterfaces()) {

                                boolean ok =
                                        parent.isAnnotatedWith(Trait_interf.class)
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
                    .should(new ArchCondition<JavaClass>("have a target role @Agent or @Worker") {
                        @Override
                        public void check(JavaClass interf, ConditionEvents events) {
                            if (!interf.isAnnotatedWith(Family_interf.class)
                                    && !interf.isAnnotatedWith(Trait_interf.class)) {
                                return;
                            } // Java interface
                            if (interf.isAnnotatedWith(Indef_obj.class)) {
                                return;
                            }
                            boolean hasAgent = interf.isAnnotatedWith(Agent.class);
                            boolean hasWorker = interf.isAnnotatedWith(Worker.class);

                            boolean ok = hasAgent ^ hasWorker; // exactement un des deux

                            events.add(new SimpleConditionEvent(
                                    interf,
                                    ok,
                                    interf.getName()
                                            + " must have exactly one target role: @Agent or @Worker"
                            ));
                        }
                    });
}