package se.freddejones.game.yakutia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import se.freddejones.game.yakutia.entity.Player;
import se.freddejones.game.yakutia.exception.PlayerAlreadyExistsException;
import se.freddejones.game.yakutia.model.dto.PlayerDTO;
import se.freddejones.game.yakutia.service.PlayerService;

import java.util.logging.Logger;

@Controller
@RequestMapping(value = "/player")
public class PlayerController {

    private Logger log = Logger.getLogger(PlayerController.class.getName());

    @Autowired
    PlayerService playerService;

    @RequestMapping(value  = "/create", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Long createPlayer(@RequestBody final PlayerDTO playerDTOs) throws PlayerAlreadyExistsException {
        log.info("Receiving PlayerDTO with name: " + playerDTOs.toString());
        Player p = PlayerDTO.bind(playerDTOs);
        return playerService.createNewPlayer(p);
    }

}
