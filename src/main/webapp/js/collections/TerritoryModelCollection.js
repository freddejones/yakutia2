define([
    'backbone',
    'models/TerritoryModel'
], function (Backbone, TerritoryModel) {

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

    return GameStateCollection;
});
