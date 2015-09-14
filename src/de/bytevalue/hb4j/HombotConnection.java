package de.bytevalue.hb4j;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import de.bytevalue.hb4j.json.DefaultJsonConnectionListener;
import de.bytevalue.hb4j.json.JsonConnection;
import de.bytevalue.hb4j.json.JsonConnectionListener;
import de.bytevalue.hb4j.json.JsonConnectionListenerPool;
import de.bytevalue.hb4j.json.JsonRequest;
import de.bytevalue.hb4j.json.JsonResponse;

public class HombotConnection extends JsonConnectionListenerPool<JsonConnectionListener> {
	private static final int SESSION_PORT = 4002;
	private static final int DATA_PORT = 4000;

	private String ip;
	private JsonConnection jsonConnection;
	
	private Timer timer;
	private TimerTask periodicTask;
	
	public HombotConnection(String ip) {
		this.ip = ip;
		this.jsonConnection = new JsonConnection(this.ip, DATA_PORT);
		this.jsonConnection.addListener(this);
		this.timer = new Timer(true);
	}
	
	public final void connect() throws IOException {
		JsonConnection sessionConnection = new JsonConnection(this.ip, SESSION_PORT);
		
		sessionConnection.addListener(this);
		sessionConnection.addListener(new DefaultJsonConnectionListener() {
			@Override
			public void onResponse(JsonConnection sessionConnection, JsonResponse response) {
				JSONObject payload = response.getPayload();
				
				if(payload.getString("CONNECT") != null && payload.getString("CONNECT").equals("ENABLE")) {
					sessionConnection.onSessionEstablished();
				}
				else {
					sessionConnection.onSessionFailed(response);
				}
				
				sessionConnection.disconnect();
				
				if(!initDataConnection()) {
					HombotConnection.this.disconnect();
				}
			}
		});
		
		sessionConnection.connect();
		sessionConnection.sendRequest(new JsonRequest("{\"CONNECT\":\"REQUEST\"}"));
	}
	
	public final boolean isConnected() {
		return this.jsonConnection.isConnected();
	}
	
	public final void disconnect() {
		this.jsonConnection.disconnect();
		
		if(this.periodicTask != null) {
			this.periodicTask.cancel();
		}
	}
	
	public final synchronized int sendRequest(JsonRequest request) throws IOException {
		return this.jsonConnection.sendRequest(request);
	}
	
	private boolean initDataConnection() {
		try {
			jsonConnection.connect();
		}
		catch(IOException ex) {
			jsonConnection.onConnectException(ex);
			return false;
		}
		
		this.periodicTask = new TimerTask() {
			@Override
			public void run() {
				try {
					jsonConnection.sendRequest(new JsonRequest("{\"SESSION\":\"ALIVE\"}"));
				}
				catch (IOException e) {}
			}
		};
		
		this.timer.schedule(this.periodicTask, 3000, 5000);
		
		return true;
	}	
}