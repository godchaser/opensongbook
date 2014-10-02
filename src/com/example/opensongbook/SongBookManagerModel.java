package com.example.opensongbook;

import com.example.opensongbook.data.DocumentWriter;
import com.example.opensongbook.data.SongSQLContainer;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.server.FileResource;

public class SongBookManagerModel {
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
