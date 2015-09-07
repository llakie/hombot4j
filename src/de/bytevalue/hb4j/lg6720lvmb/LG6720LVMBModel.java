package de.bytevalue.hb4j.lg6720lvmb;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import de.bytevalue.hb4j.HombotModel;
import de.bytevalue.hb4j.HombotModelListener;
import de.bytevalue.hb4j.joystick.JoystickDirection;
import de.bytevalue.hb4j.json.JsonConnectionListener;
import de.bytevalue.hb4j.json.JsonResponse;

public class LG6720LVMBModel extends HombotModel {
	private static final String TAG_CONNECT_INIT = "CONNECT_INIT";
	private static final String TAG_VOICEMODE = "VOICEMODE";
	private static final String TAG_NIKNAME = "NIKNAME";
	private static final String TAG_VERSION = "VERSION";
	private static final String TAG_CLEANING_MODE = "MODE";
	private static final String TAG_BATTERY_STATUS = "BATT";
	private static final String TAG_REPEAT = "REPEAT";
	private static final String TAG_ROBOT_STATE = "ROBOT_STATE";
	private static final String TAG_TURBO = "TURBO";
	private static final String TAG_JOYSTICK = "JOY";
	
	private boolean modelInited = false;
	
	private boolean modelChangeDetected = true;
	
	private boolean turboEnabled = false;
	private String robotState;
	private boolean repeatEnabled;
	private int batteryLevel;
	private String cleaningMode;
	private String nickname;
	private String version;
	private String voiceMode;
	private JoystickDirection direction = null;
	
	public boolean isTurboEnabled() {
		return this.turboEnabled;
	}
	
	public boolean isRepeatEnabled() {
		return this.repeatEnabled;
	}
	
	public String getRobotState() {
		return robotState;
	}
	
	public int getBatteryLevel() {
		return batteryLevel;
	}
	
	public String getCleaningMode() {
		return cleaningMode;
	}
	
	public String getNickname() {
		return nickname;
	}
	
	public String getVersion() {
		return version;
	}
	
	public String getVoiceMode() {
		return voiceMode;
	}
	
	public JoystickDirection getDirection() {
		return this.direction;
	}
	
	protected void apply(JsonResponse response) {
		this.modelChangeDetected = false;
		
		this.parsePayload(response.getPayload());
		
		if(!this.modelInited || !modelChangeDetected) {
			return;
		}
		
		for(HombotModelListener listener: this.listeners) {
			JsonConnectionListener.threadPool.execute(() -> {
				listener.onModelChange(response.getHeader().getId());
			});
		}
	}
	
	private void parsePayload(JSONObject payload) {
		if(payload.has(TAG_CONNECT_INIT)) {
			this.modelInited = true;
		}
		
		this.parseJsonObject(payload, new ArrayDeque<>());
	}
	
	private void parseJsonArray(JSONArray array, Deque<String> keyStack) {
		for(int i = 0; i < array.length(); i++) {
			if(array.optJSONArray(i) != null) {
				this.parseJsonArray(array.optJSONArray(i), keyStack);
			}
			else if(array.optJSONObject(i) != null) {
				this.parseJsonObject(array.optJSONObject(i), keyStack);
			}
		}
	}
	
	private void parseJsonObject(JSONObject object, Deque<String> keyStack) {
		Iterator<String> it = object.keys();
		
		while(it.hasNext()) {
			String key = it.next();
			
			keyStack.addLast(key);
			
			if(object.optJSONArray(key) != null) {
				this.parseJsonArray(object.optJSONArray(key), keyStack);
			}
			else if(object.optJSONObject(key) != null) {
				this.parseJsonObject(object.optJSONObject(key), keyStack);
			}
			else if(object.optString(key) != null) {
				this.parseKeyValue(keyStack.toArray(new String[0]), object.optString(key));
			}
			
			keyStack.removeLast();
		}
	}
	
	private void parseKeyValue(String[] keys, String value) {
		switch(keys[keys.length - 1]) {
			case TAG_TURBO: {
				this.turboEnabled = Boolean.parseBoolean(value);
				this.modelChangeDetected = true;
				break;
			}
			case TAG_ROBOT_STATE: {
				this.robotState = value;
				this.modelChangeDetected = true;
				break;
			}
			case TAG_REPEAT: {
				this.repeatEnabled = Boolean.parseBoolean(value);
				this.modelChangeDetected = true;
				break;
			}
			case TAG_BATTERY_STATUS: {
				this.batteryLevel = Integer.parseInt(value);
				this.modelChangeDetected = true;
				break;
			}
			case TAG_CLEANING_MODE: {
				this.cleaningMode = value;
				this.modelChangeDetected = true;
				break;
			}
			case TAG_NIKNAME: {
				this.nickname = value;
				this.modelChangeDetected = true;
				break;
			}
			case TAG_VERSION: {
				this.version = value;
				this.modelChangeDetected = true;
				break;
			}
			case TAG_VOICEMODE: {
				this.voiceMode = value;
				this.modelChangeDetected = true;
				break;
			}
			case TAG_JOYSTICK: {
				this.direction = JoystickDirection.valueOf(value);
				
				if(this.direction == JoystickDirection.RELEASE) {
					this.direction = null;
				}
				
				this.modelChangeDetected = true;
			}
		}
	}
}