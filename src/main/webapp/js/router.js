define([
'backbone',
'jquery',
'view/CreateGameView',
'view/ActiveGameView',
'view/ListMyGamesView',
'view/SearchFriendsView',
'view/MyFriendsView',
'view/UpdatePlayerNameView',
'view/WelcomeView'],
function(Backbone, $, CreateGameView, ActiveGameView, ListMyGamesView,
 SearchFriendsView, MyFriendsView, UpdatePlayerNameView,
    WelcomeView) {

    var YakutiaRouter = Backbone.Router.extend({

        routes: {
            "listgames" : "listGames",
            "updatePlayerName/:playerId" : "updatePlayerName",
            "createGames" : "createGames",
            "game/play/:gameId" : "playGame",
            "friends/search" : "searchFriend",
            "friends/my" : 'listMyFriends',
            "welcomePage" : "defaultRoute",
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
            //TODO add game id as options parameter
            this.attachNewBodyView(new ActiveGameView());
        },
        updatePlayerName: function (playerId) {
            this.closeBodyViewIfExists();
            this.attachNewBodyView(new UpdatePlayerNameView({playerId: playerId}));
        },
        defaultRoute: function() {
            this.closeBodyViewIfExists();
            this.attachNewBodyView(new WelcomeView());
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