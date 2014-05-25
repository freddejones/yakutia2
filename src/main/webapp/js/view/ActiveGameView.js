define(['backbone',
    'underscore',
    'kinetic',
    'text!templates/ActiveGameView.html',
    'assets/MapDefinitions',
    'view/LandAreaView',
    'collections/TerritoryModelCollection',
    'models/GameStateModel',
    'jqueryCookie'],
function(Backbone, _,
         Kinetic,
         GameMapTemplate,
         MapDefinitions,
         LandAreaView,
         TerritoryModelCollection,
         GameStateModel,
         $Cookie) {

    var ActiveGameView = Backbone.View.extend({

        el: "#bodyContainer",

        initialize: function() {
            _.bindAll(this, 'renderSubModel');

            this.model = new GameStateModel({
                playerId: $.cookie("yakutiaPlayerId"),
                gameId: $.cookie("yakutiaGameId")
            });
            this.updateState();

            window.App.vent.on('Statemodel::update', this.updateState, this);
            this.collection = new TerritoryModelCollection();
        },
        renderSubModel: function(model) {
            if (!(model.id.toLowerCase() === 'unassignedland')) {
                model.set('stage', this.stage);
                model.set('layer', this.layer);
                model.set('stateModel', this.model);
                new LandAreaView({model: model});
            }
        },
        updateState: function() {
            var self = this;
            this.model.fetch({
                success: function(model) {
                    var gameState = model.get('state');
                    self.renderGameState(gameState);
                    if (gameState === 'ATTACK') {
                        $("#nextActionButton", self.el).removeClass("disabled");
                    } else if (gameState === 'NO_TURN') {

                    } else if (gameState === 'GAME_FINISHED') {
                        console.log("game was finished");
                    }
                }
            });
        },
        renderGameState: function (state) {
            $("#currentState", self.el).text(state);
        },
        render: function() {
            var self = this;
            this.template = _.template(GameMapTemplate);
            this.$el.html(this.template(this.model));

            this.stage = new Kinetic.Stage({
                container: 'gamemap',
                width: 800,
                height: 600
            });

            this.layer = new Kinetic.Layer();
            this.stage.add(this.layer);

            this.collection.fetch({
                url: '/game/get/'+self.model.get("playerId")+'/game/'+self.model.get("gameId"),
                success: function(models) {
                    _.each(models.models, function(model) {
                        self.renderSubModel(model);
                    });
                }
            });

            return this;
        },
        close: function() {
            this.remove();
            this.unbind();
        }

    });
    return ActiveGameView;
});