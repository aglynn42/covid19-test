/*
 * Copyright (c) 2016, WSO2 Inc. (http://wso2.com) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ca.datext.test.covid;

import java.io.FileOutputStream;
import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.poi.ooxml.POIXMLDocument;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import ca.datext.test.covid.processing.ProcessDocument;

/**
 * This is the Microservice resource class. See <a href=
 * "https://github.com/wso2/msf4j#getting-started">https://github.com/wso2/msf4j#getting-started</a>
 * for the usage of annotations.
 *
 * @since 1.0.0-SNAPSHOT
 */
@Path("/covid")
public class CovidService {

	@GET
	@Path("/country/{countryName}")
	@Produces("APPLICATION/JSON")
	@Consumes("APPLICATION/JSON")
	public String get() {
		POIXMLDocument doc;
		InitializeCaches();
		ProcessDocument processDoc = new ProcessDocument();

		XWPFDocument xdoc = processDoc.getDocument();
		doc = processDoc.getPOIXMLDocument(xdoc);
		doc.setCommitted(true);
		try {
			doc.write(new FileOutputStream("converted.docx"));
		} catch (IOException e) {
			return "I/O Exception converting document";
		}
		return "Document conversion successful";
	}

	private void InitializeCaches() {
		InitializeCache.buildCountryCache();
		InitializeCache.buildISOCache();
	}

}
