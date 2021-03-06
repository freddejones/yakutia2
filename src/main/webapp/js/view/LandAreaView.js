define(['backbone',
    'underscore',
    'kinetic',
    'assets/MapDefinitions',
    'models/TerritoryModel'],
function(Backbone, _, Kinetic, MapDefinitions, TerritoryModel) {

    var PlaceUnitModel = Backbone.Model.extend({
        urlRoot: '/game/state/perform/place/unit',
        defaults: {
        }
    });

    var AttackModel = Backbone.Model.extend({
        urlRoot: '/game/state/perform/attack/territory',
        defaults: {
        }
    });

    var TerritoryData = Backbone.Model.extend({
    })

    var LandAreaView = Backbone.View.extend({
        initialize: function() {
            this.territory = this.getTerritoryObject(this.model.get('ownedByPlayer'));
            this.model.get('layer').add(this.territory);
            this.tooltip = this.getTooltip(this.model);
            this.model.get('layer').add(this.tooltip);

            window.App.vent.on('Territory::attackUpdate:'+this.model.get('id'), this.refreshStuff, this);

            this.currentStateModel = this.model.get('stateModel');
            this.currentStateModel.attackModel = new AttackModel();
            this.render();
        },
        getTerritoryObject: function (isOwnedByPlayer) {
            var self = this;
            var color = isOwnedByPlayer ? 'green' : 'red';

            var territoryDrawObject = new Kinetic.Rect({
                width: 80,
                height: 50,
                x: this.model.get('drawData').x,
                y: this.model.get('drawData').y,
                fill: color
            });

            if (isOwnedByPlayer) {
                territoryDrawObject.on('mouseover', function() {
                    this.setFill('blue');
                    self.model.get('layer').draw();
                });
                territoryDrawObject.on('mouseout', function() {
                    this.setFill(color);
                    self.model.get('layer').draw();
                });

                territoryDrawObject.on('click', function() {
                    if (self.currentStateModel.get('state') === 'PLACE_UNITS') {
                        var placeUnitModel = new PlaceUnitModel();
                        placeUnitModel.save({
                            gameId: self.currentStateModel.get('gameId'),
                            playerId: self.currentStateModel.get('playerId'),
                            territory: self.model.get('id'),
                            numberOfUnits: 1
                        },{ success: function() {
                            window.App.vent.trigger('Statemodel::update');
                            self.refreshStuff();
                        }
                        });
                    } else if (self.currentStateModel.get('state') === 'ATTACK') {
                        self.currentStateModel.attackModel = new AttackModel({
                            territoryAttackSrc: self.model.get('id'),
                            attackingNumberOfUnits: self.model.get('units'),
                            gameId: window.gameId,
                            playerId: window.playerId
                        });
                    } else {
                        console.log(self.currentStateModel.get('state'));
                    }
                });
            } else {
                territoryDrawObject.on('mouseover', function() {
                    this.setFill('yellow');
                    self.model.get('layer').draw();
                });
                territoryDrawObject.on('mouseout', function() {
                    this.setFill(color);
                    self.model.get('layer').draw();
                });
                territoryDrawObject.on('click', function() {
                    if (self.currentStateModel.get('state') === 'ATTACK') {
                        self.currentStateModel.attackModel.set('territoryAttackDest', self.model.get('id'));
                        self.currentStateModel.attackModel.save({}, {
                            success: function() {
                                window.App.vent.trigger('Statemodel::update');
                                self.refreshStuff();
                                window.App.vent.trigger('Territory::attackUpdate:'+self.currentStateModel.attackModel.get('territoryAttackSrc'));
                            }
                        });
                    }
                });
            }
            return territoryDrawObject;
        },
        render: function() {
            this.model.get('layer').draw();
            return this;
        },
        reRenderTerritoryObject: function() {
            this.territory.destroy();
            this.territory = this.getTerritoryObject(this.model.get('ownedByPlayer'));
            this.model.get('layer').add(this.territory);
            this.tooltip.destroy();
            this.model.set('units', this.model.get('units'));
            this.tooltip = this.getTooltip(this.model);
            this.model.get('layer').add(this.tooltip);
            this.model.get('layer').draw();
        },
        refreshStuff: function() {
            var self = this;
            var url = '/game/state/territory/'+window.gameId+'/'+window.playerId+'/'+this.model.get('id');
            var territoryData = new TerritoryData();
            territoryData.fetch({
                url: url,
                success: function(model) {
                    self.model.set('ownedByPlayer', model.get('ownedByPlayer'));
                    self.model.set('units', model.get('units'));
                    self.reRenderTerritoryObject();
                }
            });
        },
//        updateStateModel: function() {
////            this.currentStateModel.fetch({
////                url: '/game/state/'+window.gameId+'/'+window.playerId
////            });
//
//            var self = this;
//            var url = '/game/state/territory/'+window.gameId+'/'+window.playerId+'/'+this.model.get('id');
//            var territoryData = new TerritoryData();
//            territoryData.fetch({
//                url: url,
//                success: function(model) {
//                    self.tooltip.destroy();
//                    self.model.set('units', model.get('units'));
//                    self.tooltip = self.getTooltip(self.model);
//                    self.model.get('layer').add(self.tooltip);
//                    self.model.get('layer').draw();
//                }
//            });
//        },
        getTooltip: function(model) {

            var tooltip = new Kinetic.Label({
                x: model.get('drawData').tooltip.x,
                y: model.get('drawData').tooltip.y,
                opacity: 0.75
            });

            tooltip.add(new Kinetic.Tag({
                fill: 'black',
                pointerDirection: 'down',
                pointerWidth: 10,
                pointerHeight: 10,
                lineJoin: 'round',
                shadowColor: 'black',
                shadowBlur: 10,
                shadowOffset: {x:10,y:20},
                shadowOpacity: 0.5
            }));

            tooltip.add(new Kinetic.Text({
                name: model.get('drawData').name,
                text: model.get('drawData').name + "\n" + model.get('units') + ' units',
                fontFamily: 'Calibri',
                fontSize: 12,
                padding: 3,
                fill: 'white'
            }));

            return tooltip;
        },
        close: function() {
            this.remove();
            this.unbind();
        }

    });
    return LandAreaView;
});