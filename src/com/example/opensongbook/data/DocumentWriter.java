package com.example.opensongbook.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import org.apache.poi.xwpf.usermodel.Borders;
import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFStyles;

import com.vaadin.data.util.sqlcontainer.RowId;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.UI;

public class DocumentWriter {
    FileResource generatedFile;
    public FileResource newWordDoc(String filename, String songName,
            ArrayList<String> fileContentLines) throws Exception {

        // XWPFDocument template = new XWPFDocument(new FileInputStream(new
        // File("test\\template.dotx")));
        // Find the application directory
        String basepath = VaadinService.getCurrent().getBaseDirectory()
                .getAbsolutePath();
        FileResource templateFile = new FileResource(new File(basepath
                + "/WEB-INF/resources/template.dotx"));
        XWPFDocument template = new XWPFDocument(new FileInputStream(
                templateFile.getSourceFile()));

        XWPFDocument document = new XWPFDocument();
        // copy styles from template to new doc
        XWPFStyles newStyles = document.createStyles();
        newStyles.setStyles(template.getStyle());

        // Create Header
        XWPFParagraph tmpHeader = document.createParagraph();
        // tmpParagraph.setSpacingLineRule(LineSpacingRule.AT_LEAST);
        XWPFRun tmpRunHeader = tmpHeader.createRun();
        tmpRunHeader.setFontFamily("Arial");
        tmpRunHeader.getCTR().getRPr().getRFonts().setHAnsi("Arial");
        tmpRunHeader.setText(songName);
        tmpRunHeader.setFontSize(18);
        tmpHeader.setStyle("Heading1");
        tmpHeader.setBorderBottom(Borders.SINGLE);

        // Create Song body
        for (String line : fileContentLines) {
            XWPFParagraph tmpParagraph = document.createParagraph();
            XWPFRun tmpRun = tmpParagraph.createRun();
            tmpRun.setFontFamily("Courier New");
            tmpRun.getCTR().getRPr().getRFonts().setHAnsi("Courier New");
            tmpRun.setText(line);
            tmpRun.setFontSize(14);
            tmpParagraph.setStyle("NoSpacing");
        }

        // write everything to file
        // FileOutputStream fos = new FileOutputStream(new
        // File("test\\"+filename + ".docx"));
        String outputFile = basepath + "/WEB-INF/resources/" + "filename"
                + ".docx";
        FileOutputStream fos = new FileOutputStream(new File(outputFile));

        document.write(fos);
        fos.close();

        FileResource generatedFile = new FileResource(new File(outputFile));
        return generatedFile;
    }

    public void writeSong(XWPFDocument document, String songTitle,
            String songLyrics) {
        // Create Header
        XWPFParagraph tmpHeader = document.createParagraph();
        // tmpParagraph.setSpacingLineRule(LineSpacingRule.AT_LEAST);
        XWPFRun tmpRunHeader = tmpHeader.createRun();
        tmpRunHeader.setFontFamily("Arial");
        tmpRunHeader.getCTR().getRPr().getRFonts().setHAnsi("Arial");
        tmpRunHeader.setText(songTitle);
        tmpRunHeader.setFontSize(18);
        tmpHeader.setStyle("Heading1");
        tmpHeader.setBorderBottom(Borders.SINGLE);

        // Create Song body
        XWPFParagraph tmpParagraph = document.createParagraph();
        XWPFRun tmpRun = tmpParagraph.createRun();
        tmpRun.setFontFamily("Courier New");
        tmpRun.getCTR().getRPr().getRFonts().setHAnsi("Courier New");
        tmpRun.setText(songLyrics);
        tmpRun.setFontSize(14);
        tmpParagraph.setStyle("NoSpacing");
        tmpRunHeader.addBreak(BreakType.PAGE);
    }

