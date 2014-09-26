package com.example.opensongbook;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.opensongbook.data.ChordTransposer;
import com.example.opensongbook.data.DocumentWriter;
import com.example.opensongbook.data.SongSQLContainer;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.server.FileResource;

public class SongEditorModel implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static String newline = System.getProperty("line.separator");
    private SQLContainer songSQLContainer;

    public SongEditorModel(SongSQLContainer songSQLContainerInstance) {
        this.songSQLContainer = songSQLContainerInstance.getSongContainer();
        // TODO Auto-generated constructor stub
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

    FileResource exportSong(String songName, String songAuthor, String songText) {
        String[] songList = songText.split("[\r\n]+");
        System.out.println(songText);
        System.out.println(songList);
        ArrayList<String> songArray = new ArrayList<String>();
        FileResource generatedFile = null;
        for (String songLine : songList) {
            songArray.add(songLine);
        }
        DocumentWriter doc = new DocumentWriter();
        try {
            generatedFile = doc.newWordDoc("testOutputSong", songName,
                    songArray);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return generatedFile;
    }

    public SQLContainer getSongSQLContainer() {
        return songSQLContainer;
    }
}
