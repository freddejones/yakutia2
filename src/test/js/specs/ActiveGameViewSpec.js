define([
    'view/ActiveGameView',
    'sinon'
], function(
    ActiveGameView,
    sinon) {

    describe('ActiveGameView', function() {

        var server, updateStateStub;

        beforeEach(function () {
            server = sinon.fakeServer.create();
            window.App = {};
            window.App.vent = _.extend({}, Backbone.Events);
            updateStateStub = sinon.stub(ActiveGameView.prototype, "updateState", function () {});
        });

        afterEach(function () {
            server.restore();
            ActiveGameView.prototype.updateState.restore();
            window.App = null;
        });

        it("should called updateState on initialize", function () {
            // given

            // when
            new ActiveGameView();

            expect(updateStateStub.called).toBe(true);
        });

        it("should call updateState on event triggered", function () {
            // given
            var view = new ActiveGameView();

            // when
            window.App.vent.trigger("Statemodel::update");

            // then
            expect(updateStateStub.calledTwice).toBe(true);
        });

    });

    describe("ActiveGameView::updateState", function () {
        var server;

        beforeEach(function () {
            sinon.mock($).expects("cookie")
                .twice().returns(1);
            server = sinon.fakeServer.create();
            window.App = {};
            window.App.vent = _.extend({}, Backbone.Events);
        });

        afterEach(function () {
            window.App = null;
            server.restore();
            $.cookie.restore();
        });

        it("should update game state model when calling updateState", function () {
            // given
//            sinon.mock($).expects("cookie")
//                .twice().returns(1);

            server.respondWith("GET", "/game/state/1/1", [
                200, {"Content-Type": "application/json"},
                    '{"playerId":1, "gameId":1, "state":"ATTACK"}'
            ]);
            server.autoRespond = true;
            var view = new ActiveGameView();

            // when
            view.updateState();
            server.respond();

            // then
            expect(_.isEqual(view.model.attributes,{playerId: 1, gameId: 1, state: "ATTACK"})).toBe(true);
        });

        it("should call 'renderGameState' for successful fetch", function () {
            // given

            server.respondWith("GET", "/game/state/1/1", [
                200, {"Content-Type": "application/json"},
                '{"playerId":1, "gameId":1, "state":"ATTACK"}'
            ]);
            server.autoRespond = true;
            var view = new ActiveGameView();
            var spyRenderGameState = sinon.spy(view, "renderGameState");

            // when
            view.updateState();
            server.respond();

            // then
            expect(spyRenderGameState.calledWithMatch("ATTACK")).toBe(true);
            view.renderGameState.restore();
        });
    });

});