package org.duckdns.valci.opensongbook.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.duckdns.valci.opensongbook.data.DatabaseHelper;
import org.duckdns.valci.opensongbook.data.LineTypeChecker;
import org.duckdns.valci.opensongbook.data.SongClass;
import org.duckdns.valci.opensongbook.data.SongExporter;
import org.duckdns.valci.opensongbook.data.SongSQLContainer;
import org.duckdns.valci.opensongbook.data.SqliteManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class UnitTests {
    // @Test
    public void testDatabaseCreation() throws Exception {
        SqliteManager.createTables();
    }

    // @Test
    public void testDatabaseSongInsert() throws Exception {
        SongClass testSong = new SongClass();
        testSong.setTitle("Test song title");
        testSong.setLyrics("Test song lyrics");
        testSong.setAuthor("Test author");
        SongClass provisionedSong = SqliteManager.testDatabaseInsert(testSong.getTitle(), testSong.getLyrics(),
                testSong.getAuthor(), "Test User");
        assert (testSong.getTitle().equals(provisionedSong.getTitle())
                && testSong.getLyrics().equals(provisionedSong.getLyrics()) && testSong.getAuthor().equals(
                provisionedSong.getAuthor()));
    }

    // @Test
    public void testDatabaseTrigger() throws Exception {

    }

    // @Test
    public void testOpensongImport() throws Exception {
        String opensongDirectory = "test_data//opensong_data//";
        SongExporter.exportOpensongFolderToOpenSongbook(opensongDirectory);
    }

    @Test
    public void staticChordLinesTest() throws Exception {
        String chordFile = "test_data//chords";
        List<String> lines = Files.readAllLines(Paths.get(chordFile), StandardCharsets.UTF_8);
        for (String fileline : lines) {
            System.out.println(fileline);
            Assert.assertEquals(true, LineTypeChecker.checkChords(fileline));
        }
    }

    @Test
    public void testLineTypesFromDatabase() throws Exception {
        // reading content of local db located in "WebContent//WEB-INF//resources";
        DatabaseHelper dh = new DatabaseHelper(false);
        String res = dh.executeCustomQuerry("SELECT SONGLYRICS FROM " + SongSQLContainer.TABLE);
        StringReader reader = new StringReader(res);
        BufferedReader br = new BufferedReader(reader);
        String line;
        ArrayList<String> chordLines = new ArrayList<String>();
        ArrayList<String> plainLines = new ArrayList<String>();
        while ((line = br.readLine()) != null) {
            // remove whitespace (\n, \r, \t, \f, and " ") (1 or more times (matching the most amount possible))
            String cleanLine = line.replaceAll("\\s+", "");
            // check if line starts with . and if line has something more that only .
            if (cleanLine.length() > 1) {
                if (cleanLine.startsWith(".")) {
                    chordLines.add(line);
                } else {
                    plainLines.add(line);
                }
            } else {
                System.out.println("This is bad formatter line: " + line);
            }
        }
        // here we are testing chord lines
        for (String testline : chordLines) {
            System.out.println(testline);
            Assert.assertEquals(true, LineTypeChecker.checkChords(testline));
        }
        // here we are testing lyrics lines
        for (String testline : plainLines) {
            System.out.println(testline);
            Assert.assertEquals(false, LineTypeChecker.checkChords(testline));
        }
    }

    // @Test
    public void testDatabase2() throws Exception {
        // String opensongDirectory = "WebContent//WEB-INF//resources";
        DatabaseHelper dh = new DatabaseHelper(false);
        String res = dh.executeCustomQuerry("SELECT SONGLYRICS FROM " + SongSQLContainer.TABLE);
        System.out.println(res);
    }

    String count = "SELECT COUNT(SONGLYRICS) FROM " + SongSQLContainer.TABLE;

    // @DataProvider(name = "validChordLines")
    public static Object[][] chordLines() {
        return new Object[][] { { "A D G", true }, { "A D C", true }, { "B Hm G", true }, { "F# Hm G", true },
                { "Bb Cm7", true }, { "Cmaj7", true } };
    }

    // @DataProvider(name = "notValidChordLines")
    public static Object[][] plainLines() {
        // String opensongDirectory = "WebContent//WEB-INF//resources";
        DatabaseHelper dh = new DatabaseHelper(false);
        String res = dh.executeCustomQuerry("SELECT SONGLYRICS FROM " + SongSQLContainer.TABLE);
        // ArrayList <String> lines = new ArrayList <String>();
        // for ()
        StringReader reader = new StringReader(res);
        BufferedReader br = new BufferedReader(reader);
        String line;
        ArrayList<String> chordLines = new ArrayList<String>();
        ArrayList<String> plainLines = new ArrayList<String>();
        try {
            while ((line = br.readLine()) != null) {
                String cleanLine = line.replaceAll("\\s+", "");
                if (cleanLine.startsWith(".")) {
                    chordLines.add(line);
                } else {
                    plainLines.add(line);
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // System.out.println(chordLines);
        // for (String line2:plainLines){
        // System.out.println(line2);
        // }
        for (String line2 : chordLines) {
            // System.out.println(line2);
            // Assert.assertEquals(true, LineTypeChecker.checkChords(line2.substring(1)));
        }
        // System.out.println(plainLines);
        Object[][] test;
        int i = 0;

        return new Object[][] { { " ", false }, { "Danas je dan!", false }, { "A ti si tu", false }, { "", false },
                { "H alo", false }, { "E ti", false } };
    }

    // @Test(dataProvider = "validChordLines")
    public void testChordLineTypeChecker(String inputLine, boolean expectedResult) {
        System.out.println(inputLine + " " + expectedResult);
        Assert.assertEquals(expectedResult, LineTypeChecker.checkChords(inputLine));
    }

    // @Test(dataProvider = "notValidChordLines")
    public void testPlainLineTypeChecker(String inputLine, boolean expectedResult) {
        System.out.println(inputLine + " " + expectedResult);
        Assert.assertEquals(expectedResult, LineTypeChecker.checkChords(inputLine));
    }

}
