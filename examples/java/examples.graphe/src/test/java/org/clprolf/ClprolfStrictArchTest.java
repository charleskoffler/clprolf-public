package org.clprolf;


import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import org.clprolf.framework.*;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@AnalyzeClasses(
        packages = "org.clprolf.graph",
        importOptions = ImportOption.DoNotIncludeTests.class
)
public class ClprolfStrictArchTest {
    @ArchTest
    static final ArchRule optional_all_classes_should_have_clprolf_role =
            classes()
                    .that().areNotInterfaces()
                    .and().areNotEnums()
                    .should(new ArchCondition<JavaClass>("declare a Clprolf role") {
                        @Override
                        public void check(JavaClass clazz, ConditionEvents events) {

                            if (clazz.isAnonymousClass()
                                    || clazz.isLocalClass()
                            ) {
                                return;
                            }

                            // 1. A class is valid if it declares any of the core roles, a draft, or the new system role
                            boolean hasRole =
                                    clazz.isAnnotatedWith(ClAgent.class)
                                            || clazz.isAnnotatedWith(ClWorker.class)
                                            || clazz.isAnnotatedWith(ClDraft.class)
                                            || clazz.isAnnotatedWith(ClSystem.class); // Added support for ClSystem

                            events.add(new SimpleConditionEvent(
                                    clazz,
                                    hasRole,
                                    clazz.getName()
                                            + " should declare a Clprolf role: @ClAgent, @ClWorker, @ClSystem, or @ClDraft"
                            ));
                        }
                    });

    @ArchTest
    static final ArchRule optional_all_interfaces_should_have_clprolf_role =
            classes()
                    .that().areInterfaces()
                    .should(new ArchCondition<JavaClass>("declare a Clprolf interface role") {
                        @Override
                        public void check(JavaClass interf, ConditionEvents events) {

                            // 1. Check if the interface belongs to any of the standard Clprolf interface types
                            boolean hasRole =
                                    interf.isAnnotatedWith(ClFamily.class)
                                            || interf.isAnnotatedWith(ClTrait.class)
                                            || interf.isAnnotatedWith(ClFree.class);

                            events.add(new SimpleConditionEvent(
                                    interf,
                                    hasRole,
                                    interf.getName()
                                            + " should declare a Clprolf interface role: @ClFamily, @ClTrait, or @ClFree"
                            ));
                        }
                    });

    @ArchTest
    static final ArchRule optional_class_should_not_implement_trait_directly =
            classes()
                    .that().areNotInterfaces()
                    .should(new ArchCondition<JavaClass>("avoid direct implementation of @ClTrait") {
                        @Override
                        public void check(JavaClass clazz, ConditionEvents events) {
                            if (clazz.isAnnotatedWith(ClDraft.class)) {
                                return;
                            }

                            if (!clazz.isAnnotatedWith(ClAgent.class)
                                    && !clazz.isAnnotatedWith(ClWorker.class)
                                    && !clazz.isAnnotatedWith(ClSystem.class)) {
                                return;
                            }

                            for (JavaClass interf : clazz.getRawInterfaces()) {
                                boolean ok = !interf.isAnnotatedWith(ClTrait.class)
                                        || clazz.isAnnotatedWith(ClInterfaceBypass.class);

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
    static final ArchRule optional_class_must_implement_only_one_family_interface =
            classes()
                    .that().areNotInterfaces()
                    .should(new ArchCondition<JavaClass>("implement at most one @ClFamily") {
                        @Override
                        public void check(JavaClass clazz, ConditionEvents events) {
                            if (clazz.isAnnotatedWith(ClDraft.class)) {
                                return;
                            }

                            // CRITICAL: Include ClSystem so system-oriented classes are restricted to a single family
                            if (!clazz.isAnnotatedWith(ClAgent.class)
                                    && !clazz.isAnnotatedWith(ClWorker.class)
                                    && !clazz.isAnnotatedWith(ClSystem.class)) {
                                return;
                            }

                            long count = clazz.getRawInterfaces()
                                    .stream()
                                    .filter(i -> i.isAnnotatedWith(ClFamily.class))
                                    .count();

                            boolean ok = count <= 1 || clazz.isAnnotatedWith(ClInterfaceBypass.class);

                            events.add(new SimpleConditionEvent(
                                    clazz,
                                    ok,
                                    clazz.getName()
                                            + " implements "
                                            + count
                                            + " @ClFamily interfaces, maximum is 1"
                            ));
                        }
                    });
}