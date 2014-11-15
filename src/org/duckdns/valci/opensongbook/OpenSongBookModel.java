package org.duckdns.valci.opensongbook;

import java.io.Serializable;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Observable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.duckdns.valci.opensongbook.data.ChordTransposer;
import org.duckdns.valci.opensongbook.data.DocumentWriter;
import org.duckdns.valci.opensongbook.data.SongSQLContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.sqlcontainer.OptimisticLockException;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.server.FileResource;
import com.vaadin.ui.Notification;

public class OpenSongBookModel extends Observable implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    static final Logger LOG = LoggerFactory.getLogger(OpenSongBookModel.class);

    public static String newline = System.getProperty("line.separator");
    private SongSQLContainer songSQLContainer;

    public OpenSongBookModel(OpenSongBookController controller) {
        this.songSQLContainer = new SongSQLContainer();
        addObserver(controller);
        sortSQLContainterAlphabetical();
    }

    private void sortSQLContainterAlphabetical() {
        this.songSQLContainer.getContainer().sort(new Object[] { SongSQLContainer.propertyIds.SONGTITLE.toString() },
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
            generatedFile = doc.newSongbookWordDoc("testOutputSong", songSQLContainer.getContainer(), selectedSong,
                    progressComponents);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return generatedFile;
    }

    @SuppressWarnings("unchecked")
    public void addSong() {
        Object newSongId = songSQLContainer.getContainer().addItem();
        LOG.trace("adding item: " + newSongId.toString());

        songSQLContainer.getContainer()
                .getContainerProperty(newSongId, SongSQLContainer.propertyIds.SONGTITLE.toString()).setValue("");
        songSQLContainer.getContainer()
                .getContainerProperty(newSongId, SongSQLContainer.propertyIds.SONGLYRICS.toString()).setValue("");
        songSQLContainer.getContainer()
                .getContainerProperty(newSongId, SongSQLContainer.propertyIds.SONGAUTHOR.toString()).setValue("");
        songSQLContainer.getContainer()
                .getContainerProperty(newSongId, SongSQLContainer.propertyIds.MODIFIEDDATE.toString()).setValue("");
        songSQLContainer.getContainer()
                .getContainerProperty(newSongId, SongSQLContainer.propertyIds.MODIFIEDBY.toString()).setValue("");
        LOG.trace("now trying to add new song: " + newSongId.toString());
        LOG.trace("trying to add new song to sql db");
        if (commitToContainer()) {
            LOG.trace("commit success, selecting updated value");
            Object newRowId = songSQLContainer.getContainer().getItem(songSQLContainer);

            if (newRowId == null) {
                LOG.trace("RowId is null, so selecting last item in list");
                // this is workaround because seems that RowChangeId listener is
                // not working sometimes so we can select last item in list
                newRowId = songSQLContainer.getContainer().lastItemId();
            }
            LOG.trace("Updated new row in table: " + newRowId);
            setChanged();
            notifyObservers(newRowId);
        }
    }

    public void deleteSong(Object itemID) {
        LOG.trace("now deleting song: " + itemID.toString());
        songSQLContainer.getContainer().removeItem(itemID);
        LOG.trace("trying to commit song deletion to sql db");
        if (commitToContainer()) {
            setChanged();
            notifyObservers(itemID);
        }
    }

    public SQLContainer getSongSQLContainer() {
        return songSQLContainer.getContainer();
    }

    @SuppressWarnings("unchecked")
    public void saveSong(FieldGroup fieldGroup, Object itemID) {
        LOG.trace("now trying to save ticket");

        commitFieldGroup(fieldGroup);

        String timeStamp = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(Calendar.getInstance().getTime());
        songSQLContainer.getContainer()
                .getContainerProperty(itemID, SongSQLContainer.propertyIds.MODIFIEDDATE.toString()).setValue(timeStamp);

        if (commitToContainer()) {
            setChanged();
            notifyObservers(itemID);
        }
    }

    private boolean commitToContainer() {
        boolean commitSuccess = false;
        try {
            LOG.trace("trying to commit change to sql db");
            songSQLContainer.getContainer().commit();
            commitSuccess = true;
        } catch (UnsupportedOperationException e) {
            // TODO Auto-generated catch block
            LOG.trace("commit failed: UnsupportedOperationException" + e);
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            LOG.trace("commit failed: SQLException" + e);
            e.printStackTrace();
        } catch (OptimisticLockException e) {
            LOG.trace("Caught OptimisticLockException, should refresh page - mid air collision");
            Notification.show("Mid air collision detected",
                    "Someone already updated ticket you are using, refreshing page", Notification.Type.WARNING_MESSAGE);
            e.printStackTrace();
        }
        return commitSuccess;
    }

    private void commitFieldGroup(FieldGroup fieldGroup) {
        try {
            LOG.trace("now trying to commit field group values");
            fieldGroup.commit();
        } catch (CommitException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
}
