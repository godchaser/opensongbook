package org.duckdns.valci.opensongbook;

import java.io.Serializable;

import org.duckdns.valci.opensongbook.data.DocumentWriter;
import org.duckdns.valci.opensongbook.data.SongSQLContainer;

import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.server.FileResource;

public class SongBookManagerModel implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SQLContainer songSQLContainer;

    public SongBookManagerModel(SongSQLContainer songSQLContainerInstance) {
        this.songSQLContainer = songSQLContainerInstance.getSongContainer();
    }

    public SQLContainer getSongSQLContainer() {
        return songSQLContainer;
    }

    public FileResource generateSongbook(Object selectedSongs, Object[] progressComponents) {
        // TODO Auto-generated method stub
        FileResource generatedFile = null;
        DocumentWriter doc = new DocumentWriter();
        try {
            generatedFile = doc.newSongbookWordDoc("testOutputSong",
                    songSQLContainer, selectedSongs, progressComponents);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return generatedFile;
    }
}
