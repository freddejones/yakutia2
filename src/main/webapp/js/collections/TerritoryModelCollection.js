define([
    'backbone',
    'models/TerritoryModel',
    'assets/MapDefinitions'
], function (Backbone, TerritoryModel, MapDefinitions) {

    var GameStateCollection = Backbone.Collection.extend({
        model: TerritoryModel,
        parse: function(response){
            console.log("test:" + response);
            for (var i=0; i< response.length; i++) {
                var landName = response[i].landName.toLowerCase();
                this.push(new TerritoryModel({
                    drawData: MapDefinitions.territories[landName],
                    className: landName,
                    id: landName,
                    units: response[i].units,
                    ownedByPlayer: response[i].ownedByPlayer
                }));
            }
            return this.models;
        }
    });

    return GameStateCollection;
});
