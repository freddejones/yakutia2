define(['backbone', 'view/MainView', 'view/CreatePlayerView'],
function(Backbone, MainView, CreatePlayerView) {

    yakutia = {
        topMenuView: {},
        bodyView: {}
    };
//    var topMenuView = {};
//    var bodyMenyView = {};

    var init = function() {
        yakutia.topMenuView = new MainView();
        yakutia.bodyView = new CreatePlayerView();
    };

    init();

//    var MenuView = Backbone.View.extend({
//
//        el: "#mainMenu",
//
//        initialize: function() {
//            this.template = _.template(MenuTemplate);
//            this.render();
//        },
//        render: function() {
//            this.$el.html(this.template);
//            return this;
//        },
//
//    });
//    return MenuView;
});