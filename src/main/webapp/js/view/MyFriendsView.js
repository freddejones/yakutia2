define(['backbone',
        'underscore',
        'text!templates/MyFriends.html',
        'collections/FriendsCollection',
        'jqueryCookie'],
function(Backbone, _, MyFriendsTemplate, FriendsCollection, $Cookie) {

    var FriendModel = Backbone.Model.extend({});

    var FriendCollectionsModel = Backbone.Model.extend({});

    var MyFriendsView = Backbone.View.extend({

        el: "#bodyContainer",

        events: {
            'click .AcceptFriend' : 'acceptInvite',
            'click .DeclineFriend' : 'declineInvite'
        },

        initialize: function() {
            this.template = _.template(MyFriendsTemplate);
            this.model = new FriendCollectionsModel();
            this.tomte = new FriendsCollection();
            this.invtesCollection = new FriendsCollection();
            this.tomte.fetch({ url: '/friend/accepted/'+ $.cookie("yakutiaPlayerId")});
            this.invtesCollection.fetch({ url: '/friend/invites/'+ $.cookie("yakutiaPlayerId")});
            this.listenTo(this.tomte, "change reset add remove", this.updatesForModel);
            this.listenTo(this.invtesCollection, "change reset add remove", this.updatesForModel);
            this.render();
        },
        updatesForModel: function() {
            console.log(this.tomte.size());
            this.model.set("accepts", this.tomte);
            this.model.set("invites", this.invtesCollection);
            this.render();
        },
        render: function() {
            this.$el.html(this.template);

            var accepts = this.model.get("accepts");
            if (accepts !== undefined) {
                accepts.each(function(object) {
                    $('#friendsForReals').append('<tr><td>'+object.get('name')+'</td></tr>');
                });
            }

            var invites = this.model.get("invites");
            if (invites !== undefined) {
                invites.each(function(object) {
                    $('#friendInvites').append('<tr><td>'+object.get('name')+'</td>'
                    +'<td>'
                        +'<button value="'+object.get('id')+'" type="button" class="btn btn-primary AcceptFriend">Accept</button>'
                    +'</td>'
                    +'<td>'
                        +'<button value="'+object.get('id')+'" type="button" class="btn btn-primary DeclineFriend">Decline</button>'
                    +'</td>'
                    +'</tr>');
                });
            }
            return this;
        },
        acceptInvite: function(e) {
            var self = this;
            var friendId = $(e.currentTarget).attr("value");
            var acceptModel = new FriendModel();
            acceptModel.set('playerId', $.cookie("yakutiaPlayerId"));
            acceptModel.set('friendId', friendId);
            acceptModel.save({},{url: '/friend/accept', success: function(model, response) {
                console.log(JSON.stringify(response));
                console.log(JSON.stringify(model));
                self.collection.remove(friendId);
                var updatedModel = new FriendModel();
                updatedModel.set('id', model.get('friendId'));
                updatedModel.set('name', model.get('playerName'));
                updatedModel.set('status', model.get('friendStatus'));
                console.log("model: " + JSON.stringify(updatedModel));
                self.collection.add(updatedModel);}
            })
        },
        declineInvite: function(e) {
            var self = this;
            var friendId = $(e.currentTarget).attr("value");
            var model = new FriendModel();
            model.set('playerId', $.cookie("yakutiaPlayerId"));
            model.set('friendId', friendId)
            model.save({
                playerId: window.playerId,
                friendId: friendId }, {url: '/friend/decline'})
                .complete(function() {
                    self.collection.remove(friendId)
                });
            console.log('decline friend invite');
        },
        close: function() {
            this.remove();
            this.unbind();
        }
    });

    return MyFriendsView;
});