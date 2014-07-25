package se.freddejones.game.yakutia.usecases;

import org.junit.runner.RunWith;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import se.freddejones.game.yakutia.HibernateConfig;
import se.freddejones.game.yakutia.application.BattleEngineCalculator;
import se.freddejones.game.yakutia.application.Dice;
import se.freddejones.game.yakutia.controller.PlayerController;
import se.freddejones.game.yakutia.dao.PlayerDao;
import se.freddejones.game.yakutia.service.PlayerService;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
@Transactional(readOnly = false)
public class UseCaseConfiguration {

    @Configuration
    @Import(HibernateConfig.class)
    @ComponentScan(basePackageClasses = {PlayerController.class, PlayerService.class, PlayerDao.class, BattleEngineCalculator.class, Dice.class})
    static class TestConfiguration {}

    protected MockMvc mockMvc;

}
