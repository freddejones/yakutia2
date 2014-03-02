define([
'backbone', 'underscore', 'text!templates/CreateGame.html', 'collections/FriendsCollection'],
function(Backbone, _, CreateGameTemplate, FriendsCollection) {

    var GameCreateModel = Backbone.Model.extend({
        url: '/game/create',
        defaults: {
            createdByPlayerId: null,
            gameName: '',
            invites: {}
        }
    });

    var InvitedPlayerModel = Backbone.Model.extend({});
    var FriendModel = Backbone.Model.extend({});
    var GamePlayersCollection = Backbone.Collection.extend({
        model: InvitedPlayerModel
    });

    var CreateGameView = Backbone.View.extend({

        el: "#bodyContainer",

        events: {
            "keyup #gameName" : "handleKeyUp",
            "click #createNewGame" : "createNewGame",
            'click .InviteToGame' : "addGamePlayer",
            'click .RemoveFromGame' : "removeGamePlayer"
        },

        initialize: function() {
            _.bindAll(this, 'addGamePlayer', 'handleKeyUp');
            this.template = _.template(CreateGameTemplate);
            this.model = new GameCreateModel();
            this.model.set('invites', new GamePlayersCollection());
            this.collection = new FriendsCollection();
            this.listenTo(this.collection, "add remove", this.render)
            this.listenTo(this.collection, "add remove", this.render)
//            this.render();
            this.collection.fetch({ url: '/friend/get/all/'+window.playerId });
        },
        render: function() {
            var textInput = $('#gameName').val();

            this.$el.html(this.template);
            if (!_.isNull(textInput)) {
                $('#gameName').val(textInput);
            }
            this.collection.each(function(player) {
                this.renderPossibleGamePlayers(player);
            }, this);

            this.model.get('invites').each(function(invitedModel) {
                this.renderGamePlayers(invitedModel);
            }, this);
            return this;
        },
        handleKeyUp: function() {
            var textInput = $('#gameName').val();
            if(_.isNull(textInput) || _.isEmpty(textInput)) {
                $('#createNewGame').attr('disabled', 'disabled');
            } else if (this.model.get('invites').size() > 0) {
                $('#createNewGame').removeAttr("disabled");
            }
        },
        createNewGame: function() {
            this.model.set('gameName', $('#gameName').val());
            this.model.set('createdByPlayerId', window.playerId);   // TODO remove this playerId dependency
            this.model.save({}, {success: function() {
                window.router.navigate("#/listgames",{trigger:true});
            }});
        },
        renderPossibleGamePlayers: function(player) {
            $('#inviteToGame').append('<tr><td>'+player.get('name')+'</td>'
                +'<td>'
                +'<button value="'+player.get('id')+'" type="button" class="btn btn-primary InviteToGame">Invite to game</button>'
                +'</td>'
                +'</tr>');
        },
        renderGamePlayers: function(invitedModel) {
            console.log('adding tjohoo');
            $('#gamePlayers').append('<tr><td>'+invitedModel.get('name')+'</td>'
                +'<td>'
                +'<button value="'+invitedModel.get('id')+'" type="button" class="btn btn-primary RemoveFromGame">Remove player</button>'
                +'</td>'
                +'</tr>');
        },
        addGamePlayer: function(e) {
            var invitedPlayerId = $(e.currentTarget).attr("value");
            var invitedModel = new InvitedPlayerModel();
            invitedModel.set('id', invitedPlayerId);
            invitedModel.set('name', this.collection.get(invitedPlayerId).get('name'));
            this.model.get('invites').add(invitedModel);
            this.collection.remove(invitedPlayerId);
            console.log(JSON.stringify(this.model));
            this.handleKeyUp();
        },
        removeGamePlayer: function(e) {
            var gamePlayerId = $(e.currentTarget).attr("value");
            var friendModel = new FriendModel();
            friendModel.set('id', gamePlayerId);
            console.log(JSON.stringify(this.model.get('invites').get(gamePlayerId).get('name')));
            friendModel.set('name', this.model.get('invites').get(gamePlayerId).get('name'));
            this.model.get('invites').remove(gamePlayerId);
            this.collection.add(friendModel);
            this.handleKeyUp();
        },
        close: function() {
            this.remove();
            this.unbind();
        }

    });
    return CreateGameView;
});