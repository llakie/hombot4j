package de.bytevalue.hb4j.json;

import java.io.IOException;
import java.nio.charset.Charset;

public class DebugJsonConnectionListener extends JsonConnectionListener {
	@Override
	public void onResponse(JsonConnection conn, JsonResponse response) {
		System.out.println("---> onResponse()");
		System.out.println("Received: " + response.getPayload());
		System.out.println("Header");
		System.out.println(response.getHeader());
	}
	
	@Override
	public void onRequest(JsonRequest request) {
		System.out.println("<--- onRequest()");
		System.out.println("Sending: " + new String(request.getPayload(), Charset.forName("UTF-8")));
		System.out.println("Header");
		System.out.println(request.getHeader());
	}
	
	@Override
	public void onReadException(IOException ex) {
		System.out.println("onReadException()");
		ex.printStackTrace();
	}
	
	@Override
	public void onConnectException(IOException ex) {
		System.out.println("onConnectException()");
		ex.printStackTrace();
	}
	
	@Override
	public void onDisconnect() {
		System.out.println("onDisconnect()");
	}
	
	@Override
	public void onConnect(JsonConnection conn) {
		System.out.println("onConnect(): Port " + conn.getPort());
	}
	
	@Override
	public void onSessionEstablished() {
		System.out.println("onSessionEstablished(): Session erfolgreich initialisiert");
	}
	
	@Override
	public void onSessionFailed(JsonResponse response) {
		System.out.println("onSessionFailed(): An error ocurred while initializing the session");
		System.out.println("Received: " + response.getPayload());
	}
}