package com.example.opensongbook;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.opensongbook.data.ChordTransposer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;

public class SongEditorModel {
    static String dbPath = "test_data//opensongbookdb.sqlite";
    public static String newline = System.getProperty("line.separator");
    // JDBCConnectionPool pool = new SimpleJDBCConnectionPool(
    // "org.hsqldb.jdbc.JDBCDriver",
    // "jdbc:hsqldb:mem:sqlcontainer", "SA", "", 2, 5);
    JDBCConnectionPool pool;

    public SongEditorModel() {
        initDb();
    }

    void initDb() {
        try {
            pool = new SimpleJDBCConnectionPool("org.sqlite.JDBC",
                    "jdbc:sqlite:" + dbPath, "SA", "", 2, 5);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    String chordTranspose (int transposeAmmount, String songText) {
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
}
