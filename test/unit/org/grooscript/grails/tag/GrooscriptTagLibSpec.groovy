package org.grooscript.grails.tag

import asset.pipeline.AssetsTagLib
import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import org.grooscript.grails.bean.GrooscriptConverter
import spock.lang.Specification

/**
 * @author Jorge Franco
 * Date: 08/06/14
 */
@TestMixin(GrailsUnitTestMixin)
@TestFor(GrooscriptTagLib)
class GrooscriptTagLibSpec extends Specification {

    GrooscriptConverter grooscriptConverter
    AssetsTagLib assetsTagLib

    void setup() {
        grooscriptConverter = Mock(GrooscriptConverter)
        assetsTagLib = Mock(AssetsTagLib)
        GrooscriptTagLib.metaClass.grooscriptConverter = grooscriptConverter
        GrooscriptTagLib.metaClass.asset = assetsTagLib
    }

    void cleanup() {
    }

    static final GROOVY_CODE = 'code example'
    static final JS_CODE = 'js converted code'

    void 'test code taglib'() {
        when:
        applyTemplate("<grooscript:code>${GROOVY_CODE}</grooscript:code>")

        then:
        1 * grooscriptConverter.toJavascript(GROOVY_CODE, null) >> JS_CODE
        1 * assetsTagLib.script(['type':'text/javascript'], { it() == JS_CODE})
        0 * _
    }

    void 'test code taglib with conversion options'() {
        when:
        applyTemplate("<grooscript:code options='[recursive: true]'>${GROOVY_CODE}</grooscript:code>")

        then:
        1 * grooscriptConverter.toJavascript(GROOVY_CODE, [recursive: true]) >> JS_CODE
        1 * assetsTagLib.script(['type':'text/javascript'], { it() == JS_CODE})
        0 * _
    }

    static final FILE_PATH = 'GrailsGrooscriptGrailsPlugin.groovy'

    void 'test code taglib with a file'() {
        when:
        applyTemplate("<grooscript:code filePath='${FILE_PATH}'/>")

        then:
        1 * grooscriptConverter.toJavascript(new File(FILE_PATH).text, null) >> JS_CODE
        1 * assetsTagLib.script(['type':'text/javascript'], { it() == JS_CODE})
        0 * _
    }

    void 'test code taglib with a file and body'() {
        when:
        applyTemplate("<grooscript:code filePath='${FILE_PATH}'>${GROOVY_CODE}</grooscript:code>")

        then:
        1 * grooscriptConverter.toJavascript(new File(FILE_PATH).text+'\n'+GROOVY_CODE, null) >> JS_CODE
        1 * assetsTagLib.script(['type':'text/javascript'], { it() == JS_CODE})
        0 * _
    }

