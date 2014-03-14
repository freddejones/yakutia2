define([
    'collections/TerritoryModelCollection',
    'sinon'
], function(
    TerritoryModelCollection,
    sinon) {

        describe('TerritoryModelCollection', function() {

            it('should map result from back end to a territory model', function() {

                var server = sinon.fakeServer.create();
                server.respondWith("GET", "/game/get/1/game/1", [
                    200, {"Content-Type": "application/json"},
                    '[{"landName":"FINLAND","units":5,"ownedByPlayer":false}]'
                ]);
                server.autoRespond = true;

                // given
                var tmc = new TerritoryModelCollection();
                console.log(tmc);
                tmc.fetch({
                    url: '/game/get/1/game/1'
                });

                server.respond();

                console.log(tmc.models[0]);
//                waitsFor(function () {
//                    return server.queue.length === 0;
//                }, "Wait for queue to empty", 5000);
//
//                runs(function () {
//                    // when
//                    console.log(tmc);
//                });


//                expect(false).toBe(true);
                // then

            });

        });

    });