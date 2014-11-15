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

public class OpenSongBookController implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    static final Logger LOG = LoggerFactory.getLogger(OpenSongBookController.class);

    private OpenSongBookModel model;
    private SongEditorView editorView;
    private SongBookManagerView songbookManagerView;

    private SearchFieldTextChangeListener searchFieldTextChangeListener;
    private ClickListener buttonListener;

    // SINGLETON PATTERN
    private static OpenSongBookController instance = null;

    public static OpenSongBookController getInstance(Object view) {
        if (instance == null) {
            LOG.trace("Instantiating SongEditorController");
            instance = new OpenSongBookController(view);
        }
        if (view instanceof SongEditorView) {
            instance.registerEditorView(view);
        } else if (view instanceof SongBookManagerView) {
            instance.registerSongbookManagerView(view);
        }
        return instance;
    }

    private void registerSongbookManagerView(Object view) {
        if (editorView == null) {
            LOG.trace("Registering editorView");
            songbookManagerView = (SongBookManagerView) view;
        } else {
            LOG.trace("editorView already registered");
        }
    }

    private void registerEditorView(Object view) {
        if (editorView == null) {
            LOG.trace("Registering songbookManagerView");
            editorView = (SongEditorView) view;
        } else {
            LOG.trace("songbookManagerView already registered");
        }
    }

    private OpenSongBookController(Object songView) {
        this.model = new OpenSongBookModel();
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
                String transposedSong = model.chordTranspose((int) editorView.getSelectChordTransposition().getValue(),
                        (String) editorView.getSongTextInput().getValue());
                LOG.trace("Updating the view");
                editorView.getSongTextInput().setValue(transposedSong);
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
                model.deleteSong(editorView.getSongListTable().getValue());
                break;
            case ("exportSongButton"):
                LOG.trace("Export button clicked");
                Object selectedSong = editorView.getSelectedSong();
                FileResource generatedSong = model.generateSongbook(selectedSong, editorView.getProgressComponents());
                editorView.getDownloadExportedSongDocxLink().setResource(generatedSong);
                editorView.getFootbarLayout().addComponent(editorView.getDownloadExportedSongDocxLink());
                break;
            case ("exportSongbookButton"):
                LOG.trace("Export songbook button clicked");
                if (songbookManagerView.getSelectedSongs() != null) {
                    LOG.trace("Getting selected songs");
                    Object selectedSongs = songbookManagerView.getSelectedSongs();
                    FileResource generatedSongBook = model.generateSongbook(selectedSongs,
                            songbookManagerView.getProgressComponents());

                    songbookManagerView.getDownloadExportedSongDocxLink().setResource(generatedSongBook);
                    songbookManagerView.getFootbarLayout().addComponent(
                            songbookManagerView.getDownloadExportedSongDocxLink());
                    /*
                     * FileResource generatedFile = model.exportSong(songName, songAuthor, songText);
                     * songEditorView.getDownloadExportedSongDocxLink().setResource( generatedFile);
                     * songEditorView.getFootbarLayout().addComponent(
                     * songEditorView.getDownloadExportedSongDocxLink());
                     */
                } else {
                    LOG.trace("No songs selected");
                }
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
            Object songID = editorView.getSelectedSong();
            if (songID != null) {
                LOG.trace("Selected songID: " + songID.toString());
                // UPDATE EDITOR FIELDS
                editorView.getEditorFields().setItemDataSource(editorView.getSongListTable().getItem(songID));
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
        editorView.getSearchSongsField().setValue("");
        editorView.getSongListTable().select(null);
        editorView.getSongNameField().setValue("");
        editorView.getSongAuthorField().setValue("");
        editorView.getSongTextInput().setValue("");
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
