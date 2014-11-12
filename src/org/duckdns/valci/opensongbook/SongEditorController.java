package org.duckdns.valci.opensongbook;

import java.io.Serializable;

import org.duckdns.valci.opensongbook.data.SongSQLContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.server.FileResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

public class SongEditorController implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    static final Logger LOG = LoggerFactory.getLogger(SongEditorController.class);

    private SongEditorModel model;
    private SongEditorView view;

    private SearchFieldTextChangeListener searchFieldTextChangeListener;
    private ClickListener buttonListener;

    public SongEditorController(SongEditorView songEditorView) {
        this.model = new SongEditorModel();
        this.view = songEditorView;
        this.setSearchFieldTextChangeListener(new SearchFieldTextChangeListener());
        this.setButtonListener(new ButtonClickListener());
    }

    // this is listener for all button features
    private final class ButtonClickListener implements Button.ClickListener {

        private static final long serialVersionUID = 1L;

        public void buttonClick(ClickEvent event) {
            switch (event.getButton().getId()) {
            case ("transposeButton"):
                LOG.trace("Transpose song button clicked");
                String transposedSong = model.chordTranspose((int) view.getSelectChordTransposition().getValue(),
                        (String) view.getSongTextInput().getValue());
                LOG.trace("Updating the view");
                view.getSongTextInput().setValue(transposedSong);
                break;
            case ("newSongButton"):
                LOG.trace("New song button clicked");
                LOG.trace("Clearing input fields");
                model.addSong();
                clearSearchAndSongFields();
                break;
            case ("saveSongButton"):
                LOG.trace("Save button clicked");
                // TODO: implement save song
                // model.saveSong();
                break;
            case ("deleteSongButton"):
                LOG.trace("Delete button clicked");
                model.deleteSong(view.getSongListTable().getValue());
                break;
            case ("exportSongButton"):
                LOG.trace("Export button clicked");
                Object selectedSong = view.getSelectedSong();
                FileResource generatedFile = model.generateSongbook(selectedSong, view.getProgressComponents());
                view.getDownloadExportedSongDocxLink().setResource(generatedFile);
                view.getFootbarLayout().addComponent(view.getDownloadExportedSongDocxLink());
                break;
            }
        }
    };

    public TableValueChangeListener getTableValueChangeListener(Table songListTable, TextArea songTextInput,
            TextField songNameField, TextField songAuthorField) {
        return new TableValueChangeListener();
    }

    private final class TableValueChangeListener implements ValueChangeListener {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        public TableValueChangeListener() {
            // TODO Auto-generated constructor stub
        }

        public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
            Object songID = view.getSelectedSong();
            if (songID != null) {
                LOG.trace("Selected songID: " + songID.toString());
                // UPDATE EDITOR FIELDS
                view.getEditorFields().setItemDataSource(view.getSongListTable().getItem(songID));
            } else {
                LOG.trace("songID is null");
            }
            // view.getEditorLayout().setVisible(songID != null);
        }

    }

    private final class SearchFieldTextChangeListener implements TextChangeListener {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        @Override
        public void textChange(TextChangeEvent event) {
            LOG.trace("Removing all container filters");
            model.getSongSQLContainer().removeAllContainerFilters();
            LOG.trace("Using search filter for song lyrics string: " + event.getText());
            model.getSongSQLContainer().addContainerFilter(SongSQLContainer.propertyIds.songLyrics.toString(),
                    event.getText(), true, false);

        }
    }

    private void clearSearchAndSongFields() {
        LOG.trace("Clearing all fields");
        view.getSearchSongsField().setValue("");
        view.getSongListTable().select(null);
        view.getSongNameField().setValue("");
        view.getSongAuthorField().setValue("");
        view.getSongTextInput().setValue("");
    }

    public void setSearchFieldTextChangeListener(SearchFieldTextChangeListener searchFieldTextChangeListener) {
        this.searchFieldTextChangeListener = searchFieldTextChangeListener;
    }

    public ClickListener getButtonListener() {
        return this.buttonListener;
    }

    public void setButtonListener(ClickListener buttonListener) {
        this.buttonListener = buttonListener;
    }

    public SearchFieldTextChangeListener getSearchFieldTextChangeListener() {
        return this.searchFieldTextChangeListener;
    }

    public SQLContainer getSQLContainer() {
        return model.getSongSQLContainer();
    }

}
