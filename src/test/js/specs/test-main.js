var tests = [];
for (var file in window.__karma__.files) {
    if (window.__karma__.files.hasOwnProperty(file)) {
        if (/Spec\.js$/.test(file)) {
            tests.push(file);
        }
    }
}

requirejs.config({
    // Karma serves files from '/base'
    baseUrl: '/base/main/webapp/js/',

    paths: {
        jquery: '../lib/jquery',
        underscore: '../lib/underscore',
        'underscore-string': '../lib/underscore-string',
        backbone: '../lib/backbone',
        text: '../lib/text',
        i18n: '../lib/i18n',
        kinetic: '../lib/kinetic',
        'bootstrap': '../lib/bootstrap',
        'handlebars-orig': '../lib/handlebars',
        async: '../lib/async',
        keymaster: '../lib/keymaster',
        json2: '../lib/json2',
        sinon: '../lib/sinon-1.9.0'
    },

    shim: {
        'underscore': {
            exports: '_'
        },
        'underscore-string': {
            deps: [
                'underscore'
            ]
        },
        'handlebars-orig': {
            exports: 'Handlebars'
        },
        'backbone': {
            deps: [
                'underscore',
                'underscore-string',
                'jquery'
            ],
            exports: 'Backbone'
        },
        'bootstrap': {
            deps: [
                'jquery'
            ]
        },
        'keymaster': {
            exports: 'key'
        },
        'async': {
            exports: 'async'
        },
        "sinon": {
            "exports": "sinon"
        }
    },

    // ask Require.js to load these files (all our tests)
    deps: tests,

    // start test run, once Require.js is done
    callback: window.__karma__.start
});