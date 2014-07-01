package se.freddejones.game.yakutia.model.translators;

import org.springframework.stereotype.Component;
import se.freddejones.game.yakutia.model.BattleInformation;
import se.freddejones.game.yakutia.model.AttackActionUpdate;

@Component
public class BattleInformationBinder {

    public BattleInformation bind(AttackActionUpdate dto) {
//        Unit attackingUnit = new Unit();
//        attackingUnit.setTerritory(Territory.translateLandArea(dto.getTerritoryAttackSrc()));
//        Unit defendingUnit = new Unit();
//        defendingUnit.setTerritory(Territory.translateLandArea(dto.getTerritoryAttackDest()));
//        BattleInformation battleInformation = new BattleInformation(attackingUnit, defendingUnit, dto.getAttackingNumberOfUnits());
        return new BattleInformation(null, null, null);
    }

}
