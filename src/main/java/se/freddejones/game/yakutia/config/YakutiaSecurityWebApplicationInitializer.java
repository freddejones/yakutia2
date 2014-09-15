package se.freddejones.game.yakutia.config;

import org.springframework.core.annotation.Order;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

public class YakutiaSecurityWebApplicationInitializer
        extends AbstractSecurityWebApplicationInitializer
{
    public YakutiaSecurityWebApplicationInitializer() {
        super(YakutiaSpringSecurity.class);
    }
}
