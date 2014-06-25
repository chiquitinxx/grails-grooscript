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
}
