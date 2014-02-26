package se.freddejones.game.yakutia.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.FORBIDDEN, reason="Not allowed to view this game")
public class NoGameFoundException extends RuntimeException {
}
