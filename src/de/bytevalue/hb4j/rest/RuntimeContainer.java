package de.bytevalue.hb4j.rest;

import java.util.Timer;
import java.util.TimerTask;

import de.bytevalue.hb4j.Hombot;
import de.bytevalue.hb4j.json.DefaultJsonConnectionListener;
import de.bytevalue.hb4j.json.JsonRequest;

public class RuntimeContainer<T extends Hombot<?>> extends DefaultJsonConnectionListener {
	private static final int DISCONNECT_AFTER_IDLE_IN_MS = 60000;

	private Timer disconnectTimer;
	private TimerTask disconnectTimerTask;
	
	public T hombot;
	
	public RuntimeContainer(T hombot) {
		this.hombot = hombot;
		this.hombot.addConnectionListener(this);
		this.disconnectTimer = new Timer(true);
	}
	
	@Override
	public void onBeforeRequest(JsonRequest request) {
		if(!request.isHeartbeat()) {
			if(this.disconnectTimerTask != null) {
				this.disconnectTimerTask.cancel();
			}
			
			this.disconnectTimerTask = new TimerTask() {
				@Override
				public void run() {
					hombot.disconnect();
				}
			};
			
			this.disconnectTimer.schedule(this.disconnectTimerTask, DISCONNECT_AFTER_IDLE_IN_MS);
		}
		
		super.onBeforeRequest(request);
	}
}
