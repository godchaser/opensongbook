package com.example.opensongbook;

import com.example.opensongbook.data.SongSQLContainer;
import com.vaadin.data.util.sqlcontainer.SQLContainer;

public class SongBookManagerModel {
    private SQLContainer songSQLContainer;

    public SongBookManagerModel(SongSQLContainer songSQLContainerInstance) {
        this.songSQLContainer = songSQLContainerInstance.getSongContainer();
    }

    public SQLContainer getSongSQLContainer() {
        return songSQLContainer;
    }
}
