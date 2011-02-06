package com.buildbrighton.badge.serial;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

import com.buildbrighton.badge.BadgeListener;

public class BadgeSerialListener implements SerialPortEventListener {
	SerialPort serialPort;
	/** The port we're normally going to use. */
	private static final String PORT_NAMES[] = { "/dev/tty.usbserial-A600bNdu", // Mac
	        // OS
	        // X
	        "/dev/ttyUSB0", // Linux
	        "COM3", // Windows
	};

	/** Buffered input stream from the port */
	private InputStream input;
	/** The output stream to the port */
	private OutputStream output;
	/** Milliseconds to block while waiting for port open */
	private static final int TIME_OUT = 2000;
	/** Default bits per second for COM port. */
	private static final int DATA_RATE = 9600;

	private BadgeListener badgeListener;

	private boolean S;
	private boolean T;
	private boolean A;
	private boolean R;
	private byte[] buffer = new byte[4];
	private int bufferHead;

	public BadgeSerialListener() {
		initialize();
	}

	public void initialize() {
		CommPortIdentifier portId = null;

		@SuppressWarnings("unchecked")
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

		// iterate through, looking for the port
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum
			        .nextElement();
			for (String portName : PORT_NAMES) {
				if (currPortId.getName().equals(portName)) {
					portId = currPortId;
					break;
				}
			}
		}

		if (portId == null) {
			System.out.println("Could not find COM port.");
			return;
		}

		try {
			// open serial port, and use class name for the appName.
			serialPort = (SerialPort) portId.open(this.getClass().getName(),
			        TIME_OUT);

			// set port parameters
			serialPort.setSerialPortParams(DATA_RATE, SerialPort.DATABITS_8,
			        SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

			// open the streams
			input = serialPort.getInputStream();
			output = serialPort.getOutputStream();

			// add event listeners
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}

	/**
	 * This should be called when you stop using the port. This will prevent
	 * port locking on platforms like Linux.
	 */
	public synchronized void close() {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}

	public synchronized void write(byte[] data) throws IOException {
		output.write(data);
	}

	/**
	 * Handle an event on the serial port. Read the data and print it.
	 */
	public synchronized void serialEvent(SerialPortEvent oEvent) {
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {

				int available = input.available();
				byte chunk[] = new byte[available];
				input.read(chunk, 0, available);

				for (byte b : chunk) {
					//Unsigned to signed
					b = (byte) (b & 0xFF);
					
					System.out.print(b + " ");
					if(b == 's'){
						S = true;
					}
					if(S && b == 't'){
						S = false;
						T = true;
					}
					if(T && b == 'a'){
						T = false;
						A = true;
					}
					if(A && b == 'r'){
						A = false;
						R = true;
					}
					if(R && b == 't'){
						R = false;
						buffer = new byte[4];
						bufferHead = 0;
						System.out.println();
						continue;
					}
					if(bufferHead < 4){
						buffer[bufferHead] = b;
					}
					
					bufferHead++;
					
					if(bufferHead == 4){
						System.out.println();
						badgeListener.dataAvailable(buffer);
					}
				}

			} catch (Exception e) {
				e.printStackTrace(System.err);
			}
		}
		// Ignore all the other eventTypes, but you should consider the other
		// ones.
	}

	public static void main(String[] args) throws Exception {
		BadgeSerialListener main = new BadgeSerialListener();
		main.initialize();
		System.out.println("Started");
	}

	public void setBadgeListener(BadgeListener badgeListener) {
		this.badgeListener = badgeListener;
	}

	public BadgeListener getBadgeListener() {
		return badgeListener;
	}
}