package com.example.opensongbook;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.opensongbook.data.SongSQLContainer;
import com.vaadin.data.Item;
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

    static final Logger LOG = LoggerFactory
            .getLogger(SongEditorController.class);

    private SongEditorModel model;
    private SongEditorView songEditorView;

    public SongEditorController(SongEditorView songEditorView,
            SongSQLContainer songSQLContainerInstance) {
        this.model = new SongEditorModel(songSQLContainerInstance);
        this.songEditorView = songEditorView;
    }

    // this is listener for all button features
    ClickListener buttonListener = new Button.ClickListener() {

        private static final long serialVersionUID = 1L;

        public void buttonClick(ClickEvent event) {
            switch (event.getButton().getId()) {
            // TODO: log which button is clicked
            case ("transposeButton"):
                LOG.trace("Transpose song button clicked");
                String transposedSong = model.chordTranspose(
                        (int) songEditorView.getSelectChordTransposition()
                                .getValue(), (String) songEditorView
                                .getSongTextInput().getValue());
                LOG.trace("Updating the view");
                songEditorView.getSongTextInput().setValue(transposedSong);
                break;
            case ("newSongButton"):
                LOG.trace("New song button clicked");
                break;
            case ("saveSongButton"):
                LOG.trace("Export button clicked");
                break;
            case ("deleteSongButton"):
                LOG.trace("Delete button clicked");
                break;
            case ("exportSongButton"):
                LOG.trace("Export button clicked");

                Object selectedSong = songEditorView.getSelectedSong();
                FileResource generatedFile = model.generateSongbook(
                        selectedSong, songEditorView.getProgressComponents());

                songEditorView.getDownloadExportedSongDocxLink().setResource(
                        generatedFile);
                songEditorView.getFootbarLayout().addComponent(
                        songEditorView.getDownloadExportedSongDocxLink());
                break;
            }
        }
    };

    public TableValueChangeListener getTableValueChangeListener(
            Table songListTable, TextArea songTextInput,
            TextField songNameField, TextField songAuthorField) {
        return new TableValueChangeListener(songListTable, songTextInput,
                songNameField, songAuthorField);
    }

    private final class TableValueChangeListener implements ValueChangeListener {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;
        private Table songListTable;
        private TextArea songTextInput;
        private TextField songNameField;
        private TextField songAuthorField;

        TableValueChangeListener(Table songListTable, TextArea songTextInput,
                TextField songNameField, TextField songAuthorField) {
            this.songListTable = songListTable;
            this.songTextInput = songTextInput;
            this.songNameField = songNameField;
            this.songAuthorField = songAuthorField;
        }

        public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {

            LOG.trace("Selected item (itemId) from table: "
                    + this.songListTable.getValue());

            Item it = model.getSongSQLContainer().getItem(
                    this.songListTable.getValue());

            String lyrics = (String) it.getItemProperty(
                    SongSQLContainer.propertyIds.songLyrics.toString())
                    .getValue();
            String songTitle = (String) it.getItemProperty(
                    SongSQLContainer.propertyIds.songTitle.toString())
                    .getValue();
            String songAuthor = (String) it.getItemProperty(
                    SongSQLContainer.propertyIds.songAuthor.toString())
                    .getValue();

            LOG.trace("Selected lyrics: " + lyrics);
            LOG.trace("Selected title: " + songTitle);
            LOG.trace("Selected author: " + songAuthor);

            songTextInput.setValue(lyrics);
            songNameField.setValue(songTitle);
            songAuthorField.setValue(songAuthor);
        }
    }

    public SearchFieldTextChangeListener getSearchFieldTextChangeListener() {
        return new SearchFieldTextChangeListener();
    }

    private final class SearchFieldTextChangeListener implements
            TextChangeListener {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        @Override
        public void textChange(TextChangeEvent event) {
            LOG.trace("Removing all container filters");
            model.getSongSQLContainer().removeAllContainerFilters();
            LOG.trace("Using search filter for song lyrics string: "
                    + event.getText());
            model.getSongSQLContainer().addContainerFilter(
                    SongSQLContainer.propertyIds.songLyrics.toString(),
                    event.getText(), true, false);

        }
    }

    public SQLContainer getSQLContainer() {
        return model.getSongSQLContainer();
    }

}
