define([
        'backbone',
        'underscore',
        'jqueryCookie',
        'text!templates/WelcomeView.html'],
    function(Backbone, _,
             $Cookie,
             WelcomeViewTemplate) {

        var PlayerObj = Backbone.Model.extend({
            urlRoot: 'player/fetch',

            defaults: {
                playerId: null,
                playerName: "...",
                email: null
            }
        });

        var WelcomeView = Backbone.View.extend({

            el: "#bodyContainer",

            initialize: function() {
                var self = this;
                this.template = _.template(WelcomeViewTemplate);
                var playerId = $.cookie("yakutiaPlayerId");
                this.model = new PlayerObj({id: playerId});
                this.model.fetch({
                    success: function () {
                        self.render();
                    }
                });
            },
            render: function() {
                this.$el.html(this.template(this.model.toJSON()));
                return this;
            },
            close: function() {
                this.remove();
                this.unbind();
            }
        });
        return WelcomeView;
    });