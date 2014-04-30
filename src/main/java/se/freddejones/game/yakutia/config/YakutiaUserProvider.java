package se.freddejones.game.yakutia.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.openid.OpenIDAttribute;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.stereotype.Service;
import se.freddejones.game.yakutia.entity.Player;
import se.freddejones.game.yakutia.exception.PlayerAlreadyExistsException;
import se.freddejones.game.yakutia.service.PlayerService;

import java.util.ArrayList;
import java.util.List;

@Service("yakutiaUserProvider")
public class YakutiaUserProvider implements UserDetailsService, AuthenticationUserDetailsService {

    private PlayerService playerService;

    @Autowired
    public YakutiaUserProvider(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return null;
    }

    @Override
    public UserDetails loadUserDetails(Authentication authentication) throws UsernameNotFoundException {
        OpenIDAuthenticationToken token = (OpenIDAuthenticationToken) authentication;
        List<OpenIDAttribute> attrs = token.getAttributes();

        Player player = parseAuthData(attrs);

        // TODO check if user exists, otherwise create it and return it
        try {
            playerService.createNewPlayer(player);
        } catch (PlayerAlreadyExistsException e) {
            throw new UsernameNotFoundException(e.getMessage());
        }

        List<GrantedAuthority> auths = new ArrayList<>();
        auths.add(new SimpleGrantedAuthority("ROLE_USER"));
        UserDetails userDetails = new User(player.getEmail(), "dsfsf", auths);
        return userDetails;
    }


    private Player parseAuthData(List<OpenIDAttribute> attributes) {
        Player p = new Player();
        if ("oiContactEmail".equals(attributes.get(0).getName())) {
            List<String> values = attributes.get(0).getValues();
            if (values.size() == 1) {
                p.setEmail(values.get(0));
            }
        }
        return p;
    }
}
