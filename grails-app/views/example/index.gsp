<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>

    <asset:javascript src="app/jquery.min.js"/>
    <asset:javascript src="spring-websocket"/>
    <asset:javascript src="grooscript-grails.js"/>

</head>
<body>
<div id="helloDiv"></div>

<grooscript:initSpringWebsocket>
    websocketClient.send("/app/hello", null, "")
</grooscript:initSpringWebsocket>

<grooscript:onServerEvent path="/topic/hello">
    $("#helloDiv").append('<p>'+data+'</p>')
</grooscript:onServerEvent>

<grooscript:onServerEvent path="/topic/salute">
    $("#helloDiv").append('<p>hello '+data.who+'!</p>')
    $("#helloDiv").append('<p>hello '+data.to+'!</p>')
</grooscript:onServerEvent>

<grooscript:code>
    class Salute {
        def who
        def to
        def say = {
            who + ': hello ' + to + '!'
        }
    }
</grooscript:code>

<grooscript:onServerEvent path="/topic/salute" type="Salute">

    $("#helloDiv").append('<p>'+data.say()+'</p>')
</grooscript:onServerEvent>

<grooscript:onServerEvent path="/topic/list">
    data.each {
        $("#helloDiv").append('<p>'+it+'</p>')
    }
</grooscript:onServerEvent>

<asset:deferredScripts/>

</body>
</html>