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
        packages = "org.clprolf.examples.games.snake_game",
        importOptions = ImportOption.DoNotIncludeTests.class
)
public class ClprolfArchTest {

    @ArchTest
    static final ArchRule clprolf_classes_must_not_mix_agent_and_worker =
            classes()
                    .that().areNotInterfaces()
                    .should(new ArchCondition<JavaClass>("not mix @ClAgent, @ClSystem and @ClWorker") {
                        @Override
                        public void check(JavaClass clazz, ConditionEvents events) {
                            if (clazz.isAnnotatedWith(ClDraft.class)) {
                                return;
                            }

                            boolean hasAgent = clazz.isAnnotatedWith(ClAgent.class);
                            boolean hasWorker = clazz.isAnnotatedWith(ClWorker.class);
                            boolean hasSystem = clazz.isAnnotatedWith(ClSystem.class);

                            if (!hasAgent && !hasWorker && !hasSystem) {
                                return;
                            }

                            // A class should have only one class role, and not more
                            int rolesCount = (hasAgent ? 1 : 0) + (hasWorker ? 1 : 0) + (hasSystem ? 1 : 0);
                            boolean ok = (rolesCount <= 1);

                            events.add(new SimpleConditionEvent(
                                    clazz,
                                    ok,
                                    clazz.getName()
                                            + " cannot mix @ClAgent, @ClSystem, and @ClWorker roles"
                            ));
                        }
                    });

