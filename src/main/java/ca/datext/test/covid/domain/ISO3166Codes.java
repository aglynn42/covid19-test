package ca.datext.test.covid.domain;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ISO3166Codes {

	@XmlElement
	public static String name;
	@XmlElement
	public String alpha2Code;
	@XmlElement
	public String alpha3Code;

}
