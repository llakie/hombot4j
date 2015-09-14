package de.bytevalue.hb4j.example;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.json.JSONException;
import org.json.JSONObject;

import de.bytevalue.hb4j.HombotModel;
import de.bytevalue.hb4j.HombotModelListener;
import de.bytevalue.hb4j.ThreadPool;
import de.bytevalue.hb4j.joystick.JoystickControlKeyAdapter;
import de.bytevalue.hb4j.joystick.JoystickDirection;
import de.bytevalue.hb4j.json.JsonConnection;
import de.bytevalue.hb4j.json.JsonRequest;
import de.bytevalue.hb4j.json.JsonResponse;
import de.bytevalue.hb4j.lg6720lvmb.CleanMode;
import de.bytevalue.hb4j.lg6720lvmb.LG6720LVMB;
import de.bytevalue.hb4j.lg6720lvmb.Reservation;

public class LG6720LVMBUI extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private LG6720LVMB bot;
	private HombotModel model;
	
	private JButton btSetReservation;
	private JButton btRemoveReservation;
	
	private JTextField tfCommand;
	private JButton btSendCommand;
	
	public static void main(String[] args) {
		new LG6720LVMBUI();
	}
	
	public LG6720LVMBUI() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Container cp = this.getContentPane();
		
		cp.setLayout(new BorderLayout(10, 10));
		
		this.tfCommand = new JTextField("{\"VERSION\":\"REQUEST\"}");
		cp.add(tfCommand, BorderLayout.CENTER);
		
		this.btSendCommand = new JButton("Senden");
		cp.add(this.btSendCommand, BorderLayout.SOUTH);
		
		JPanel reservationPanel = new JPanel(new BorderLayout(10, 0));
		this.btSetReservation = new JButton("Setzen");
		this.btSetReservation.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setReservation();
			}
		});
		reservationPanel.add(this.btSetReservation, BorderLayout.WEST);
		this.btRemoveReservation = new JButton("Entfernen");
		this.btRemoveReservation.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				removeReservation();
			}
		});
		reservationPanel.add(this.btRemoveReservation, BorderLayout.EAST);
		
		cp.add(reservationPanel, BorderLayout.NORTH);
		
		this.btSendCommand.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					JSONObject obj = new JSONObject(tfCommand.getText());
					bot.sendRequest(new JsonRequest(obj.toString()));
				}
				catch(JSONException | IOException ex) {
					JOptionPane.showMessageDialog(null, ex.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		this.init();
		
		this.setFocusable(true);
		
		JoystickControlKeyAdapter adapter = new JoystickControlKeyAdapter(this.bot);
		
		adapter.mapDirectionToKeyCodes(JoystickDirection.FORWARD, KeyEvent.VK_UP);
		adapter.mapDirectionToKeyCodes(JoystickDirection.FORWARD_RIGHT, KeyEvent.VK_UP, KeyEvent.VK_RIGHT);
		adapter.mapDirectionToKeyCodes(JoystickDirection.FORWARD_LEFT, KeyEvent.VK_UP, KeyEvent.VK_LEFT);
		adapter.mapDirectionToKeyCodes(JoystickDirection.BACKWARD, KeyEvent.VK_DOWN);
		adapter.mapDirectionToKeyCodes(JoystickDirection.BACKWARD_RIGHT, KeyEvent.VK_DOWN, KeyEvent.VK_RIGHT);
		adapter.mapDirectionToKeyCodes(JoystickDirection.BACKWARD_LEFT, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT);
		adapter.mapDirectionToKeyCodes(JoystickDirection.LEFT, KeyEvent.VK_LEFT);
		adapter.mapDirectionToKeyCodes(JoystickDirection.RIGHT, KeyEvent.VK_RIGHT);
		
		this.addKeyListener(adapter);
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				bot.disconnect();
				ThreadPool.getInstance().shutdown();
			}
		});
	}
	
	private void setReservation() {
		Reservation reservation = new Reservation(CleanMode.CELL, 21, 0, false);
		try {
			this.bot.setReservation(reservation);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void removeReservation() {
		try {
			this.bot.removeReservation();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void init() {
		this.bot = new LG6720LVMB("192.168.0.26");
		this.model = bot.getModel();
		
		this.model.addListener(new HombotModelListener() {
			@Override
			public void onModelChange(int requestId) {
				System.out.println("Model changed");
				updateUI();
			}
			
			@Override
			public void onBeforeRequest(JsonRequest request) {
				System.out.println(">>>" + new String(request.getPayload()));
				super.onBeforeRequest(request);
			}
			
			@Override
			public void onResponse(JsonConnection conn, JsonResponse response) {
				System.out.println("<<<" + response.getPayload());
				super.onResponse(conn, response);
			}
		});
		
		try {
			this.bot.connect();
		}
		catch (IOException ex) {}
	}
	
	private void updateUI() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				if(!isVisible()) {
					pack();
					requestFocus();
					setVisible(true);
				}
			}
		});
	}
}