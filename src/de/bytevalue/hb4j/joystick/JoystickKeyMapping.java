package de.bytevalue.hb4j.joystick;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JoystickKeyMapping {
	private List<KeyMapping> keyMappings;
	private int maxKeyCodeCount;
	
	public JoystickKeyMapping() {
		this.keyMappings = new ArrayList<>();
	}
	
	public void map(JoystickDirection direction, Integer... keyCodes) {
		KeyMapping keyMapping = new KeyMapping(direction, keyCodes);
		
		this.maxKeyCodeCount = Math.max(this.maxKeyCodeCount, keyMapping.getKeyCodes().size());
		
		this.keyMappings.add(keyMapping);
	}
	
	public int getMaxKeyCodeCount() {
		return this.maxKeyCodeCount;
	}
	
	public JoystickDirection getDirectionForKeyCodes(List<Integer> keyCodes) throws DirectionNotFoundException {
		for(KeyMapping keyMapping: this.keyMappings) {
			if(keyMapping.getKeyCodes().size() == keyCodes.size() && keyMapping.getKeyCodes().containsAll(keyCodes)) {
				return keyMapping.getDirection();
			}
		}
		
		throw new DirectionNotFoundException();
	}
	
	public boolean containsKeyCode(Integer keyCode) {
		for(KeyMapping keyMapping: this.keyMappings) {
			if(keyMapping.getKeyCodes().contains(keyCode)) {
				return true;
			}
		}
		
		return false;
	}
	
	class KeyMapping {
		private List<Integer> keyCodes;
		private JoystickDirection direction;
		
		public KeyMapping(JoystickDirection direction, Integer... keyCodes) {
			this.direction = direction;
			this.keyCodes = Arrays.asList(keyCodes);
		}
		
		public JoystickDirection getDirection() {
			return direction;
		}
		
		public List<Integer> getKeyCodes() {
			return keyCodes;
		}
	}
}