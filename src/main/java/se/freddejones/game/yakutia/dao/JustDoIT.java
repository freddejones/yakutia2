package se.freddejones.game.yakutia.dao;

import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.FileSystemResourceAccessor;
import liquibase.resource.ResourceAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
@Profile("mock")
public class JustDoIT {

    private Connection conn;
    private Liquibase liquibase;
    private Database database;

    private final Environment env;

    @Autowired
    public JustDoIT(Environment env) throws ClassNotFoundException, SQLException, LiquibaseException {
        this.env = env;
        Class.forName(env.getProperty("jdbc.driver.className"));
        String connectionUrl = env.getProperty("jdbc.url");
        conn = DriverManager.getConnection(connectionUrl, "", "");

        database = DatabaseFactory.getInstance()
                .findCorrectDatabaseImplementation(new JdbcConnection(conn));

        loadChangeSet("src/main/resources/db/testdata/game_gameplayers_players.xml");
        conn.commit();
        conn.close();
    }

    public void loadChangeSet(String resourcePath) throws LiquibaseException {
        ResourceAccessor resourceAccessor = new FileSystemResourceAccessor();
        liquibase = new Liquibase(resourcePath,
                resourceAccessor, database);
        liquibase.update(new Contexts("test"));
    }


}
