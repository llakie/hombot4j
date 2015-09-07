package de.bytevalue.hb4j.json;
import java.nio.charset.Charset;

import org.json.JSONObject;
import org.json.JSONTokener;


public class JsonResponse {
	private JsonHeader header;
	
	private String payload;
	
	public JsonHeader getHeader() {
		return header;
	}
	
	public void setHeader(JsonHeader header) {
		this.header = header;
	}
	
	public JSONObject getPayload() {
		return new JSONObject(new JSONTokener(this.payload));
	}
	
	public void setPayload(byte[] payload) {
		this.payload = new String(payload, Charset.forName("UTF-8"));
		this.payload = this.payload.replaceAll("[\\r\\n]+", "");
	}
}