package org.grooscript.grails.tag

import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.web.mapping.LinkGenerator
import org.grooscript.grails.Templates
import org.grooscript.grails.bean.GrooscriptConverter
import org.grooscript.grails.util.GrooscriptTemplate

import static org.grooscript.grails.util.Util.*

class GrooscriptTagLib {

    static final REMOTE_URL_SETTED = 'grooscriptRemoteUrl'

    static namespace = 'grooscript'

    GrailsApplication grailsApplication
    GrooscriptConverter grooscriptConverter
    LinkGenerator grailsLinkGenerator
    GrooscriptTemplate grooscriptTemplate

    /**
     * grooscript:code
     * filePath - optional - path to the file to be converted from project dir
     */
    def code = { attrs, body ->
        def script
        if (attrs.filePath) {
            try {
                script = new File(attrs.filePath).text
                if (body()) {
                    script += '\n' + body()
                }
            } catch (e) {
                consoleError "${namespace}.code error reading file('${attrs.filePath}'): ${e.message}", e
            }
        } else {
            script = body()
        }
        if (script) {
            def jsCode = grooscriptConverter.toJavascript(script.toString(), attrs.options)
            asset.script(type: 'text/javascript') {
                jsCode
            }
        }
    }

    /**
     * grooscript: template
     *
     * filePath - optional - path to file to be used as template, from project dir
     * functionName - optional - name of the function that renders the template
     * itemSelector - optional - jQuery string selector where html generated will be placed
     * renderOnReady - optional defaults true - if template will be render onReady page event
     * listenEvents - optional - string list of events that render the page
     */
    def template = { attrs, body ->
        def script
        if (attrs.filePath) {
            try {
                script = new File(attrs.filePath).text
            } catch (e) {
                consoleError "GrooScriptVertxTagLib.template error reading file('${attrs.filePath}'): ${e.message}", e
            }
        } else {
            script = body()
        }
        if (script) {
            def functionName = attrs.functionName ?: newTemplateName
            String jsCode = grooscriptConverter.toJavascript("def gsTextHtml = { data -> HtmlBuilder.build { -> ${script}}}").trim()

            //initGrooscriptGrails()

            //processTemplateEvents(attrs.listenEvents, functionName)

            if (!attrs.itemSelector) {
                out << "\n<div id='${functionName}'></div>\n"
            }

            asset.script(type: 'text/javascript') {
                def result = grooscriptTemplate.apply(Templates.TEMPLATE_DRAW, [
                        functionName: functionName,
                        jsCode: jsCode,
                        selector: attrs.itemSelector ? attrs.itemSelector : "#${functionName}"
                ])
                if (!attrs.renderOnReady) {
                    result += grooscriptTemplate.apply(Templates.TEMPLATE_ON_READY, [functionName: functionName])
                }
                result
            }
        }
    }

    /**
     * grooscript:remoteModel
     * domainClass - REQUIRED name of the model class
     */
    def remoteModel = { attrs ->
        if (validDomainClassName(attrs.domainClass)) {
            initGrooscriptGrails()
            out << asset.script(type: 'text/javascript') {
                grooscriptConverter.convertRemoteDomainClass(attrs.domainClass)
            }
        }
    }

    private initGrooscriptGrails() {
        def urlSetted = request.getAttribute(REMOTE_URL_SETTED)
        if (!urlSetted) {
            asset.script(type: 'text/javascript') {
                grooscriptTemplate.apply(Templates.INIT_GROOSCRIPT_GRAILS,
                        [remoteUrl: grailsLinkGenerator.serverBaseURL])
            }
            request.setAttribute(REMOTE_URL_SETTED, true)
        }
    }

    /*
    private processTemplateEvents(listEvents, functionName) {
        if (listEvents) {
            r.require(module: 'clientEvents')
            r.script() {
                listEvents.each { nameEvent ->
                    out << "\ngrooscriptEvents.onEvent('${nameEvent}', ${functionName});\n"
                }
            }
        }
    }*/

    /**
     * grooscript:model
     * domainClass - REQUIRED name of the model class
     */
    /*
    def model = { attrs ->
        if (validDomainClassName(attrs.domainClass)) {
            grooscriptConverter.convertDomainClass(attrs.domainClass)
            r.require(module: 'domain')
        }
    }*/

    private validDomainClassName(String name) {
        if (!name || !(name instanceof String)) {
            consoleError "GrooscriptVertxTagLib have to define domainClass property as String"
        } else {
            if (existDomainClass(name)) {
                return true
            } else {
                consoleError "Not exist domain class ${name}"
            }
        }
        return false
    }

    private boolean existDomainClass(String nameClass) {
        grailsApplication.domainClasses.find { it.fullName == nameClass || it.name == nameClass }
    }

    /**
     * grooscript:onEvent
     * name - name of the event
     */
    /*
    def onEvent = { attrs, body ->
        String name = attrs.name
        if (name) {
            r.require(module: 'clientEvents')
            initGrooscriptGrails()

            r.script() {
                def script = body()
                def jsCode = grooscriptConverter.toJavascript("{ message -> ${script}}").trim()
                jsCode = removeLastSemicolon(jsCode)

                out << "\ngrooscriptEvents.onEvent('${name}', ${jsCode});\n"
            }
        } else {
            consoleError 'GrooScriptVertxTagLib onEvent need define name property'
        }
    }

    private removeLastSemicolon(String code) {
        if (code.lastIndexOf(';') >= 0) {
            return code.substring(0, code.lastIndexOf(';'))
        } else {
            return code
        }
    }

    private initGrooscriptGrails() {
        def urlSetted = request.getAttribute(REMOTE_URL_SETTED)
        if (!urlSetted) {
            r.require(module: 'grooscriptGrails')
            r.script() {
                out << '$(document).ready(function() {\n'
                out << "  GrooscriptGrails.remoteUrl = '${grailsLinkGenerator.serverBaseURL}';\n"
                out << '});\n'
            }
            request.setAttribute(REMOTE_URL_SETTED, true)
        }
    }

    private shortDomainClassName(String domainClassName) {
        def pos = domainClassName.lastIndexOf('.')
        if (pos) {
            return domainClassName.substring(pos + 1)
        } else {
            return domainClassName
        }
    }*/
}
