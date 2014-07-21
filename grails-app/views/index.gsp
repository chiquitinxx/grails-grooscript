<%--
  User: jorgefrancoleza
  Date: 05/06/14
--%>

<%@ page import="test.Book" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Grooscript plugin</title>
    <asset:javascript src="app/jquery.min.js"/>
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

    <p>List of good actors:</p>
    <ul id="actorList">
    </ul>

    <p>To use a domain class in the client, without sync with the server, use grooscript:model</p>
    <grooscript:model domainClass="Actor"/>

    <grooscript:code>
        import test.Actor

        def actors = [[name: 'Good Actor', oscars: 2], [name: 'Not so good actor', oscars: 0], [name: '', oscars: 35]]

        actors.each {
            def actor = new Actor(it)
            if (actor.validate() && actor.goodActor()) {
                actor.save()
            }
        }

        Actor.list().each {
            $('#actorList').append '<li>'+it.name+'</li>'
        }
    </grooscript:code>

    <ul id="bookList">
    </ul>

    <p>To use a domain class in the server, annotated with @Resource(uri='XXX'), use grooscript:remoteModel</p>
    <grooscript:remoteModel domainClass="Book"/>

    <grooscript:code>
        import test.Book

        //To get some console info
        //gs.consoleInfo = true

        def getList = {
            Book.list().then({ list ->
                $('#bookList').prepend('<p>Total number of books:'+list.size()+'</p>')
                list.each {
                    $('#bookList').append('<li>'+it.upperTitle()+'</li>')
                }
            })
        }

        def createAndDeleteBook = { titleBook ->
            new Book(title: titleBook).save().then({ newBook ->
                println 'Also created: ' + newBook.upperTitle()
                newBook.delete().then({ res ->
                    println 'Deleted success: ' + res
                    GrooscriptGrails.sendClientMessage('delete', newBook.title)
                }, { fail ->
                    println 'Deleted error: ' + fail
                })
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
                    createAndDeleteBook('Gray')
                },
                { result -> println 'Fail creation:' + result}
            )
        })
    </grooscript:code>

    <p>Can combine templates with local events</p>
    <div id="deleteEvent"></div>
    <div id="lastEvent"></div>
    <grooscript:code>
        $(document).ready {
            gsEvents.sendMessage('newEvent', 'Application started.')
            gsEvents.onEvent('delete', { title ->
                $('#deleteEvent').html 'Deleted ' + title
            })
        }
    </grooscript:code>

    <grooscript:template onLoad="false" onEvent="newEvent" itemSelector="#lastEvent">
        p 'New event: ' + data
    </grooscript:template>

    <p>Can react directly to events</p>
    <grooscript:code>
        $(document).ready {
            gsEvents.sendMessage('startEvent', 'Application ready.')
        }
    </grooscript:code>
    <grooscript:onEvent name="startEvent">
        $('#customReadEvent').append '<p>Data event: ' + event + '</p>'
    </grooscript:onEvent>
    <div id="customReadEvent"></div>

    <p>Can add converted files from th daemon</p>
    <asset:javascript src="app/MyScript.js"/>

    <p>Have to render all scripts with asset:deferredScripts at the end</p>
    <asset:deferredScripts/>

</body>
</html>