<%--
  User: jorgefrancoleza
  Date: 05/06/14
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Grooscript plugin</title>
    <script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
    <asset:javascript src="grooscript-all.js"/>
</head>

<body>
    <h2>Grooscript plugin</h2>

    <p>To convert groovy code to javascript use grooscript:code</p>
    <grooscript:code>
        println 'Hello world!'
    </grooscript:code>

    <p>To create a template use grooscript:template</p>
    <p>List of tools:</p>
    <!-- Must get 3 '.tools' -->
    <grooscript:template>
        ul {
            ['Groovy', 'Grails', 'Grooscript'].each {
                li ([class: "tools"], it)
            }
        }
    </grooscript:template>

    <p>Have to render all scripts with asset:deferredScripts at the end</p>
    <asset:deferredScripts/>
</body>
</html>