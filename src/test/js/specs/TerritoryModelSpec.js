define(
    ['models/TerritoryModel'
    ], function(TerritoryModel) {

    describe('TerritoryModel', function() {

        it('should return territory id as territory name', function() {
            // given
            var territoryModel = new TerritoryModel({
                id: 'territoryName'
            });

            // when
            var result = territoryModel.getTerritoryName();

            // then
            expect(result).toBe("territoryName");
        });

    });

});