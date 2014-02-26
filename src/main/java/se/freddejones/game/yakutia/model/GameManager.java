package se.freddejones.game.yakutia.model;

import java.util.ArrayList;
import java.util.List;

public class GameManager {

    public static List<Territory> getLandAreas() {
        List<Territory> territories = new ArrayList<Territory>();
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
        }

        return false;
    }
}
