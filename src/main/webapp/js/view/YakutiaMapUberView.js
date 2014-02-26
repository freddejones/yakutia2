define(['backbone', 'lib/jquery.imagemapster2', 'underscore', 'view/YakutiaLandView'],
function(Backbone, ImageMapster, _, YakutiaLandView) {

    // Some default settings for the imagemap
    var defaultMapSettings = {
        stroke: true,
        strokeColor: '000000',
        fillColor: 'FFAA00',
        singleSelect: true,
        render_highlight: {
            fillColor: '2aff00',
            fillOpacity: 0.4
        },
        render_select: {
            fillOpacity: 0.7
        },
        mapKey: 'data-state',
        areas: [{
            key: 'NOT_YOURS',
            selected: false,
            isSelectable: false,
        },
        {
            key: 'YOURS',
            selected: true,
            isSelectable: false,
            highlight: false,
        }
        ]
    };

    // The poly coords settings
    var YakutiaMapCoords = function(land) {
        if (land === 'SWEDEN') {
            return "209,130,196,131,158,158,144,175,142,225,157,254,220,263,257,266,289,237,291,219,279,208,259,195,246,191,246,180,263,169,273,151,272,133,266,112,245,109,242,113,229,115,220,119,217,129,214,129";
        } else if (land === 'FINLAND') {
            return "95,52,85,62,75,75,63,100,64,118,79,127,98,134,99,153,92,167,92,187,121,204,143,205,145,177,152,164,177,143,191,137,213,129,218,127,227,112,227,99,214,84,192,78,161,78,155,81,135,80,121,56,99,51";
        } else if (land === 'NORWAY') {
            return "357,31,327,48,291,78,280,86,262,107,263,112,278,120,278,139,275,144,275,151,270,160,271,167,274,173,281,176,291,188,295,186,298,175,305,168,312,171,357,207,348,192,356,170,381,147,383,143,380,131,360,106,360,84,394,77,418,64,420,48,412,34,402,26,382,24,365,27,360,29";
        }
        return '';
    };

    var YakutiaModel = Backbone.Model.extend({});

    var YakutiaColl = Backbone.Collection.extend({
        model: YakutiaModel,
        parse: function(response){

            for (var i=0; i< response.length; i++) {
                console.log(response[i].landName);
                var landName = response[i].landName;
                this.push(new  YakutiaModel({
                    coords: YakutiaMapCoords(landName),
                    className: landName,
                    id: landName,
                    units: response[i].units,
                    ownedByPlayer: response[i].ownedByPlayer,
                }));
            }
            return this.models;
        }
    });

    var YakutiaGameStateModel = Backbone.Model.extend({
        defaults: {
            state: 'NONE',
            playerId: window.playerId,
            gameId: window.gameId
        }
    });

    var YakuitaMapView = Backbone.View.extend({
        tagName: 'map',
        id: "test",
        name: 'gamemap',

        events: {
            'click area': 'clickedOnArea'
        },

        initialize: function() {
            _.bindAll(this, 'render', 'clickedOnArea', 'clickPlaceUnitButton');
            this.collection = new YakutiaColl();
            var self = this;
            console.log(this.model.get('stateModel').get('state'));
            this.model = new YakutiaGameStateModel();
            this.model.fetch({
                url: '/game/state/'+window.gameId+'/'+window.playerId
            });
            this.collection.reset({});
            this.collection.fetch({
                    url: '/game/get/'+window.playerId+'/game/'+window.gameId
                }).complete(function() {
                self.render();
            });
            $('#placeUnitButton').bind('click', this.clickPlaceUnitButton);
            this.listenTo(this.model, 'change', this.render);
        },
        render: function() {
            if (this.model.get('state') === 'PLACE_UNITS') {
                $('#placeUnitButton').slideDown();
            } else if (this.model.get('state') === 'ATTACK') {
                $('#placeUnitButton').slideUp();
                $('#attackButton').slideDown();
            } else if (this.model.get('state') === 'MOVE_UNITS') {

            }

            $('#yakutia-map-container').append(this.el);
            this.$el.prop('name', this.name);   // <- wtf is this?

            this.collection.each(function(yakutiaLandModel) {
                var yakutiaLandView = new YakutiaLandView({ model: yakutiaLandModel });
                this.$el.append(yakutiaLandView.render().el);

                // Set logic for imagemapster here
                if (yakutiaLandModel.get('ownedByPlayer') === true) {
                    $('#'+yakutiaLandModel.get('id')).attr('data-state','YOURS');
                } else {
                    $('#'+yakutiaLandModel.get('id')).attr('data-state','NOT_YOURS');
                }
            }, this);

            $('img').mapster(defaultMapSettings);
        },
        clickedOnArea: function(e) {
            var self = this;
            console.log('clicked on area');
            if (this.model.get('state') === 'PLACE_UNITS') {
                var land = $(e.currentTarget).attr("class");
                this.model.set('placeUnitUpdate', {numberOfUnits: 1, landArea: land});
                this.model.save({}, {
                    url: '/game/state/update/',
                    success: function() {
                        var numberOfUnits = self.collection.get(land).get('units');
                        self.collection.get(land).set('units', numberOfUnits+1);
                    }
                });
            }
        },
        clickPlaceUnitButton: function() {
            if (this.model.get('state') === 'PLACE_UNITS') {
                console.log('what to do with this one');
            }
        },
        close: function() {
            this.remove();
            this.unbind();
        }

    });

    return YakuitaMapView;

});