package de.bytevalue.hb4j.lg6720lvmb;

public enum CleanMode {
	CELL("SB"), ZIGZAG("ZZ");
	
	private String shortString;

	private CleanMode(String shortString) {
		this.shortString = shortString;
	}
	
	public String getShortString() {
		return this.shortString;
	}
	
	public static CleanMode valueOfShortString(String shortString) {
		switch(shortString) {
			case "ZZ": return CleanMode.ZIGZAG;
			default: return CleanMode.CELL;
		}
	}
}