define([
'backbone', 'underscore', 'text!templates/ListMyGames.html', 'router'],
function(Backbone, _, ListMyGamesTmpl, options, router) {

    var Game = Backbone.Model.extend({
        defaults: {
            id: 0,
            name: "",
            status: "",
            canStartGame: false,
            date: ""
        }
    });

    var GamesCollection = Backbone.Collection.extend({
        model: Game,
        parse: function(response){

            for (var i=0; i<response.length; i++) {
                this.push(new  Game({
                    id: response[i].id,
                    name: response[i].name,
                    status: response[i].status,
                    date: response[i].date,
                    canStartGame: response[i].canStartGame
                }));
            }
            return this.models;
        }
    });

    var ListMyGamesView = Backbone.View.extend({

        el: "#bodyContainer",

        events: {
            'click .StartGame' : 'startGame',
            'click .OpenGame' : 'openGame'
        },

        initialize: function() {
            _.bindAll(this, 'render', 'startGame', 'openGame');
            this.template = _.template(ListMyGamesTmpl);
            this.model = Backbone.Model.extend({});
            var self = this;
            this.collection = new GamesCollection();
            this.listenTo(this.collection, "change reset add remove", this.render)
            this.collection.fetch({ url: '/game/get/'+window.playerId });
        },
        startGame: function(e) {
            var self = this;
            var gameId = $(e.currentTarget).attr("value");
            this.collection.get(gameId).save({status: 'ONGOING'}, {url: '/game/start/'+gameId});
        },
        openGame: function(e) {
            var gameId = $(e.currentTarget).attr("value");
            window.gameId=gameId;
            window.App.router.navigate("#/game/play/"+gameId,{trigger:true});
        },
        render: function() {
            this.$el.html(this.template);
            console.log("render");
            this.collection.each( function(gameObject) {
                if (gameObject.get('status') === 'ONGOING') {
                    $("#activeGameTable").append(
                        '<tr><td>'+gameObject.get('name')+'</td>'
                        +'<td>'+gameObject.get('status')+'</td>'
                        +'<td>'+gameObject.get('date')+'</td>'
                        +'<td>'
                        +'<button value="'+gameObject.get('id')+'" type="button" class="btn btn-primary OpenGame">Go!</button>'
                        +'</td>'
                        +'</tr>');
                } else if (gameObject.get('canStartGame') === true) {
                    $("#nonActiveGameTable").append('<tr><td>'+gameObject.get('name')+'</td>'
                                            +'<td>'+gameObject.get('status')+'</td>'
                                            +'<td>'+gameObject.get('date')+'</td>'
                                            +'<td>'
                                            +'<button value="'+gameObject.get('id')+'" type="button" class="btn btn-primary StartGame">Start game</button>'
                                            +'</td>'
                                            +'</tr>');
                }
                else {
                    $("#nonActiveGameTable").append('<tr><td>'+gameObject.get('name')+'</td>'
                        +'<td>'+gameObject.get('status')+'</td>'
                        +'<td>'+gameObject.get('date')+'</td>'
                        +'<td>NO BUTTON HERE</td>'
                        +'</tr>');
                }
            });
            return this;
        },
        close: function() {
            this.remove();
            this.unbind();
        }
    });
    return ListMyGamesView;
});