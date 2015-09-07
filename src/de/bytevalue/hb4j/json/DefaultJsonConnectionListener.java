package de.bytevalue.hb4j.json;

import java.io.IOException;

public class DefaultJsonConnectionListener extends JsonConnectionListener {
	@Override
	public void onSessionEstablished() {}
	
	@Override
	public void onSessionFailed(JsonResponse response) {}
	
	@Override
	public void onConnect(JsonConnection conn) {}

	@Override
	public void onDisconnect() {}

	@Override
	public void onRequest(JsonRequest request) {}

	@Override
	public void onResponse(JsonConnection conn, JsonResponse response) {}

	@Override
	public void onReadException(IOException ex) {}

	@Override
	public void onConnectException(IOException ex) {}
}