package ca.datext.test.covid.processing;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.ooxml.POIXMLDocument;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.xmlbeans.XmlException;
import org.eclipse.collections.api.map.MutableMap;

import ca.datext.test.covid.InitializeCache;
import ca.datext.test.covid.domain.Country;
import ca.datext.test.covid.domain.ISO3166Codes;

public class ProcessDocument {

	ProcessParagraphs data = new ProcessParagraphs(Logger.getLogger(ProcessDocument.class.getName()));
	POIXMLDocument doc;
	private XWPFWordExtractor extractor;
	private Logger logger;

	public XWPFDocument getDocument() {
		XWPFDocument xdoc = null;
		return getXWPFLDocument(xdoc);

	}

	public POIXMLDocument getPOIXMLDocument(XWPFDocument xdoc) {
		try {
			xdoc = new XWPFDocument(OPCPackage.open(new FileInputStream("sample.docx")));
			extractor = new XWPFWordExtractor(xdoc.getPackage());
			doc = extractor.getDocument();
		} catch (InvalidFormatException | IOException e) {
			data.logger.log(Level.ALL, "POI failed to read Word document:  " + e.getLocalizedMessage());
		} catch (XmlException e) {
			data.logger.log(Level.ALL, "POI failed to read Word document:  " + e.getLocalizedMessage());
		} catch (OpenXML4JException e) {
			data.logger.log(Level.ALL, "POI failed to read Word document:  " + e.getLocalizedMessage());
		}
		return doc;
	}

	private XWPFDocument getXWPFLDocument(XWPFDocument xdoc) {
		try {
			xdoc = new XWPFDocument(OPCPackage.open(new FileInputStream("sample.docx")));
		} catch (InvalidFormatException | IOException e) {
			data.logger.log(Level.ALL, "POI failed to read Word document:  " + e.getLocalizedMessage());
		}
		return xdoc;
	}

	public void process(XWPFDocument xdoc) {
		substituteAll();
	}

	private XWPFDocument substituteAll() {
		XWPFDocument xdoc = new ProcessDocument().getDocument();
		MutableMap<String, ISO3166Codes> mmap = InitializeCache.codesMap;
		MutableMap<String, Country> cmap = InitializeCache.countryMap;
		List<XWPFParagraph> paragraphs = new ProcessParagraphs(logger).getParagraphs(xdoc);
		new ProcessParagraphs(logger).substituteByParagraph(xdoc, mmap, cmap, paragraphs);
		return xdoc;
	}
}
