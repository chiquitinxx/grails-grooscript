package org.grooscript.grails.tag

import asset.pipeline.AssetsTagLib
import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import org.codehaus.groovy.grails.web.mapping.LinkGenerator
import org.grooscript.grails.Templates
import org.grooscript.grails.bean.GrooscriptConverter
import org.grooscript.grails.util.GrooscriptTemplate
import org.grooscript.grails.util.Util
import spock.lang.Specification
import spock.lang.Unroll

/**
 * @author Jorge Franco
 * Date: 08/06/14
 */
@TestMixin(GrailsUnitTestMixin)
@TestFor(GrooscriptTagLib)
class GrooscriptTagLibSpec extends Specification {

    GrooscriptConverter grooscriptConverter
    AssetsTagLib assetsTagLib
    GrooscriptTemplate template = new GrooscriptTemplate()
    LinkGenerator linkGenerator = Mock(LinkGenerator)

    void setup() {
        grooscriptConverter = Mock(GrooscriptConverter)
        assetsTagLib = Mock(AssetsTagLib)
        GrooscriptTagLib.metaClass.grooscriptConverter = grooscriptConverter
        GrooscriptTagLib.metaClass.asset = assetsTagLib
        GrooscriptTagLib.metaClass.grooscriptTemplate = template
        GrooscriptTagLib.metaClass.grailsLinkGenerator = linkGenerator
    }

    void cleanup() {
    }

    static final GROOVY_CODE = 'code example'
    static final JS_CODE = 'js converted code'
    static final REMOTE_URL = 'my url'
    static final TEMPLATE_NAME = 'template name'

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

    /*static final FILE_PATH = 'GrooscriptGrailsPlugin.groovy'

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
    }*/

    void 'test basic template'() {
        given:
        GroovySpy(Util, global: true)

        when:
        def result = applyTemplate("<grooscript:template>assert true</grooscript:template>")

        then:
        1 * Util.newTemplateName >> TEMPLATE_NAME
        1 * linkGenerator.getServerBaseURL() >> REMOTE_URL
        1 * assetsTagLib.script(['type':'text/javascript'], {
            it() == template.apply(Templates.INIT_GROOSCRIPT_GRAILS, [remoteUrl: REMOTE_URL])
        })
        1 * assetsTagLib.script(['type':'text/javascript'], {
            it() == template.apply(Templates.TEMPLATE_DRAW, [
                    functionName: TEMPLATE_NAME, jsCode: JS_CODE, selector: "#$TEMPLATE_NAME"]) +
                        template.apply(Templates.TEMPLATE_ON_READY, [functionName: TEMPLATE_NAME])
        })
        1 * grooscriptConverter.toJavascript('def gsTextHtml = { data -> HtmlBuilder.build { -> assert true}}') >> JS_CODE
        0 * _
        result == "\n<div id='$TEMPLATE_NAME'></div>\n"
    }

    void 'very basic test template options'() {
        when:
        def result = applyTemplate("<grooscript:template functionName='jarJar'" +
                " itemSelector='#anyId' onLoad=\"${false}\">assert true</grooscript:template>")

        then:
        1 * assetsTagLib.script(['type':'text/javascript'], {
            it() == template.apply(Templates.TEMPLATE_DRAW, [
                    functionName: 'jarJar', jsCode: JS_CODE, selector: '#anyId'])
        })
        1 * grooscriptConverter.toJavascript('def gsTextHtml = { data -> HtmlBuilder.build { -> assert true}}') >> JS_CODE
        !result
    }

    void 'test onEvent template option'() {
        given:
        GroovySpy(Util, global: true)

        when:
        def result = applyTemplate("<grooscript:template onEvent='myEvent' onLoad=\"${false}\">assert true</grooscript:template>")

        then:
        1 * Util.newTemplateName >> TEMPLATE_NAME
        1 * assetsTagLib.script(['type':'text/javascript'], {
            it() == template.apply(Templates.CLIENT_EVENT, [
                    functionName: TEMPLATE_NAME, nameEvent: 'myEvent'])
        })
        1 * grooscriptConverter.toJavascript(_) >> JS_CODE
        result == "\n<div id='$TEMPLATE_NAME'></div>\n"
    }

    /*static final FILE_PATH_TEMPLATE = 'src/groovy/MyTemplate.groovy'

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

    void 'test onEvent'() {
        when:
        applyTemplate("<grooscript:onEvent name='nameEvent'>assert true</grooscript:onEvent>")

        then:
        2 * resourceTaglib.script(_)
        1 * resourceTaglib.require([module: 'clientEvents'])
        1 * resourceTaglib.require([module: 'grooscriptGrails'])
        0 * _
    }

    */

    static final FAKE_NAME = 'FAKE'
    static final DOMAIN_CLASS_NAME = 'correctDomainClass'
    static final DOMAIN_CLASS_NAME_WITH_PACKAGE = 'org.grooscript.correctDomainClass'

    @Unroll
    void 'test remote model with domain class'() {
        given:
        GrooscriptTagLib.metaClass.existDomainClass = { String name ->
            name != FAKE_NAME
        }

        when:
        applyTemplate("<grooscript:remoteModel domainClass='${domainClassName}'/>")

        then:
        numberTimes * assetsTagLib.script(['type':'text/javascript'], {
            it() == JS_CODE
        })
        numberTimes * grooscriptConverter.convertRemoteDomainClass(domainClassName) >> JS_CODE

        where:
        domainClassName                | numberTimes
        FAKE_NAME                      | 0
        DOMAIN_CLASS_NAME              | 1
        DOMAIN_CLASS_NAME_WITH_PACKAGE | 1
    }
}
