define([
    'router',
    'bootstrap',
    'view/YakutiaManager',
    'backbone'
    ],
function(YakutiaRouter, Bootstrap, YakutiaManager, Backbone) {

    window.App = {};
    window.App.vent = _.extend({}, Backbone.Events);
    window.playerId = 1;
    window.gameId = 1;
    window.router = new YakutiaRouter();
    Backbone.history.start();
//    new YakutiaManager().init();
});
