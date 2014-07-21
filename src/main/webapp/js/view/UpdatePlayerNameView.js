define(['backbone',
        'jquery',
        'underscore',
        'text!templates/UpdatePlayerNameView.html'],
    function(Backbone, $,  _, UpdatePlayerNameViewTemplate) {

        var UpdatePlayer = Backbone.Model.extend({
            url: "/player/update/name",
            defaults: {
                playerName: "",
                playerId: null
            }
        });

        var UpdatePlayerNameView = Backbone.View.extend({

            el: "#modalPlaceHolder",

            events: {
                "click #submit" : "updateName"
            },

            initialize: function() {
                this.model = new UpdatePlayer();
                this.model.set("playerId",this.options.playerId);
            },

            render: function() {
                this.template = _.template(UpdatePlayerNameViewTemplate);
                this.$el.html(this.template(this));
                $("#loginModal", this.$el).modal();
                return this;
            },

            updateName: function () {
                var self = this;
                this.model.set('playerName', $('#yakutia-playername', this.$el).val());
                this.model.idAttribute = 'playerId';
                this.model.id = this.model.get('playerId');
                this.model.save(null, {
                    success: function (model, response) {
                        var $modalEl = $('#loginModal', self.$el);
                        $modalEl.on('hidden.bs.modal', function(){
                            window.App.router.navigate('/', {trigger: true});
                        }).modal('hide');
                    }
                });
            },

            close: function() {
                this.remove();
                this.unbind();
            }

        });
        return UpdatePlayerNameView;
    });
