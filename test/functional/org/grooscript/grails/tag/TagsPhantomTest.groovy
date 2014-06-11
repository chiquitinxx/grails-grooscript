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
    void testApplicationUp() {
        assert $('title').text() == 'Grooscript plugin'
    }

    @PhantomJsTest(url = 'http://localhost:8080/grooscript')
    void testBasicTemplate() {
        assert $('.tools').size() == 3
    }
}
