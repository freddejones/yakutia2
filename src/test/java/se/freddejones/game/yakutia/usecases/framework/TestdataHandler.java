package se.freddejones.game.yakutia.usecases.framework;

import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringLiquibase;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.FileSystemResourceAccessor;
import liquibase.resource.ResourceAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestdataHandler {

    private static Connection conn;
    private static Liquibase liquibase;
    private static Database database;

    public static Connection getConnection() {
        return conn;
    }

    private static void setupConnection() throws SQLException,
            ClassNotFoundException, DatabaseException {
        Class.forName("org.h2.Driver");
        String connectionUrl = "jdbc:h2:mem:test";
        conn = DriverManager.getConnection(connectionUrl, "", "");

        database = DatabaseFactory.getInstance()
                .findCorrectDatabaseImplementation(new JdbcConnection(conn));
    }

    public static void test() throws Exception {
        setupConnection();
    }

    public static void resetAndRebuild() throws Exception  {

        if (database == null) {
            setupConnection();
        }

        try {
            liquibase.dropAll();
        } catch (NullPointerException npx) {
            // silently accept
        }

        liquibase = new Liquibase("db/changeset/yakutia-cs-01.xml",
                new ClassLoaderResourceAccessor(), database);
        liquibase.update(new Contexts("test"));
    }

    public static void loadChangeSet(String resourcePath) throws Exception {

        if (database == null) {
            setupConnection();
        }

        ResourceAccessor resourceAccessor = new FileSystemResourceAccessor();
        liquibase = new Liquibase(resourcePath,
                resourceAccessor, database);
        liquibase.update(new Contexts("test"));
    }
}
