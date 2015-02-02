package nl.noppe.auke.aquarium.metrics.aqua;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;

import nl.noppe.auke.aquarium.tasks.MetricsCollectScheduler.SerialResponseCallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AquaMetricsCollector implements SerialPortEventListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(AquaMetricsCollector.class);
	
	private SerialPort serialPort;
	private final static String DEVICE_NAME = "/dev/ttyACM0";
//	private final static String DEVICE_NAME = "/dev/tty.usbmodem1411";
	
	/** The output stream to the port */
	private OutputStream output;

	private SerialResponseCallback serialResponseCallback;
	/** Milliseconds to block while waiting for port open */
	private static final int TIME_OUT = 2000;
	/** Default bits per second for COM port. */
	private static final int DATA_RATE = 115200;

	public void initialize() {
                // the next line is for Raspberry Pi and 
                // gets us into the while loop and was suggested here was suggested http://www.raspberrypi.org/phpBB3/viewtopic.php?f=81&t=32186
                System.setProperty("gnu.io.rxtx.SerialPorts", DEVICE_NAME);
//                System.setProperty("gnu.io.rxtx.SerialPorts", "/dev/ttyACM0");

		CommPortIdentifier portId = null;
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

		//First, Find an instance of serial port as set in PORT_NAMES.
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
			LOGGER.warn("port: {}", currPortId.getName());
			if (currPortId.getName().equals(DEVICE_NAME)) {
				portId = currPortId;
				LOGGER.debug("portID: {}", portId.toString());
			}
		}
		if (portId == null) {
			LOGGER.error("Could not find COM port.");
			return;
		}

		try {
			// open serial port, and use class name for the appName.
			serialPort = (SerialPort) portId.open(this.getClass().getName(),
					TIME_OUT);

			// set port parameters
			serialPort.setSerialPortParams(DATA_RATE,
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);

			// open the streams
			output = serialPort.getOutputStream();

			// add event listeners
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
			
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	/**
	 * This should be called when you stop using the port.
	 * This will prevent port locking on platforms like Linux.
	 */
	public synchronized void close() {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}
	
	/**
	 * Handle an event on the serial port. Read the data and print it.
	 */
	public synchronized void serialEvent(SerialPortEvent oEvent) {
		LOGGER.debug("event type: {}", oEvent.getEventType());
		LOGGER.debug("event source: {}", oEvent.getSource());
		try {
			switch (oEvent.getEventType()) {
			case SerialPortEvent.DATA_AVAILABLE:
				LOGGER.debug("Event 'DATA_AVAILABLE' received.");
				Thread.sleep(100);
				byte[] readBuffer = new byte[1024];
		        int availableBytes = serialPort.getInputStream().available();
				if (availableBytes > 0) {
		            // Read the serial port
					serialPort.getInputStream().read(readBuffer, 0, availableBytes);
		 
					LOGGER.info("received data: {}", new String(readBuffer, 0, availableBytes));
					serialResponseCallback.callback(new String(readBuffer, 0, availableBytes));
					
		        }
				
				break;

			default:
				break;
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
	
	public synchronized void toggleMoonLight() {
		try {
			output.write(new String("TML").getBytes());
		} catch(IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	public synchronized void getReading(SerialResponseCallback serialResponseCallback) {
		this.serialResponseCallback = serialResponseCallback;
		LOGGER.debug("Sending read command to Arduino");
		try {
			output.write(new String("R\r").getBytes());
			
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
}
