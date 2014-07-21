package websockets

import org.grooscript.grails.websocket.WebsocketSender
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo

class ExampleController {

    def websocketSender

    def index() { }

    @MessageMapping("/hello")
    protected String hello() {
        websocketSender.send "/topic/hello", "hello from controller!"
    }
}
