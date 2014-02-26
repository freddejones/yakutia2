define(['backbone', 'underscore'],
function(Backbone, _) {

    var YakutiaModel = Backbone.Model.extend({
        defaults: {
            drawData: '',
            id: '',
            units: -1,
            ownedByPlayer: -1,
            stage: ''
        }
    });

    return YakutiaModel;
});