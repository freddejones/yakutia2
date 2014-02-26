package se.freddejones.game.yakutia.model;

import se.freddejones.game.yakutia.entity.GamePlayer;
import se.freddejones.game.yakutia.entity.Unit;

import java.util.List;

public class GameSetup {

    private GamePlayer gp;
    private List<Unit> units;
    private int totalNumberOfUnits;

    public GamePlayer getGp() {
        return gp;
    }

    public void setGp(GamePlayer gp) {
        this.gp = gp;
    }

    public List<Unit> getUnits() {
        return units;
    }

    public void setUnits(List<Unit> units) {
        this.units = units;
    }

    public int getTotalNumberOfUnits() {
        return totalNumberOfUnits;
    }

    public void setTotalNumberOfUnits(int totalNumberOfUnits) {
        this.totalNumberOfUnits = totalNumberOfUnits;
    }
}
