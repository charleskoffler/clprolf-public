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
        packages = "org.clprolf.weatherapp",
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
                            boolean hasRole =
                                    clazz.isAnnotatedWith(ClAgent.class)
                                            || clazz.isAnnotatedWith(ClWorker.class)
                                            || clazz.isAnnotatedWith(ClDraft.class);

                            events.add(new SimpleConditionEvent(
                                    clazz,
                                    hasRole,
                                    clazz.getName()
                                            + " should declare a Clprolf role: @ClAgent, @ClWorker, or @ClDraft"
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
                                    && !clazz.isAnnotatedWith(ClWorker.class)) {
                                return;
                            } // Java class

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
                            if (!clazz.isAnnotatedWith(ClAgent.class)
                                    && !clazz.isAnnotatedWith(ClWorker.class)) {
                                return;
                            } // Java class

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