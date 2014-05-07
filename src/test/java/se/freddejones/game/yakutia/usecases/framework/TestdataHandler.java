package se.freddejones.game.yakutia.usecases.framework;

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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestdataHandler {

    private static Connection conn;
    private static Liquibase liquibase;
    private static Database database;

    private static void setupConnection() throws SQLException,
            ClassNotFoundException, DatabaseException {
        Class.forName("org.hsqldb.jdbcDriver");

        String connectionUrl = "jdbc:hsqldb:mem:testdb;shutdown=false";
        conn = DriverManager.getConnection(connectionUrl, "SA", "");

        database = DatabaseFactory.getInstance()
                .findCorrectDatabaseImplementation(new JdbcConnection(conn));
    }

    public static void resetAndRebuild() throws SQLException,
            ClassNotFoundException, LiquibaseException {

        if (database == null) {
            setupConnection();
        }
        ResourceAccessor resourceAccessor = new FileSystemResourceAccessor();
        liquibase = new Liquibase("src/test/resources/db/testdata/yakutia-testdata-01.xml",
                resourceAccessor, database);
        liquibase.update(new Contexts("test"));

//        liquibase = new Liquibase("src/test/resources/db/testdata/changeset/reset.xml",
//                resourceAccessor, database);
//        liquibase.update(new Contexts("test"));

//        liquibase = new Liquibase("db/changeset/yakutia-cs-01.xml",
//                new ClassLoaderResourceAccessor(), database);
//        liquibase.update(new Contexts("test"));




    }

}
