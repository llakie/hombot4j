package de.bytevalue.hb4j.json;

import java.io.IOException;

import de.bytevalue.hb4j.ThreadPool;

public abstract class JsonConnectionListener {
	public abstract void onConnect(JsonConnection conn);
	public final void executeOnConnect(JsonConnection conn) {
		ThreadPool.getInstance().execute(() -> {
			this.onConnect(conn);
		});
	}
	
	public abstract void onDisconnect();
	public final void executeOnDisconnect() {
		ThreadPool.getInstance().execute(() -> {
			this.onDisconnect();
		});
	}
	
	public abstract void onBeforeRequest(JsonRequest request);
	public final void executeOnRequest(JsonRequest request) {
		ThreadPool.getInstance().execute(() -> {
			this.onBeforeRequest(request);
		});
	}
	
	public abstract void onResponse(JsonConnection conn, JsonResponse response);
	public final void executeOnResponse(JsonConnection conn, JsonResponse response) {
		ThreadPool.getInstance().execute(() -> {
			this.onResponse(conn, response);
		});
	}
	
	public abstract void onReadException(IOException ex);
	public final void executeOnReadException(IOException ex) {
		ThreadPool.getInstance().execute(() -> {
			this.onReadException(ex);
		});
	}
	
	public abstract void onConnectException(IOException ex);
	public final void executeOnConnectException(IOException ex) {
		ThreadPool.getInstance().execute(() -> {
			this.onConnectException(ex);
		});
	}
	
	public abstract void onSessionEstablished();
	public final void executeOnSessionEstablished() {
		ThreadPool.getInstance().execute(() -> {
			this.onSessionEstablished();
		});
	}
	
	public abstract void onSessionFailed(JsonResponse response);
	public final void executeOnSessionFailed(JsonResponse response) {
		ThreadPool.getInstance().execute(() -> {
			this.onSessionFailed(response);
		});
	}
}