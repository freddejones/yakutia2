package se.freddejones.game.yakutia.auth;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.openid.OpenIDAttribute;
import org.springframework.security.openid.OpenIDAuthenticationStatus;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import se.freddejones.game.yakutia.config.YakutiaUserProvider;
import se.freddejones.game.yakutia.service.PlayerService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;

public class YakutiaUserProviderTest {

    private YakutiaUserProvider yakutiaUserProvider;
    private PlayerService playerServiceMock;

    @Before
    public void setup() {
        playerServiceMock = mock(PlayerService.class);
        yakutiaUserProvider = new YakutiaUserProvider(playerServiceMock);
    }

    @Test
    public void testUserNotExistsIsCreated() throws Exception {
        yakutiaUserProvider.loadUserDetails(getAuthData());

    }

    private Authentication getAuthData() {
        List<OpenIDAttribute> attributes = new ArrayList<>();
        List<String> values = new ArrayList<>();
        values.add("someEmail@england.com");
        attributes.add(new OpenIDAttribute("oiContactEmail","type", values));
        Authentication auth = new OpenIDAuthenticationToken(OpenIDAuthenticationStatus.SUCCESS, "anyurl", "anyMessage", attributes);
        return auth;
    }
}
