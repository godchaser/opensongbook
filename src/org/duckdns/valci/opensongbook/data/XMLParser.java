package org.duckdns.valci.opensongbook.data;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLParser {

    public static SongClass xmlToSongParser(String xmlFile) throws ParserConfigurationException, SAXException,
            IOException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document dom = db.parse(xmlFile);
        NodeList nodes = dom.getChildNodes();
        Element eElement = (Element) nodes.item(0).getChildNodes();
        // System.out.println("\nCurrent Element :" + eElement.getNodeName());
        SongClass song = new SongClass();
        song.setTitle(eElement.getElementsByTagName("title").item(0).getTextContent());
        song.setAuthor(eElement.getElementsByTagName("author").item(0).getTextContent());
        song.setLyrics(eElement.getElementsByTagName("lyrics").item(0).getTextContent());
        return song;
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        try {
            SongClass song = XMLParser.xmlToSongParser("test_data//opensong_data//Alabare");
            System.out.println("Title: " + song.getTitle());
            System.out.println("Author: " + song.getAuthor());
            System.out.println("Lyrics: " + song.getLyrics());
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
