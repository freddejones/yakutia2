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
                console.log(response[i].landName);
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
            this.collection = new GameStateCollection();
            //this.collection.listenTo(this.collection, "change reset add remove", this.tomteStuff());
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
                    var territoryView = new LandAreaView({model: model});
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
            var layerHUD;
            var layerMap;



            //var tooltip = this.getTooltip(this.tomte);

            var group = new Kinetic.Group({
                x: 100,
                y: 50,
            });

            //group.add(pathExample);
            //group.add(tooltip);

            //layer.add(group);
            this.stage.add(this.layer);

            console.log("Done rendering active game view");
            return this;
        },
        close: function() {
            this.remove();
            this.unbind();
        }

    });
    return ActiveGameView;
});