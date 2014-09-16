package se.freddejones.game.yakutia.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(
        basePackages = {
                "se.freddejones.game.yakutia.dao",
                "se.freddejones.game.yakutia.service",
                "se.freddejones.game.yakutia.entity",
                "se.freddejones.game.yakutia.application",
                "se.freddejones.game.yakutia.security"
        })
public class ApplicationCoreConfig {
}
