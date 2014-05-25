define([
    'router',
    'bootstrap',
    'view/YakutiaManager',
    'backbone'
    ],
function(YakutiaRouter, Bootstrap, YakutiaManager, Backbone) {

    window.App = {};
    window.App.vent = _.extend({}, Backbone.Events);
    window.App.router = new YakutiaRouter();
    Backbone.history.start();

});
