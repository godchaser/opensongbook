package com.example.opensongbook;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.opensongbook.data.SongSQLContainer;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.server.FileResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class SongBookManagerController implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    static final Logger LOG = LoggerFactory
            .getLogger(SongBookManagerController.class);
    
    SongBookManagerModel model;
    SongBookManagerView songBookManagerView;

    public SongBookManagerController(
            SongBookManagerView songBookManagerViewReference,
            SongSQLContainer songSQLContainerInstance) {
        this.model = new SongBookManagerModel(songSQLContainerInstance);
        this.songBookManagerView = songBookManagerViewReference;
    }

    public SQLContainer getSQLContainer() {
        return model.getSongSQLContainer();
    }

    ClickListener buttonListener = new Button.ClickListener() {

        private static final long serialVersionUID = 1L;

        public void buttonClick(ClickEvent event) {
            switch (event.getButton().getId()) {
            // TODO: log which button is clicked
            case ("transposeButton"):
                // transpose song
                /*
                 * String transposedSong = model.chordTranspose( (int)
                 * songEditorView.getSelectChordTransposition() .getValue(),
                 * (String) songEditorView .getSongTextInput().getValue()); //
                 * update the views
                 * songEditorView.getSongTextInput().setValue(transposedSong);
                 */
                break;
            case ("exportSongbookButton"):
                LOG.trace("Export songbook button clicked");
                Object selectedSongs = songBookManagerView.getSelectedSongs();
                FileResource generatedFile = model
                        .generateSongbook(selectedSongs, songBookManagerView.getProgressComponents());

                songBookManagerView.getDownloadExportedSongDocxLink()
                        .setResource(generatedFile);
                songBookManagerView.getFootbarLayout().addComponent(
                        songBookManagerView.getDownloadExportedSongDocxLink());
                /*
                 * FileResource generatedFile = model.exportSong(songName,
                 * songAuthor, songText);
                 * songEditorView.getDownloadExportedSongDocxLink().setResource(
                 * generatedFile);
                 * songEditorView.getFootbarLayout().addComponent(
                 * songEditorView.getDownloadExportedSongDocxLink());
                 */
                break;
            case ("saveSongButton"):
                break;
            case ("deleteSongButton"):
                break;
            case ("exportSongButton"):
                /*
                 * String songText = (String) songEditorView.getSongTextInput()
                 * .getValue(); String songName = (String)
                 * songEditorView.getSongNameField() .getValue(); String
                 * songAuthor = (String) songEditorView
                 * .getSongAuthorField().getValue(); FileResource generatedFile
                 * = model.exportSong(songName, songAuthor, songText);
                 * songEditorView.getDownloadExportedSongDocxLink().setResource(
                 * generatedFile);
                 * songEditorView.getFootbarLayout().addComponent(
                 * songEditorView.getDownloadExportedSongDocxLink()); break;
                 */
            }
        }
    };
}
