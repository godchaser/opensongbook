package org.duckdns.valci.opensongbook.data;

import java.sql.SQLException;

import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;

public class SongSQLContainer {

    private DatabaseHelper dbHelper = null;
    private SQLContainer songContainer = null;

    public static enum propertyIds {
        ID, songTitle, songLyrics, songAuthor, modifiedBy, modifiedDate;
    }

    public SQLContainer getSongContainer() {
        return songContainer;
    }

    public SongSQLContainer() {
        initContainers();
    }

    private void initContainers() {
        try {
            /* TableQuery and SQLContainer for song -table */
            dbHelper = new DatabaseHelper();

            TableQuery q1 = new TableQuery("songs",
                    dbHelper.getConnectionPool());
            q1.setVersionColumn("version");
            songContainer = new SQLContainer(q1);
            // TODO: maybe should also add song_revisions
            songContainer.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
