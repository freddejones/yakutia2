define(
    ['models/GameStateModel'
    ], function(GameStateModel) {

        describe('GameStateModel', function() {

            it('should update url with playerid and gameid when calling updateUrl', function() {
                // given
                var gameStateModel = new GameStateModel({
                    state: 'ATTACK',
                    playerId: 1,
                    gameId: 2
                });

                // when
                gameStateModel.updateUrl();

                // then
                expect(gameStateModel.url).toBe("/game/state/2/1");
            });

        });

    });