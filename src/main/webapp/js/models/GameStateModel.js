define(['backbone'], function (Backbone) {

    var GameStateModel = Backbone.Model.extend({
        url: function () {
            return '/game/state/'+this.get('gameId')+'/'+this.get('playerId');
        },

        defaults: {
            state: 'NONE'
        }
    });

    return GameStateModel;
});
