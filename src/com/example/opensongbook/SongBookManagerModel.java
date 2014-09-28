package com.example.opensongbook;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import com.example.opensongbook.data.DocumentWriter;
import com.example.opensongbook.data.SongSQLContainer;
import com.vaadin.data.util.sqlcontainer.RowId;
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

    public FileResource generateSongbook(Object itemIds) {
        // TODO Auto-generated method stub
        FileResource generatedFile = null;
        DocumentWriter doc = new DocumentWriter();
        ArrayList<String> songsArray = new ArrayList<String>();
        if (itemIds instanceof Set) {
            // itemIds.
            Iterator iter = ((Set) itemIds).iterator();
            while (iter.hasNext()) {
                com.vaadin.data.util.sqlcontainer.RowId r = (RowId) iter.next();
                songsArray.add(r.getId().toString());
            }
            // songs = (String[]) ((Set<String>) itemIds).toArray();
        }
        try {
            generatedFile = doc.newSongbookWordDoc("testOutputSong",
                    songSQLContainer, songsArray);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return generatedFile;
    }
}
