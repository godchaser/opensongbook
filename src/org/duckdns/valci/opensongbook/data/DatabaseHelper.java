package org.duckdns.valci.opensongbook.data;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinService;

public class DatabaseHelper {
    String basepath = VaadinService.getCurrent().getBaseDirectory()
            .getAbsolutePath();
    FileResource dbFile = new FileResource(new File(basepath
            + "/WEB-INF/resources/opensongbookdb.sqlite"));
    // String dbPath = "test//opensongbookdb.sqlite";

    private JDBCConnectionPool connectionPool = null;

    public DatabaseHelper() {
        initConnectionPool();
    }

    private JDBCConnectionPool initConnectionPool() {
        try {
            String dbPath = dbFile.getSourceFile().getCanonicalPath();
            connectionPool = new SimpleJDBCConnectionPool("org.sqlite.JDBC",
                    "jdbc:sqlite:" + dbPath, "", "", 2, 5);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return connectionPool;
    }

    public JDBCConnectionPool getConnectionPool() {
        return connectionPool;
    }

    public static void main(String[] args) {
        DatabaseHelper dh = new DatabaseHelper();
    }
}
