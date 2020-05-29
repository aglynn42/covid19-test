package ca.datext.test.covid.domain;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Country {

	@XmlElement
	public String country;
	@XmlElement
	public String slug;
	@XmlElement
	public String shortForm;
	@XmlElement
	public String TotalConfirmed;
	@XmlElement
	public String Date;
}