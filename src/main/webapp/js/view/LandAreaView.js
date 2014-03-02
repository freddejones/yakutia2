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

    var LandAreaView = Backbone.View.extend({

        initialize: function() {
            _.bind(this, 'clickEvent');
            var self = this;
            var isOwnedByPlayer = this.model.get('ownedByPlayer');
            var color = isOwnedByPlayer ? 'green' : 'red';
            this.territory = new Kinetic.Rect({
                width: 80,
                height: 50,
                x: this.model.get('drawData').x,
                y: this.model.get('drawData').y,
                //data: this.model.get('drawData').path,
                fill: color,
                //scale: {x:1, y:1}
            });

            this.model.get('layer').add(this.territory);
            this.tooltip = this.getTooltip(this.model);
            this.model.get('layer').add(this.tooltip);

            this.currentStateModel = this.model.get('stateModel');
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
//                        placeUnitModel.set('gameId', self.currentStateModel.get('gameId'));
//                        placeUnitModel.set('playerId', self.currentStateModel.get('playerId'));
//                        placeUnitModel.set('territory', self.model.get('id'));
//                        placeUnitModel.set('numberOfUnits', 1);
//                      self.currentStateModel.set('placeUnitUpdate', {numberOfUnits: 1, landArea: self.model.get('id')});
                        console.log(JSON.stringify({
                            gameId: self.currentStateModel.get('gameId'),
                            playerId: self.currentStateModel.get('playerId'),
                            territory: self.model.get('id'),
                            numberOfUnits: 1
                        }));
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
                          }
                        });
                    } else if (self.currentStateModel.get('state') === 'ATTACK') {
                        self.currentStateModel.set('attackActionUpdate', {territoryAttackSrc: self.model.get('id'),
                            attackingNumberOfUnits: self.model.get('units')});
                    } else {
                        console.log(self.currentStateModel.get('state'));
                    }
                });
            } else {
                this.territory.on('click', function() {
                    if (self.currentStateModel.get('state') === 'ATTACK') {
                        _.extend(self.currentStateModel.get('attackActionUpdate'), {territoryAttackDest: self.model.get('id')});
                        self.currentStateModel.save({}, {
                              url: '/game/state/update/',
                              success: function(stuff) {
                                  console.log('attack was made');
//                                  self.model.set('units', self.model.get('units')+1);
//                                  self.territory.fill('yellow');
                                  self.rebuild = true;
                                  self.tooltip.destroy();
                                  self.territory.destroy();
//                                  self.tooltip = self.getTooltip(self.model);
//                                  self.model.get('layer').add(self.tooltip);
//                                  self.model.get('layer').draw();
                              }
                          });
                        }
                  });
            }
            this.render();
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
            //this.render();
        },
        close: function() {
            this.remove();
            this.unbind();
        }

    });
    return LandAreaView;
});