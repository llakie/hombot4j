package de.bytevalue.hb4j.lg6720lvmb;

import org.json.JSONArray;

public class Reservation {
	private int hour;
	private int minute;
	private boolean isRepeat;
	private boolean isEnabled;
	private CleanMode cleanMode;
	
	// ["0","7","12","OOOOOOO","ZZ","false","true"]
	public Reservation(JSONArray data) {
		this.hour = data.getInt(1);
		this.minute = data.getInt(2);
		this.cleanMode = CleanMode.fromShortName(data.getString(4));
		this.isRepeat = data.getBoolean(5);
		this.isEnabled = data.getBoolean(6);
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
		return this.isRepeat;
	}
	
	public boolean isEnabled() {
		return this.isEnabled;
	}
}