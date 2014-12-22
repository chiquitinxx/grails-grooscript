import org.grooscript.grails.bean.GrooscriptConverter
import org.grooscript.grails.util.GrooscriptTemplate
import org.grooscript.grails.websocket.SpringWebsocketPlugin

class GrooscriptGrailsPlugin {
    def version = "0.7"
    def grailsVersion = "2.4 > *"
    def pluginExcludes = [
        "grails-app/assets/javascripts/app/**",
        "grails-app/controllers/**",
        "grails-app/domain/**",
        "grails-app/views/**",
        "src/adoc/**",
        "src/groovy/MyScript.groovy",
        "web-app/css/**",
        "web-app/images/**",
        "web-app/js/**"
    ]

    def title = "Grooscript Plugin"
    def author = "Jorge Franco"
    def authorEmail = "grooscript@gmail.com"
    def description = '''\
Use grooscript to work in the client side with your groovy code.
It converts the code to javascript and your groovy code will run in the browser.
'''

    def documentation = "http://grooscript.org/grails-plugin/index.html"

    def license = "APACHE"

    def organization = [ name: "Grails Community", url: "http://grails.org/" ]

    def developers = []

    def issueManagement = [ system: "GITHUB", url: "https://github.com/chiquitinxx/grails-grooscript/issues" ]

    def scm = [ url: "http://github.com/chiquitinxx/grails-grooscript/" ]

    def doWithSpring = {
        grooscriptConverter(GrooscriptConverter) {
            grailsApplication = ref('grailsApplication')
        }
        grooscriptTemplate(GrooscriptTemplate)

        if (application.config.grooscript?.websockets == 'springWebsocketPlugin') {
            websocketSender(SpringWebsocketPlugin) {
                brokerMessagingTemplate = ref('brokerMessagingTemplate')
            }
        }
    }
}
