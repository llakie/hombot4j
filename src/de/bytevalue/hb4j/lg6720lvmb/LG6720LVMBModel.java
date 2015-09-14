package de.bytevalue.hb4j.lg6720lvmb;

import java.util.Iterator;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.json.JSONArray;
import org.json.JSONObject;

import de.bytevalue.hb4j.HombotModel;
import de.bytevalue.hb4j.joystick.JoystickDirection;
import de.bytevalue.hb4j.json.JsonResponse;

@XmlRootElement
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
	
	@XmlTransient private boolean modelInited = false;
	@XmlTransient public boolean turbo = false;
	@XmlTransient public String robotState;
	@XmlTransient public boolean repeat;
	@XmlTransient public int batteryLevel;
	@XmlTransient public CleanMode cleanMode;
	@XmlTransient public String nickname;
	@XmlTransient public String version;
	@XmlTransient public String voiceMode;
	@XmlTransient public JoystickDirection direction = null;
	@XmlTransient public Reservation reservation;
	
	public LG6720LVMBModel() {}
	
	@XmlElement(name="turbo")
	public boolean isTurboEnabled() {
		return this.turbo;
	}
	@XmlElement(name="repeat")
	public boolean isRepeatEnabled() {
		return this.repeat;
	}
	@XmlElement(name="state")
	public String getRobotState() {
		return this.robotState;
	}
	@XmlElement(name="battery")
	public int getBatteryLevel() {
		return this.batteryLevel;
	}
	@XmlElement(name="mode")
	public CleanMode getCleanMode() {
		return this.cleanMode;
	}
	
	public void setCleanMode(CleanMode cleanMode) {
		if(this.cleanMode == cleanMode) {
			return;
		}
		
		this.cleanMode = cleanMode;
		this.triggerModelChange(HombotModel.VIRTUAL_RESPONSE_ID);
	}
	@XmlElement(name="nickname")
	public String getNickname() {
		return this.nickname;
	}
	@XmlElement(name="version")
	public String getVersion() {
		return this.version;
	}
	@XmlElement(name="voice")
	public String getVoiceMode() {
		return this.voiceMode;
	}
	@XmlElement(name="direction")
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
	@XmlElement(name="reservation")
	public Reservation getReservation() {
		return this.reservation;
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
				this.turbo = Boolean.parseBoolean(value);
				break;
			}
			case TAG_ROBOT_STATE: {
				this.robotState = value;
				break;
			}
			case TAG_REPEAT: {
				this.repeat = Boolean.parseBoolean(value);
				break;
			}
			case TAG_BATTERY_STATUS: {
				this.batteryLevel = Integer.parseInt(value);
				break;
			}
			case TAG_CLEANING_MODE: {
				this.cleanMode = CleanMode.valueOfShortString(value);
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