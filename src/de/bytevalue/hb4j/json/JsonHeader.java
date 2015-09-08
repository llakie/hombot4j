package de.bytevalue.hb4j.json;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class JsonHeader {
	public static final short MAGIC_NUMBER = 0x40D;
	public static final int SIZE = 12;
	
	private int id = 0;
	private short length = 0;
	
	private int paramInt2 = 0;
	
	private short paramShort1 = 0;
	private short paramShort2 = 1;
	
	public int getParamInt2() {
		return paramInt2;
	}
	
	public void setParamInt2(int paramInt2) {
		this.paramInt2 = paramInt2;
	}
	
	public short getParamShort1() {
		return paramShort1;
	}
	
	public void setParamShort1(short paramShort1) {
		this.paramShort1 = paramShort1;
	}
	
	public short getParamShort2() {
		return paramShort2;
	}
	
	public void setParamShort2(short paramShort2) {
		this.paramShort2 = paramShort2;
	}
	
	public int getPayloadLength() {
		return length;
	}
	
	public void setLength(short length) {
		this.length = length;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void parse(byte[] input) {
		this.id = input[2] & 0xFF;
		this.paramInt2 = input[3] & 0xFF;
		this.paramShort1 = ByteBuffer.wrap(input, 4, 2).order(ByteOrder.LITTLE_ENDIAN).getShort();
		this.paramShort2 = ByteBuffer.wrap(input, 6, 2).order(ByteOrder.LITTLE_ENDIAN).getShort();
		this.length = ByteBuffer.wrap(input, 8, 2).order(ByteOrder.LITTLE_ENDIAN).getShort();
	}
	
	public byte[] getBytes() {
		byte[] headerData = new byte[JsonHeader.SIZE];
		
		ByteBuffer.wrap(headerData, 0, 2).order(ByteOrder.LITTLE_ENDIAN).putShort(JsonHeader.MAGIC_NUMBER);
		
		headerData[2] = (byte)this.id;
		headerData[3] = (byte)(this.paramInt2 & 0xFF);
		
		// Irgendein Parameter
		ByteBuffer.wrap(headerData, 4, 2).order(ByteOrder.LITTLE_ENDIAN).putShort(this.paramShort1);
		// So etwas wie 01 = Request, 10 Response
		ByteBuffer.wrap(headerData, 6, 2).order(ByteOrder.LITTLE_ENDIAN).putShort(this.paramShort2);
		// Länge des Datenpakets
		ByteBuffer.wrap(headerData, 8, 2).order(ByteOrder.LITTLE_ENDIAN).putShort(this.length);
		
		// Die letzten beiden Bytes werden implizit auf 0 gelassen. Wahrscheinlich handelt es sich um eine Art Trenner
		
		return headerData;
	}
	
	public int getTotalLength() {
		return JsonHeader.SIZE + this.getPayloadLength();
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		
		buf.append("Id: " + this.id + "\n");
		buf.append("Payload: " + this.length + " Bytes\n");
		buf.append("ParamInt2: " + this.paramInt2 + "\n");
		buf.append("ParamShort1: " + this.paramShort1 + "\n");
		buf.append("ParamShort2: " + this.paramShort2);
		
		return buf.toString();
	}
	
	public String getBytesAsString() {
		StringBuilder sb = new StringBuilder();
		
		byte[] rawBytes = this.getBytes();
		
		for(int i = 0; i < rawBytes.length; i++) {
			sb.append(rawBytes[i] & 0xFF);
			sb.append(',');
		}
		
		sb.deleteCharAt(sb.length() - 1);
	
		return sb.toString();
	}
}