define(
    ['models/GameStateModel'
    ], function(GameStateModel) {

        describe('GameStateModel', function() {

            it('should update url with playerid and gameid', function() {
                // given, when
                var gameStateModel = new GameStateModel({
                    state: 'ATTACK',
                    playerId: 1,
                    gameId: 2
                });

                // then
                expect(gameStateModel.url()).toBe("/game/state/2/1");
            });

            it('should set default state as "NONE"', function () {
                // when
                var gameStateModel = new GameStateModel({});

                // then
                expect(gameStateModel.get('state')).toBe('NONE');
            });
        });

    });