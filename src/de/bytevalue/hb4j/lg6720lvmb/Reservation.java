package de.bytevalue.hb4j.lg6720lvmb;

import java.io.Serializable;

import org.json.JSONArray;

public class Reservation implements Serializable {
	private static final long serialVersionUID = -1169783792792530794L;
	
	private int hour;
	private int minute;
	private boolean repeat;
	private boolean enabled;
	private CleanMode cleanMode;
	
	// ["0","7","12","OOOOOOO","ZZ","false","true"]
	public Reservation(JSONArray data) {
		this.hour = data.getInt(1);
		this.minute = data.getInt(2);
		this.cleanMode = CleanMode.apiResponseValueOf(data.getString(4));
		this.repeat = data.getBoolean(5);
		this.enabled = data.getBoolean(6);
	}
	
	public int getHour() {
		return hour;
	}
	
	public int getMinute() {
		return minute;
	}
	
	public CleanMode getCleanMode() {
		return cleanMode;
	}
	
	public boolean isRepeat() {
		return this.repeat;
	}
	
	public boolean isEnabled() {
		return this.enabled;
	}
}