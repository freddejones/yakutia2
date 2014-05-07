package se.freddejones.game.yakutia.usecases.player;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import se.freddejones.game.yakutia.controller.PlayerController;
import se.freddejones.game.yakutia.entity.Player;
import se.freddejones.game.yakutia.service.PlayerService;
import se.freddejones.game.yakutia.usecases.framework.TestdataHandler;

import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath*:**/hibernateTestContext.xml"
        ,"classpath*:**/applicationTestContext.xml"
})
@Transactional(readOnly = false)
public class CreatePlayerUseCases {

    @Autowired
    private PlayerService test;
    @Autowired
    private PlayerController controller;

    @Test
    public void UC_001_createPlayer() throws Exception {
        TestdataHandler.resetAndRebuild();
//        Player p = test.getPlayerById(1L);
//        System.out.println(p.getEmail());
        System.out.println(controller.fetchPlayer(1L).getEmail());
    }

    @Test
    public void UC_002_updatePlayerName() {
        fail("not implemented");
    }

    @Test
    public void UC_003_fetchPlayer() {
        fail("not implemented");
    }

    @Test
    public void UC_004_createPlayerAndFetch() {
        fail("not implemented");
    }

}
