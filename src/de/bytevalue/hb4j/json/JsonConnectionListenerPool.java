package de.bytevalue.hb4j.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonConnectionListenerPool<T extends JsonConnectionListener> extends JsonConnectionListener {
	protected List<T> listeners;
	
	public JsonConnectionListenerPool() {
		this.listeners = new ArrayList<>();
	}
	
	public void addListener(T connectionListener) {
		this.listeners.add(connectionListener);
	}
	
	@Override
	public void onConnect(JsonConnection conn) {
		for(JsonConnectionListener listener: this.listeners) {
			listener.executeOnConnect(conn);
		}
	}

	@Override
	public void onDisconnect() {
		for(JsonConnectionListener listener: this.listeners) {
			listener.executeOnDisconnect();
		}
	}

	@Override
	public void onBeforeRequest(JsonRequest request) {
		for(JsonConnectionListener listener: this.listeners) {
			listener.executeOnRequest(request);
		}
	}

	@Override
	public void onResponse(JsonConnection conn, JsonResponse response) {
		for(JsonConnectionListener listener: this.listeners) {
			listener.executeOnResponse(conn, response);
		}
	}

	@Override
	public void onReadException(IOException ex) {
		for(JsonConnectionListener listener: this.listeners) {
			listener.executeOnReadException(ex);
		}
	}

	@Override
	public void onConnectException(IOException ex) {
		for(JsonConnectionListener listener: this.listeners) {
			listener.executeOnConnectException(ex);
		}
	}
	
	@Override
	public void onSessionEstablished() {
		for(JsonConnectionListener listener: this.listeners) {
			listener.executeOnSessionEstablished();
		}
	}
	
	@Override
	public void onSessionFailed(JsonResponse response) {
		for(JsonConnectionListener listener: this.listeners) {
			listener.executeOnSessionFailed(response);
		}
	}
}