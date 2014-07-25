package se.freddejones.game.yakutia.model.translators;

import org.junit.Test;
import se.freddejones.game.yakutia.model.CreateGame;
import se.freddejones.game.yakutia.model.PlayerId;
import se.freddejones.game.yakutia.model.dto.CreateGameDTO;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;

public class CreateGameBinderTest {

    @Test
    public void testMapCreateGameDTO() {

        // given
        CreateGameDTO createGameDTO = new CreateGameDTO();
        createGameDTO.setCreatedByPlayerId(1L);
        createGameDTO.setGameName("tomtefräs");
        createGameDTO.setInvites(new ArrayList<Long>());

        // when
        CreateGameBinder createGameBinder = new CreateGameBinder();
        CreateGame createGame = createGameBinder.bind(createGameDTO);

        // then
        assertThat(createGame.getGameName(), is("tomtefräs"));
        assertThat(createGame.getInvitedPlayers().size(), is(0));
        assertThat(createGame.getPlayerId().getPlayerId(), is(1L));
    }
}