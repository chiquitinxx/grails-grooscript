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
    websocketClient.send("/app/hello", null, "");
</grooscript:initSpringWebsocket>

<asset:deferredScripts/>

</body>
</html>