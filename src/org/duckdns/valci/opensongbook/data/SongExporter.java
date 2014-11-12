package org.duckdns.valci.opensongbook.data;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class SongExporter {

    public static void exportOpensongFolderToOpenSongbook(String opensongDirectory) throws IOException,
            ClassNotFoundException, ParserConfigurationException, SAXException {
        File path = new File(opensongDirectory);
        File[] files = path.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                System.out.println("Starting file: " + files[i].getCanonicalPath());
                SongExporter.exportOpensongFileToOpenSongbook(files[i].getCanonicalPath());
            }
        }
    }

    public static void exportOpensongFileToOpenSongbook(String fileName) throws ParserConfigurationException,
            SAXException, IOException, ClassNotFoundException {
        SongClass song = XMLParser.xmlToSongParser(fileName);
        SqliteManager.importSongToDB(song);
    }

    public static void main(String[] args) {
    }

}
