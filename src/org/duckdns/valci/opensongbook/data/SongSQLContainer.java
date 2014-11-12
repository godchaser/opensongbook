package org.duckdns.valci.opensongbook.data;

import java.io.Serializable;
import java.sql.SQLException;

import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;

public class SongSQLContainer implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private DatabaseHelper dbHelper = null;
    private SQLContainer songContainer = null;

    public static final String TABLE = "opensongbook";

    public static enum propertyIds {
        ID, songTitle, songLyrics, songAuthor, modifiedBy, modifiedDate;
    }

    public SQLContainer getContainer() {
        return songContainer;
    }

    public SongSQLContainer() {
        initContainers();
    }

    private void initContainers() {
        try {
            /* TableQuery and SQLContainer for song -table */
            dbHelper = DatabaseHelper.getInstance();
            TableQuery q1 = new TableQuery(TABLE, dbHelper.getConnectionPool());
            q1.setVersionColumn("version");
            songContainer = new SQLContainer(q1);
            // TODO: maybe I should also add song_revisions
            songContainer.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
