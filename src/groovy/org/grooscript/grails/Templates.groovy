package org.grooscript.grails

/**
 * User: jorgefrancoleza
 * Date: 11/06/14
 */
class Templates {

    static final INIT_GROOSCRIPT_GRAILS = '''
\\$(document).ready(function() {
  GrooscriptGrails.remoteUrl = '${remoteUrl}';
});'''

    static final TEMPLATE_DRAW = '''
function ${functionName}(templateParams) {
  ${jsCode}
  var code = gsTextHtml(templateParams);
  \\$('${selector}').html(code);
};'''

    static final TEMPLATE_ON_READY = '''
\\$(document).ready(function() {
  ${functionName}();
})'''

    static final CLIENT_EVENT = '''
gsEvents.onEvent('${nameEvent}', ${functionName});
'''

    static final ON_EVENT_TAG = '''
gsEvents.onEvent('${nameEvent}', ${jsCode});
'''
}
