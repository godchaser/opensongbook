package com.example.opensongbook;

import com.example.opensongbook.data.SongSQLContainer;
import com.vaadin.data.util.sqlcontainer.SQLContainer;

public class SongBookManagerController {
    SongBookManagerModel model;

    public SongBookManagerController(SongSQLContainer songSQLContainerInstance) {
        this.model = new SongBookManagerModel(songSQLContainerInstance);
    }

    public SQLContainer getSQLContainer() {
        return model.getSongSQLContainer();
    }
}
