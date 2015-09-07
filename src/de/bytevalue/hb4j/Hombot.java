package de.bytevalue.hb4j;

import java.io.IOException;

import de.bytevalue.hb4j.json.JsonRequest;

public abstract class Hombot<T extends HombotModel> {
	private HombotConnection conn;
	private T model;
	
	public Hombot(String ip) {
		this.model = this.createModel();
		this.conn = new HombotConnection(ip);
		this.conn.addListener(this.model);
	}
	
	public T getModel() {
		return this.model;
	}
	
	public void connect() throws IOException {
		this.conn.connect();
	}
	
	public void disconnect() {
		this.conn.disconnect();
	}
	
	public int sendRequest(JsonRequest request) throws IOException {
		return this.conn.sendRequest(request);
	}
	
	protected abstract T createModel();
}