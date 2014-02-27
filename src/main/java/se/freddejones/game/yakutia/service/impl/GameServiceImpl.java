package se.freddejones.game.yakutia.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.freddejones.game.yakutia.dao.GameDao;
import se.freddejones.game.yakutia.dao.GamePlayerDao;
import se.freddejones.game.yakutia.dao.UnitDao;
import se.freddejones.game.yakutia.entity.Game;
import se.freddejones.game.yakutia.entity.GamePlayer;
import se.freddejones.game.yakutia.entity.Unit;
import se.freddejones.game.yakutia.exception.*;
import se.freddejones.game.yakutia.model.*;
import se.freddejones.game.yakutia.model.dto.*;
import se.freddejones.game.yakutia.model.statuses.ActionStatus;
import se.freddejones.game.yakutia.model.statuses.GamePlayerStatus;
import se.freddejones.game.yakutia.service.GameService;
import se.freddejones.game.yakutia.service.GameSetupService;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static se.freddejones.game.yakutia.model.GameManager.getLandAreas;

@Service("gameservice")
@Transactional(readOnly = true)
public class GameServiceImpl implements GameService {

    private Logger log = Logger.getLogger(GameServiceImpl.class.getName());

    @Autowired
    protected GameDao gameDao;
    @Autowired
    protected GamePlayerDao gamePlayerDao;
    @Autowired
    protected GameSetupService gameSetupService;
    @Autowired
    protected UnitDao unitDao;
    private BattleCalculator battleCalculator;

    public GameServiceImpl() {
        this.battleCalculator = new BattleCalculator();
    }

    public void setBattleCalculator(BattleCalculator battleCalculator) {
        this.battleCalculator = battleCalculator;
    }

    @Override
    @Transactional(readOnly = false)
    public Long createNewGame(CreateGameDTO createGameDTO) {
        return gameDao.createNewGame(createGameDTO);
    }

    @Override
    public List<GameDTO> getGamesForPlayerById(Long playerid) {
        List<GameDTO> gamesForPlayer = new ArrayList<GameDTO>();
        List<GamePlayer> gamePlayersList = gamePlayerDao.getGamePlayersByPlayerId(playerid);
        for(GamePlayer gamePlayer : gamePlayersList) {
            Game game = gameDao.getGameByGameId(gamePlayer.getGameId());
            GameDTO gameDto = buildGameDTO(playerid, game);
            gamesForPlayer.add(gameDto);
        }
        return gamesForPlayer;
    }

    private GameDTO buildGameDTO(Long playerid, Game game) {
        GameDTO gameDto = new GameDTO();
        gameDto.setId(game.getGameId());
        gameDto.setCanStartGame((playerid == game.getGameCreatorPlayerId()));
        gameDto.setName(game.getName());
        gameDto.setDate(game.getCreationTime().toString());
        gameDto.setStatus(game.getGameStatus().toString());
        return gameDto;
    }

    @Override
    public List<TerritoryDTO> getTerritoryInformationForActiveGame(Long playerId, Long gameId) {
        List<TerritoryDTO> territoryDTOs = new ArrayList<TerritoryDTO>();
        GamePlayer gp = gamePlayerDao.getGamePlayerByGameIdAndPlayerId(playerId, gameId);
        List<GamePlayer> gamePlayers = gamePlayerDao.getGamePlayersByGameId(gameId);
        for (GamePlayer gamePlayer : gamePlayers) {
            if (gp.getGamePlayerId() == gamePlayer.getGamePlayerId()) {
                for (Unit unit : gamePlayer.getUnits()) {
                    territoryDTOs.add(new TerritoryDTO(unit.getTerritory().toString(), unit.getStrength(), true));
                }
            } else {
                for (Unit unit : gamePlayer.getUnits()) {
                    if (unit.getTerritory() != Territory.UNASSIGNEDLAND) {
                        territoryDTOs.add(new TerritoryDTO(unit.getTerritory().toString(), unit.getStrength(), false));
                    }
                }
            }
        }

        return territoryDTOs;
    }

    @Override
    @Transactional(readOnly = false)
    public void setGameToStarted(Long gameId) throws NotEnoughPlayersException,
            ToManyPlayersException, CouldNotCreateGameException {

        List<GamePlayer> gamePlayers = gamePlayerDao.getGamePlayersByGameId(gameId);

        if (gamePlayers.isEmpty() || gamePlayers.size() <= 1
                || !isAtLeastTwoAcceptedGamePlayers(gamePlayers)) {
            throw new NotEnoughPlayersException("Not enough players to start game");
        } else if (gamePlayers.size() > getLandAreas().size()) {
            throw new ToManyPlayersException("To many players to start game");
        }

        gameSetupService.initializeNewGame(gamePlayers);
        gameDao.startGame(gameId);
    }

