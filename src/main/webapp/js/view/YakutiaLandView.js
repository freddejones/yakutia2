define(['backbone', 'underscore'], function(Backbone, _) {

    var YakutiaLand = Backbone.View.extend({
        tagName: 'area',

        events: {
            'mouseover': 'doHoover',
            'mouseout': 'resetWell'
        },

        initialize: function() {
            _.bindAll(this, 'render');
        },

        render: function() {
            this.$el.prop('shape', 'poly')
                .prop('coords', this.model.get('coords'))
                .prop('class', this.model.get('className'))
                .prop('id', this.model.get('id'))
                .prop('href', '#');
            return this;
        },

        doHoover: function() {
            if (this.model.get('ownedByPlayer') === true) {
                $('#landinfo').text("Landname: " + this.model.get('className')
                    + ' with number of units: ' + this.model.get('units'));
            } else {
                $('#landinfo').text("You are not allowed to view this");
            }
        },

        resetWell: function() {
            $('#landinfo').text("Hoover the map please")
        },
        close: function() {
            this.remove();
            this.unbind();
        }

    });

    return YakutiaLand;
});