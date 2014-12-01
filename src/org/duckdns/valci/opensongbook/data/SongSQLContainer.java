package org.duckdns.valci.opensongbook.data;

import java.io.Serializable;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.QueryDelegate;
import com.vaadin.data.util.sqlcontainer.query.QueryDelegate.RowIdChangeEvent;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;

public class SongSQLContainer implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    static final Logger LOG = LoggerFactory.getLogger(SongSQLContainer.class);
    
    //private static SongSQLContainer instance;
    private DatabaseHelper dbHelper = null;
    private SQLContainer songContainer = null;

    private Object oldRowId = null;
    private Object newRowId = null;

    public static final String TABLE = "opensongbook";

    public static enum propertyIds {
        ID, SONGTITLE, SONGLYRICS, SONGAUTHOR, MODIFIEDBY, MODIFIEDDATE, VERSION;
    }
    /*
    public static SongSQLContainer getInstance() {
        if (instance == null) {
            LOG.trace("Instantiating SongSQLContainer");
            instance = new SongSQLContainer();
        }      
        LOG.trace("Returning already instantiated SongSQLContainer");
        return instance;
    }
    */
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
            q1.setVersionColumn(propertyIds.VERSION.toString());
            songContainer = new SQLContainer(q1);
            // TODO: maybe I should also add song_revisions
            songContainer.setAutoCommit(false);
            songContainer.addRowIdChangeListener(new QueryDelegate.RowIdChangeListener() {
                /**
                         * 
                         */
                private static final long serialVersionUID = 1L;

                @Override
                public void rowIdChange(RowIdChangeEvent event) {
                    LOG.trace("RowId change event fired!");
                    Object oldId = event.getOldRowId();
                    Object newId = event.getNewRowId();
                    setOldRowId(oldId);
                    if (oldId != null) {
                        LOG.trace("OldRowId: " + getOldRowId().toString());
                    }
                    setNewRowId(newId);
                    if (newId != null) {
                        LOG.trace("NewRowId: " + getNewRowId().toString());
                    }

                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Object getOldRowId() {
        return oldRowId;
    }

    public void setOldRowId(Object oldId) {
        oldRowId = oldId;
    }

    public Object getNewRowId() {
        return newRowId;
    }

    public void setNewRowId(Object newId) {
        newRowId = newId;
    }
}
