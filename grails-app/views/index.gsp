<%--
  User: jorgefrancoleza
  Date: 05/06/14
--%>

<%@ page import="test.Book" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Grooscript plugin</title>
    <script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
    <asset:javascript src="grooscript-grails.js"/>
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

    <ul id="bookList">
    </ul>

    <grooscript:remoteModel domainClass="Book"/>

    <grooscript:code>
        import test.Book

        def getList = {
            Book.list().then({ list ->
                $('#bookList').prepend('<p>Total number of books:'+list.size()+'</p>')
                list.each {
                    $('#bookList').append('<li>'+it.upperTitle()+'</li>')
                }
            })
        }

        $(document).ready({
            new Book(title: 'MyBook').save().then(
                { result ->
                    println 'Created: ' + result.upperTitle()
                    Book.get(result.id).then(
                        { getResult ->
                            println 'Get: ' + getResult.title
                            getList()
                        },
                        { getResult -> println 'Fail get:' + getResult}
                    )
                },
                { result -> println 'Fail creation:' + result}
            )
        })
    </grooscript:code>

    <p>Have to render all scripts with asset:deferredScripts at the end</p>
    <asset:deferredScripts/>

</body>
</html>