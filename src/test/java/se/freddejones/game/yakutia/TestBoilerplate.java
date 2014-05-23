package se.freddejones.game.yakutia;

import se.freddejones.game.yakutia.entity.GamePlayer;
import se.freddejones.game.yakutia.entity.Unit;
import se.freddejones.game.yakutia.model.Territory;
import se.freddejones.game.yakutia.model.TerritoryDTO;
import se.freddejones.game.yakutia.model.statuses.GamePlayerStatus;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestBoilerplate {

    public static class GamePlayersListBuilder {
        private List<GamePlayer> gamePlayers;

        public GamePlayersListBuilder() {
            gamePlayers = new ArrayList<>();
        }

        public GamePlayersListBuilder addGamePlayer(GamePlayer gp) {
            gamePlayers.add(gp);
            return this;
        }

        public List<GamePlayer> build() {
            return gamePlayers;
        }
    }

    public static class UnitBuilder {
        private List<Unit> units;

        public UnitBuilder() {
            units = new ArrayList<>();
        }

        public UnitBuilder addUnit(Territory territory, int strength) {
            Unit u = new Unit();
            u.setTerritory(territory);
            u.setStrength(strength);
            units.add(u);
            return this;
        }

        public List<Unit> build() {
            return units;
        }
    }

    public static class GamePlayerMockBuilder {
        private GamePlayer gamePlayerMock;

        public GamePlayerMockBuilder() {
            gamePlayerMock = mock(GamePlayer.class);
        }

        public GamePlayerMockBuilder setGamePlayerId(Long id) {
            when(gamePlayerMock.getGamePlayerId()).thenReturn(id);
            return this;
        }

        public GamePlayerMockBuilder setUnits(List<Unit> units) {
            when(gamePlayerMock.getUnits()).thenReturn(units);
            return this;
        }

        public GamePlayerMockBuilder setGamePlayerStatus(GamePlayerStatus gamePlayerStatus) {
            when(gamePlayerMock.getGamePlayerStatus()).thenReturn(gamePlayerStatus);
            return this;
        }

        public GamePlayer build() {
            return gamePlayerMock;
        }
    }


    public static boolean atLeastOneNotOwnedByPlayer(List<TerritoryDTO> list) {
        for(TerritoryDTO territoryDTO : list) {
            if (!territoryDTO.isOwnedByPlayer()) {
                return true;
            }
        }
        return false;
    }
}
