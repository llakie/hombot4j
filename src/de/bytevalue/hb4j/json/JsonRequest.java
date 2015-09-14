package de.bytevalue.hb4j.json;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class JsonRequest {
	private byte[] payload;
	private int id;
	private boolean isHeartbeat = false;
	
	public JsonRequest(String payload) {
		this.payload = payload.getBytes(Charset.forName("UTF-8"));
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public boolean isHeartbeat() {
		return this.isHeartbeat;
	}
	
	public void setHeartbeat(boolean isHeartbeat) {
		this.isHeartbeat = isHeartbeat;
	}
	
	public byte[] getPayload() {
		return payload;
	}
	
	public byte[] getBytes() {
		ByteBuffer buf = ByteBuffer.allocate(JsonHeader.SIZE + payload.length);
		
		JsonHeader header = this.getHeader();
		
		buf.put(header.getBytes());
		buf.put(payload);
		
		return buf.array();
	}
	
	public JsonHeader getHeader() {
		JsonHeader header = new JsonHeader();
		header.setId(this.id);
		header.setLength((short)this.payload.length);
		return header;
	}
}