package org.duckdns.valci.opensongbook;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.duckdns.valci.opensongbook.data.ChordTransposer;
import org.duckdns.valci.opensongbook.data.DocumentWriter;
import org.duckdns.valci.opensongbook.data.SongSQLContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.server.FileResource;

public class OpenSongBookModel implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    static final Logger LOG = LoggerFactory.getLogger(OpenSongBookModel.class);

    public static String newline = System.getProperty("line.separator");
    private SQLContainer songSQLContainer;

    public OpenSongBookModel() {
        this.songSQLContainer = new SongSQLContainer().getContainer();
        sortSQLContainterAlphabetical();
    }

    private void sortSQLContainterAlphabetical() {
        this.songSQLContainer.sort(new Object[] { SongSQLContainer.propertyIds.songTitle.toString() },
                new boolean[] { true });
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
                    String transposed = (".".equals(rootChord)) ? "." : ChordTransposer.improvedTransposeChord(
                            rootChord, transposeAmmount);
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

    public FileResource generateSongbook(Object selectedSong, Object[] progressComponents) {
        FileResource generatedFile = null;
        DocumentWriter doc = new DocumentWriter();
        try {
            generatedFile = doc
                    .newSongbookWordDoc("testOutputSong", songSQLContainer, selectedSong, progressComponents);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return generatedFile;
    }

    @SuppressWarnings("unchecked")
    public void addSong() {
        Object newSongId = songSQLContainer.addItem();
        LOG.trace("adding item: " + newSongId.toString());

        songSQLContainer.getContainerProperty(newSongId, SongSQLContainer.propertyIds.songTitle.toString())
                .setValue("");
        songSQLContainer.getContainerProperty(newSongId, SongSQLContainer.propertyIds.songLyrics.toString()).setValue(
                "");
        songSQLContainer.getContainerProperty(newSongId, SongSQLContainer.propertyIds.songAuthor.toString()).setValue(
                "");
        songSQLContainer.getContainerProperty(newSongId, SongSQLContainer.propertyIds.modifiedDate.toString())
                .setValue("");
        songSQLContainer.getContainerProperty(newSongId, SongSQLContainer.propertyIds.modifiedBy.toString()).setValue(
                "");
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

    }

    public void deleteSong(Object deleteSong) {
        LOG.trace("now deleting song: " + deleteSong.toString());
        songSQLContainer.removeItem(deleteSong);
        try {
            LOG.trace("trying to commit song deletion to sql db");
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
    }

    public SQLContainer getSongSQLContainer() {
        return songSQLContainer;
    }
}
