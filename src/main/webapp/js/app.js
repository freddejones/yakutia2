define([
    'router',
    'bootstrap',
    'view/YakutiaManager'
    ],
function(YakutiaRouter, Bootstrap, YakutiaManager) {

    window.playerId = 1;
    window.gameId = 1;
    window.router = new YakutiaRouter();
    Backbone.history.start();
//    new YakutiaManager().init();
});
