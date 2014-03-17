define(['backbone',
    'underscore',
    'kinetic',
    'text!templates/ActiveGameView.html',
    'assets/MapDefinitions',
    'view/LandAreaView',
    'collections/TerritoryModelCollection',
    'models/GameStateModel'],
function(Backbone, _,
         Kinetic,
         GameMapTemplate,
         MapDefinitions,
         LandAreaView,
         TerritoryModelCollection,
         GameStateModel) {

    var ActiveGameView = Backbone.View.extend({

        el: "#bodyContainer",

        initialize: function() {
            _.bindAll(this, 'renderSubModel');

            this.model = new GameStateModel({
                playerId: window.playerId,
                gameId: window.gameId
            });
            this.model.updateUrl();
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
                    $("#currentState", self.el).text(model.get('state'));
                    console.log(JSON.stringify(model));
                    if (model.get('state') === 'ATTACK') {
                        $("#nextActionButton", self.el).removeClass("disabled");
                    }
                }
            });
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
                url: '/game/get/'+window.playerId+'/game/'+window.gameId,
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