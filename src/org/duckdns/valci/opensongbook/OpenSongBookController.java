package org.duckdns.valci.opensongbook;

import java.io.Serializable;
import java.util.Observable;
import java.util.Observer;

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

public class OpenSongBookController implements Observer, Serializable {
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

    private boolean isCurrentlyModified = false;

    @Override
    public void update(Observable o, Object itemId) {
        if (itemId != null) {
            LOG.trace("Model observable data updated ItemId: " + itemId.toString());
        } else {
            LOG.trace("Model observable data ItemId is null");
        }

        if (model.getSongSQLContainer().containsId(itemId)) {
            LOG.trace("selecting updated item!");
            editorView.getSongListTable().select(itemId);
        } else {
            LOG.trace("ItemId not present in model, probably deleted");
            editorView.getSongListTable().select(null);
            // Object lastItemId = model.getSongSQLContainer().lastItemId();
            // if (lastItemId != null) {
            // LOG.trace("Selecting last itemId: " + lastItemId);
            // editorView.getSongListTable().select(lastItemId);
            // }
        }
    }

    public OpenSongBookController(Object view) {
        LOG.trace("Instantiating OpenSongBookModel");
        this.model = new OpenSongBookModel(this);
        if (view instanceof SongEditorView) {
            registerEditorView(view);
        } else if (view instanceof SongBookManagerView) {
            registerSongbookManagerView(view);
        }
        this.setSearchFieldTextChangeListener(new SearchFieldTextChangeListener());
        this.setButtonListener(new ButtonClickListener());
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

    // this is listener for all button features
    private final class ButtonClickListener implements Button.ClickListener {

        private static final long serialVersionUID = 1L;

        public void buttonClick(ClickEvent event) {
            String buttonID = event.getButton().getId();
            LOG.trace(buttonID + "button clicked");
            switch (buttonID) {
            case ("transposeButton"):
                String transposedSong = model.chordTranspose((int) editorView.getSelectChordTransposition().getValue(),
                        (String) editorView.getSongTextInput().getValue());
                LOG.trace("Updating the view");
                editorView.getSongTextInput().setValue(transposedSong);
                break;
            case ("newSongButton"):
                LOG.trace("Temporary unbind fields");
                editorView.getEditorFields().unbind(editorView.getSongNameField());
                editorView.getEditorFields().unbind(editorView.getSongAuthorField());
                editorView.getEditorFields().unbind(editorView.getSongTextInput());
                LOG.trace("Clearing input fields");
                clearSearchAndSongFields();
                // model.saveSong(editorView.getEditorFields(), null);
                // editorView.getSearchSongsField().setValue("");
                LOG.trace("isCurrentlyModified: " + isCurrentlyModified);
                isCurrentlyModified = true;
                LOG.trace("isCurrentlyModified: " + isCurrentlyModified);
                // editorView.getEditorFields().getField(SongSQLContainer.propertyIds.SONGTITLE.toString()).focus();
                // clearSearchAndSongFields();
                break;
            case ("cancelSongAddition"):
                LOG.trace("Reverting song addition");
                // model.deleteSong(editorView.getSongListTable().getValue());
                isCurrentlyModified = false;
                // model.revertSongAddition();
                LOG.trace("Now repainting cancel button -> save button");
                editorView.getNewSongButton().setCaption("New song");
                editorView.getNewSongButton().setId("newSongButton");
                break;
            case ("saveSongButton"):
                LOG.trace("isCurrentlyModified: " + isCurrentlyModified);
                if (isCurrentlyModified) {
                    isCurrentlyModified = false;
                    editorView.getEditorFields().bind(editorView.getSongNameField(), SongSQLContainer.propertyIds.SONGTITLE.toString());
                    editorView.getEditorFields().bind(editorView.getSongAuthorField(), SongSQLContainer.propertyIds.SONGAUTHOR.toString());
                    editorView.getEditorFields().bind(editorView.getSongTextInput(), SongSQLContainer.propertyIds.SONGLYRICS.toString());
                    model.commitFieldGroup(editorView.getEditorFields());
                    model.commitToContainer();
                } else {
                    model.saveSong(editorView.getEditorFields(), editorView.getSelectedSong());
                }
                // clearSearchAndSongFields();
                // model.discardFieldGroupModifications(editorView.getEditorFields());
                break;
            case ("deleteSongButton"):
                model.deleteSong(editorView.getSongListTable().getValue());
                clearSearchAndSongFields();
                break;
            case ("exportSongButton"):
                Object selectedSong = editorView.getSelectedSong();
                FileResource generatedSong = model.generateSongbook(selectedSong, editorView.getProgressComponents());
                editorView.getDownloadExportedSongDocxLink().setResource(generatedSong);
                editorView.getFootbarLayout().addComponent(editorView.getDownloadExportedSongDocxLink());
                break;
            case ("exportSongbookButton"):
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
                     * songEditorView.getDownloadExportedSongDocxLink ().setResource( generatedFile);
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
                editorView.getSongNameField().removeAllValidators();
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
            model.getSongSQLContainer().addContainerFilter(SongSQLContainer.propertyIds.SONGLYRICS.toString(),
                    event.getText(), true, false);

        }
    }

    private void clearSearchAndSongFields() {
        LOG.trace("Clearing all fields");
        editorView.getSongListTable().select(null);
        editorView.getSearchSongsField().setValue("");

        editorView.getSongNameField().setValue("");
        editorView.getSongAuthorField().setValue("");
        editorView.getSongTextInput().setValue("");

        // editorView.getEditorFields().discard();
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
