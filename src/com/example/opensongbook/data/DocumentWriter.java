package com.example.opensongbook.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

import org.apache.poi.xwpf.usermodel.Borders;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFStyles;

import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinService;

public class DocumentWriter {
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

	public static void main(String[] args) throws Exception {
		DocumentWriter doc = new DocumentWriter();
		ArrayList<String> song = ParserHelpers.readFile("test_data\\"
				+ "inputTestSong");
		doc.newWordDoc("testOutputSong", "Test song", song);
	}
}
