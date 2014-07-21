package se.freddejones.game.yakutia.service;

import se.freddejones.game.yakutia.model.Territory;

import java.util.List;
import java.util.Set;

public interface GameTerritoryHandlerService {

    public int getNumberOfTerritories();
    public List<Territory> getShuffledTerritories();


}
