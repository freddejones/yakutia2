package se.freddejones.game.yakutia.model.translators;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import se.freddejones.game.yakutia.dao.UnitDao;
import se.freddejones.game.yakutia.model.BattleInformation;
import se.freddejones.game.yakutia.model.AttackActionUpdate;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

@Ignore
public class BattleInformationBinderTest {

    public static final int STRENGTH = 5;
    public static final long GAME_ID = 1L;
    public static final long PLAYER_ID = 2L;
    public static final int TERRITORY_ATTACK_SRC = 11;
    public static final int TERRITORY_ATTACK_DEST = 12;
    private BattleInformationBinder battleInformationBinder;
    private AttackActionUpdate attackActionUpdate;
    private UnitDao unitDao;

    @Before
    public void setUp() throws Exception {
        unitDao = mock(UnitDao.class);
//        battleInformationBinder = new BattleInformationBinder(unitDao);
//        attackActionUpdate = new AttackActionUpdate(TERRITORY_ATTACK_SRC, TERRITORY_ATTACK_DEST, STRENGTH, GAME_ID, PLAYER_ID);
    }

    @Test
    public void shouldMapAllFieldsForAValidInputDTO() {
        // when
        BattleInformation battleInformation = battleInformationBinder.bind(attackActionUpdate);

        // then
        assertThat(battleInformation.getAttackingStrength(), is(STRENGTH));
        assertThat(battleInformation.getAttackingUnit().getUnitId(), is(TERRITORY_ATTACK_SRC));
        assertThat(battleInformation.getDefendingUnit().getUnitId(), is(TERRITORY_ATTACK_DEST));
    }

    @Test
    public void shouldFetchUnitFromDatabase() {

    }
}