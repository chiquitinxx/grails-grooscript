package org.grooscript.grails.util

/**
 * User: jorgefrancoleza
 * Date: 13/09/13
 */
class Util {

    static final SEP = System.getProperty('file.separator')

    static final String DOMAIN_NAME = 'domain'
    static final String REMOTE_NAME = 'remoteDomain'
    static final String GROOVY_SRC_DIR = "src${SEP}groovy"
    static final String DOMAIN_DIR = "grails-app${SEP}${DOMAIN_NAME}"
    static final String JS_DIR = "web-app${SEP}js"
    static final String DOMAIN_JS_DIR = "${JS_DIR}${SEP}${DOMAIN_NAME}"
    static final String REMOTE_JS_DIR = "${JS_DIR}${SEP}${REMOTE_NAME}"

    static final PLUGIN_MESSAGE = '[Grooscript Plugin]'

    static consoleMessage(message) {
        println "${PLUGIN_MESSAGE} [INFO] $message"
    }

    static consoleError(message) {
        println "\u001B[91m${PLUGIN_MESSAGE} [ERROR] $message\u001B[0m"
    }

    static consoleWarning(message) {
        println "\u001B[93m${PLUGIN_MESSAGE} [WARNING] $message\u001B[0m"
    }

    static String getNewTemplateName() {
        'fTemplate' + new Date().time.toString()
    }
}
