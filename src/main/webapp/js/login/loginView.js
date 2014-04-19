define(['backbone',
        'jquery',
        'underscore',
        'text!login/LoginView.html'],
    function(Backbone, $,  _, LoginViewTemplate) {

        var LoginView = Backbone.View.extend({

            el: "#modalPlaceHolder",

            initialize: function() {

            },

            render: function() {
                var self = this;
                this.template = _.template(LoginViewTemplate);
                this.$el.html(this.template(this));
                $("#loginModal", this.$el) .modal();
//                this.$el.show();
//                this.$el.modal("show");
//                $("#modalContent", this).modal("show");
//                this.$el("#modalContent").modal("show");
//                this.stage = new Kinetic.Stage({
//                    container: 'gamemap',
//                    width: 800,
//                    height: 600
//                });
//
//                this.layer = new Kinetic.Layer();
//                this.stage.add(this.layer);
//
//                this.collection.fetch({
//                    url: '/game/get/'+window.playerId+'/game/'+window.gameId,
//                    success: function(models) {
//                        _.each(models.models, function(model) {
//                            self.renderSubModel(model);
//                        });
//                    }
//                });

                return this;
            },

            close: function() {
                this.remove();
                this.unbind();
            }

        });
        return LoginView;
    });
