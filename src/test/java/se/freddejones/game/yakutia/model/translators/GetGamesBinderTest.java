package se.freddejones.game.yakutia.model.translators;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import se.freddejones.game.yakutia.entity.Game;
import se.freddejones.game.yakutia.entity.GamePlayer;
import se.freddejones.game.yakutia.model.PlayerId;
import se.freddejones.game.yakutia.model.dto.GameDTO;
import se.freddejones.game.yakutia.model.statuses.GameStatus;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class GetGamesBinderTest {

    private GetGamesBinder getGamesBinder;
    private Date assertionDate;
    private PlayerId playerId;

    @Before
    public void setUp() throws Exception {
        getGamesBinder = new GetGamesBinder();
        assertionDate = new LocalDate().toDate();
        playerId = new PlayerId(1L);
    }


    @Test
    public void testTranslateAllFields() {
        // given
        boolean isActivePlayerTurn = true;
        Game game = createGameObject(isActivePlayerTurn);

        // when
        GameDTO gameDTO = getGamesBinder.bind(game, playerId);

        // then
        assertThat(gameDTO.getId(), is(1L));
        assertThat(gameDTO.getDate(), is(assertionDate.toString()));
        assertThat(gameDTO.getName(), is("tomte"));
        assertThat(gameDTO.isCanStartGame(), is(true));
        assertThat(gameDTO.getStatus(), is("ONGOING"));
        assertThat(gameDTO.isPlayersTurn(), is(isActivePlayerTurn));
    }

    @Test
    public void testTranslateWithNotActiveTurn() {
        // given
        boolean isActivePlayerTurn = false;
        Game game = createGameObject(isActivePlayerTurn);

        // when
        GameDTO gameDTO = getGamesBinder.bind(game, playerId);

        // then
        assertThat(gameDTO.isPlayersTurn(), is(isActivePlayerTurn));
    }

    @Test
    public void testTranslateList() {
        // given
        List<Game> games = new ArrayList<>();
        Game game = createGameObject(true);
        games.add(game);

        // when
        List<GameDTO> gameDTOs = getGamesBinder.bind(games, playerId);

        // then
        assertThat(gameDTOs.size(), is(1));
    }

    private Game createGameObject(boolean activePlayerTurn) {
        Game game = new Game();
        game.setGameId(1L);
        game.setCreationTime(assertionDate);
        game.setName("tomte");
        game.setGameCreatorPlayerId(1L);
        game.setGameStatus(GameStatus.ONGOING);
        Set<GamePlayer> gamePlayerSet = new HashSet<>();
        GamePlayer gamePlayer = new GamePlayer();
        gamePlayer.setPlayerId(playerId.getPlayerId());
        gamePlayer.setActivePlayerTurn(activePlayerTurn);
        gamePlayerSet.add(gamePlayer);
        game.setPlayers(gamePlayerSet);
        return game;
    }
}