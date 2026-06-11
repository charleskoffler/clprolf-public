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
        packages = "org.clprolf.weatherapp",
        importOptions = ImportOption.DoNotIncludeTests.class
)
public class ClprolfArchTest {

    @ArchTest
    static final ArchRule clprolf_classes_must_not_mix_agent_and_worker =
            classes()
                    .that().areNotInterfaces()
                    .should(new ArchCondition<JavaClass>("not mix @ClAgent and @ClWorker") {
                        @Override
                        public void check(JavaClass clazz, ConditionEvents events) {
                            if (clazz.isAnnotatedWith(ClDraft.class)) {
                                return;
                            }
                            if (!clazz.isAnnotatedWith(ClAgent.class) // Java class
                                    && !clazz.isAnnotatedWith(ClWorker.class)) {
                                return;
                            }

                            boolean hasAgent = clazz.isAnnotatedWith(ClAgent.class);
                            boolean hasWorker = clazz.isAnnotatedWith(ClWorker.class);

                            boolean ok = !(hasAgent && hasWorker);

                            events.add(new SimpleConditionEvent(
                                    clazz,
                                    ok,
                                    clazz.getName()
                                            + " cannot be both @ClAgent and @ClWorker"
                            ));
                        }
                    });

    @ArchTest
    static final ArchRule agent_worker_inheritance_must_not_mix =
            classes()
                    .that().areAnnotatedWith(ClAgent.class)
                    .or().areAnnotatedWith(ClWorker.class)
                    .should(new ArchCondition<JavaClass>("inherit only from same Clprolf role unless @ClBypass") {

                        public void check(JavaClass clazz, ConditionEvents events) {
                            if (clazz.isAnnotatedWith(ClDraft.class)) {
                                return;
                            }
                            if (!clazz.isAnnotatedWith(ClAgent.class) // Java class
                                    && !clazz.isAnnotatedWith(ClWorker.class)) {
                                return;
                            }

                            if (clazz.isAnnotatedWith(ClBypass.class)) return;

                            clazz.getRawSuperclass().ifPresent(parent -> {
                                if (parent.getName().equals("java.lang.Object")) return;

                                boolean childAgent = clazz.isAnnotatedWith(ClAgent.class);
                                boolean childWorker = clazz.isAnnotatedWith(ClWorker.class);
                                boolean parentAgent = parent.isAnnotatedWith(ClAgent.class);
                                boolean parentWorker = parent.isAnnotatedWith(ClWorker.class);

                                boolean ok =
                                        (childAgent && parentAgent) ||
                                                (childWorker && parentWorker) ||
                                                parent.isAnnotatedWith(ClDraft.class) ||
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
                    .should(new ArchCondition<JavaClass>("implement only matching @ClFamily role") {
                        @Override
                        public void check(JavaClass clazz, ConditionEvents events) {
                            if (clazz.isAnnotatedWith(ClDraft.class)) {
                                return;
                            }

                            if (!clazz.isAnnotatedWith(ClAgent.class) // Java class
                                    && !clazz.isAnnotatedWith(ClWorker.class)) {
                                return;
                            }

                            for (JavaClass interf : clazz.getRawInterfaces()) {
                                if (!interf.isAnnotatedWith(ClFamily.class)) continue;

                                boolean ok =
                                        clazz.isAnnotatedWith(ClBypass.class) ||
                                                (clazz.isAnnotatedWith(ClAgent.class) && interf.isAnnotatedWith(ClAgent.class)) ||
                                                (clazz.isAnnotatedWith(ClWorker.class) && interf.isAnnotatedWith(ClWorker.class));

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
                            "have a valid target role @ClAgent and/or @ClWorker"
                    ) {
                        @Override
                        public void check(JavaClass interf, ConditionEvents events) {

                            boolean isFamily = interf.isAnnotatedWith(ClFamily.class);
                            boolean isTrait = interf.isAnnotatedWith(ClTrait.class);

                            if (!isFamily && !isTrait) { //Java interface or ClFree
                                return;
                            }

                            boolean hasAgent = interf.isAnnotatedWith(ClAgent.class);
                            boolean hasWorker = interf.isAnnotatedWith(ClWorker.class);

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
                                            + "@ClFamily requires exactly one of @ClAgent or @ClWorker; "
                                            + "@ClTrait requires at least one of @ClAgent or @ClWorker"
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

                            for (JavaClass parent : interf.getRawInterfaces()) {

                                if (!parent.isAnnotatedWith(ClTrait.class)) {
                                    continue;
                                }

                                boolean parentIsAgent = parent.isAnnotatedWith(ClAgent.class);
                                boolean parentIsWorker = parent.isAnnotatedWith(ClWorker.class);

                                boolean compatible =
                                        interf.isAnnotatedWith(ClBypass.class)
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
                    .and().areAnnotatedWith(ClFamily.class)
                    .should(new ArchCondition<JavaClass>(
                            "inherit only family interfaces with compatible target roles"
                    ) {
                        @Override
                        public void check(JavaClass interf, ConditionEvents events) {

                            boolean childIsAgent = interf.isAnnotatedWith(ClAgent.class);
                            boolean childIsWorker = interf.isAnnotatedWith(ClWorker.class);

                            for (JavaClass parent : interf.getRawInterfaces()) {

                                if (!parent.isAnnotatedWith(ClFamily.class)) {
                                    continue;
                                }

                                boolean parentIsAgent = parent.isAnnotatedWith(ClAgent.class);
                                boolean parentIsWorker = parent.isAnnotatedWith(ClWorker.class);

                                boolean compatible =
                                        interf.isAnnotatedWith(ClBypass.class)
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
                    .should(new ArchCondition<JavaClass>("implement only matching @ClTrait role") {
                        @Override
                        public void check(JavaClass clazz, ConditionEvents events) {
                            if (clazz.isAnnotatedWith(ClDraft.class)) {
                                return;
                            }
                            if (!clazz.isAnnotatedWith(ClAgent.class) // Java class
                                    && !clazz.isAnnotatedWith(ClWorker.class)) {
                                return;
                            }

                            for (JavaClass interf : clazz.getRawInterfaces()) {
                                if (!interf.isAnnotatedWith(ClTrait.class)) {
                                    continue;
                                }

                                boolean ok =
                                        clazz.isAnnotatedWith(ClBypass.class)
                                                || (clazz.isAnnotatedWith(ClAgent.class)
                                                && interf.isAnnotatedWith(ClAgent.class))
                                                || (clazz.isAnnotatedWith(ClWorker.class)
                                                && interf.isAnnotatedWith(ClWorker.class));

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