package websockets

import org.springframework.messaging.handler.annotation.MessageMapping

class ExampleController {

    def websocketSender

    def index() { }

    @MessageMapping("/hello")
    protected String hello() {
        websocketSender.send "/topic/hello", "hello from controller!"
        websocketSender.send "/topic/salute", [to: 'grooscript', who: 'plugin']
        websocketSender.send "/topic/list", [4, 2, 5, 8]
    }
}
