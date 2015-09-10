package de.bytevalue.hb4j.lg6720lvmb;

enum CleanMode {
	SPOT("CLEAN_SPOT"), CELL("CLEAN_SB"), ZIGZAG("CLEAN_ZZ");
	
	private String apiRequestValue;

	private CleanMode(String command) {
		this.apiRequestValue = command;
	}
	
	public String getApiRequestValue() {
		return this.apiRequestValue;
	}
	
	public static CleanMode apiResponseValueOf(String name) {
		switch(name) {
			case "ZZ": return CleanMode.ZIGZAG;
			// Case SB
			default: return CleanMode.CELL;
		}
	}
}