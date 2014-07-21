package se.freddejones.game.yakutia.application;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class Dice {

    private final Random random;

    public Dice() {
        random = new Random();
    }

    public int rollDice() {
        return random.nextInt(6) + 1;
    }

}
