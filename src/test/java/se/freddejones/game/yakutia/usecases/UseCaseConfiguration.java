package se.freddejones.game.yakutia.usecases;

import com.jolbox.bonecp.BoneCPDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;
import javax.sql.DataSource;

//@Configuration
//@PropertySource("classpath:dev/h2.properties")
public class UseCaseConfiguration {

//    @Resource
//    private Environment environment;
//
//    @Bean
//    public DataSource dataSource() {
//        BoneCPDataSource dataSource = new BoneCPDataSource();
//
//        dataSource.setDriverClass(environment.getRequiredProperty("jdbc.driver.className"));
//        dataSource.setJdbcUrl(environment.getRequiredProperty("jdbc.url"));
//        dataSource.setUsername(environment.getRequiredProperty("jdbc.user"));
//        dataSource.setPassword(environment.getRequiredProperty("jdbc.password"));
//
//        return dataSource;
//    }

//    @Bean
//    public PlayerDao playerDao() {
//        return new PlayerDaoImpl();
//    }
//
//    @Bean
//    public PlayerService playerService() {
//        return new PlayerServiceImpl();
//    }
//
//    @Bean
//    public PlayerController playerController() {
//        return new PlayerController();
//    }

}
