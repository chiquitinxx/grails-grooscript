package org.grooscript.grails.websocket

import org.springframework.messaging.core.MessageSendingOperations
import spock.lang.Specification

/**
 * Created by jorge on 20/07/14.
 */
class SpringWebsocketPluginSpec extends Specification {

    def brokerMessagingTemplate = Mock(MessageSendingOperations)
    SpringWebsocketPlugin springWebsocketPlugin = new SpringWebsocketPlugin(
        brokerMessagingTemplate: brokerMessagingTemplate
    )

    def 'send message'() {
        when:
        springWebsocketPlugin.send('path', 'data')

        then:
        1 * brokerMessagingTemplate.convertAndSend('path', 'data')
    }
}
