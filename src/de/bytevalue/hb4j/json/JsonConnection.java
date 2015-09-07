package de.bytevalue.hb4j.json;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;

public final class JsonConnection extends JsonConnectionListenerPool<JsonConnectionListener> implements Runnable {
	private Socket s;
	private InputStream is;
	private OutputStream os;
	private int port;
	private String ip;
	private int requestId = 0;
	
	public JsonConnection(String ip, int port) {
		super();
		this.ip = ip;
		this.port = port;
	}
	
	public void connect() throws IOException {
		try {
			this.disconnect();
			
			this.s = new Socket();
			this.s.setSoTimeout(0);
			this.s.setTcpNoDelay(true);
			this.s.connect(new InetSocketAddress(this.ip, port), 2000);
		}
		catch(IOException ioe) {
			this.onConnectException(ioe);
			throw ioe;
		}
		
		try {
			this.is = s.getInputStream();
			this.os = s.getOutputStream();
			this.onConnect(this);
			new Thread(this).start();
		}
		catch(IOException ioe) {
			this.onConnectException(ioe);
			this.disconnect();
			throw ioe;
		}
	}
	
	public void reconnect() throws IOException {
		this.disconnect();
		this.connect();
	}
	
	public void disconnect() {
		if(!this.isConnected()) {
			return;
		}
		
		try {
			this.s.close();
		}
		catch(IOException ioe) {
			// Ignore
		}
		finally {
			this.onDisconnect();
		}
	}
	
	public boolean isConnected() {
		if(this.s == null) {
			return false;
		}
		
		return !this.s.isClosed();
	}
	
	public int getPort() {
		return this.s.getPort();
	}
	
	public int sendRequest(JsonRequest request) throws IOException {
		if(this.requestId == 256) {
			this.requestId = 0;
		}
		
		request.setId(this.requestId++);
		
		this.onRequest(request);
		
		this.os.write(request.getBytes());
		this.os.flush();
		
		return request.getId();
	}
	
	public void run() {
		try {
			byte[] buffer = new byte[65536];
			
			while(true) {
				JsonHeader header = this.readHeader();
				
				int position = 0;
				int bytesRead;
				int payloadLength = header.getPayloadLength();				
				
				do {
					bytesRead = this.is.read(buffer, position, payloadLength - position);
					position += bytesRead;
				}
				while (position < payloadLength && bytesRead >= 0);
			
				if(bytesRead == -1) {
					throw new IOException("Could not read Bytes for payload from InputStream @ Port " + this.getPort());
				}
				
				JsonResponse response = new JsonResponse();
				response.setHeader(header);
				response.setPayload(ByteBuffer.wrap(buffer, 0, position).array());
				
				this.onResponse(this, response);
			}
		}	
		catch (IOException ioe) {
			// Ignore
		}
	}
	
	private JsonHeader readHeader() throws IOException {
		byte[] headerData = new byte[JsonHeader.SIZE];
		int position = 0;
		int bytesRead;
		
		do {
			int bytesLeft = headerData.length - position;
			
			bytesRead = this.is.read(headerData, position, bytesLeft);
			
			position += bytesRead;
		} 
		while (position < headerData.length && bytesRead >= 0);
		
		if(bytesRead == -1) {
			throw new IOException("Could not read Bytes for header from InputStream @ Port " + this.getPort());
		}
		
		JsonHeader header = new JsonHeader();
		header.parse(headerData);
		
		return header;
	}
}