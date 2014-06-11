import org.grooscript.grails.bean.GrooscriptConverter
import org.grooscript.grails.util.GrooscriptTemplate
import org.grooscript.grails.util.GrooscriptTemplate

class GrooscriptGrailsPlugin {
    def version = "0.1-SNAPSHOT"
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

    def title = "Grails grooscript Plugin"
    def author = "Jorge Franco"
    def authorEmail = "grooscript@gmail.com"
    def description = '''\
Use grooscript to work in the client size with your groovy code.
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
        // TODO Implement post initialization spring config (optional)
    }

    def onChange = { event ->
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    def onConfigChange = { event ->
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }

    def onShutdown = { event ->
        // TODO Implement code that is executed when the application shuts down (optional)
    }
}
