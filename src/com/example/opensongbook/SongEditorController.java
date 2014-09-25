package com.example.opensongbook;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sound.midi.ControllerEventListener;

import com.example.opensongbook.data.ChordTransposer;
import com.example.opensongbook.data.DocumentWriter;
import com.example.opensongbook.data.SongSQLContainer;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.event.Action.Listener;
import com.vaadin.server.FileResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Link;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

public class SongEditorController {
    private SongEditorModel model;
    private SongEditorView songEditorView;
    // private SongSQLContainer songSQLContainerInstance;
    private SQLContainer songSQLContainer;

    public SongEditorController(SongEditorView songEditorView,
            SongSQLContainer songSQLContainerInstance) {
        this.model = new SongEditorModel(songSQLContainerInstance);
        this.songEditorView = songEditorView;
    }


    public TableValueChangeListener getTableValueChangeListener(
            Table songListTable, TextArea songTextInput,
            TextField songNameField, TextField songAuthorField) {
        return new TableValueChangeListener(songListTable, songTextInput,
                songNameField, songAuthorField);
    }

    // ValueChangeListener list = new Table.ValueChangeListener();
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
            // in multiselect mode, a Set of itemIds is returned,
            // in singleselect mode the itemId is returned directly
            // TODO: all this should be logged
            System.out
                    .println("songListTable " + this.songListTable.getValue());
            Item it = songSQLContainer.getItem(this.songListTable.getValue());
            // System.out.println("item: " + it);
            String lyrics = (String) it.getItemProperty(
                    SongSQLContainer.propertyIds.songLyrics.toString())
                    .getValue();
            String songTitle = (String) it.getItemProperty(
                    SongSQLContainer.propertyIds.songTitle.toString())
                    .getValue();
            String songAuthor = (String) it.getItemProperty(
                    SongSQLContainer.propertyIds.songAuthor.toString())
                    .getValue();
            // System.out.println("lyrics  " +lyrics);
            songTextInput.setValue(lyrics);
            songNameField.setValue(songTitle);
            songAuthorField.setValue(songAuthor);
        }
    }

    // Property prop = new Table.ValueChangeListener() ;

    // this is listener for all button features
    ClickListener buttonListener = new Button.ClickListener() {

        private static final long serialVersionUID = 1L;

        public void buttonClick(ClickEvent event) {
            switch (event.getButton().getId()) {
            // TODO: log which button is clicked
            case ("transposeButton"):
                // transpose song
                String transposedSong = model.chordTranspose(
                        (int) songEditorView.getSelectChordTransposition()
                                .getValue(), (String) songEditorView
                                .getSongTextInput().getValue());
                // update the views
                songEditorView.getSongTextInput().setValue(transposedSong);
                break;
            case ("newSongButton"):
                break;
            case ("saveSongButton"):
                break;
            case ("deleteSongButton"):
                break;
            case ("exportSongButton"):
                String songText = (String) songEditorView.getSongTextInput()
                        .getValue();
                String songName = (String) songEditorView.getSongNameField()
                        .getValue();
                String songAuthor = (String) songEditorView
                        .getSongAuthorField().getValue();
                FileResource generatedFile = model.exportSong(songName,
                        songAuthor, songText);
                songEditorView.getDownloadExportedSongDocxLink().setData(
                        generatedFile);
                songEditorView.getFootbarLayout().addComponent(
                        songEditorView.getDownloadExportedSongDocxLink());
                break;
            }
        }
    };

}
