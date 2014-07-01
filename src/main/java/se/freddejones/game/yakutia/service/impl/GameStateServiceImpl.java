package se.freddejones.game.yakutia.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.freddejones.game.yakutia.dao.GamePlayerDao;
import se.freddejones.game.yakutia.model.GamePlayerId;
import se.freddejones.game.yakutia.model.TerritoryDTO;
import se.freddejones.game.yakutia.model.UnitId;
import se.freddejones.game.yakutia.model.dto.GameStateModelDTO;
import se.freddejones.game.yakutia.service.GameService;
import se.freddejones.game.yakutia.service.GameStateService;

import java.util.List;

@Service
public class GameStateServiceImpl implements GameStateService {

    private GamePlayerDao gamePlayerDao;
    private GameService gameService;

    @Autowired
    public GameStateServiceImpl(GamePlayerDao gamePlayerDao, GameService gameService) {
        this.gamePlayerDao = gamePlayerDao;
        this.gameService = gameService;
    }

    @Override
    public List<TerritoryDTO> getTerritoryInformationForActiveGame(GamePlayerId gamePlayerId) {
        return null;
    }

    @Override
    public GameStateModelDTO getGameStateModel(GamePlayerId gamePlayerId) {
        return null;
    }

    @Override
    public TerritoryDTO getTerritoryInformation(UnitId unitId, GamePlayerId gamePlayerId) {
        return null;
    }


//    @Override
//    public List<TerritoryDTO> getTerritoryInformationForActiveGame(Long playerId, Long gameId) {
//        List<TerritoryDTO> territoryDTOs = new ArrayList<>();
//        GamePlayer gp = gamePlayerDao.getGamePlayerByGameIdAndPlayerId(playerId, gameId);
//        List<GamePlayer> gamePlayers = gamePlayerDao.getGamePlayersByGameId(gameId);
//        for (GamePlayer gamePlayer : gamePlayers) {
//            if (gp.getGamePlayerId() == gamePlayer.getGamePlayerId()) {
//                for (Unit unit : gamePlayer.getUnits()) {
//                    territoryDTOs.add(new TerritoryDTO(unit.getTerritory().toString(), unit.getStrength(), true));
//                }
//            } else {
//                for (Unit unit : gamePlayer.getUnits()) {
//                    if (unit.getTerritory() != Territory.UNASSIGNEDLAND) {
//                        territoryDTOs.add(new TerritoryDTO(unit.getTerritory().toString(), unit.getStrength(), false));
//                    }
//                }
//            }
//        }
//
//        return territoryDTOs;
//    }
//
//    @Override
//    @Transactional(readOnly = false)
//    public GameStateModelDTO getGameStateModel(Long gameId, Long playerId) {
//        GamePlayer gamePlayer = gamePlayerDao.getGamePlayerByGameIdAndPlayerId(playerId, gameId);
//
//        GameStateModelDTO gameStateModelDTO = new GameStateModelDTO();
//        gameStateModelDTO.setGameId(gameId);
//        gameStateModelDTO.setPlayerId(playerId);
//
//        if (gamePlayer.getActionStatus() == null) {
//            gamePlayerDao.setActionStatus(gamePlayer.getGamePlayerId(), ActionStatus.PLACE_UNITS);
//            gameStateModelDTO.setState(ActionStatus.PLACE_UNITS.toString());
//        }
//
//        if (gamePlayer.getActionStatus() == ActionStatus.PLACE_UNITS) {
//            gameStateModelDTO.setState(ActionStatus.PLACE_UNITS.toString());
//        } else if (gamePlayer.getActionStatus() == ActionStatus.ATTACK) {
//            gameStateModelDTO.setState(ActionStatus.ATTACK.toString());
//        } else if (gamePlayer.getActionStatus() == ActionStatus.MOVE) {
//            gameStateModelDTO.setState(ActionStatus.MOVE.toString());
//        }
//
//        isGameOver(gamePlayer);
//
//        return gameStateModelDTO;
//    }
//
//    @Override
//    public TerritoryDTO getTerritoryInformationForTerritory(Territory territory, Long gameId, Long playerId) {
//        GamePlayer reqGp = gamePlayerDao.getGamePlayerByGameIdAndPlayerId(playerId, gameId);
//        GamePlayer gp = gamePlayerDao.getGamePlayerByGameIdAndUnitId(gameId, territory);
//
//        TerritoryDTO territoryDTO = new TerritoryDTO(
//                territory.toString(),
//                gp.getUnitByTerritory(territory).getStrength(),
//                reqGp.getGamePlayerId() == gp.getGamePlayerId());
//        return territoryDTO;
//    }

//    private void isGameOver(GamePlayer gamePlayer) {
//        // refresh gamePlayer
//        List<GamePlayer> gamePlayersList = gamePlayerDao.getGamePlayersByGameId(gamePlayer.getGameId());
//
//        boolean isGameOver = false;
//        for(GamePlayer gp : gamePlayersList) {
//            if (gp.getUnits().size() == 1 && gp.getGamePlayerId() != gamePlayer.getGamePlayerId()) {
//                isGameOver = true;
//                break;
//            }
//        }
//
//        if (isGameOver) {
//            // TODO fix this?
//            gameService.setGameToFinished(gamePlayer.getGameId());
//        }
//
//    }
}
