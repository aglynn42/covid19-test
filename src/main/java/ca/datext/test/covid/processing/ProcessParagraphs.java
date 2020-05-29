package ca.datext.test.covid.processing;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.eclipse.collections.api.map.MutableMap;

import ca.datext.test.covid.domain.Country;
import ca.datext.test.covid.domain.ISO3166Codes;

public class ProcessParagraphs {
	Logger logger;

	public ProcessParagraphs(Logger logger) {
		this.logger = logger;
	}

	List<XWPFParagraph> getParagraphs(XWPFDocument xdoc) {
		return xdoc.getParagraphs();
	}

	public void substituteByParagraph(XWPFDocument xdoc, MutableMap<String, ISO3166Codes> mmap,
			MutableMap<String, Country> cmap, List<XWPFParagraph> paragraphs) {
		String fullName;
		for (XWPFParagraph paragraph : paragraphs) {
			int j = 0;
			String txt = paragraphs.get(j).getText();
			j++;
			String longName = txt.substring(txt.indexOf("[COVID-19", (txt.indexOf("]", txt.indexOf("[COVID-19")))));
			final String countryName = longName.substring(longName.indexOf(" " + 1));
			boolean found = mmap.containsKey(countryName);
			if (found) {
				Country details = cmap.get(countryName);
				txt.replaceFirst("^/[COVID-19[a-zA-Z][0-9]{3,}/]$/", countryName + ": Cases: " + details.TotalConfirmed);
			} else {
				if (countryName.length() == 2) {
					Stream<ISO3166Codes> strm = mmap.stream().filter(e -> e.alpha2Code.equalsIgnoreCase(countryName));
					strm.iterator().next();
					fullName = ISO3166Codes.name;
					Country details = cmap.get(fullName);
					txt.replaceFirst("^/[COVID-19[a-zA-Z][0-9]{3,}/]$/", fullName + ": Cases: " + details.TotalConfirmed);

				} else if (countryName.length() == 3) {
					Stream<ISO3166Codes> strm = mmap.stream().filter(i -> i.alpha3Code.equalsIgnoreCase(countryName));
					strm.iterator().next();
					fullName = ISO3166Codes.name;
					Country details = cmap.get(fullName);
					txt.replaceFirst("^/[COVID-19[a-zA-Z][0-9]{3,}/]$/", countryName + ": Cases: " + details.TotalConfirmed);

				} else {
					logger.log(Level.ALL, "Country not found in ISO list.");
				}

			}

			paragraph = substituteCOVID_19(paragraph);
			xdoc.setParagraph(paragraph, j);
		}
	}

	private XWPFParagraph substituteCOVID_19(final XWPFParagraph paragraph) {
		paragraph.getParagraphText().replaceAll("COVID-19", "Vaccine");
		return paragraph;

	}
}