package org.grooscript.grails.websocket

import org.springframework.messaging.simp.SimpMessageSendingOperations

/**
 * Created by jorge on 20/07/14.
 */
class SpringWebsocketPlugin implements WebsocketSender {

    def brokerMessagingTemplate

    void send(String path, Object data) {
        brokerMessagingTemplate.convertAndSend path, data
    }
}
