package de.bytevalue.hb4j.joystick;

public class DirectionNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;

	public DirectionNotFoundException() {
		super("There is no direction mapping for the given key codes");
	}
}