package org.grooscript.grails.test

import groovy.transform.CompileStatic
import groovy.transform.TypeCheckingMode
import org.codehaus.groovy.grails.test.GrailsTestTargetPattern
import org.codehaus.groovy.grails.test.GrailsTestTypeResult
import org.codehaus.groovy.grails.test.event.GrailsTestEventPublisher
import org.codehaus.groovy.grails.test.event.GrailsTestRunNotifier
import org.codehaus.groovy.grails.test.junit4.listener.SuiteRunListener
import org.codehaus.groovy.grails.test.junit4.result.JUnit4ResultGrailsTestTypeResultAdapter
import org.codehaus.groovy.grails.test.junit4.runner.GrailsTestCaseRunnerBuilder
import org.codehaus.groovy.grails.test.report.junit.JUnitReportsFactory
import org.codehaus.groovy.grails.test.support.GrailsTestMode
import org.codehaus.groovy.grails.test.support.GrailsTestTypeSupport
import org.junit.runner.Result
import org.junit.runners.Suite

import java.lang.reflect.Modifier

/**
 * User: jorgefrancoleza
 * Date: 08/06/14
 */
class PhantomJsTestType extends GrailsTestTypeSupport {

    static final List<String> SUFFIXES = ["PhantomTest", "PhantomTests"].asImmutable()

    protected suite
    protected GrailsTestMode mode

    PhantomJsTestType(String name, String sourceDirectory) {
        this(name, sourceDirectory, null)
    }

    PhantomJsTestType(String name, String sourceDirectory, GrailsTestMode mode) {
        super(name, sourceDirectory)
        this.mode = mode
    }

    protected List<String> getTestSuffixes() { SUFFIXES }

    protected int doPrepare() {
        def testClasses = getTestClasses()
        if (testClasses) {
            suite = createSuite(testClasses)
            suite.testCount()
        }
        else {
            0
        }
    }

    protected getTestClasses() {
        def classes = []
        eachSourceFile { GrailsTestTargetPattern testTargetPattern, File sourceFile ->
            def testClass = sourceFileToClass(sourceFile)
            if (!Modifier.isAbstract(testClass.modifiers)) {
                classes << testClass
            }
        }
        classes
    }

    protected createRunnerBuilder() {
        if (mode) {
            new GrailsTestCaseRunnerBuilder(mode, getApplicationContext(), testTargetPatterns)
        }
        else {
            new GrailsTestCaseRunnerBuilder(testTargetPatterns)
        }
    }

    protected createSuite(classes) {
        new Suite(createRunnerBuilder(), classes as Class[])
    }

    @CompileStatic
    protected createJUnitReportsFactory() {
        JUnitReportsFactory.createFromBuildBinding(buildBinding)
    }

    protected createListener(GrailsTestEventPublisher eventPublisher) {
        new SuiteRunListener(eventPublisher, createJUnitReportsFactory(), createSystemOutAndErrSwapper())
    }

    @CompileStatic(TypeCheckingMode.SKIP)
    protected createNotifier(eventPublisher) {
        int total = 0
        if (suite.hasProperty("children")) {
            total = suite.children.collect {
                it.hasProperty("children") ? it.children.size() : 0
            }.sum()
        }
        def notifier = new GrailsTestRunNotifier(total)
        notifier.addListener(createListener(eventPublisher))
        notifier
    }

    protected GrailsTestTypeResult doRun(GrailsTestEventPublisher eventPublisher) {
        def notifier = createNotifier(eventPublisher)
        def result = new Result()
        notifier.addListener(result.createListener())

        suite.run(notifier)

        notifier.fireTestRunFinished(result)
        new JUnit4ResultGrailsTestTypeResultAdapter(result)
    }
}
