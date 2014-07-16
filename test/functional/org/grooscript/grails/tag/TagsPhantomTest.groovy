package org.grooscript.grails.tag

import org.grooscript.asts.PhantomJsTest

/**
 * User: jorgefrancoleza
 * Date: 08/06/14
 */
class TagsPhantomTest extends GroovyTestCase {

    void testTrue() {
        assert true
    }

    @PhantomJsTest(url = 'http://localhost:8080/grooscript')
    void testBookActions() {
        assert $('#bookList li').size() > 0, 'Number of books is ' + $('#bookList li').size()
    }

    @PhantomJsTest(url = 'http://localhost:8080/grooscript')
    void testApplicationUp() {
        assert $('title').text() == 'Grooscript plugin'
    }

    @PhantomJsTest(url = 'http://localhost:8080/grooscript')
    void testBasicTemplate() {
        assert $('.tools').size() == 3
    }

    @PhantomJsTest(url = 'http://localhost:8080/grooscript', waitSeconds = 1)
    void testTemplateWithEventAtStart() {
        assert $('#lastEvent').text() == 'New event: Application started.', 'Last event is: ' + $('#lastEvent').text()
    }

    @PhantomJsTest(url = 'http://localhost:8080/grooscript', waitSeconds = 1)
    void testDeleteBook() {
        assert $('#deleteEvent').text() == 'Deleted Gray', 'Delete event is: ' + $('#deleteEvent').text()
    }

    @PhantomJsTest(url = 'http://localhost:8080/grooscript', waitSeconds = 1)
    void testModelActors() {
        assert $('#actorList li').size() == 1, 'Number of actors is ' + $('#actorList li').size()
    }

    @PhantomJsTest(url = 'http://localhost:8080/grooscript')
    void testDaemonConversion() {
        assert $('h4').text() == 'The end!', 'h4 is ' + $('h4').text()
    }

    @PhantomJsTest(url = 'http://localhost:8080/grooscript', waitSeconds = 1)
    void testOnScriptTag() {
        assert $('#customReadEvent').text() == 'Data event: Application ready.', 'Data event is: ' + $('#customReadEvent').text()
    }
}
