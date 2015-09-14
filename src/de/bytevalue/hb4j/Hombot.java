package de.bytevalue.hb4j;

import java.io.IOException;

import de.bytevalue.hb4j.json.JsonRequest;

public abstract class Hombot<T extends HombotModel> {
	private HombotConnection conn;
	protected T model;
	
	public Hombot(String ip) {
		this.model = this.createModelAdapter();
		this.conn = new HombotConnection(ip);
		this.conn.addListener(this.model);
	}
	
	public T getModel() {
		return this.model;
	}
	
	public boolean isConnected() {
		return this.conn.isConnected();
	}
	
	public void connect() throws IOException {
		if(!this.isConnected()) {
			this.conn.connect();
		}
	}
	
	public void disconnect() {
		if(this.isConnected()) {
			this.conn.disconnect();
		}
	}
	
	public int sendRequest(JsonRequest request) throws IOException {
		return this.conn.sendRequest(request);
	}
	
	protected abstract T createModelAdapter();
}