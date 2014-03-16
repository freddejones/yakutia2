define([
    'collections/TerritoryModelCollection',
    'sinon'
], function(
    TerritoryModelCollection,
    sinon) {

        describe('TerritoryModelCollection', function() {

            var server, tmc;

            beforeEach(function () {
                server = sinon.fakeServer.create();
                tmc = new TerritoryModelCollection();
            });

            afterEach(function () {
            });

            it('should get two models back from back end', function() {
                // given
                server.respondWith("GET", "/game/get/1/game/1", [
                    200, {"Content-Type": "application/json"},
                    '[{"landName":"FINLAND","units":5,"ownedByPlayer":false},' +
                        '{"landName":"SWEDEN","units":5,"ownedByPlayer":true}]'
                ]);
                server.autoRespond = true;

                // when
                tmc.fetch({
                    url: '/game/get/1/game/1'
                });

                server.respond();

                // given
                expect(tmc.models.length).toBe(2);
            });

            it('should map result from back end to a territory model', function () {
                // given
                server.respondWith("GET", "/game/get/1/game/2", [
                    200, {"Content-Type": "application/json"},
                    '[{"landName":"FINLAND","units":5,"ownedByPlayer":false}]'
                ]);
                server.autoRespond = true;

                // when
                tmc.fetch({
                    url: '/game/get/1/game/2'
                });
                server.respond();

                // given
                expect(tmc.models[0].get('id')).toBe("finland");
                expect(tmc.models[0].get('units')).toBe(5);
                expect(tmc.models[0].get('ownedByPlayer')).toBe(false);
            });

        });

    });