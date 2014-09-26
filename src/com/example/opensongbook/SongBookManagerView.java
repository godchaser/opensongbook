package com.example.opensongbook;

import com.example.opensongbook.UI.Menu;
import com.example.opensongbook.data.SongSQLContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class SongBookManagerView extends VerticalLayout implements View {
    TwinColSelect songBookManagerTwinColSelect;
    SongBookManagerController controller;
    
    public SongBookManagerView(SongSQLContainer songSQLContainerInstance) {
        this.controller = new SongBookManagerController(songSQLContainerInstance);
        createSongBookManagerComponents();
    }

    private void createSongBookManagerComponents() {
        setSizeFull();
        setSpacing(true);
        setMargin(true);
        addComponent(new Menu());
        songBookManagerTwinColSelect = new TwinColSelect("SongBookManager", controller.getSQLContainer());
        songBookManagerTwinColSelect.setItemCaptionPropertyId(SongSQLContainer.propertyIds.songTitle.toString());;
        addComponent(songBookManagerTwinColSelect);
    }

    @Override
    public void enter(ViewChangeEvent event) {
        // TODO Auto-generated method stub

    }
}