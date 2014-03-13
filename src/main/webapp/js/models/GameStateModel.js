define(['backbone'], function (Backbone) {

    var GameStateModel = Backbone.Model.extend({
        url: undefined,

        defaults: {
            state: 'NONE'
        },

        updateUrl: function () {
            this.url = '/game/state/'+this.get('gameId')+'/'+this.get('playerId');
        }
    });

    return GameStateModel;
});