    private boolean isAtLeastTwoAcceptedGamePlayers(List<GamePlayer> gamePlayers) {

        if ("dev".equals(System.getProperty("ENVIRONMENT"))) { return true; }

        int count = 0;
        for (GamePlayer gp : gamePlayers) {
            if (gp.getGamePlayerStatus() == GamePlayerStatus.ACCEPTED) {
                count++;
            }
        }
        return count >= 2;
    }

    @Override
    public TerritoryDTO placeUnitAction(PlaceUnitUpdate placeUnitUpdate) throws NotEnoughUnitsException {
        GamePlayer gamePlayer = gamePlayerDao.
                getGamePlayerByGameIdAndPlayerId(placeUnitUpdate.getPlayerId(), placeUnitUpdate.getGameId());

        if (gamePlayerDao.getUnassignedLand(gamePlayer.getGamePlayerId()).getStrength()
                < placeUnitUpdate.getNumberOfUnits()) {
            throw new NotEnoughUnitsException("Insufficient funds");
        }

        int strength = 0;
        for (Unit unit : gamePlayer.getUnits()) {
            if (unit.getTerritory().equals(Territory.translateLandArea(placeUnitUpdate.getLandArea()))) {
                strength = unit.getStrength() + placeUnitUpdate.getNumberOfUnits();
                unit.setStrength(strength);
                gamePlayerDao.setUnitsToGamePlayer(gamePlayer.getGamePlayerId(), unit);
            }
        }
        return new TerritoryDTO(placeUnitUpdate.getLandArea(), strength, true);
    }

    @Override
    public TerritoryDTO attackTerritoryAction(AttackActionUpdate attackActionUpdate) throws TerritoryNotConnectedException {
        Long playerId = attackActionUpdate.getPlayerId();
        Long gameId = attackActionUpdate.getGameId();
        GamePlayer attackingGamePlayer = gamePlayerDao.getGamePlayerByGameIdAndPlayerId(playerId, gameId);

        Territory destionationTerritory = Territory.translateLandArea(attackActionUpdate.getTerritoryAttackDest());
        if (!GameManager.isTerritoriesConnected(
                Territory.translateLandArea(attackActionUpdate.getTerritoryAttackSrc()),
                destionationTerritory)) {
            throw new TerritoryNotConnectedException("Territories are not connected");
        }

        GamePlayer defendingGamePlayer = gamePlayerDao.getGamePlayerByGameIdAndTerritory(gameId, destionationTerritory);

        Unit defendingUnit = getUnitByTerritory(attackActionUpdate.getTerritoryAttackDest(), defendingGamePlayer.getUnits());
        Unit attackingUnit = getUnitByTerritory(attackActionUpdate.getTerritoryAttackSrc(), attackingGamePlayer.getUnits());

        BattleResult battleResult = battleCalculator.battle(attackingUnit, defendingUnit);

        int attackingCurrentStrength = attackingUnit.getStrength();
        attackingUnit.setStrength(attackingCurrentStrength-battleResult.getAttackingTerritoryLosses());

        int defendingCurrentStrenght = defendingUnit.getStrength();
        defendingUnit.setStrength(defendingCurrentStrenght-battleResult.getDefendingTerritoryLosses());

        if (battleResult.isTakenOver()) {
            gamePlayerDao.setUnitsToGamePlayer(attackingGamePlayer.getGamePlayerId(), attackingUnit);
            gamePlayerDao.setUnitsToGamePlayer(attackingGamePlayer.getGamePlayerId(), defendingUnit);
        }


        TerritoryDTO territoryDTO = new TerritoryDTO(
                attackActionUpdate.getTerritoryAttackSrc(), attackingUnit.getStrength(), true);
        return territoryDTO;
    }

    private Unit getUnitByTerritory(String territory, List<Unit> defendingGamePlayerUnits) {
        for (Unit unit : defendingGamePlayerUnits) {
            if (unit.getTerritory().equals(Territory.translateLandArea(territory))) {
                return unit;
            }
        }
        return null;
    }


    @Override
    public TerritoryDTO moveUnitsAction(PlaceUnitUpdate placeUnitUpdate) {
        return null;
    }

