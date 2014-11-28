package org.duckdns.valci.opensongbook.data;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;

import javax.xml.bind.JAXBElement;

import org.docx4j.XmlUtils;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.openpackaging.parts.WordprocessingML.StyleDefinitionsPart;
import org.docx4j.wml.Body;
import org.docx4j.wml.Br;
import org.docx4j.wml.CTBorder;
import org.docx4j.wml.CTLanguage;
import org.docx4j.wml.Document;
import org.docx4j.wml.HpsMeasure;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.P;
import org.docx4j.wml.PPr;
import org.docx4j.wml.PPrBase;
import org.docx4j.wml.ParaRPr;
import org.docx4j.wml.ProofErr;
import org.docx4j.wml.R;
import org.docx4j.wml.RFonts;
import org.docx4j.wml.RPr;
import org.docx4j.wml.Styles;
import org.docx4j.wml.Text;

public class DocxWriter {

    public static void testDocxWrite(ArrayList<SongClass> songs) {
        String templatePath = "/home/samuel/DEVELOPMENT/test/template.dotx";
        String testOutputPath = "/home/samuel/DEVELOPMENT/test/output.docx";
        // open template
        WordprocessingMLPackage wordMLPackage = null;
        MainDocumentPart wordDocumentPart = null;

        // Create the package
        //WordprocessingMLPackage wordMLPackage = new WordprocessingMLPackage();
        // Create the main document part (word/document.xml)
        
        try {
             wordMLPackage = WordprocessingMLPackage.load(new File(templatePath));
             wordDocumentPart = wordMLPackage.getMainDocumentPart();
        } catch (Docx4JException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        // Create main document part content
        ObjectFactory wmlObjectFactory = Context.getWmlObjectFactory();
        Body body = wmlObjectFactory.createBody();
        Document wmlDocumentEl = wmlObjectFactory.createDocument();
        wmlDocumentEl.setBody(body);
        
        //*********************PARAGRAPH****************************
        /*
        
        // Create object for p
        P p = wmlObjectFactory.createP(); 
        body.getContent().add( p); 
            // Create object for pPr
            PPr ppr = wmlObjectFactory.createPPr(); 
            p.setPPr(ppr); 
                // Create object for rPr
                ParaRPr pararpr = wmlObjectFactory.createParaRPr(); 
                ppr.setRPr(pararpr); 
                // Create object for pStyle
                PPrBase.PStyle pprbasepstyle = wmlObjectFactory.createPPrBasePStyle(); 
                ppr.setPStyle(pprbasepstyle); 
                    pprbasepstyle.setVal( "Heading1"); 
            // Create object for r
            R r = wmlObjectFactory.createR(); 
            p.getContent().add( r); 
                // Create object for t (wrapped in JAXBElement) 
                Text text = wmlObjectFactory.createText(); 
                JAXBElement<org.docx4j.wml.Text> textWrapped = wmlObjectFactory.createRT(text); 
                r.getContent().add( textWrapped); 
                    text.setValue( "1000 "); 
                    text.setSpace( "preserve"); 
            // Create object for r
            R r2 = wmlObjectFactory.createR(); 
            p.getContent().add(r2); 
                // Create object for t (wrapped in JAXBElement) 
                Text text2 = wmlObjectFactory.createText(); 
                JAXBElement<org.docx4j.wml.Text> textWrapped2 = wmlObjectFactory.createRT(text2); 
                r2.getContent().add( textWrapped2); 
                    text2.setValue( "Razloga"); 
                
          /////////////////////////////////////////////////////////////
                // Create object for p
                P p4 = wmlObjectFactory.createP(); 
                body.getContent().add(p4); 
                    // Create object for pPr
                    PPr ppr4 = wmlObjectFactory.createPPr(); 
                    p4.setPPr(ppr4); 
                        // Create object for rPr
                        ParaRPr pararpr4 = wmlObjectFactory.createParaRPr(); 
                        ppr4.setRPr(pararpr4); 
                            // Create object for rFonts
                            RFonts rfonts4 = wmlObjectFactory.createRFonts(); 
                            pararpr4.setRFonts(rfonts4); 
                                rfonts4.setCs( "Courier New"); 
                                rfonts4.setAscii( "Courier New"); 
                                rfonts4.setHAnsi( "Courier New"); 
                        // Create object for pStyle
                        PPrBase.PStyle pprbasepstyle4 = wmlObjectFactory.createPPrBasePStyle(); 
                        ppr4.setPStyle(pprbasepstyle4); 
                            pprbasepstyle4.setVal( "NoSpacing"); 
                    // Create object for r
                    R r4 = wmlObjectFactory.createR(); 
                    p4.getContent().add( r4); 
                        // Create object for rPr
                        RPr rpr4 = wmlObjectFactory.createRPr(); 
                        r4.setRPr(rpr4); 
                            // Create object for rFonts
                            RFonts rfonts5 = wmlObjectFactory.createRFonts(); 
                            rpr4.setRFonts(rfonts5); 
                                rfonts5.setCs( "Courier New"); 
                                rfonts5.setAscii( "Courier New"); 
                                rfonts5.setHAnsi( "Courier New"); 
                        // Create object for t (wrapped in JAXBElement) 
                        Text text4 = wmlObjectFactory.createText(); 
                        JAXBElement<org.docx4j.wml.Text> textWrapped4 = wmlObjectFactory.createRT(text4); 
                        r4.getContent().add( textWrapped4); 
                            text4.setValue( "      F    C   G/E    Am"); 
                            text4.setSpace( "preserve");
                       //// THIS IS TRY TO ADD PAGE BREAK
                       // Create object for lastRenderedPageBreak (wrapped in JAXBElement) 
                            // Create object for br
                            Br br = wmlObjectFactory.createBr(); 
                            r4.getContent().add( br); 
                                br.setType(org.docx4j.wml.STBrType.PAGE);
   
         */     
        //************************************************************

        for (SongClass song:songs){
         // Create object for p
            P p = wmlObjectFactory.createP(); 
            body.getContent().add( p); 
                // Create object for pPr
                PPr ppr = wmlObjectFactory.createPPr(); 
                p.setPPr(ppr); 
                    // Create object for pStyle
                    PPrBase.PStyle pprbasepstyle = wmlObjectFactory.createPPrBasePStyle(); 
                    ppr.setPStyle(pprbasepstyle); 
                        pprbasepstyle.setVal( "Heading1"); 
                    // Create object for pBdr
                    PPrBase.PBdr pprbasepbdr = wmlObjectFactory.createPPrBasePBdr(); 
                    ppr.setPBdr(pprbasepbdr); 
                        // Create object for bottom
                        CTBorder border = wmlObjectFactory.createCTBorder(); 
                        pprbasepbdr.setBottom(border); 
                            border.setVal(org.docx4j.wml.STBorder.SINGLE);
                // Create object for r
                R r = wmlObjectFactory.createR(); 
                p.getContent().add( r); 
                    // Create object for rPr
                    RPr rpr = wmlObjectFactory.createRPr(); 
                    r.setRPr(rpr); 
                        // Create object for rFonts
                        RFonts rfonts = wmlObjectFactory.createRFonts(); 
                        rpr.setRFonts(rfonts); 
                            rfonts.setAscii( "Arial"); 
                            rfonts.setHAnsi( "Arial"); 
                        // Create object for sz
                        HpsMeasure hpsmeasure = wmlObjectFactory.createHpsMeasure(); 
                        rpr.setSz(hpsmeasure); 
                            hpsmeasure.setVal( BigInteger.valueOf( 36) ); 
                    // Create object for t (wrapped in JAXBElement) 
                    Text text = wmlObjectFactory.createText(); 
                    JAXBElement<org.docx4j.wml.Text> textWrapped = wmlObjectFactory.createRT(text); 
                    r.getContent().add( textWrapped); 
                        text.setValue( song.getTitle()); 
            // Create object for p
            P p2 = wmlObjectFactory.createP(); 
            body.getContent().add( p2); 
                // Create object for pPr
                PPr ppr2 = wmlObjectFactory.createPPr(); 
                p2.setPPr(ppr2); 
                    // Create object for pStyle
                    PPrBase.PStyle pprbasepstyle2 = wmlObjectFactory.createPPrBasePStyle(); 
                    ppr2.setPStyle(pprbasepstyle2); 
                        pprbasepstyle2.setVal( "NoSpacing"); 
                String[] lines = song.getLyrics().split("\n");
                
                
                R r2 = null;
                for(String line:lines){
                // Create object for r
                r2 = wmlObjectFactory.createR(); 
                p2.getContent().add( r2); 
                    // Create object for rPr
                    RPr rpr2 = wmlObjectFactory.createRPr(); 
                    r2.setRPr(rpr2); 
                        // Create object for rFonts
                        RFonts rfonts2 = wmlObjectFactory.createRFonts(); 
                        rpr2.setRFonts(rfonts2); 
                            rfonts2.setAscii( "Courier New"); 
                            rfonts2.setHAnsi( "Courier New"); 
                        // Create object for sz
                        HpsMeasure hpsmeasure2 = wmlObjectFactory.createHpsMeasure(); 
                        rpr2.setSz(hpsmeasure2); 
                            hpsmeasure2.setVal( BigInteger.valueOf( 28) ); 
                    // Create object for t (wrapped in JAXBElement) 
                    Text text2 = wmlObjectFactory.createText(); 
                    JAXBElement<org.docx4j.wml.Text> textWrapped2 = wmlObjectFactory.createRT(text2); 
                    r2.getContent().add( textWrapped2); 
                        text2.setValue(line); 
                        text2.setSpace( "preserve"); 
                }
                // Create object for br
                    Br br = wmlObjectFactory.createBr();
                    r2.getContent().add( br); 
                        br.setType(org.docx4j.wml.STBrType.PAGE);
            // Create object for p
        }

        
        //*********************************************************
        
        // Put the content in the part
        wordDocumentPart.setJaxbElement(wmlDocumentEl);
                
        // Add the main document part to the package relationships
        // (creating it if necessary)
        try {
            wordMLPackage.addTargetPart(wordDocumentPart);
        } catch (InvalidFormatException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        
       // WordprocessingMLPackage wordMLPackage;
        try {
            //WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
            //MainDocumentPart mdp = wordMLPackage.getMainDocumentPart();
            //mdp.addStyledParagraphOfText("Heading1", title);
            //wordMLPackage.getMainDocumentPart().addParagraphOfText("Hello Word! čćžšđ");
            wordMLPackage.save(new java.io.File(testOutputPath));
        } catch (InvalidFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Docx4JException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        /*
     // Create the package
        WordprocessingMLPackage wordMLPackage2 = WordprocessingMLPackage.createPackage();
     
        // Create the main document part (word/document.xml)
        MainDocumentPart wordDocumentPart = new MainDocumentPart();
     
        // Create main document part content
        ObjectFactory factory = Context.getWmlObjectFactory();
        org.docx4j.wml.Body body = factory .createBody();
        org.docx4j.wml.Document wmlDocumentEl = factory .createDocument();
        wmlDocumentEl.setBody(body);
        
        // Put the content in the part
        wordDocumentPart.setJaxbElement(wmlDocumentEl);
                
        
        // Add the main document part to the package relationships
        // (creating it if necessary)
       
        wmlPack.addTargetPart(wordDocumentPart);
        */
    }

    public static void main(String[] args) {
        DatabaseHelper dh = new DatabaseHelper(false);
        ArrayList<SongClass> songs = new ArrayList<SongClass>();
        for (int i=1; i<10; i++){
        String songTitle = dh.executeCustomQuerry("SELECT SONGTITLE FROM " + SongSQLContainer.TABLE + " WHERE ID="+i);
        String songLyrics = dh.executeCustomQuerry("SELECT SONGLYRICS FROM " + SongSQLContainer.TABLE + " WHERE ID="+i);
        SongClass song = new SongClass();
        song.setTitle(songTitle);
        song.setLyrics(songLyrics);
        songs.add(song);
        System.out.println(song.getTitle());
        System.out.println(song.getLyrics());
        }

        DocxWriter.testDocxWrite(songs);
    }

}
