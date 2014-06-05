package org.grooscript.grails.bean

import grails.plugin.cache.Cacheable
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.grooscript.GrooScript
import org.grooscript.daemon.ConversionDaemon

import static org.grooscript.grails.util.Util.*

/**
 * User: jorgefrancoleza
 * Date: 22/09/13
 */
class GrooscriptConverter {

    ConversionDaemon conversionDaemon
    GrailsApplication grailsApplication

    @Cacheable('conversions')
    String toJavascript(String groovyCode, options = null) {
        String jsCode = ''
        if (groovyCode) {
            GrooScript.clearAllOptions()
            try {
                options = addGroovySourceClassPathIfNeeded(options)
                options.each { key, value ->
                    GrooScript.setConversionProperty(key, value)
                }

                jsCode = GrooScript.convert(groovyCode)

            } catch (e) {
                consoleError "Error converting to javascript: ${e.message}"
            }
        }
        jsCode
    }

    void startDaemon() {

        if (conversionDaemon) {
            stopDaemon()
        }

        def config = grailsApplication.config

        def source = config.grooscript?.daemon?.source
        def destination = config.grooscript?.daemon?.destination

        //By default
        def options = config.grooscript?.daemon?.options
        options = addGroovySourceClassPathIfNeeded(options)

        conversionDaemon = GrooScript.startConversionDaemon(source, destination, options,
                closureToRunAfterDaemonConversion)
    }

    void stopDaemon() {
        if (conversionDaemon) {
            conversionDaemon.stop()
        }
    }

    private Closure getClosureToRunAfterDaemonConversion() {

        //Config option to do
        def doAfterDaemonConversion = grailsApplication.config.grooscript?.daemon?.doAfter

        //Full action to do after some change
        Closure doAfterDaemon = null
        if (doAfterDaemonConversion && doAfterDaemonConversion instanceof Closure) {
            doAfterDaemon = { listFilesList ->
                doAfterDaemonConversion(listFilesList)
            }
        }
        doAfterDaemon
    }

    private addGroovySourceClassPathIfNeeded(options) {
        def conversionOptions = options ?: [:]
        if (!conversionOptions.classPath) {
            conversionOptions.classPath = []
        } else {
            if (conversionOptions.classPath instanceof String) {
                conversionOptions.classPath = [conversionOptions.classPath]
            }
        }
        if (!conversionOptions.classPath.contains(GROOVY_SRC_DIR)) {
            conversionOptions.classPath << GROOVY_SRC_DIR
        }
        conversionOptions
    }
}
