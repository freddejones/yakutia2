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
            this.model.fetch({});

            window.App.vent.on('Statemodel::update', this.updateState, this);
            this.collection = new TerritoryModelCollection();
            var self = this;
            this.collection.fetch({
                url: '/game/get/'+window.playerId+'/game/'+window.gameId,
                success: function(models, response) {   //TODO remove response?
                    self.renderSubModel();
                }
            });
        },
        renderSubModel: function() {
            var self = this;

            _.each(this.collection.models, function(model) {
                if (!(model.id.toLowerCase() === 'unassignedland')) {
                    model.set('stage', self.stage);
                    model.set('layer', self.layer);
                    model.set('stateModel', self.model);
                    new LandAreaView({model: model});
                }
            });
        },
        updateState: function() {
            var self = this;
            this.model.fetch({
                url: '/game/state/'+window.gameId+'/'+window.playerId,
                success: function(model) {
                    $("#currentState", self.el).text(model.get('state'));
                    console.log("updated state?");
                    console.log(JSON.stringify(model));
                    if (model.get('state') === 'ATTACK') {
                        $("#nextActionButton", self.el).removeClass("disabled");
                    }
                }
            });
        },
        render: function() {
            this.template = _.template(GameMapTemplate);
            this.$el.html(this.template(this.model));
            this.stage = new Kinetic.Stage({
                container: 'gamemap',
                width: 800,
                height: 600
            });

            this.layer = new Kinetic.Layer();
            this.stage.add(this.layer);

            this.updateState();
            return this;
        },
        close: function() {
            this.remove();
            this.unbind();
        }

    });
    return ActiveGameView;
});