    @ArchTest
    static final ArchRule agent_worker_inheritance_must_not_mix =
            classes()
                    .that().areAnnotatedWith(ClAgent.class)
                    .or().areAnnotatedWith(ClWorker.class)
                    .or().areAnnotatedWith(ClSystem.class) // 1. Intercept ClSystem at the entry point
                    .should(new ArchCondition<JavaClass>("inherit only from same Clprolf role unless @ClBypass") {

                        @Override
                        public void check(JavaClass clazz, ConditionEvents events) {
                            if (clazz.isAnnotatedWith(ClDraft.class)) {
                                return;
                            }

                            boolean childAgent = clazz.isAnnotatedWith(ClAgent.class);
                            boolean childWorker = clazz.isAnnotatedWith(ClWorker.class);
                            boolean childSystem = clazz.isAnnotatedWith(ClSystem.class);

                            // 2. Guard clause if the class does not have any of the three roles
                            if (!childAgent && !childWorker && !childSystem) {
                                return;
                            }

                            if (clazz.isAnnotatedWith(ClBypass.class)) return;

                            clazz.getRawSuperclass().ifPresent(parent -> {
                                if (parent.getName().equals("java.lang.Object")) return;

                                boolean parentAgent = parent.isAnnotatedWith(ClAgent.class);
                                boolean parentWorker = parent.isAnnotatedWith(ClWorker.class);
                                boolean parentSystem = parent.isAnnotatedWith(ClSystem.class);

                                // 3. Strict inheritance rule: roles must match perfectly
                                boolean ok =
                                        (childAgent && parentAgent) ||
                                                (childWorker && parentWorker) ||
                                                (childSystem && parentSystem) || // A System can only extend a System
                                                parent.isAnnotatedWith(ClDraft.class) ||
                                                (!parentAgent && !parentWorker && !parentSystem); // Untagged parent (e.g., external lib, JDK)

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
                    .should(new ArchCondition<JavaClass>("implement only matching @ClFamily role") {
                        @Override
                        public void check(JavaClass clazz, ConditionEvents events) {
                            if (clazz.isAnnotatedWith(ClDraft.class)) {
                                return;
                            }

                            boolean childAgent = clazz.isAnnotatedWith(ClAgent.class);
                            boolean childWorker = clazz.isAnnotatedWith(ClWorker.class);
                            boolean childSystem = clazz.isAnnotatedWith(ClSystem.class);

                            // 1. Guard clause if the class does not have any of the three roles
                            if (!childAgent && !childWorker && !childSystem) {
                                return;
                            }

                            for (JavaClass interf : clazz.getRawInterfaces()) {
                                if (!interf.isAnnotatedWith(ClFamily.class)) continue;

                                // 2. Strict matching rule: class role and interface role must be identical
                                boolean ok =
                                        clazz.isAnnotatedWith(ClBypass.class) ||
                                                (childAgent && interf.isAnnotatedWith(ClAgent.class)) ||
                                                (childWorker && interf.isAnnotatedWith(ClWorker.class)) ||
                                                (childSystem && interf.isAnnotatedWith(ClSystem.class)); // System class with System family

                                events.add(new SimpleConditionEvent(
                                        clazz,
                                        ok,
                                        clazz.getName() + " implements " + interf.getName()
                                                + " but class role and @ClFamily target role do not match"
                                ));
                            }
                        }
                    });

    @ArchTest
    static final ArchRule trait_interfaces_must_extend_only_trait_interfaces =
            classes()
                    .that().areInterfaces()
                    .and().areAnnotatedWith(ClTrait.class)
                    .should(new ArchCondition<JavaClass>("extend only @ClTrait") {

                        @Override
                        public void check(JavaClass trait, ConditionEvents events) {
                            for (JavaClass parent : trait.getRawInterfaces()) {

                                boolean ok =
                                        parent.isAnnotatedWith(ClTrait.class)
                                                || (parent.isAnnotatedWith(ClFree.class))
                                                || (!parent.isAnnotatedWith(ClFamily.class) && !parent.isAnnotatedWith(ClTrait.class)) // Java interface
                                                || trait.isAnnotatedWith(ClInterfaceBypass.class);

                                events.add(new SimpleConditionEvent(
                                        trait,
                                        ok,
                                        trait.getName()
                                                + " extends "
                                                + parent.getName()
                                                + " but a @ClTrait may extend only another @ClTrait"
                                ));
                            }
                        }
                    })
                    .allowEmptyShould(true);

    @ArchTest
    static final ArchRule clprolf_interfaces_must_have_target_role =
            classes()
                    .that().areInterfaces()
                    .and().areAnnotatedWith(ClFamily.class)
                    .or().areAnnotatedWith(ClTrait.class)
                    .should(new ArchCondition<JavaClass>(
                            "have a valid target role (@ClAgent, @ClWorker and/or @ClSystem)"
                    ) {
                        @Override
                        public void check(JavaClass interf, ConditionEvents events) {

                            boolean isFamily = interf.isAnnotatedWith(ClFamily.class);
                            boolean isTrait = interf.isAnnotatedWith(ClTrait.class);

                            if (!isFamily && !isTrait) { // Java interface or ClFree
                                return;
                            }

                            boolean hasAgent = interf.isAnnotatedWith(ClAgent.class);
                            boolean hasWorker = interf.isAnnotatedWith(ClWorker.class);
                            boolean hasSystem = interf.isAnnotatedWith(ClSystem.class);

                            boolean ok;

                            if (isFamily) {
                                // 1. A family must target exactly one of the three roles
                                int rolesCount = (hasAgent ? 1 : 0) + (hasWorker ? 1 : 0) + (hasSystem ? 1 : 0);
                                ok = (rolesCount == 1);
                            } else {
                                // 2. A trait must target at least one of the three roles
                                ok = hasAgent || hasWorker || hasSystem;
                            }

                            events.add(new SimpleConditionEvent(
                                    interf,
                                    ok,
                                    interf.getName()
                                            + " must have a valid target role: "
                                            + "@ClFamily requires exactly one of @ClAgent, @ClWorker or @ClSystem; "
                                            + "@ClTrait requires at least one of @ClAgent, @ClWorker or @ClSystem"
                            ));
                        }
                    });

    @ArchTest
    static final ArchRule inheriting_interface_role_must_match_trait_interface_target_role =
            classes()
                    .that().areInterfaces()
                    .and().areAnnotatedWith(ClFamily.class)
                    .or().areAnnotatedWith(ClTrait.class)
                    .should(new ArchCondition<JavaClass>(
                            "inherit only trait interfaces with compatible target roles"
                    ) {
                        @Override
                        public void check(JavaClass interf, ConditionEvents events) {

                            boolean inheritorIsAgent = interf.isAnnotatedWith(ClAgent.class);
                            boolean inheritorIsWorker = interf.isAnnotatedWith(ClWorker.class);
                            boolean inheritorIsSystem = interf.isAnnotatedWith(ClSystem.class);

                            for (JavaClass parent : interf.getRawInterfaces()) {

                                if (!parent.isAnnotatedWith(ClTrait.class)) {
                                    continue;
                                }

                                boolean parentIsAgent = parent.isAnnotatedWith(ClAgent.class);
                                boolean parentIsWorker = parent.isAnnotatedWith(ClWorker.class);
                                boolean parentIsSystem = parent.isAnnotatedWith(ClSystem.class);

                                // 1. Compatibility check: roles must align between the inheritor and the parent trait
                                boolean compatible =
                                        interf.isAnnotatedWith(ClBypass.class)
                                                || (inheritorIsAgent && parentIsAgent)
                                                || (inheritorIsWorker && parentIsWorker)
                                                || (inheritorIsSystem && parentIsSystem); // System components can inherit System traits

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
                    .and().areAnnotatedWith(ClFamily.class)
                    .should(new ArchCondition<JavaClass>(
                            "inherit only family interfaces with compatible target roles"
                    ) {
                        @Override
                        public void check(JavaClass interf, ConditionEvents events) {

                            boolean childIsAgent = interf.isAnnotatedWith(ClAgent.class);
                            boolean childIsWorker = interf.isAnnotatedWith(ClWorker.class);
                            boolean childIsSystem = interf.isAnnotatedWith(ClSystem.class);

                            for (JavaClass parent : interf.getRawInterfaces()) {

                                if (!parent.isAnnotatedWith(ClFamily.class)) {
                                    continue;
                                }

                                boolean parentIsAgent = parent.isAnnotatedWith(ClAgent.class);
                                boolean parentIsWorker = parent.isAnnotatedWith(ClWorker.class);
                                boolean parentIsSystem = parent.isAnnotatedWith(ClSystem.class);

                                // 1. Compatibility check: family inheritance must strictly preserve the target role
                                boolean compatible =
                                        interf.isAnnotatedWith(ClBypass.class)
                                                || (childIsAgent && parentIsAgent)
                                                || (childIsWorker && parentIsWorker)
                                                || (childIsSystem && parentIsSystem); // System families can only extend System families

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
                    .should(new ArchCondition<JavaClass>("implement only matching @ClTrait role") {
                        @Override
                        public void check(JavaClass clazz, ConditionEvents events) {
                            if (clazz.isAnnotatedWith(ClDraft.class)) {
                                return;
                            }

                            boolean childAgent = clazz.isAnnotatedWith(ClAgent.class);
                            boolean childWorker = clazz.isAnnotatedWith(ClWorker.class);
                            boolean childSystem = clazz.isAnnotatedWith(ClSystem.class);

                            // 1. Guard clause if the class does not have any of the three roles
                            if (!childAgent && !childWorker && !childSystem) {
                                return;
                            }

                            for (JavaClass interf : clazz.getRawInterfaces()) {
                                if (!interf.isAnnotatedWith(ClTrait.class)) {
                                    continue;
                                }

                                // 2. Compatibility check for direct trait implementation in non-strict mode
                                boolean ok =
                                        clazz.isAnnotatedWith(ClBypass.class)
                                                || (childAgent && interf.isAnnotatedWith(ClAgent.class))
                                                || (childWorker && interf.isAnnotatedWith(ClWorker.class))
                                                || (childSystem && interf.isAnnotatedWith(ClSystem.class)); // System class with System trait

                                events.add(new SimpleConditionEvent(
                                        clazz,
                                        ok,
                                        clazz.getName()
                                                + " implements "
                                                + interf.getName()
                                                + " but class role and @ClTrait target role do not match"
                                ));
                            }
                        }
                    });
}