    @Override
    public GameStateModelDTO getGameStateModel(Long gameId, Long playerId) {
        GamePlayer gamePlayer = gamePlayerDao.getGamePlayerByGameIdAndPlayerId(playerId, gameId);

        GameStateModelDTO gameStateModelDTO = new GameStateModelDTO();
        gameStateModelDTO.setGameId(gameId);
        gameStateModelDTO.setPlayerId(playerId);

        if (gamePlayer.getActionStatus() == null ||
                gamePlayer.getActionStatus() == ActionStatus.PLACE_UNITS) {
            gameStateModelDTO.setState(ActionStatus.PLACE_UNITS.toString());
        } else if (gamePlayer.getActionStatus() == ActionStatus.ATTACK) {
            gameStateModelDTO.setState(ActionStatus.ATTACK.toString());
        } else if (gamePlayer.getActionStatus() == ActionStatus.MOVE) {
            gameStateModelDTO.setState(ActionStatus.MOVE.toString());
        }

        return gameStateModelDTO;
    }

//    @Override
//    @Transactional(readOnly = false)
//    public GameStateModelDTO updateStateModel(GameStateModelDTO gameStateModelDTO)
//            throws NotEnoughUnitsException, TerritoryNotConnectedException {
//        GamePlayer gamePlayer = gamePlayerDao.getGamePlayerByGameIdAndPlayerId(
//                gameStateModelDTO.getPlayerId(), gameStateModelDTO.getGameId());
//
//        if (ActionStatus.PLACE_UNITS.toString().equals(gameStateModelDTO.getState())) {
//            placeUnitUpdate(gameStateModelDTO, gamePlayer);
//        } else if (ActionStatus.ATTACK.toString().equals(gameStateModelDTO.getState())) {
//            attackTerritory(gameStateModelDTO, gamePlayer);
//        }
//
//        return gameStateModelDTO;
//    }

//    private void attackTerritory(GameStateModelDTO gameStateModelDTO, GamePlayer gamePlayer) throws TerritoryNotConnectedException {
//        gameStateModelDTO.getAttackActionUpdate();
//        Territory attackingTerritory = Territory.translateLandArea(gameStateModelDTO.getAttackActionUpdate().getTerritoryAttackSrc());
//        Territory defendingTerritory = Territory.translateLandArea(gameStateModelDTO.getAttackActionUpdate().getTerritoryAttackDest());
//
//        if (!GameManager.isTerritoriesConnected(attackingTerritory, defendingTerritory)) {
//            throw new TerritoryNotConnectedException(String.format("[%s] not connected to [%s]",
//                    attackingTerritory.toString(), defendingTerritory.toString()));
//        }
//
//        Long defendingGamePlayerId = unitDao.getGamePlayerIdByLandAreaAndGameId(
//                gamePlayer.getGameId(), defendingTerritory);
//
//        GamePlayer defendingGamePlayer = gamePlayerDao.getGamePlayerByGamePlayerId(defendingGamePlayerId);
//        for (Unit u : defendingGamePlayer.getUnits()) {
//            if (defendingTerritory.equals(u.getTerritory())) {
//                int remainingUnit = u.getStrength() - gameStateModelDTO.getAttackActionUpdate().getAttackingNumberOfUnits();
//                u.setStrength(remainingUnit);
//                gamePlayerDao.setUnitsToGamePlayer(defendingGamePlayerId, u);
//
//                if (remainingUnit <= 0) {
//                    // Change owner of territory
//                    u.setGamePlayer(gamePlayer);
//                    gamePlayerDao.setUnitsToGamePlayer(gamePlayer.getGamePlayerId(), u);
//                }
//
//            }
//        }
//    }

//    private void placeUnitUpdate(GameStateModelDTO gameStateModelDTO, GamePlayer gamePlayer) throws NotEnoughUnitsException {
//        Unit unassignedLandUnit = gamePlayerDao.getUnassignedLand(gamePlayer.getGamePlayerId());
//        Territory landAreaInRequest = Territory.translateLandArea(
//                gameStateModelDTO.getPlaceUnitUpdate().getTerritory());
//
//        if (unassignedLandUnit.getStrength() - gameStateModelDTO.getPlaceUnitUpdate().getNumberOfUnits() < 0) {
//            throw new NotEnoughUnitsException("Insufficient units for doing PLACE UNIT operation");
//        }
//
//        for (Unit unit : gamePlayer.getUnits()) {
//            if (unit.getTerritory() == landAreaInRequest) {
//                int numberOfUnits = gameStateModelDTO.getPlaceUnitUpdate().getNumberOfUnits();
//                unit.setStrength(unit.getStrength() + numberOfUnits);
//                gamePlayerDao.setUnitsToGamePlayer(gamePlayer.getGamePlayerId(), unit);
//                unassignedLandUnit.setStrength(unassignedLandUnit.getStrength()-numberOfUnits);
//                gamePlayerDao.setUnitsToGamePlayer(gamePlayer.getGamePlayerId(), unassignedLandUnit);
//                if (unassignedLandUnit.getStrength() == 0) {
//                    gameStateModelDTO.setState(ActionStatus.ATTACK.toString());
//                    gamePlayerDao.setActionStatus(gamePlayer.getGamePlayerId(), ActionStatus.ATTACK);
//                }
//            }
//        }
//    }
}
