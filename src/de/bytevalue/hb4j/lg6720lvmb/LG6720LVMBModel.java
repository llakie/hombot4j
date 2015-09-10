package de.bytevalue.hb4j.lg6720lvmb;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import de.bytevalue.hb4j.HombotModel;
import de.bytevalue.hb4j.joystick.JoystickDirection;
import de.bytevalue.hb4j.json.JsonResponse;

public class LG6720LVMBModel extends HombotModel {
	private static final long serialVersionUID = -3507480247871144027L;
	
	private static final String TAG_RESPONSE = "RESPONSE";
	private static final String TAG_CONNECT_INIT = "CONNECT_INIT";
	private static final String TAG_VOICEMODE = "VOICEMODE";
	private static final String TAG_NICKNAME = "NICKNAME";
	private static final String TAG_VERSION = "VERSION";
	private static final String TAG_CLEANING_MODE = "MODE";
	private static final String TAG_BATTERY_STATUS = "BATT";
	private static final String TAG_REPEAT = "REPEAT";
	private static final String TAG_ROBOT_STATE = "ROBOT_STATE";
	private static final String TAG_TURBO = "TURBO";
	private static final String TAG_JOYSTICK = "JOY";
	private static final String TAG_RESP_RSVSTATE = "RESP_RSVSTATE";
	
	private boolean modelInited = false;
	
	private boolean turboEnabled = false;
	private String robotState;
	private boolean repeatEnabled;
	private int batteryLevel;
	private CleanMode cleanMode;
	private String nickname;
	private String version;
	private String voiceMode;
	private JoystickDirection direction = null;
	private Reservation reservation;
	
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
	
	public CleanMode getCleanMode() {
		return cleanMode;
	}
	
	public void setCleanMode(CleanMode cleanMode) {
		if(this.cleanMode == cleanMode) {
			return;
		}
		
		this.cleanMode = cleanMode;
		this.triggerModelChange(HombotModel.VIRTUAL_RESPONSE_ID);
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
	
	public void setDirection(JoystickDirection direction) {
		if(this.direction == direction) {
			return;
		}
		
		this.direction = direction;
		this.triggerModelChange(HombotModel.VIRTUAL_RESPONSE_ID);
	}
	
	public boolean hasReservation() {
		return this.reservation != null;
	}
	
	public Reservation getReservation() {
		return reservation;
	}
	
	protected void parse(JsonResponse response, JSONObject payload) {
		if(payload.has(TAG_CONNECT_INIT)) {
			this.modelInited = true;
		}
		
		if(!this.modelInited) {
			return;
		}
		
		this.parseJsonObject(payload);
	}
	
	protected void parseJsonObject(JSONObject object) {
		Iterator<String> it = object.keys();
		
		while(it.hasNext()) {
			String key = it.next();
			
			if(object.optJSONArray(key) != null) {
				JSONArray currentArray = object.optJSONArray(key);
				
				switch(key) {
					case TAG_RESP_RSVSTATE: {
						this.reservation = new Reservation(currentArray);
						break;
					}
					default: {
						this.parseJsonArray(currentArray);
					}
				}
			}
			else if(object.optJSONObject(key) != null) {
				JSONObject currentObject = object.optJSONObject(key);
				
				switch(key) {
					case TAG_NICKNAME: {
						this.nickname = currentObject.getString(TAG_RESPONSE);
						break;
					}
					default: {
						this.parseJsonObject(currentObject);
					}
				}
			}
			else if(object.optString(key) != null) {
				this.parseKeyValue(key, object.optString(key));
			}
		}
	}
	
	protected void parseKeyValue(String key, String value) {
		switch(key) {
			case TAG_TURBO: {
				this.turboEnabled = Boolean.parseBoolean(value);
				break;
			}
			case TAG_ROBOT_STATE: {
				this.robotState = value;
				break;
			}
			case TAG_REPEAT: {
				this.repeatEnabled = Boolean.parseBoolean(value);
				break;
			}
			case TAG_BATTERY_STATUS: {
				this.batteryLevel = Integer.parseInt(value);
				break;
			}
			case TAG_CLEANING_MODE: {
				this.cleanMode = CleanMode.apiResponseValueOf(value);
				break;
			}
			case TAG_NICKNAME: {
				this.nickname = value;
				break;
			}
			case TAG_VERSION: {
				this.version = value;
				break;
			}
			case TAG_VOICEMODE: {
				this.voiceMode = value;
				break;
			}
			case TAG_JOYSTICK: {
				this.direction = JoystickDirection.valueOf(value);
				
				if(this.direction == JoystickDirection.RELEASE) {
					this.direction = null;
				}
				
				break;
			}
			case TAG_RESP_RSVSTATE: {
				if(!Boolean.valueOf(value)) {
					this.reservation = null;
				}
			}
		}
	}
}