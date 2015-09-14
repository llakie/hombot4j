package de.bytevalue.hb4j.lg6720lvmb;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.json.JSONArray;

@XmlRootElement
public class Reservation implements Serializable {
	private static final long serialVersionUID = -1169783792792530794L;
	
	@XmlTransient private int hour;
	@XmlTransient private int minute;
	@XmlTransient private boolean repeat;
	@XmlTransient private boolean enabled;
	@XmlTransient private CleanMode cleanMode;
	
	public Reservation() {}
	
	public Reservation(CleanMode cleanMode, int hour, int minute, boolean repeat) {
		this.cleanMode = cleanMode;
		this.hour = hour;
		this.minute = minute;
		this.repeat = repeat;
		this.enabled = true;
	}
	
	// ["0","7","12","OOOOOOO","ZZ","false","true"]
	public Reservation(JSONArray data) {
		this.hour = data.getInt(1);
		this.minute = data.getInt(2);
		this.cleanMode = CleanMode.valueOfShortString(data.getString(4));
		this.repeat = data.getBoolean(5);
		this.enabled = data.getBoolean(6);
	}
	
	@XmlElement(name="hour")
	public int getHour() {
		return hour;
	}
	
	public void setHour(int hour) {
		this.hour = hour;
	}
	
	@XmlElement(name="minute")
	public int getMinute() {
		return minute;
	}
	
	public void setMinute(int minute) {
		this.minute = minute;
	}
	
	@XmlElement(name="mode")
	public CleanMode getCleanMode() {
		return cleanMode;
	}
	
	public void setCleanMode(CleanMode cleanMode) {
		this.cleanMode = cleanMode;
	}
	
	@XmlElement(name="repeat")
	public boolean isRepeat() {
		return this.repeat;
	}
	
	public void setRepeat(boolean repeat) {
		this.repeat = repeat;
	}
	
	@XmlElement(name="enabled")
	public boolean isEnabled() {
		return this.enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}