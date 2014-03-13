define(['backbone',
    'underscore',
    'kinetic',
    'text!templates/ActiveGameView.html',
    'assets/MapDefinitions',
    'models/TerritoryModel',
    'view/LandAreaView'],
function(Backbone, _, Kinetic, GameMapTemplate, MapDefinitions, TerritoryModel, LandAreaView) {

    //TODO: refactor this:
    // extract model
    // remove window binding stuff when login is fixed
    var YakutiaGameStateModel = Backbone.Model.extend({
        defaults: {
            state: 'NONE',
            playerId: window.playerId,
            gameId: window.gameId
        }
    });

    // TODO: refactor
    // Extract to collection own js file
    //
    var GameStateCollection = Backbone.Collection.extend({
        model: TerritoryModel,
        parse: function(response){

            for (var i=0; i< response.length; i++) {
                var landName = response[i].landName.toLowerCase();
                this.push(new TerritoryModel({
                    drawData: MapDefinition.territories[landName],
                    className: landName,
                    id: landName,
                    units: response[i].units,
                    ownedByPlayer: response[i].ownedByPlayer,
                }));
            }
            return this.models;
        }
    });

    var ActiveGameView = Backbone.View.extend({

        el: "#bodyContainer",

        initialize: function() {
            _.bindAll(this, 'renderSubModel');
            this.template = _.template(GameMapTemplate);
            this.model = new YakutiaGameStateModel();
            this.model.fetch({
                url: '/game/state/'+window.gameId+'/'+window.playerId
            });
            window.App.vent.on('Statemodel::update', this.updateState, this);
            this.collection = new GameStateCollection();
            var self = this;
            this.collection.fetch({
                url: '/game/get/'+window.playerId+'/game/'+window.gameId,
                success: function(models, response) {   //TODO remove response?
                    self.renderSubModel();
                }
            });
            this.listenTo(this.collection, 'change', function() {
                this.collection.fetch({
                    url: '/game/get/'+window.playerId+'/game/'+window.gameId,
                    success: function(models, response) {

                    }
                });
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
        testForUnitStuff: function() {
            this.model.set('test', 'apa');
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