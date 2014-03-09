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
        List<GameDTO> gamesForPlayer = new ArrayList<>();
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
        List<TerritoryDTO> territoryDTOs = new ArrayList<>();
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

        // TODO remove this properly
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
    @Transactional(readOnly = false)
    public TerritoryDTO placeUnitAction(PlaceUnitUpdate placeUnitUpdate) throws NotEnoughUnitsException {
        GamePlayer gamePlayer = gamePlayerDao.
                getGamePlayerByGameIdAndPlayerId(placeUnitUpdate.getPlayerId(), placeUnitUpdate.getGameId());

        Unit unassignedLandUnit = gamePlayerDao.getUnassignedLand(gamePlayer.getGamePlayerId());
        if (unassignedLandUnit.getStrength()
                < placeUnitUpdate.getNumberOfUnits()) {
            throw new NotEnoughUnitsException("Insufficient funds");
        }

        int strength = 0;
        for (Unit unit : gamePlayer.getUnits()) {
            if (unit.getTerritory().equals(Territory.translateLandArea(placeUnitUpdate.getTerritory()))) {
                strength = unit.getStrength() + placeUnitUpdate.getNumberOfUnits();
                unit.setStrength(strength);
                unassignedLandUnit.setStrength(unassignedLandUnit.getStrength()-placeUnitUpdate.getNumberOfUnits());
                gamePlayerDao.setUnitsToGamePlayer(gamePlayer.getGamePlayerId(), unit);
                gamePlayerDao.setUnitsToGamePlayer(gamePlayer.getGamePlayerId(), unassignedLandUnit);
            }
        }

        if (unassignedLandUnit.getStrength() == 0) {
            gamePlayerDao.setActionStatus(gamePlayer.getGamePlayerId(), ActionStatus.ATTACK);
        }

        return new TerritoryDTO(placeUnitUpdate.getTerritory(), strength, true);
    }

    @Override
    @Transactional(readOnly = false)
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

        attackingUnit.setStrength(attackingUnit.getStrength()-battleResult.getAttackingTerritoryLosses());
        defendingUnit.setStrength(defendingUnit.getStrength()-battleResult.getDefendingTerritoryLosses());

        gamePlayerDao.setUnitsToGamePlayer(attackingGamePlayer.getGamePlayerId(), attackingUnit);

        if (battleResult.isTakenOver()) {
            gamePlayerDao.setUnitsToGamePlayer(attackingGamePlayer.getGamePlayerId(), defendingUnit);
        } else {
            gamePlayerDao.setUnitsToGamePlayer(defendingGamePlayer.getGamePlayerId(), defendingUnit);
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
    @Transactional(readOnly = false)
    public GameStateModelDTO getGameStateModel(Long gameId, Long playerId) {
        GamePlayer gamePlayer = gamePlayerDao.getGamePlayerByGameIdAndPlayerId(playerId, gameId);

        GameStateModelDTO gameStateModelDTO = new GameStateModelDTO();
        gameStateModelDTO.setGameId(gameId);
        gameStateModelDTO.setPlayerId(playerId);

        if (gamePlayer.getActionStatus() == null) {
            gamePlayerDao.setActionStatus(gamePlayer.getGamePlayerId(), ActionStatus.PLACE_UNITS);
            gameStateModelDTO.setState(ActionStatus.PLACE_UNITS.toString());
        }

        if (gamePlayer.getActionStatus() == ActionStatus.PLACE_UNITS) {
            gameStateModelDTO.setState(ActionStatus.PLACE_UNITS.toString());
        } else if (gamePlayer.getActionStatus() == ActionStatus.ATTACK) {
            gameStateModelDTO.setState(ActionStatus.ATTACK.toString());
        } else if (gamePlayer.getActionStatus() == ActionStatus.MOVE) {
            gameStateModelDTO.setState(ActionStatus.MOVE.toString());
        }

        return gameStateModelDTO;
    }

    @Override
    public TerritoryDTO getTerritoryInformationForTerritory(Territory territory, Long gameId, Long playerId) {
        GamePlayer reqGp = gamePlayerDao.getGamePlayerByGameIdAndPlayerId(playerId, gameId);
        GamePlayer gp = gamePlayerDao.getGamePlayerByGameIdAndTerritory(gameId, territory);

        TerritoryDTO territoryDTO = new TerritoryDTO(
                territory.toString(),
                gp.getUnitByTerritory(territory).getStrength(),
                reqGp.getGamePlayerId() == gp.getGamePlayerId());
        return territoryDTO;
    }
}
