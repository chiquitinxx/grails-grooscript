def phantomJsTestTypeClassName = 'org.grooscript.grails.test.PhantomJsTestType'
def phantomJsProperty = 'PHANTOMJS_HOME'

softLoadClass = { className ->
    try {
        classLoader.loadClass(className)
    } catch (ClassNotFoundException e) {
        null
    }
}

eventAllTestsStart = {
    [phantomJsTestTypeClassName].each { testTypeClassName ->
        def testTypeClass = softLoadClass(testTypeClassName)
        if (testTypeClass) {
            if (!functionalTests.any { it.class == testTypeClass }) {
                functionalTests << testTypeClass.newInstance('phantomJs', 'functional')

                if (!System.getProperty(phantomJsProperty) && config.phantomjs?.path) {
                    System.setProperty(phantomJsProperty, config.phantomjs.path)
                }
            }
        }
    }
}


