package de.bytevalue.hb4j.rest;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import de.bytevalue.hb4j.lg6720lvmb.Reservation;

@XmlRootElement
public class ParamDto {
	public String ip;
	public String name;
	
	@XmlElement(name="value")
	public String value;
	
	@XmlElement(name="reservation")
	public Reservation reservation;
	
	public ParamDto() {}
}
