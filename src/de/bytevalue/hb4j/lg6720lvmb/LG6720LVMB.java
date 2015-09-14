package de.bytevalue.hb4j.lg6720lvmb;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import de.bytevalue.hb4j.Hombot;
import de.bytevalue.hb4j.joystick.JoystickControlListener;
import de.bytevalue.hb4j.joystick.JoystickDirection;
import de.bytevalue.hb4j.json.JsonRequest;

public final class LG6720LVMB extends Hombot<LG6720LVMBModel> implements JoystickControlListener {
	// {"DIAGNOSIS":{"RECENT":"REQUEST"}}		>> {"DIAGNOSIS":{"RECENT":"false"}}
	// {"DIAGNOSIS": "REQUEST"}					>> {"DIAGNOSIS":{"RESPONSE":{"TOTAL_NUM":"0","TIME":"0","RESULT_LIST":[]}}}
	// {"BLACKBOX":"REQUEST_ABSTRACT"} 			>> {"BLACKBOX":{"RECENT_ABS":"false"}}
	
	public LG6720LVMB(String ip) {
		super(ip);
	}
	
	// {"COMMAND":"CLEAN_START"} 				>> {"RESPONSE":"POS"}
	public final int startClean() throws IOException {
		return this.sendRequest(new JsonRequest("{\"COMMAND\":\"CLEAN_START\"}"));
	}
	
	// {"COMMAND":"PAUSE"} 						>> {"RESPONSE":"POS"}
	public final int pauseClean() throws IOException {
		return this.sendRequest(new JsonRequest("{\"COMMAND\":\"PAUSE\"}"));
	}
	
	// {"COMMAND":"HOMING"} 					>> {"RESPONSE":"POS"}
	public final int goHome() throws IOException {
		return this.sendRequest(new JsonRequest("{\"COMMAND\":\"HOMING\"}"));
	}
	
	// {"COMMAND":{"CLEAN_MODE":"CLEAN_SPOT"}} 	>> {"RESPONSE":"POS"} = Spot Modus
	// {"COMMAND":{"CLEAN_MODE":"CLEAN_SB"}} 	>> {"RESPONSE":"POS"} = CellByCell Modus
	// {"COMMAND":{"CLEAN_MODE":"CLEAN_ZZ"}} 	>> {"RESPONSE":"POS"} = ZickZack Modus
	public final int setCleanMode(CleanMode cleanMode) throws IOException {
		this.model.setCleanMode(cleanMode);
		return this.sendRequest(new JsonRequest("{\"COMMAND\":{\"CLEAN_MODE\":\"CLEAN_" + cleanMode.getShortString() + "\"}}"));
	}
	
	// {"RESERVATION":"REQ_RSVSTATE"}			>> {"RESERVATION":{"RESP_RSVSTATE":false}}
	// {"RESERVATION":"REQ_RSVSTATE"}			>> {"RESERVATION":{"RESP_RSVSTATE":["0","7","12","OOOOOOO","ZZ","false","true"]}}
	public final int getReservation() throws IOException{
		// Return values when reservation is present
		// RESP_RSVSTATE[0] = index
		// RESP_RSVSTATE[1] = hour
		// RESP_RSVSTATE[2] = minute
		// RESP_RSVSTATE[3] = weekday (O = no, X = yes) 
		// RESP_RSVSTATE[4] = cleanmode 
		// RESP_RSVSTATE[5] = repeat 
		// RESP_RSVSTATE[6] = enable
		
		return this.sendRequest(new JsonRequest("{\"RESERVATION\":\"REQ_RSVSTATE\"}"));
	}
	
	// {"RESERVATION":{"EDIT":{"INDEX":"0","HOUR":"12","MINUTE":"0","WEEKDAY":"OOOOOOO","CLEANMODE":"ZZ","REPEAT":"true","ENABLE":"true"}}}"
	//											>> {"RESERVATION":{"RESP_RSVSTATE":["0","21","0","OOOOOOO","SB","false","true"]}}
	public final int setReservation(Reservation reservation) throws IOException{
		String requestString = String.format("{\"RESERVATION\":{\"EDIT\":{\"INDEX\":\"0\",\"HOUR\":\"%d\",\"MINUTE\":\"%d\",\"WEEKDAY\":\"OOOOOOO\",\"CLEANMODE\":\"%2s\",\"REPEAT\":\"%b\",\"ENABLE\":\"%b\"}}}",
				reservation.getHour(),
				reservation.getMinute(),
				reservation.getCleanMode().getShortString(),
				reservation.isRepeat(),
				reservation.isEnabled());
		
		return this.sendRequest(new JsonRequest(requestString));
	}
	