    /*
    void 'test init vertx variable'() {
        given:
        initVertx()

        when:
        applyTemplate("<grooscript:initVertx/>")

        then:
        1 * resourceTaglib.require([module: 'vertx'])
        1 * resourceTaglib.script(_)
    }

    void 'test reload page'() {
        given:
        initVertx()

        when:
        applyTemplate("<grooscript:reloadPage/>")

        then:
        1 * resourceTaglib.require([module: 'vertx'])
        2 * resourceTaglib.script(_)
        0 * _
    }

    void 'test template'() {
        when:
        def result = applyTemplate("<grooscript:template>assert true</grooscript:template>")

        then:
        2 * resourceTaglib.script(_)
        1 * resourceTaglib.require([module: 'grooscript'])
        1 * resourceTaglib.require([module: 'grooscriptGrails'])
        1 * grooscriptConverter.toJavascript('def gsTextHtml = { data -> Builder.process { -> assert true}}') >> ''
        0 * _
        result.startsWith "\n<div id='fTemplate"
    }

    void 'very basic test template options'() {
        when:
        def result = applyTemplate("<grooscript:template functionName='jarJar'" +
                " itemSelector='#anyId' renderOnReady=\"${true}\">assert true</grooscript:template>")

        then:
        2 * resourceTaglib.script(_)
        1 * grooscriptConverter.toJavascript(_) >> ''
        2 * resourceTaglib.require(_)
        0 * _
        !result
    }

    static final FILE_PATH_TEMPLATE = 'src/groovy/MyTemplate.groovy'

    void 'test template with a file'() {
        when:
        def result = applyTemplate("<grooscript:template filePath='${FILE_PATH_TEMPLATE}'/>")

        then:
        2 * resourceTaglib.script(_)
        1 * grooscriptConverter.toJavascript("def gsTextHtml = { data -> Builder.process { -> ${new File(FILE_PATH_TEMPLATE).text}}}") >> ''
        2 * resourceTaglib.require(_)
        0 * _
        result.startsWith "\n<div id='fTemplate"
    }

    void 'test template with a reload event'() {
        when:
        applyTemplate('<grooscript:template listenEvents="$events">h3 \'Hello!\'</grooscript:template>',
                [events: ['redraw']])

        then:
        3 * resourceTaglib.script(_)
        2 * resourceTaglib.require(_)
        1 * grooscriptConverter.toJavascript(_) >> ''
        1 * resourceTaglib.require([module: 'clientEvents'])
        0 * _
    }

    static final FAKE_NAME = 'FAKE'
    static final DOMAIN_CLASS_NAME = 'correctDomainClass'
    static final DOMAIN_CLASS_NAME_WITH_PACKAGE = 'org.grooscript.correctDomainClass'

    @Unroll
    void 'test model with domain class'() {
        given:
        GrooScriptVertxTagLib.metaClass.existDomainClass = { String name ->
            name != FAKE_NAME
        }

        when:
        applyTemplate("<grooscript:model domainClass='${domainClassName}'/>")

        then:
        numberTimes * resourceTaglib.require([module: 'domain'])
        numberTimes * grooscriptConverter.convertDomainClass(domainClassName)
        0 * _

        where:
        domainClassName                | numberTimes
        FAKE_NAME                      | 0
        DOMAIN_CLASS_NAME              | 1
        DOMAIN_CLASS_NAME_WITH_PACKAGE | 1
    }

    @Unroll
    void 'test remote model with domain class'() {
        given:
        GrooScriptVertxTagLib.metaClass.existDomainClass = { String name ->
            name != FAKE_NAME
        }

        when:
        applyTemplate("<grooscript:remoteModel domainClass='${domainClassName}'/>")

        then:
        numberTimes * resourceTaglib.require([module: 'grooscriptGrails'])
        numberTimes * resourceTaglib.require([module: 'remoteDomain'])
        numberTimes * resourceTaglib.script(_)
        numberTimes * grooscriptConverter.convertDomainClass(domainClassName, true)
        0 * _

        where:
        domainClassName                | numberTimes
        FAKE_NAME                      | 0
        DOMAIN_CLASS_NAME              | 1
        DOMAIN_CLASS_NAME_WITH_PACKAGE | 1
    }

    private initVertx() {
        defineBeans {
            "${GrooScriptVertxTagLib.VERTX_EVENTBUS_BEAN}"(VertxEventBus,'localhost',8989)
        }
    }

    void 'test onEvent'() {
        when:
        applyTemplate("<grooscript:onEvent name='nameEvent'>assert true</grooscript:onEvent>")

        then:
        2 * resourceTaglib.script(_)
        1 * resourceTaglib.require([module: 'clientEvents'])
        1 * resourceTaglib.require([module: 'grooscriptGrails'])
        0 * _
    }

    void 'test onServerEvent'() {
        given:
        initVertx()

        when:
        applyTemplate("<grooscript:onServerEvent name='nameEvent'>assert true</grooscript:onServerEvent>")

        then:
        3 * resourceTaglib.script(_)
        1 * resourceTaglib.require([module: 'grooscriptGrails'])
        1 * resourceTaglib.require([module: 'vertx'])
        0 * _
    }

    void 'test onVertxStarted'() {
        given:
        initVertx()

        when:
        applyTemplate("<grooscript:onVertxStarted>assert true</grooscript:onVertxStarted>")

        then:
        3 * resourceTaglib.script(_)
        1 * resourceTaglib.require([module: 'grooscriptGrails'])
        1 * resourceTaglib.require([module: 'vertx'])
        0 * _
    }
    */
}
