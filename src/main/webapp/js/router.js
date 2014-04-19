define([
'backbone',
'jquery',
'view/CreateGameView',
'view/ActiveGameView',
'view/ListMyGamesView',
'view/CreatePlayerView',
'view/SearchFriendsView',
'view/MyFriendsView',
'login/LoginView'],
function(Backbone, $, CreateGameView, ActiveGameView, ListMyGamesView,
CreatePlayerView, SearchFriendsView, MyFriendsView, LoginView) {

//    var activeView = {};
//    var topView = {};

    var YakutiaRouter = Backbone.Router.extend({

        routes: {
            "listgames" : "listGames",
            "createGames" : "createGames",
            "game/play/:gameId" : "playGame",
            "friends/search" : "searchFriend",
            "friends/my" : 'listMyFriends',
            '*path':  'defaultRoute'
        },
        listGames: function() {
            this.closeBodyViewIfExists();
            this.attachNewBodyView(new ListMyGamesView());
        },
        createGames: function() {
            this.closeBodyViewIfExists();
            this.attachNewBodyView(new CreateGameView());
        },
        playGame: function(gameId) {
            this.closeBodyViewIfExists();
            this.attachNewBodyView(new ActiveGameView());
        },
        defaultRoute: function() {
            //TODO go to login and after that it is handled separately
            this.closeBodyViewIfExists();
            this.attachNewBodyView(new LoginView());
//            this.attachNewBodyView(new CreatePlayerView());
        },
        searchFriend: function() {
            this.closeBodyViewIfExists();
            this.attachNewBodyView(new SearchFriendsView());
        },
        listMyFriends: function() {
            this.closeBodyViewIfExists();
            this.attachNewBodyView(new MyFriendsView());
        },
        closeBodyViewIfExists: function() {
            if (yakutia.bodyView){
                yakutia.bodyView.close();
                $('#superContainer').append('<div id="bodyContainer" class="container"></div>')
            }
        },
        attachNewBodyView: function(view) {
            yakutia.bodyView = view;
            yakutia.bodyView.render();
        },
    });

    return YakutiaRouter;
});