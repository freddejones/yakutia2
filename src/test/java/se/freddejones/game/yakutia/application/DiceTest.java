package se.freddejones.game.yakutia.application;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.core.Is.is;

public class DiceTest {

    @Test
    public void testName() {
        Dice d = new Dice();

        for (int i=0; i<10000; i++) {
            int diceResult = d.rollDice();
            assertThat(diceResult, is(greaterThanOrEqualTo(1)));
            assertThat(diceResult, is(lessThanOrEqualTo(6)));
        }

    }
}