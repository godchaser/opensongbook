package org.duckdns.valci.opensongbook;

import java.io.Serializable;

import org.duckdns.valci.opensongbook.data.SongSQLContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private SongEditorView view;

    private SearchFieldTextChangeListener searchFieldTextChangeListener;
    private ClickListener buttonListener;

    public SongEditorController(SongEditorView songEditorView,
            SongSQLContainer songSQLContainerInstance) {
        this.model = new SongEditorModel(songSQLContainerInstance);
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
                String transposedSong = model.chordTranspose((int) view
                        .getSelectChordTransposition().getValue(),
                        (String) view.getSongTextInput().getValue());
                LOG.trace("Updating the view");
                view.getSongTextInput().setValue(transposedSong);
                break;
            case ("newSongButton"):
                LOG.trace("New song button clicked");
                LOG.trace("Clearing input fields");
                view.clearSearchAndSongFields();
                break;
            case ("saveSongButton"):
                LOG.trace("Save button clicked");
                model.addSong(view.getSongNameField().getValue(), view
                        .getSongTextInput().getValue(), view
                        .getSongAuthorField().getValue());
                break;
            case ("deleteSongButton"):
                LOG.trace("Delete button clicked");
                model.deleteSong(view.getSongListTable().getValue());
                break;
            case ("exportSongButton"):
                LOG.trace("Export button clicked");

                Object selectedSong = view.getSelectedSong();
                FileResource generatedFile = model.generateSongbook(
                        selectedSong, view.getProgressComponents());

                view.getDownloadExportedSongDocxLink().setResource(
                        generatedFile);
                view.getFootbarLayout().addComponent(
                        view.getDownloadExportedSongDocxLink());
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
            if (this.songListTable.getValue() != null) {
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
            } else {
                LOG.trace("Nothing selected in search table");
            }
        }
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

    public void setSearchFieldTextChangeListener(
            SearchFieldTextChangeListener searchFieldTextChangeListener) {
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
