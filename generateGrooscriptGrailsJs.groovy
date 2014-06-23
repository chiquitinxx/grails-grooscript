@Grab('org.grooscript:grooscript:0.5.1')

import org.grooscript.GrooScript

String text = GrooScript.classLoader.getResourceAsStream('META-INF/resources/grooscript-all.js').text

GrooScript.setConversionProperty(GrooScript.CLASSPATH_OPTION, ['src/groovy'])
text += GrooScript.convert(new File('src/groovy/org/grooscript/grails/util/GrooscriptGrails.groovy').text)
text += GrooScript.convert(new File('src/groovy/org/grooscript/grails/promise/RemoteDomain.groovy').text)

new File('grails-app/assets/javascripts/grooscript-grails.js').text = text