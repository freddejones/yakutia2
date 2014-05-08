package se.freddejones.game.yakutia.usecases.framework;

import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

@Configuration
@ContextConfiguration(locations = {
        "classpath:applicationTestContext.xml",
        "classpath:hibernateTestContext.xml"})
public class FullApplicationContextConfiguration {
}
