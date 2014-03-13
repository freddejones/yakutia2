define(
    ['backbone'], function(Backbone) {

    var YakutiaModel = Backbone.Model.extend({
        defaults: {
            drawData: '',
            id: '',
            units: -1,
            ownedByPlayer: false,
            stage: ''
        },

        getTerritoryName: function () {
            return this.get('id');
        }
    });

    return YakutiaModel;
});