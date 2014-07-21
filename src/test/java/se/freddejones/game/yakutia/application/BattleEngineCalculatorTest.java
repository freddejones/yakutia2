package se.freddejones.game.yakutia.application;

import org.junit.Before;
import org.junit.Test;
import se.freddejones.game.yakutia.model.BattleResult;
import se.freddejones.game.yakutia.model.UnitType;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;

public class BattleEngineCalculatorTest {

    private BattleEngineCalculator battleEngineCalculator;
    private Dice dice;

    @Before
    public void setUp() throws Exception {
        dice = mock(Dice.class);
        battleEngineCalculator = new BattleEngineCalculator(dice);
    }

    @Test
    public void testAttackingWithThreeSoldierUnitsAndOneDefendingSoldier() {
        // given
        when(dice.rollDice()).thenReturn(1);
        Map<UnitType, Integer> defendingUnits = setupSoldierUnits(1);
        Map<UnitType, Integer> attackingUnits = setupSoldierUnits(3);

        // when
        battleEngineCalculator.battleForTerritory(attackingUnits, defendingUnits);

        // then
        verify(dice, times(3+1)).rollDice();
    }

    @Test
    public void testAttackingWithThreeSoldierUnitsAndTwoDefendingSoldiers() {
        // given
        when(dice.rollDice()).thenReturn(1);
        Map<UnitType, Integer> defendingUnits = setupSoldierUnits(2);
        Map<UnitType, Integer> attackingUnits = setupSoldierUnits(3);

        // when
        battleEngineCalculator.battleForTerritory(attackingUnits, defendingUnits);

        // then
        verify(dice, times(3+2)).rollDice();
    }

    @Test
    public void testAttackingWithThreeSoldierUnitsAndThreeDefendingSoldiers() {
        // given
        when(dice.rollDice()).thenReturn(1);
        Map<UnitType, Integer> defendingUnits = setupSoldierUnits(3);
        Map<UnitType, Integer> attackingUnits = setupSoldierUnits(3);

        // when
        battleEngineCalculator.battleForTerritory(attackingUnits, defendingUnits);

        // then
        verify(dice, times(3+2)).rollDice();
    }

    @Test
    public void testAttackingWithTwoSoldierUnitsAndThreeDefendingSoldiers() {
        // given
        when(dice.rollDice()).thenReturn(1);
        Map<UnitType, Integer> defendingUnits = setupSoldierUnits(3);
        Map<UnitType, Integer> attackingUnits = setupSoldierUnits(2);

        // when
        battleEngineCalculator.battleForTerritory(attackingUnits, defendingUnits);

        // then
        verify(dice, times(2+2)).rollDice();
    }

    @Test
    public void testAttackingWithOneSoldierUnitsAndThreeDefendingSoldiers() {
        // given
        when(dice.rollDice()).thenReturn(1);
        Map<UnitType, Integer> defendingUnits = setupSoldierUnits(3);
        Map<UnitType, Integer> attackingUnits = setupSoldierUnits(1);

        // when
        battleEngineCalculator.battleForTerritory(attackingUnits, defendingUnits);

        // then
        verify(dice, times(1+1)).rollDice();
    }

    @Test
    public void testAttackingWithTwoSoldierUnitsAndTwoDefendingSoldiers() {
        // given
        when(dice.rollDice()).thenReturn(1);
        Map<UnitType, Integer> defendingUnits = setupSoldierUnits(2);
        Map<UnitType, Integer> attackingUnits = setupSoldierUnits(2);

        // when
        battleEngineCalculator.battleForTerritory(attackingUnits, defendingUnits);

        // then
        verify(dice, times(2+2)).rollDice();
    }

    @Test
    public void testAttackingWithTwoSoldierUnitsAndOneDefendingSoldiers() {
        // given
        when(dice.rollDice()).thenReturn(1);
        Map<UnitType, Integer> defendingUnits = setupSoldierUnits(1);
        Map<UnitType, Integer> attackingUnits = setupSoldierUnits(2);

        // when
        battleEngineCalculator.battleForTerritory(attackingUnits, defendingUnits);

        // then
        verify(dice, times(2+1)).rollDice();
    }

    @Test
    public void testAttackingWithOneSoldierUnitsAndTwoDefendingSoldiers() {
        // given
        when(dice.rollDice()).thenReturn(1);
        Map<UnitType, Integer> defendingUnits = setupSoldierUnits(2);
        Map<UnitType, Integer> attackingUnits = setupSoldierUnits(1);

        // when
        battleEngineCalculator.battleForTerritory(attackingUnits, defendingUnits);

        // then
        verify(dice, times(1+1)).rollDice();
    }

    @Test
    public void testAttackingWithOneSoldierUnitsAndOneDefendingSoldiers() {
        // given
        when(dice.rollDice()).thenReturn(1);
        Map<UnitType, Integer> defendingUnits = setupSoldierUnits(1);
        Map<UnitType, Integer> attackingUnits = setupSoldierUnits(1);

        // when
        battleEngineCalculator.battleForTerritory(attackingUnits, defendingUnits);

        // then
        verify(dice, times(1+1)).rollDice();
    }

