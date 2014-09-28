package com.example.opensongbook;

import java.util.Set;

import com.example.opensongbook.UI.Menu;
import com.example.opensongbook.data.SongSQLContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Link;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class SongBookManagerView extends VerticalLayout implements View {
    TwinColSelect songBookManagerTwinColSelect;
    SongBookManagerController controller;
    
    HorizontalLayout footbarLayout;
    Link downloadExportedSongDocxLink;
    
    public HorizontalLayout getFootbarLayout() {
        return footbarLayout;
    }
    
    public Link getDownloadExportedSongDocxLink() {
        return downloadExportedSongDocxLink;
    }
    
    public SongBookManagerView(SongSQLContainer songSQLContainerInstance) {
        this.controller = new SongBookManagerController(this, songSQLContainerInstance);
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
        Button exportButton = new Button("Export songbook");
        exportButton.setId("exportSongbookButton");
        exportButton.addClickListener(controller.buttonListener);
        addComponent(exportButton);
        
        footbarLayout = new HorizontalLayout();
        addComponent(footbarLayout);
        downloadExportedSongDocxLink = new Link();
        downloadExportedSongDocxLink.setCaption("exported song (docx)");
        downloadExportedSongDocxLink.setId("downloadExportedSongDocxLink");
    }
   
    public Object getSelectedSongs( ){
        Object songs = songBookManagerTwinColSelect.getValue();
        if (songs instanceof Set) {
            System.out.println(songs);
        } else {
            // this is single select?
        }
        return songs;
    }
    @Override
    public void enter(ViewChangeEvent event) {
        // TODO Auto-generated method stub

    }

}