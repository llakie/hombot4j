package de.bytevalue.hb4j.rest;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ResponseDto {
	public String ip;
	public String message;
	
	public ResponseDto() {}
	
	public ResponseDto(String ip, String message) {
		this.ip = ip;
		this.message = message;
	}
}
