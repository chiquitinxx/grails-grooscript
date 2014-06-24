package org.grooscript.grails.event

/**
 * User: jorgefrancoleza
 * Date: 14/09/13
 */
class ClientEventHandler implements EventHandler {

    def mapHandlers = [:]

    void sendMessage(String channel, Map data) {
        if (mapHandlers[channel]) {
            mapHandlers[channel].each { action ->
                action(data)
            }
        }
    }

    void onEvent(String channel, Closure action) {
        if (!mapHandlers[channel]) {
            mapHandlers[channel] = []
        }
        mapHandlers[channel] << action
    }

    void close() {
        mapHandlers = [:]
    }
}
