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
                        '[{ "landName": "sweden"}]']);

                // given
                var tmc = new TerritoryModelCollection();
                tmc.fetch({
                    url: '/game/get/1/game/1'
                });

                // when
                console.log(tmc);

                // then

            });

        });

    });