define([
    'backbone',
    'models/TerritoryModel',
    'assets/MapDefinitions'
], function (Backbone, TerritoryModel, MapDefinitions) {

    var GameStateCollection = Backbone.Collection.extend({
        model: TerritoryModel,
        parse: function(response){
            for (var i=0; i< response.length; i++) {
                var territory = response[i].landName.toLowerCase();
                this.push(new TerritoryModel({
                    drawData: MapDefinition.territories[territory],
                    id: territory,
                    units: response[i].units,
                    ownedByPlayer: response[i].ownedByPlayer
                }));
            }
            return this.models;
        }
    });

    return GameStateCollection;
});
