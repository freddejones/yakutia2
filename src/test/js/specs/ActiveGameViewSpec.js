define([
    'view/ActiveGameView',
    'sinon'
], function(
    ActiveGameView,
    sinon) {

    describe('ActiveGameView', function() {

        var server;

        beforeEach(function () {
            server = sinon.fakeServer.create();
        });

        afterEach(function () {
        });

        it('should initialize it self with current state', function() {
            // given
            window.gameId = 1;
            window.playerId = 1;
            server.respondWith("GET", "/game/get/1/game/1", [
                200, {"Content-Type": "application/json"},
                '[{"landName":"FINLAND","units":5,"ownedByPlayer":false},' +
                    '{"landName":"SWEDEN","units":5,"ownedByPlayer":true}]'
            ]);
            server.autoRespond = true;
            window.App = {};
            window.App.vent = _.extend({}, Backbone.Events);
            var spy = spyOn(window.App.vent, "on");
            var spyRender = sinon.stub(ActiveGameView.prototype, "renderSubModel");

            // when
            var view = new ActiveGameView();
            server.respond();

            // then
            expect(spy, 'on').toHaveBeenCalled();
            expect(view.collection.length).toBe(2);
            expect(spyRender.calledOnce).toBe(true);
        });

        it('should render game state', function() {

        });

        it('should now draw any map if it is not current players turn', function() {
            expect("not implemented yet").toBe(true);
        });
    });

});