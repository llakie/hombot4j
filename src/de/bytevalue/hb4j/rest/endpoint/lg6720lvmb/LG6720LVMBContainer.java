package de.bytevalue.hb4j.rest.endpoint.lg6720lvmb;

import de.bytevalue.hb4j.joystick.JoystickControl;
import de.bytevalue.hb4j.lg6720lvmb.LG6720LVMB;
import de.bytevalue.hb4j.rest.RuntimeContainer;

public class LG6720LVMBContainer extends RuntimeContainer<LG6720LVMB>{
	private JoystickControl joystickControl;
	
	public LG6720LVMBContainer(LG6720LVMB hombot) {
		super(hombot);
		this.joystickControl = new JoystickControl(this.hombot);
	}
	
	public JoystickControl getJoystickControl() {
		return joystickControl;
	}
}
