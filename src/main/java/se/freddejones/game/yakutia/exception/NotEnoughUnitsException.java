package se.freddejones.game.yakutia.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.FORBIDDEN, reason="No such Order")
public class NotEnoughUnitsException extends RuntimeException {

    public NotEnoughUnitsException(String message) {
        super(message);
    }

}
