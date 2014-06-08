<%--
  User: jorgefrancoleza
  Date: 05/06/14
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Grooscript plugin</title>
    <asset:javascript src="grooscript-all.js"/>
</head>

<body>
    <h2>Grooscript plugin</h2>

    <p>To convert groovy code to javascript use grooscript:code</p>
    <grooscript:code>
        println 'Hello world!'
    </grooscript:code>

    <p>Have to render all scripts with asset:deferredScripts</p>
    <asset:deferredScripts/>
</body>
</html>