package de.bytevalue.hb4j.lg6720lvmb;

enum CleanMode {
	SPOT("CLEAN_SPOT"), CELL("CLEAN_SB"), ZIGZAG("CLEAN_ZZ");
	
	private String apiName;

	private CleanMode(String command) {
		this.apiName = command;
	}
	
	public String getAPIName() {
		return apiName;
	}
}