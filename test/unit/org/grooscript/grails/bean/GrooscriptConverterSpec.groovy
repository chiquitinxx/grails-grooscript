package org.grooscript.grails.bean

import org.codehaus.groovy.grails.commons.GrailsApplication
import org.grooscript.GrooScript
import org.grooscript.daemon.ConversionDaemon
import spock.lang.Specification

/**
 * User: jorgefrancoleza
 * Date: 05/06/14
 */
class GrooscriptConverterSpec extends Specification {

    private static final CODE = 'def a = 5; b.go()'
    def grooscriptConverter = new GrooscriptConverter()
    def grailsApplication = Stub(GrailsApplication)

    def setup() {
        GroovySpy(GrooScript, global: true)
        grooscriptConverter.grailsApplication = grailsApplication
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
        1 * GrooScript.convert(CODE)
        1 * GrooScript.getNewConverter()
        0 * _
        result == 'var a = 5;\ngs.mc(b,"go",[]);\n'
    }

    def 'start conversion daemon without conversion options'() {
        given:
        def conversionOptions = [:]
        def daemon = Mock(ConversionDaemon)
        grailsApplication.config >> [grooscript: [daemon:
            [source: 'source', destination: 'destination', options: conversionOptions]
        ]]

        when:
        grooscriptConverter.startDaemon()

        then:
        1 * GrooScript.startConversionDaemon('source', 'destination', ['classPath':['src/groovy']], null) >> daemon
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
}
