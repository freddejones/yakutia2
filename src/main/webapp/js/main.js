// Set the require.js configuration for your application.
require.config({
    waitSeconds: 200,
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
        }
    },

    // Libraries
    paths: {
        jquery: '../lib/jquery',
        underscore: '../lib/underscore',
        'underscore-string': '../lib/underscore-string',
        backbone: '../lib/backbone',
        text: '../lib/text',
        i18n: '../lib/i18n',
        kinetic: '../lib/kinetic',
        bootstrap: '../lib/bootstrap',
        'handlebars-orig': '../lib/handlebars',
        async: '../lib/async',
        keymaster: '../lib/keymaster',
		json2: '../lib/json2',
        router: 'router'
    }
});

// Load our app module and pass it to our definition function
require(['app']);
