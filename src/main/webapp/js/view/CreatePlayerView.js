define([
'backbone', 'underscore', 'text!templates/CreatePlayerTemplate.html'],
function(Backbone, _, CreatePlayerTemplate) {

    var CreatePlayerModel = Backbone.Model.extend({
        url: 'player/create',

        defaults: {
            playerName: null,
            email: null
        }
    });

    var CreatePlayerView = Backbone.View.extend({

        el: "#bodyContainer",

        events: {
            "keyup #gameName" : "handleKeyUp",
            "click #createPlayerButton" : "createNewPlayer",
            "click #createAScenario" : "createScenario"
        },

        initialize: function() {
            this.template = _.template(CreatePlayerTemplate);
            this.model = new CreatePlayerModel();
            this.tomte = 0;
            this.render();
        },
        render: function() {
            this.$el.html(this.template(this.model.attributes));
            return this;
        },
        createNewPlayer: function() {
            this.model.set('playerName', $('#playerName').val());
            this.model.set('email', $('#email').val());
            this.model.save(null, {
                success: function (model, response) {
                    console.log(response);
                    window.playerId=response;
                }
            });
        },
        close: function() {
            this.remove();
            this.unbind();
        }
    });
    return CreatePlayerView;
});