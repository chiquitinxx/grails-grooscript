@Grab('org.grooscript:grooscript:0.6.2-SNAPSHOT')

import org.grooscript.GrooScript
import org.grooscript.convert.ConversionOptions

String text = GrooScript.classLoader.getResourceAsStream('META-INF/resources/grooscript.js').text
text += GrooScript.classLoader.getResourceAsStream('META-INF/resources/grooscript-tools.js').text

GrooScript.setConversionProperty(ConversionOptions.CLASSPATH.text, ['src/groovy'])
text += GrooScript.convert(new File('src/groovy/org/grooscript/grails/util/GrooscriptGrails.groovy').text)
text += GrooScript.convert(new File('src/groovy/org/grooscript/grails/promise/RemoteDomain.groovy').text)
GrooScript.setConversionProperty(ConversionOptions.FINAL_TEXT.text, 'var gsEvents = ClientEventHandler();')
text += GrooScript.convert(new File('src/groovy/org/grooscript/grails/event/ClientEventHandler.groovy').text)

new File('grails-app/assets/javascripts/grooscript-grails.js').text = text