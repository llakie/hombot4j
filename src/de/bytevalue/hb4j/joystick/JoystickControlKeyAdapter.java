package de.bytevalue.hb4j.joystick;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class JoystickControlKeyAdapter extends KeyAdapter {
	private static final int MILLIS_TO_STOP_AFTER_ALL_KEYS_RELEASED = 250;
	private Timer timer;
	private TimerTask buttonReleaseTimer;
	private JoystickControl control;
	private List<Integer> pressedKeyCodes;
	private JoystickKeyMapping keyMapping;
	
	public JoystickControlKeyAdapter(JoystickControl control) {
		this.pressedKeyCodes = new ArrayList<>();
		this.timer = new Timer();
		this.control = control;
		this.keyMapping = new JoystickKeyMapping();
	}
	
	public void mapDirectionToKeyCodes(JoystickDirection direction, Integer... keyCodes) {
		this.keyMapping.map(direction, keyCodes);
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		Integer keyCode = Integer.valueOf(e.getKeyCode());
		
		if(!this.keyMapping.containsKeyCode(keyCode)) {
			return;
		}
		
		if(this.pressedKeyCodes.contains(keyCode)) {
			return;
		}
		
		if(this.pressedKeyCodes.size() >= this.keyMapping.getMaxKeyCodeCount()) {
			this.pressedKeyCodes.remove(0);
		}
		
		this.pressedKeyCodes.add(keyCode);
		
		this.evaluatePressedKeys();
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		if(!this.keyMapping.containsKeyCode(e.getKeyCode())) {
			return;
		}
		
		this.pressedKeyCodes.remove(Integer.valueOf(e.getKeyCode()));
		
		if(this.pressedKeyCodes.size() == 0) {
			this.buttonReleaseTimer = new TimerTask() {
				@Override
				public void run() {
					control.stop();
				}
			};
			
			this.timer.schedule(this.buttonReleaseTimer, MILLIS_TO_STOP_AFTER_ALL_KEYS_RELEASED);
		}
		else {
			this.evaluatePressedKeys();
		}
	}
	
	private void evaluatePressedKeys() {
		try {
			JoystickDirection direction = this.keyMapping.getDirectionForKeyCodes(this.pressedKeyCodes);
			
			if(direction == this.control.getCurrentDirection()) {
				return;
			}
					
			if(this.buttonReleaseTimer != null) {
				this.buttonReleaseTimer.cancel();
			}
					
			this.control.move(direction);
		}
		catch(DirectionNotFoundException ex) {
			// Ignore
		}
	}
}