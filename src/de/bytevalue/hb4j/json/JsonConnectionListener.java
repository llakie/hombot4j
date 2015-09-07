package de.bytevalue.hb4j.json;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public abstract class JsonConnectionListener {
	protected static ExecutorService threadPool;
	
	static {
		JsonConnectionListener.threadPool = Executors.newCachedThreadPool();
	}
	
	public static void initThreadPool() {
		JsonConnectionListener.shutdownThreadPool();
		JsonConnectionListener.threadPool = Executors.newCachedThreadPool();
	}
	
	public static void shutdownThreadPool() {
		if(JsonConnectionListener.threadPool == null) {
			return;
		}
		
		if(JsonConnectionListener.threadPool.isShutdown()) {
			return;
		}
		
		try {
			JsonConnectionListener.threadPool.awaitTermination(1000, TimeUnit.MILLISECONDS);
		}
		catch (InterruptedException e) {
			// Ignore
		}
		
		JsonConnectionListener.threadPool.shutdown();
	}
	
	public abstract void onConnect(JsonConnection conn);
	public final void executeOnConnect(JsonConnection conn) {
		JsonConnectionListener.threadPool.execute(() -> {
			this.onConnect(conn);
		});
	}
	
	public abstract void onDisconnect();
	public final void executeOnDisconnect() {
		JsonConnectionListener.threadPool.execute(() -> {
			this.onDisconnect();
		});
	}
	
	public abstract void onRequest(JsonRequest request);
	public final void executeOnRequest(JsonRequest request) {
		JsonConnectionListener.threadPool.execute(() -> {
			this.onRequest(request);
		});
	}
	
	public abstract void onResponse(JsonConnection conn, JsonResponse response);
	public final void executeOnResponse(JsonConnection conn, JsonResponse response) {
		JsonConnectionListener.threadPool.execute(() -> {
			this.onResponse(conn, response);
		});
	}
	
	public abstract void onReadException(IOException ex);
	public final void executeOnReadException(IOException ex) {
		JsonConnectionListener.threadPool.execute(() -> {
			this.onReadException(ex);
		});
	}
	
	public abstract void onConnectException(IOException ex);
	public final void executeOnConnectException(IOException ex) {
		JsonConnectionListener.threadPool.execute(() -> {
			this.onConnectException(ex);
		});
	}
	
	public abstract void onSessionEstablished();
	public final void executeOnSessionEstablished() {
		JsonConnectionListener.threadPool.execute(() -> {
			this.onSessionEstablished();
		});
	}
	
	public abstract void onSessionFailed(JsonResponse response);
	public final void executeOnSessionFailed(JsonResponse response) {
		JsonConnectionListener.threadPool.execute(() -> {
			this.onSessionFailed(response);
		});
	}
}