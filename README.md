Grooscript grails plugin 1.0-SNAPSHOT
===

It requires Grails 2.4+, asset-pipeline and cache plugin.

Use your groovy code in your gsp's, your code, converted to javascript, will run in your browser.

[Plugin documentation](http://grooscript.org/grails-plugin/index.html)

[Guide](http://grooscript.org/grails-plugin/rest-demo.html) to use Rest support.

[Info](http://grooscript.org/grails-plugin/websocket-support.html) websocket support.

[Grooscript](http://grooscript.org/)

Build
---

Generate javascript files with:

    groovy generateGrooscriptGrailsJs.groovy

To run tests, need PhantomJs installed, set path in Config.groovy