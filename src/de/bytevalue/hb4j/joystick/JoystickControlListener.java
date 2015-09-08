package de.bytevalue.hb4j.joystick;

import java.io.IOException;

public interface JoystickControlListener {
	// This event is sent periodically every second with the currently resolved direction when the keys currently pressed
	// can be mapped to any direction.
	// When no key is pressed for more than 250ms the event is triggered once with JoystickDirection.RELEASE.
	public int onJoystickDirection(JoystickDirection direction) throws IOException;
}
