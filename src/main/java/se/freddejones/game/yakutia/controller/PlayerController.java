package se.freddejones.game.yakutia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import se.freddejones.game.yakutia.entity.Player;
import se.freddejones.game.yakutia.exception.PlayerAlreadyExistsException;
import se.freddejones.game.yakutia.model.PlayerId;
import se.freddejones.game.yakutia.model.dto.PlayerDTO;
import se.freddejones.game.yakutia.model.translators.PlayerBinder;
import se.freddejones.game.yakutia.service.PlayerService;

import java.util.logging.Logger;

@Controller
@RequestMapping(value = "/player")
public class PlayerController {

    private static final Logger log = Logger.getLogger(PlayerController.class.getName());

    private final PlayerBinder playerBinder;
    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerBinder = new PlayerBinder();
        this.playerService = playerService;
    }

    @RequestMapping(value  = "/create", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Long createPlayer(@RequestBody final PlayerDTO playerDTO) throws PlayerAlreadyExistsException {
        log.info("Createing player from received PlayerDTO: " + playerDTO.toString());
        PlayerId playerId = playerService.createNewPlayer(playerBinder.bind(playerDTO));
        return playerId.getPlayerId();
    }

    @RequestMapping(value = "/update/name", method = RequestMethod.POST)
    @ResponseBody
    public Long updatePlayerName(@RequestBody final PlayerDTO playerDTO) {
//        Player p = playerService.getPlayerById(playerDTO.getPlayerId());
//        p.setName(playerDTO.getPlayerName());
//        return playerService.updatePlayerName(p);
        return 1L;
    }

    @RequestMapping(value = "/fetch/{id}", method = RequestMethod.GET)
    @ResponseBody
    public PlayerDTO fetchPlayer(@PathVariable("id") final Long id) {
//        PlayerDTO playerDTO = Player.translate(playerService.getPlayerById(id));
//        return playerDTO;
        return null;
    }
}
