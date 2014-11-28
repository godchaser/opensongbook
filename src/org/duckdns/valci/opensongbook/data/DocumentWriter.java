package org.duckdns.valci.opensongbook.data;

import java.io.File;
import java.io.FileInputStream;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.sift.SiftAction;

import com.vaadin.data.util.sqlcontainer.RowId;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinService;

public class DocumentWriter {
    static final Logger LOG = LoggerFactory.getLogger(DocumentWriter.class);

    private String songTitleFont = "Arial";
    private int songTitleSize = 18;
    private String songLyricsFont = "Courier New";
    private int songLyricsFontSize = 14;
    FileResource generatedFile;

    public void writeSong(XWPFDocument document, String songTitle, String songLyrics, int songTotalNumber) {
        LOG.trace("Exporting song: " + songTitle);
        XWPFParagraph tmpHeader = document.createParagraph();
        XWPFRun tmpRunHeader = tmpHeader.createRun();
        tmpRunHeader.setFontFamily(songTitleFont);
        tmpRunHeader.getCTR().getRPr().getRFonts().setHAnsi(songTitleFont);
        tmpRunHeader.setText(songTitle);
        tmpRunHeader.setFontSize(songTitleSize);
        tmpHeader.setStyle("Heading1");
        tmpHeader.setBorderBottom(Borders.SINGLE);

        // Create Song body
        XWPFParagraph tmpParagraph = document.createParagraph();

        XWPFRun tmpRun = null;
        // breaking song by new lines so we can write multiple runs in paragraph and add break after each reun
        String[] songLines = songLyrics.split("\\r?\\n");

        for (String songLine : songLines) {
            tmpRun = tmpParagraph.createRun();
            tmpRun.setFontFamily(songLyricsFont);
            tmpRun.getCTR().getRPr().getRFonts().setHAnsi(songLyricsFont);
            tmpRun.setText(songLine);
            tmpRun.setFontSize(songLyricsFontSize);
            tmpRun.addBreak(BreakType.TEXT_WRAPPING);
            tmpParagraph.setStyle("NoSpacing");
        }
        if (songTotalNumber != -1) {
            tmpRun.addBreak(BreakType.PAGE);
        }
    }

    public FileResource newSongbookWordDoc(String filename, final SQLContainer sqlContainter, Object selectedSongs,
            final Object[] progressComponents) throws Exception {

        final String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
        final String outputFile = basepath + "/WEB-INF/resources/" + "filename" + ".docx";
        FileResource templateFile = new FileResource(new File(basepath + "/WEB-INF/resources/template.dotx"));
        XWPFDocument template = new XWPFDocument(new FileInputStream(templateFile.getSourceFile()));

        final XWPFDocument document = new XWPFDocument();
        // copy styles from template to new doc
        XWPFStyles newStyles = document.createStyles();
        newStyles.setStyles(template.getStyle());

        final ArrayList<RowId> selectedSongsRowIds = new ArrayList<RowId>();
        if (selectedSongs instanceof Set) {
            LOG.trace("Multiple song export");
            @SuppressWarnings("unchecked")
            Set<RowId> set = (Set<RowId>) selectedSongs;
            Iterator<RowId> iter = set.iterator();
            while (iter.hasNext()) {
                RowId row = (RowId) iter.next();
                selectedSongsRowIds.add(row);
            }
        } else {
            LOG.trace("Single song export");
            selectedSongsRowIds.add((RowId) selectedSongs);
        }

        boolean singleSong = (selectedSongsRowIds.size() == 1) ? true : false;
        int songTotalNumber = (singleSong) ? -1 : selectedSongsRowIds.size();
        LOG.trace("Number of songs (-1 if single page): " + songTotalNumber);

        for (int i = 0; i < selectedSongsRowIds.size(); i++) {
            RowId row = selectedSongsRowIds.get(i);
            
            // if this is single page or last page then we don't need page break
            if (singleSong || (i == selectedSongsRowIds.size()-1)) {
                songTotalNumber = -1;
            } else {
                songTotalNumber = i;
            }
            LOG.trace("Now exporting songId: " + selectedSongsRowIds.get(i));
            writeSong(document,
                    sqlContainter.getItem(row).getItemProperty(SongSQLContainer.propertyIds.SONGTITLE.toString())
                            .getValue().toString(),
                    sqlContainter.getItem(row).getItemProperty(SongSQLContainer.propertyIds.SONGLYRICS.toString())
                            .getValue().toString(), songTotalNumber);
        }

        FileOutputStream fos = null;
        try {
            LOG.trace("Now writing to exported file: " + outputFile);
            fos = new FileOutputStream(new File(outputFile));
            document.write(fos);
            fos.close();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        generatedFile = new FileResource(new File(outputFile));

        return generatedFile;
    }

    public static void main(String[] args) throws Exception {
        // DocumentWriter doc = new DocumentWriter();
        // ArrayList<String> song = ParserHelpers.readFile("test_data\\" + "inputTestSong");
        // TODO: this should be unit testable somehow?!
        // doc.newSongbookWordDoc("testOutputSong", "Test song", song);
    }
}
