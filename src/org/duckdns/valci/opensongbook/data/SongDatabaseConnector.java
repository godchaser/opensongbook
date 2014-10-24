package org.duckdns.valci.opensongbook.data;

import java.sql.SQLException;

import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;

public class SongDatabaseConnector {
    static String dbPath = "test_data//opensongbookdb.sqlite";
    public static String newline = System.getProperty("line.separator");
    // JDBCConnectionPool pool = new SimpleJDBCConnectionPool(
    // "org.hsqldb.jdbc.JDBCDriver",
    // "jdbc:hsqldb:mem:sqlcontainer", "SA", "", 2, 5);
    JDBCConnectionPool pool;

    public SongDatabaseConnector() {
        initDb();
    }

    void initDb() {
        try {
            pool = new SimpleJDBCConnectionPool("org.sqlite.JDBC",
                    "jdbc:sqlite:" + dbPath, "SA", "", 2, 5);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
