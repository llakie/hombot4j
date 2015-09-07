package de.bytevalue.hb4j.joystick;

import java.io.IOException;

public interface JoystickControlListener {
	public int onJoystickDirection(JoystickDirection direction) throws IOException;
}
