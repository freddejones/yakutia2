define([
    'collections/TerritoryModelCollection',
    'sinon'
], function(
    TerritoryModelCollection,
    sinon) {

        describe('TerritoryModelCollection', function() {

            it('should map result from back end to a territory model', function() {

                var server = sinon.fakeServer.create();

                server.respondWith("GET", "/game/get/1/game/1",
                    [200, { "Content-Type": "application/json" },
                        '[{ landName: "sweden", units: 1, ownedByPlayer: true}]']);

                // given
                var tmc = new TerritoryModelCollection();
                console.log(tmc);
                tmc.fetch({
                    url: '/game/get/1/game/1',
                    success: function(models, response) {   //TODO remove response?
                        console.log("fräs");
                    },
                    error: function(model, error) {
                        console.log("fräs2: " + error.constructor);
                    }
                });

                server.respond();

                // when
                console.log(tmc);
                expect(false).toBe(true);
                // then

            });

        });

    });