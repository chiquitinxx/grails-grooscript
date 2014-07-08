import grails.util.Environment
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.grooscript.grails.bean.GrooscriptConverter
import org.grooscript.grails.util.GrooscriptTemplate

import static org.grooscript.grails.util.Util.consoleMessage
import static org.grooscript.grails.util.Util.consoleMessage

class GrooscriptGrailsPlugin {
    def version = "0.5-SNAPSHOT"
    def grailsVersion = "2.4 > *"
    def pluginExcludes = [
        "grails-app/views/error.gsp",
        "grails-app/controllers/**",
        "grails-app/domain/**",
        "grails-app/views/**",
        "src/docs/**",
        "web-app/css/**",
        "web-app/images/**",
        "web-app/js/**"
    ]

    def title = "Grails Grooscript Plugin"
    def author = "Jorge Franco"
    def authorEmail = "grooscript@gmail.com"
    def description = '''\
Use grooscript to work in the client side with your groovy code.
It converts the code to javascript and your groovy code will run in your browser.
'''

    def documentation = "http://grooscript.org/grailsPlugin"

    def license = "APACHE"

    def organization = [ name: "Grails Community", url: "http://grails.org/" ]

    def developers = []

    def issueManagement = [ system: "GITHUB", url: "https://github.com/chiquitinxx/grails-grooscript/issues" ]

    def scm = [ url: "http://github.com/chiquitinxx/grails-grooscript/" ]

    def doWithWebDescriptor = { xml ->
        // TODO Implement additions to web.xml (optional), this event occurs before
    }

    def doWithSpring = {
        grooscriptConverter(GrooscriptConverter) {
            grailsApplication = ref('grailsApplication')
        }
        grooscriptTemplate(GrooscriptTemplate)
    }

    def doWithDynamicMethods = { ctx ->
        // TODO Implement registering dynamic methods to classes (optional)
    }

    def doWithApplicationContext = { ctx ->
        initGrooscriptDaemon(application)
    }

    def onChange = { event ->
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    def onConfigChange = { event ->
        GrooscriptConverter grooscriptConverter = application.mainContext.grooscriptConverter
        if (grooscriptConverter.conversionDaemon) {
            consoleMessage 'Restarting grooscript daemon ...'
            grooscriptConverter.startDaemon()
        } else {
            initGrooscriptDaemon(application)
        }
    }

    def onShutdown = { event ->
        GrooscriptConverter grooscriptConverter = application.mainContext.grooscriptConverter
        if (grooscriptConverter.conversionDaemon) {
            consoleMessage 'Stopping grooscript daemon ...'
            grooscriptConverter.stopDaemon()
        }
    }

    private void initGrooscriptDaemon(GrailsApplication application) {

        if (Environment.current == Environment.DEVELOPMENT) {

            def source = application.config.grooscript?.daemon?.source
            def destination = application.config.grooscript?.daemon?.destination

            //Start the daemon if source and destination are ok
            if (source && destination && application.mainContext.grooscriptConverter) {
                consoleMessage 'Starting grooscript daemon ...'
                application.mainContext.grooscriptConverter.startDaemon()
            } else {
                consoleMessage 'Grooscript daemon not started.'
            }
        }
    }
}