    public FileResource newSongbookWordDoc(String filename,
            final SQLContainer sqlContainter, Object selectedSongs,
            final Object[] progressComponents) throws Exception {

        // XWPFDocument template = new XWPFDocument(new FileInputStream(new
        // File("test\\template.dotx")));
        // Find the application directory
        //final FileResource generatedFile;
        final String basepath = VaadinService.getCurrent().getBaseDirectory()
                .getAbsolutePath();
        final String outputFile = basepath + "/WEB-INF/resources/" + "filename"
                + ".docx";
        FileResource templateFile = new FileResource(new File(basepath
                + "/WEB-INF/resources/template.dotx"));
        XWPFDocument template = new XWPFDocument(new FileInputStream(
                templateFile.getSourceFile()));

        final XWPFDocument document = new XWPFDocument();
        // copy styles from template to new doc
        XWPFStyles newStyles = document.createStyles();
        newStyles.setStyles(template.getStyle());

        final ArrayList<RowId> selectedSongsRowIds = new ArrayList<RowId>();
        if (selectedSongs instanceof Set) {
            @SuppressWarnings("unchecked")
            Set<RowId> set = (Set<RowId>) selectedSongs;
            Iterator<RowId> iter = set.iterator();
            while (iter.hasNext()) {
                RowId row = (RowId) iter.next();
                selectedSongsRowIds.add(row);
            }
        } else {
            selectedSongsRowIds.add((RowId) selectedSongs);
        }
        

        // A thread to do some work
        class WorkThread extends Thread {
            // Volatile because read in another thread in access()
            volatile double current = 0.0;

            @Override
            public void run() {
                // Count up until 1.0 is reached
                while (current < 1.0) {
                    current += 0.01;

                    // Do some "heavy work"
                    for (RowId songitemId : selectedSongsRowIds) {
                        System.out.println("now getting row: " + songitemId);
                        writeSong(
                                document,
                                sqlContainter
                                        .getItem(songitemId)
                                        .getItemProperty(
                                                SongSQLContainer.propertyIds.songTitle
                                                        .toString()).getValue()
                                        .toString(),
                                sqlContainter
                                        .getItem(songitemId)
                                        .getItemProperty(
                                                SongSQLContainer.propertyIds.songLyrics
                                                        .toString()).getValue()
                                        .toString());
                    }
                    // tmpRunHeader.addBreak(BreakType.PAGE);

                    // write everything to file
                    // FileOutputStream fos = new FileOutputStream(new
                    // File("test\\"+filename + ".docx"));

                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(new File(outputFile));
                        document.write(fos);
                        fos.close();
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    
                    generatedFile = new FileResource(new File(outputFile));

                    try {
                        sleep(50); // Sleep for 50 milliseconds
                    } catch (InterruptedException e) {
                    }

                    // Update the UI thread-safely
                    UI.getCurrent().access(new Runnable() {
                        @Override
                        public void run() {
                            ((ProgressBar) progressComponents[1])
                                    .setValue(new Float(current));
                            if (current < 1.0)
                                ((Label) progressComponents[0]).setValue(""
                                        + ((int) (current * 100)) + "% done");
                            else
                                ((Label) progressComponents[0])
                                        .setValue("all done");
                        }
                    });
                }

                // Show the "all done" for a while
                try {
                    sleep(2000); // Sleep for 2 seconds
                } catch (InterruptedException e) {
                }

                // Update the UI thread-safely
                UI.getCurrent().access(new Runnable() {
                    @Override
                    public void run() {
                        // Restore the state to initial
                        ((ProgressBar) progressComponents[1])
                                .setValue(new Float(0.0));
                        ((ProgressBar) progressComponents[1]).setEnabled(false);

                        // Stop polling
                        UI.getCurrent().setPollInterval(-1);

                        // button.setEnabled(true);
                        ((Label) progressComponents[0]).setValue("not running");
                    }
                });
            }
        }
        
        new WorkThread().run();

        return generatedFile;
    }

    public static void main(String[] args) throws Exception {
        DocumentWriter doc = new DocumentWriter();
        ArrayList<String> song = ParserHelpers.readFile("test_data\\"
                + "inputTestSong");
        doc.newWordDoc("testOutputSong", "Test song", song);
    }
}
