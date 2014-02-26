define(['backbone'],
function(Backbone) {

    var FriendModel = Backbone.Model.extend({});

    var FriendsCollection = Backbone.Collection.extend({
        model: FriendModel,
        parse: function(response){
            for (var i=0; i<response.length; i++) {
                this.push(new FriendModel({
                    id: response[i].friendId,
                    name: response[i].playerName,
                    status: response[i].friendStatus
                }));
            }
            return this.models;
        }
    });

    return FriendsCollection;
});