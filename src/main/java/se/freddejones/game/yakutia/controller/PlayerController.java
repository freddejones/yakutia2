package se.freddejones.game.yakutia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import se.freddejones.game.yakutia.exception.PlayerAlreadyExistsException;
import se.freddejones.game.yakutia.model.PlayerId;
import se.freddejones.game.yakutia.model.dto.PlayerDTO;
import se.freddejones.game.yakutia.model.translators.PlayerMapper;
import se.freddejones.game.yakutia.service.PlayerService;

import java.util.logging.Logger;

@RestController
public class PlayerController {

    private static final Logger log = Logger.getLogger(PlayerController.class.getName());

    private final PlayerMapper playerMapper;
    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerMapper = new PlayerMapper();
        this.playerService = playerService;
    }

    @RequestMapping(value  = "/player/create", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Long createPlayer(@RequestBody final PlayerDTO playerDTO) throws PlayerAlreadyExistsException {
        log.info("Creating player from received PlayerDTO: " + playerDTO.toString());
        PlayerId playerId = playerService.createNewPlayer(playerMapper.map(playerDTO));
        return playerId.getPlayerId();
    }

    @RequestMapping(value = "/player/update/name", method = RequestMethod.PUT)
    @ResponseBody
    public Long updatePlayerName(@RequestBody final PlayerDTO playerDTO) {
        log.info("received: " + playerDTO.toString());
        PlayerId playerId = new PlayerId(playerDTO.getPlayerId());
        playerId = playerService.updatePlayerName(playerId, playerDTO.getPlayerName());
        return playerId.getPlayerId();
    }

    @RequestMapping(value = "/player/fetch/{id}", method = RequestMethod.GET)
    @ResponseBody
    public PlayerDTO fetchPlayer(@PathVariable("id") final Long id) {
        log.info("fetching player with id: "+ id);
        return playerMapper.map(playerService.getPlayerById(new PlayerId(id)));
    }
}
