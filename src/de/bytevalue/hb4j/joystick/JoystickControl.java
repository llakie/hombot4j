package de.bytevalue.hb4j.joystick;
import java.awt.event.KeyAdapter;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class JoystickControl extends KeyAdapter {
	private static final int MOVE_COMMAND_INTERVAL = 1000;
	private JoystickControlListener listener;
	private Timer timer;
	private TimerTask sendMoveTask;
	private JoystickDirection currentDirection;
	
	public JoystickControl(JoystickControlListener listener) {
		this.listener = listener;
		this.timer = new Timer();
	}
	
	public JoystickDirection getCurrentDirection() {
		return currentDirection;
	}
	
	public synchronized void move(JoystickDirection direction) {
		if(currentDirection == direction) {
			return;
		}
		
		this.currentDirection = direction;
		
		if(this.sendMoveTask != null) {
			sendMoveTask.cancel();
		}
		
		this.sendMoveTask = new TimerTask() {
			@Override
			public void run() {
				try {
					listener.onJoystickDirection(currentDirection);
				}
				catch (IOException ex) {}
			}
		};
		
		this.timer.schedule(this.sendMoveTask, 0, MOVE_COMMAND_INTERVAL);
	}
	
	public synchronized void stop() {
		this.sendMoveTask.cancel();
		
		try {
			listener.onJoystickDirection(JoystickDirection.RELEASE);
		}
		catch (IOException ex) {}
		finally {
			this.currentDirection = null;
		}
	}
}