package se.freddejones.game.yakutia.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.FORBIDDEN, reason="Territories are not connected")
public class TerritoryNotConnectedException extends RuntimeException {

    public TerritoryNotConnectedException(String message) {
        super(message);
    }
}
