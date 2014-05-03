package se.freddejones.game.yakutia.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import se.freddejones.game.yakutia.entity.Player;
import se.freddejones.game.yakutia.service.PlayerService;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component("feedSuccessHandler")
public class FeedSuccessHandler implements AuthenticationSuccessHandler {

    private PlayerService playerService;

    @Autowired
    public FeedSuccessHandler(PlayerService playerService) {
        this.playerService = playerService;
    }

    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {


        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Long playerId = Long.parseLong(userDetails.getUsername());
        Player p = playerService.getPlayerById(playerId);
        Cookie cookieEmail = new Cookie("yakutiaPlayerEmail", p.getEmail());
        Cookie cookiePlayerId = new Cookie("yakutiaPlayerId", userDetails.getUsername());
        response.addCookie(cookieEmail);
        response.addCookie(cookiePlayerId);



        if (playerService.isPlayerFullyCreated(Long.parseLong(userDetails.getUsername()))) {
            response.sendRedirect("/#");
            return;
        }

        response.sendRedirect("/#updatePlayerName/"+userDetails.getUsername());
    }
}
