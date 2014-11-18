package org.duckdns.valci.opensongbook;

import org.duckdns.valci.opensongbook.data.SongSQLContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.Table;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class SongBookManagerView extends VerticalLayout implements View {

    static final Logger LOG = LoggerFactory.getLogger(SongBookManagerView.class);

    //TwinColSelect songBookManagerTwinColSelect;
    ListSelect songBookManagerTwinColSelect;
    OpenSongBookController controller;

    HorizontalLayout footbarLayout;
    Link downloadExportedSongDocxLink;

    ProgressBar songBookExportProgress;
    Label songBookExportStatus;

    int songBookManagerRows = 30;

    public HorizontalLayout getFootbarLayout() {
        return footbarLayout;
    }

    public Link getDownloadExportedSongDocxLink() {
        return downloadExportedSongDocxLink;
    }

    public SongBookManagerView() {
        this.controller = new OpenSongBookController(this);
        createSongBookManagerComponents();
    }

    private void createSongBookManagerComponents() {
        setSizeFull();
        setSpacing(true);
        setMargin(true);
        addComponent(new NavigationMenu());

        //songBookManagerTwinColSelect = new TwinColSelect("SongBookManager", controller.getSQLContainer());
        songBookManagerTwinColSelect = new ListSelect("SongBookManager", controller.getSQLContainer());
        songBookManagerTwinColSelect.setItemCaptionPropertyId(SongSQLContainer.propertyIds.SONGTITLE.toString());
        songBookManagerTwinColSelect.setRows(songBookManagerRows);
        addComponent(songBookManagerTwinColSelect);
        Button exportButton = new Button("Export songbook");
        exportButton.setId("exportSongbookButton");
        exportButton.addClickListener(controller.getButtonListener());
        addComponent(exportButton);

        footbarLayout = new HorizontalLayout();
        addComponent(footbarLayout);
        downloadExportedSongDocxLink = new Link();
        downloadExportedSongDocxLink.setCaption("exported song (docx)");
        downloadExportedSongDocxLink.setId("downloadExportedSongDocxLink");

        songBookExportStatus = new Label("not running");
        footbarLayout.addComponent(songBookExportStatus);

        songBookExportProgress = new ProgressBar(new Float(0.0));
        songBookExportProgress.setEnabled(false);
        footbarLayout.addComponent(songBookExportProgress);
    }

    public Object getSelectedSongs() {
        Object selectedSongs = songBookManagerTwinColSelect.getValue();
        return selectedSongs;
    }

    public Object[] getProgressComponents() {
        Object[] progressComponents = new Object[2];
        progressComponents[0] = songBookExportStatus;
        progressComponents[1] = songBookExportProgress;
        return progressComponents;
    }

    @Override
    public void enter(ViewChangeEvent event) {
        LOG.trace("Entered view");
        // TODO Auto-generated method stub
    }

}