	// {"RESERVATION":{"REMOVE":{"REMOVENUM":"1","ARR_INDEX":["0"]}}}"
	//											>> {"RESERVATION":{"RESP_RSVSTATE":false}}
	public final int removeReservation() throws IOException {
		return this.sendRequest(new JsonRequest("{\"RESERVATION\":{\"REMOVE\":{\"REMOVENUM\":\"1\",\"ARR_INDEX\":[\"0\"]}}}"));
	}
	
	// {"VERSION":"REQUEST"} 					>> {"VERSION":{"RESPONSE":{"NOW":"13865"}}}
	public final int getVersion() throws IOException {
		return this.sendRequest(new JsonRequest("{\"VERSION\":\"REQUEST\"}"));
	}
	
	// {"NICKNAME":"REQUEST"}					>> {"NICKNAME":{"RESPONSE":"LAKIE"}}
	public final int getNickname() throws IOException {
		return this.sendRequest(new JsonRequest("{\"NICKNAME\":\"REQUEST\"}"));
	}
	
	// {"NICKNAME":{"SET":"LAKIE"}}				>> {"NICKNAME":{"RESPONSE":"LAKIE"}}
	public final int setNickname(String nickname) throws IOException {
		return this.sendRequest(new JsonRequest("{\"NICKNAME\":{\"SET\":\"" + nickname + "\"}}"));
	}
	
	/// {"COMMAND":{"REPEAT": "<true | false>"}} >> {"RESPONSE":"POS"}
	public final int enableRepeat(boolean enableRepeat) throws IOException {
		return this.sendRequest(new JsonRequest("{\"COMMAND\":{\"REPEAT\":\"" + Boolean.toString(enableRepeat) + "\"}}"));
	}
	
	// {"COMMAND":{"TURBO": "<true | false>"}} 	>> {"TURBO":"<true | false>"} >> {"RESPONSE":"POS"}
	public final int enableTurbo(boolean enableTurbo) throws IOException {
		return this.sendRequest(new JsonRequest("{\"COMMAND\":{\"TURBO\":\"" + Boolean.toString(enableTurbo) + "\"}}"));
	}
	
	// {"TIME_SET":{"DATE":"20150906","DAY":"0","HOUR":"14","MINUTE":"11","SECOND":"02"}} >> No Answer
	public final int setDateTime(Date date) throws IOException {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
		String requestString = String.format("{\"TIME_SET\":{\"DATE\":\"%4d%02d%02d\",\"DAY\":\"%1d\",\"HOUR\":\"%02d\",\"MINUTE\":\"%02d\",\"SECOND\":\"%02d\"}}", 
				cal.get(Calendar.YEAR), 
				cal.get(Calendar.MONTH) + 1, 
				cal.get(Calendar.DATE), 
				dayOfWeek, 
				cal.get(Calendar.HOUR_OF_DAY), 
				cal.get(Calendar.MINUTE), 
				cal.get(Calendar.SECOND));
		
		return this.sendRequest(new JsonRequest(requestString));
	}
	
	// {"JOY":"FORWARD"} 						>> {"RESPONSE":"POS"}
	// {"JOY":"FORWARD_LEFT"} 					>> {"RESPONSE":"POS"}
	// {"JOY":"FORWARD_RIGHT"} 					>> {"RESPONSE":"POS"}
	// {"JOY":"LEFT"} 							>> {"RESPONSE":"POS"}
	// {"JOY":"RIGHT"} 							>> {"RESPONSE":"POS"}
	// {"JOY":"BACKWARD"}				 		>> {"RESPONSE":"POS"}
	// {"JOY":"BACKWARD_LEFT"} 					>> {"RESPONSE":"POS"}
	// {"JOY":"BACKWARD_RIGHT"} 				>> {"RESPONSE":"POS"}
	// {"JOY":"RELEASE"} 						>> {"RESPONSE":"POS"}
	public final int move(JoystickDirection direction) throws IOException {
		this.model.setDirection(direction);
		return this.sendRequest(new JsonRequest("{\"JOY\":\"" + direction.toString() + "\"}"));
	}
	
	@Override
	public final int onJoystickDirection(JoystickDirection direction) throws IOException {
		return this.move(direction);
	}
	
	@Override
	protected LG6720LVMBModel createModelAdapter() {
		return new LG6720LVMBModel();
	}
}