    @Test
    public void test_Attacking_3_Defending_2_TotalWin() {
        // given
        when(dice.rollDice()).thenReturn(6).thenReturn(6).thenReturn(6).thenReturn(5).thenReturn(5);
        Map<UnitType, Integer> defendingUnits = setupSoldierUnits(2);
        Map<UnitType, Integer> attackingUnits = setupSoldierUnits(3);

        // when
        BattleResult battleResult = battleEngineCalculator.battleForTerritory(attackingUnits, defendingUnits);

        // then
        assertThat(totalUnitsLeft(battleResult.getDefendingUnitsLeft()), is(0));
        assertThat(battleResult.isTakenOver(), is(true));
    }

    @Test
    public void test_Attacking_3_Defending_2_EqualLosses() {
        // given
        when(dice.rollDice()).thenReturn(6).thenReturn(6).thenReturn(6).thenReturn(6).thenReturn(5);
        Map<UnitType, Integer> defendingUnits = setupSoldierUnits(2);
        Map<UnitType, Integer> attackingUnits = setupSoldierUnits(3);

        // when
        BattleResult battleResult = battleEngineCalculator.battleForTerritory(attackingUnits, defendingUnits);

        // then
        assertThat(totalUnitsLeft(battleResult.getDefendingUnitsLeft()), is(1));
        assertThat(totalUnitsLeft(battleResult.getAttackingUnitsLeft()), is(2));
        assertThat(battleResult.isTakenOver(), is(false));
    }

    @Test
    public void test_Attacking_3_Defending_2_FullLoss() {
        // given
        when(dice.rollDice()).thenReturn(1).thenReturn(2).thenReturn(3).thenReturn(4).thenReturn(3);
        Map<UnitType, Integer> defendingUnits = setupSoldierUnits(2);
        Map<UnitType, Integer> attackingUnits = setupSoldierUnits(3);

        // when
        BattleResult battleResult = battleEngineCalculator.battleForTerritory(attackingUnits, defendingUnits);

        // then
        assertThat(totalUnitsLeft(battleResult.getDefendingUnitsLeft()), is(2));
        assertThat(totalUnitsLeft(battleResult.getAttackingUnitsLeft()), is(1));
        assertThat(battleResult.isTakenOver(), is(false));
    }

    @Test
    public void test_Attacking_3_Defending_1_TotalWin() {
        // given
        when(dice.rollDice()).thenReturn(6).thenReturn(6).thenReturn(6).thenReturn(5);
        Map<UnitType, Integer> defendingUnits = setupSoldierUnits(1);
        Map<UnitType, Integer> attackingUnits = setupSoldierUnits(3);

        // when
        BattleResult battleResult = battleEngineCalculator.battleForTerritory(attackingUnits, defendingUnits);

        // then
        assertThat(totalUnitsLeft(battleResult.getDefendingUnitsLeft()), is(0));
        assertThat(totalUnitsLeft(battleResult.getAttackingUnitsLeft()), is(3));
        assertThat(battleResult.isTakenOver(), is(true));
    }

    @Test
    public void test_Attacking_3_Defending_1_FullLoss() {
        // given
        when(dice.rollDice()).thenReturn(1).thenReturn(1).thenReturn(1).thenReturn(2);
        Map<UnitType, Integer> defendingUnits = setupSoldierUnits(1);
        Map<UnitType, Integer> attackingUnits = setupSoldierUnits(3);

        // when
        BattleResult battleResult = battleEngineCalculator.battleForTerritory(attackingUnits, defendingUnits);

        // then
        assertThat(totalUnitsLeft(battleResult.getDefendingUnitsLeft()), is(1));
        assertThat(totalUnitsLeft(battleResult.getAttackingUnitsLeft()), is(2));
        assertThat(battleResult.isTakenOver(), is(false));
    }

    @Test
    public void test_Attacking_2_Defending_1_FullWin() {
        // given
        when(dice.rollDice()).thenReturn(2).thenReturn(3).thenReturn(2);
        Map<UnitType, Integer> defendingUnits = setupSoldierUnits(1);
        Map<UnitType, Integer> attackingUnits = setupSoldierUnits(2);

        // when
        BattleResult battleResult = battleEngineCalculator.battleForTerritory(attackingUnits, defendingUnits);

        // then
        assertThat(totalUnitsLeft(battleResult.getDefendingUnitsLeft()), is(0));
        assertThat(totalUnitsLeft(battleResult.getAttackingUnitsLeft()), is(2));
        assertThat(battleResult.isTakenOver(), is(true));
    }

    @Test
    public void test_Attacking_2_Defending_1_FullLoss() {
        // given
        when(dice.rollDice()).thenReturn(1).thenReturn(1).thenReturn(2);
        Map<UnitType, Integer> defendingUnits = setupSoldierUnits(1);
        Map<UnitType, Integer> attackingUnits = setupSoldierUnits(2);

        // when
        BattleResult battleResult = battleEngineCalculator.battleForTerritory(attackingUnits, defendingUnits);

        // then
        assertThat(totalUnitsLeft(battleResult.getDefendingUnitsLeft()), is(1));
        assertThat(totalUnitsLeft(battleResult.getAttackingUnitsLeft()), is(1));
        assertThat(battleResult.isTakenOver(), is(false));
    }

    private Map<UnitType, Integer> setupSoldierUnits(int numberOfSoldiers) {
        Map<UnitType,Integer> units = new HashMap<>();
        units.put(UnitType.SOLDIER, numberOfSoldiers);
        return units;
    }

    private int totalUnitsLeft(Map<UnitType, Integer> map) {
        int count = 0;
        for (Integer strength : map.values()) {
            count += strength;
        }
        return count;
    }
}