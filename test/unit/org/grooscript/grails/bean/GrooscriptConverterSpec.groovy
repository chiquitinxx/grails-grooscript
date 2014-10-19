package org.grooscript.grails.bean

import org.codehaus.groovy.grails.commons.GrailsApplication
import org.grooscript.GrooScript
import org.grooscript.daemon.ConversionDaemon
import org.grooscript.grails.domain.DomainClass
import org.grooscript.grails.remote.RemoteDomainClass
import org.grooscript.grails.util.Util
import spock.lang.Specification
import spock.lang.Unroll

/**
 * User: jorgefrancoleza
 * Date: 05/06/14
 */
class GrooscriptConverterSpec extends Specification {

    private static final JS = 'var a = 5; b.go()'
    private static final CODE = 'def a = 5; b.go()'
    private static final DOMAIN_CLASS = 'domainClass'
    private static final DOMAIN_CLASS_PATH = 'domainClassPath'
    private static final DO_AFTER_RESULT = '5'
    def grooscriptConverter = new GrooscriptConverter()
    def grailsApplication = Stub(GrailsApplication)

    def setup() {
        GroovySpy(GrooScript, global: true)
        grooscriptConverter.grailsApplication = grailsApplication
        grooscriptConverter.metaClass.getDomainFilePath = { String domainClass ->
            domainClass == DOMAIN_CLASS ? DOMAIN_CLASS_PATH : null
        }
    }

    def 'convert to javascript without conversion options'() {
        given:
        def code = CODE

        when:
        def result = grooscriptConverter.toJavascript(code, null)

        then:
        1 * GrooScript.clearAllOptions()
        1 * GrooScript.setConversionProperty('classPath', ['src/groovy'])
        1 * GrooScript.setConversionProperty('mainContextScope', GrooscriptConverter.DEFAULT_CONVERSION_SCOPE_VARS)
        0 * GrooScript.setConversionProperty(_)
        1 * GrooScript.convert(CODE)
        result == 'var a = 5;\ngs.mc(b,"go",[]);\n'
    }

    def 'start conversion daemon without conversion options'() {
        given:
        def daemon = Mock(ConversionDaemon)
        def options = [source: 'source', destination: 'destination']

        when:
        grooscriptConverter.startDaemon(options)

        then:
        1 * GrooScript.startConversionDaemon('source', 'destination',
                ['classPath':['src/groovy'], 'mainContextScope': GrooscriptConverter.DEFAULT_CONVERSION_SCOPE_VARS],
                null) >> daemon
        0 * _
        grooscriptConverter.conversionDaemon == daemon
    }

    def 'start conversion daemon with conversion options'() {
        given:
        def doAfter = { list -> DO_AFTER_RESULT }
        def conversionOptions = [classPath: 'mySrc']
        def daemon = Mock(ConversionDaemon)
        def options = [source: 'source', destination: 'destination',
                       conversionOptions: conversionOptions, doAfter: doAfter]

        when:
        grooscriptConverter.startDaemon(options)

        then:
        1 * GrooScript.startConversionDaemon('source', 'destination',
                [
                    'classPath':['mySrc', 'src/groovy'],
                    'mainContextScope': GrooscriptConverter.DEFAULT_CONVERSION_SCOPE_VARS
                ], { it([]) == DO_AFTER_RESULT}) >> daemon
        0 * _
        grooscriptConverter.conversionDaemon == daemon
    }



    def 'stop the daemon'() {
        given:
        def daemon = Mock(ConversionDaemon)
        grooscriptConverter.conversionDaemon = daemon

        when:
        grooscriptConverter.stopDaemon()

        then:
        1 * daemon.stop()
    }

    def 'convert a model domain class'() {
        given:
        GroovySpy(Util, global: true)

        when:
        def result = grooscriptConverter.convertDomainClass(DOMAIN_CLASS)

        then:
        1 * GrooScript.clearAllOptions()
        1 * Util.getDomainFileText(DOMAIN_CLASS, grailsApplication) >> CODE
        1 * Util.addCustomizationAstOption(DomainClass)
        1 * GrooScript.setConversionProperty('customization', _)
        0 * GrooScript.setConversionProperty(_)
        1 * GrooScript.convert(CODE) >> JS
        result == JS
    }

    def 'convert a remote model domain class'() {
        given:
        GroovySpy(Util, global: true)

        when:
        def result = grooscriptConverter.convertRemoteDomainClass(DOMAIN_CLASS)

        then:
        1 * GrooScript.clearAllOptions()
        1 * Util.getDomainFileText(DOMAIN_CLASS, grailsApplication) >> CODE
        1 * Util.addCustomizationAstOption(RemoteDomainClass)
        1 * GrooScript.setConversionProperty('customization', _)
        0 * GrooScript.setConversionProperty(_)
        1 * GrooScript.convert(CODE) >> JS
        result == JS
    }
}
