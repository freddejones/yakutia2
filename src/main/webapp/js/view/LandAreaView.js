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

    var LandAreaView = Backbone.View.extend({
        initialize: function() {
            var self = this;
            var isOwnedByPlayer = this.model.get('ownedByPlayer');
            var color = isOwnedByPlayer ? 'green' : 'red';
            this.territory = this.getTerritoryObject(color);

            this.model.get('layer').add(this.territory);
            this.tooltip = this.getTooltip(this.model);
            this.model.get('layer').add(this.tooltip);

            this.currentStateModel = this.model.get('stateModel');
            this.currentStateModel.attackModel = new AttackModel();

            if (isOwnedByPlayer) {
                this.territory.on('mouseover', function() {
                    this.setFill('blue');
                    self.model.get('layer').draw();
                });
                this.territory.on('mouseout', function() {
                    this.setFill(color);
                    self.model.get('layer').draw();
                });

                this.territory.on('click', function() {
                    if (self.currentStateModel.get('state') === 'PLACE_UNITS') {
                        var placeUnitModel = new PlaceUnitModel();
                        placeUnitModel.save({
                            gameId: self.currentStateModel.get('gameId'),
                            playerId: self.currentStateModel.get('playerId'),
                            territory: self.model.get('id'),
                            numberOfUnits: 1
                            },{
                          success: function() {
                              console.log('unit placed');
                              self.model.set('units', self.model.get('units')+1);
                              self.tooltip.destroy();
                              self.tooltip = self.getTooltip(self.model);
                              self.model.get('layer').add(self.tooltip);
                              self.model.get('layer').draw();
                              self.currentStateModel.fetch({
                                  url: '/game/state/'+window.gameId+'/'+window.playerId
                              });
                          }
                        });
                    } else if (self.currentStateModel.get('state') === 'ATTACK') {
//                        self.attackModel.set('territoryAttackSrc', self.model.get('id'));
//                        self.attackModel.set('attackingNumberOfUnits', self.model.get('units'));
//                        self.attackModel.set('gameId', window.gameId);
//                        self.attackModel.set('playerId', window.playerId);
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
                this.territory.on('click', function() {
                    if (self.currentStateModel.get('state') === 'ATTACK') {
//                        self.currentStateModel.attackModel = _.extend(self.currentStateModel.attackModel, {territoryAttackDest: self.model.get('id')});
                        self.currentStateModel.attackModel.set('territoryAttackDest', self.model.get('id'));
                        self.currentStateModel.attackModel.save({}, {
                            success: function() {
                                console.log("tomtefräsattacken")
                                // TODO add refresh of state +
                                // trigger event to other destination territory
                            }
                        });
                    }
                  });
            }
            this.render();
        },
        getTerritoryObject: function (color) {
            return new Kinetic.Rect({
                width: 80,
                height: 50,
                x: this.model.get('drawData').x,
                y: this.model.get('drawData').y,
                //data: this.model.get('drawData').path,
                fill: color
                //scale: {x:1, y:1}
            });
        },
        render: function() {
            this.model.get('layer').draw();
            return this;
        },
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
                fill: 'white',
            }));

            return tooltip;
        },
        clickEvent: function(landId) {
            var stateModel = this.model.get('stateModel');
            if (stateModel.get('state') === 'PLACE_UNITS') {
                stateModel.set('placeUnitUpdate', {numberOfUnits: 1, landArea: landId});
                stateModel.save({}, {
                    url: '/game/state/update/',
                    success: function() {
                        console.log('unit placed');
                    }
                });
            }
        },
        close: function() {
            this.remove();
            this.unbind();
        }

    });
    return LandAreaView;
});