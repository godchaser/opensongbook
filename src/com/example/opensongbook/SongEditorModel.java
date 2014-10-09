package com.example.opensongbook;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.opensongbook.data.ChordTransposer;
import com.example.opensongbook.data.DocumentWriter;
import com.example.opensongbook.data.SongSQLContainer;
import com.sun.org.apache.regexp.internal.recompile;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.sqlcontainer.RowId;
import com.vaadin.data.util.sqlcontainer.RowItem;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.TemporaryRowId;
import com.vaadin.server.FileResource;

public class SongEditorModel implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    static final Logger LOG = LoggerFactory.getLogger(SongEditorModel.class);

    public static String newline = System.getProperty("line.separator");
    private SQLContainer songSQLContainer;

    public SongEditorModel(SongSQLContainer songSQLContainerInstance) {
        this.songSQLContainer = songSQLContainerInstance.getSongContainer();
        sortSQLContainterAlphabetical();
    }

    private void sortSQLContainterAlphabetical() {
        this.songSQLContainer.sort(
                new Object[] { SongSQLContainer.propertyIds.songTitle
                        .toString() }, new boolean[] { true });
    }

    String chordTranspose(int transposeAmmount, String songText) {
        String[] songList = songText.split("[\r\n]+");
        StringBuilder updatedSong = new StringBuilder();
        for (String songLine : songList) {
            // this is a chord line
            if (songLine.startsWith(".")) {
                Pattern p = Pattern.compile("[\\w'\\S]+");
                Matcher m = p.matcher(songLine);
                StringBuffer chordLineBuilder = new StringBuffer(songLine);
                // go through each chord
                while (m.find()) {
                    String rootChord = songLine.substring(m.start(), m.end());
                    String transposed = (".".equals(rootChord)) ? "."
                            : ChordTransposer.improvedTransposeChord(rootChord,
                                    transposeAmmount);
                    ;
                    // String transposed =
                    // ChordTransposer.improvedTransposeChord(rootChord,
                    // (int) selectChordTransposition.getValue());
                    chordLineBuilder.replace(m.start(), m.end(), transposed);
                }
                updatedSong.append(chordLineBuilder.toString() + newline);
            } else {
                updatedSong.append(songLine + newline);
            }
        }
        return updatedSong.toString();
    }

    public FileResource generateSongbook(Object selectedSong,
            Object[] progressComponents) {
        // TODO Auto-generated method stub
        FileResource generatedFile = null;
        DocumentWriter doc = new DocumentWriter();
        try {
            generatedFile = doc.newSongbookWordDoc("testOutputSong",
                    songSQLContainer, selectedSong, progressComponents);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return generatedFile;
    }

    @SuppressWarnings("unchecked")
    public void addSong(String songTitle, String songLyrics, String songAuthor) {
        RowId newSongId = (RowId) songSQLContainer.addItem();
        LOG.trace("newSongId: " + newSongId);
        LOG.trace("setting values to new song entry: " + songTitle + "\n"
                + songAuthor + "\n" + songLyrics);
        ArrayList a = new ArrayList(songSQLContainer.getItem(newSongId)
                .getItemPropertyIds());

        LOG.trace("itemProperties: " + a.toString());
        LOG.trace("item: " + songSQLContainer.getItem(newSongId).toString());
        // LOG.trace("item: " +
        // songSQLContainer.getItem(newSongId).addItemProperty(id, property));

        /*
         * The item type of SQLContainer (RowItem) doesn't support adding
         * properties to an Item. Instead the Item already contains the
         * properties and you set the values for it. Either
         * SQLContainer.getItemProperty(itemId, propertyId).setValue(Object) or
         * SQLContainer
         * .getItem(itemId).getItemProperty(propertyId).setValue(Object).
         */
        /*
         * LOG.trace("newSongId ID: " + songSQLContainer.getItem(newSongId)
         * .getItemProperty(SongSQLContainer.propertyIds.ID)
         * .getValue().toString()); LOG.trace("newSongId Title: " +
         * songSQLContainer .getItem(newSongId)
         * .getItemProperty(SongSQLContainer.propertyIds.songTitle)
         * .getValue().toString());
         */
        try {
            LOG.trace("trying to commit song entry to sql db");
            songSQLContainer.commit();
        } catch (UnsupportedOperationException e) {
            // TODO Auto-generated catch block
            LOG.trace("commit failed: UnsupportedOperationException" + e);
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            LOG.trace("commit failed: SQLException" + e);
            e.printStackTrace();
        }
        
        songSQLContainer.getItem(newSongId).addItemProperty(SongSQLContainer.propertyIds.songTitle, songTitle);
        songSQLContainer.getItem(newSongId)
                .getItemProperty(SongSQLContainer.propertyIds.songTitle)
                .setValue(songTitle);
        LOG.trace("item updated: "
                + songSQLContainer.getItem(newSongId).toString());
        songSQLContainer.getItem(newSongId)
                .getItemProperty(SongSQLContainer.propertyIds.songLyrics)
                .setValue(songLyrics);
        songSQLContainer.getItem(newSongId)
                .getItemProperty(SongSQLContainer.propertyIds.songAuthor)
                .setValue(songAuthor);
        try {
            LOG.trace("trying to commit song entry to sql db");
            songSQLContainer.commit();
        } catch (UnsupportedOperationException e) {
            // TODO Auto-generated catch block
            LOG.trace("commit failed: UnsupportedOperationException" + e);
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            LOG.trace("commit failed: SQLException" + e);
            e.printStackTrace();
        }
        /*
         * newUser.getItemProperty("role").setValue("admin");
         * container.commit();
         */
    }

    public SQLContainer getSongSQLContainer() {
        return songSQLContainer;
    }

}
