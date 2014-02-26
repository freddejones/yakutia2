define(['backbone', 'underscore', 'text!templates/FriendSearch.html'],
function(Backbone, _, FriendSearchTemplate) {

    var FriendModel = Backbone.Model.extend({});
    var FriendInviteModel = Backbone.Model.extend({});

    var FriendsCollection = Backbone.Collection.extend({
            model: FriendModel,
            parse: function(response){
                for (var i=0; i<response.length; i++) {
                    this.push(new FriendModel({
                        id: response[i].playerId,
                        name: response[i].playerName,
                        email: response[i].email
                    }));
                }
                return this.models;
            }
        });

    var ActiveGameView = Backbone.View.extend({

        el: "#bodyContainer",

        events: {
            'click .FriendRequest' : 'sendFriendRequest'
        },

        initialize: function() {
            this.template = _.template(FriendSearchTemplate);
            this.collection = new FriendsCollection();
            this.render();
            this.listenTo(this.collection, "change reset add remove", this.render)
            this.collection.fetch({ url: '/friend/non/friends/'+window.playerId });
        },
        render: function() {
            this.$el.html(this.template);
            this.collection.each(function(friendObject) {
                this.renderFriends(friendObject);
            }, this);
            return this;
        },
        renderFriends: function(friendModel) {
            $('#nonFriendPlayers').append('<tr><td>'+friendModel.get('name')+'</td>'
                +'<td>'
                +'<button value="'+friendModel.get('id')+'" type="button" class="btn btn-primary FriendRequest">Invite</button>'
                +'</td>'
                +'</tr>');
        },
        sendFriendRequest: function(e) {
            var self = this;
            var friendId = $(e.currentTarget).attr("value");
            var invite = new FriendInviteModel();
            console.log("Trying to save " + friendId);
            invite.save({
                playerId: window.playerId,
                friendId: friendId }, {url: '/friend/invite'})
                .complete(function() {
                    self.collection.remove(friendId)
                });
        },
        close: function() {
            this.remove();
            this.unbind();
        }
    });

    return ActiveGameView;
});