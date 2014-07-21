package se.freddejones.game.yakutia.service.impl;

import org.springframework.stereotype.Component;
import se.freddejones.game.yakutia.model.Territory;
import se.freddejones.game.yakutia.service.GameTerritoryHandlerService;

import java.util.*;

@Component
public class GameTerritoryHandlerServiceImpl implements GameTerritoryHandlerService {

    @Override
    public int getNumberOfTerritories() {
        return 0;
    }

    @Override
    public List<Territory> getShuffledTerritories() {
        List<Territory> territories = new ArrayList<>();
        territories.add(Territory.SWEDEN);
        territories.add(Territory.FINLAND);
        territories.add(Territory.NORWAY);
        territories.add(Territory.DENMARK);
        territories.add(Territory.ICELAND);
        territories.add(Territory.TYSKLAND);
        territories.add(Territory.UKRAINA);
        territories.add(Territory.SKAUNE);
        territories.add(Territory.TOMTEBODA);
        Collections.shuffle(territories);
        return territories;
    }

    public static List<Territory> getLandAreas() {
        List<Territory> territories = new ArrayList<>();
        territories.add(Territory.SWEDEN);
        territories.add(Territory.FINLAND);
        territories.add(Territory.NORWAY);
        territories.add(Territory.DENMARK);
        territories.add(Territory.ICELAND);
        territories.add(Territory.TYSKLAND);
        territories.add(Territory.UKRAINA);
        territories.add(Territory.SKAUNE);
        territories.add(Territory.TOMTEBODA);
        return territories;
    }

    public static boolean isTerritoriesConnected(Territory src, Territory dst) {
        switch (src) {
            case SWEDEN:
                if (dst == Territory.NORWAY || dst == Territory.ICELAND) {
                    return true;
                } break;
            case NORWAY:
                if (dst == Territory.SWEDEN || dst == Territory.FINLAND
                        || dst == Territory.TYSKLAND) {
                    return true;
                } break;
            case FINLAND:
                if (dst == Territory.NORWAY || dst == Territory.DENMARK
                        || dst == Territory.UKRAINA) {
                    return true;
                } break;
            case DENMARK:
                if (dst == Territory.FINLAND) {
                    return true;
                } break;
            case ICELAND:
                if (dst == Territory.SWEDEN || dst == Territory.SKAUNE
                        || dst == Territory.TYSKLAND) {
                    return true;
                } break;
            case TYSKLAND:
                if (dst == Territory.NORWAY || dst == Territory.ICELAND
                        || dst == Territory.UKRAINA || dst == Territory.TOMTEBODA) {
                    return true;
                } break;
            case UKRAINA:
                if (dst == Territory.FINLAND || dst == Territory.TYSKLAND) {
                    return true;
                } break;
            case SKAUNE:
                if (dst == Territory.ICELAND || dst == Territory.TOMTEBODA) {
                    return true;
                } break;
            case TOMTEBODA:
                if (dst == Territory.SKAUNE || dst == Territory.TYSKLAND) {
                    return true;
                } break;
            default: return false;
        }

        return false;
    }
}
