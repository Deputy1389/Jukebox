package src.model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.Observable;

/**
 * Defines an observable that will notify observers at midnight. Requires
 * updateEvent() to be called on every GUI event.
 * 
 * @author Taylor Heimbichner
 */
public class DateUpdater extends Observable
{
	// single instance of DateUpdater
	private static final DateUpdater dateUpdater = new DateUpdater();
	
	// used to simulate midnights
	private int midnightOffset = 0;
	
	// the date on which updateEvent() was last called
	private LocalDate lastUpdate;
	
	/**
	 * Private constructor that initializes lastUpdate to right now
	 */
	private DateUpdater()
	{
		lastUpdate = getDate();
	}
	
	/**
	 * Returns the single instance of DateUpdater.
	 */
	public static DateUpdater getInstance()
	{
		return dateUpdater;
	}
	
	/**
	 * Updates observers if the date has changed since this method was last called.
	 */
	public void updateEvent()
	{
		LocalDate now = getDate();
		if (!lastUpdate.equals(now))
		{
			setChanged();
			notifyObservers();
		}
		lastUpdate = now;
	}
	
	/**
	 * Gets the current date, adjusted for simulated midnights.
	 */
	private LocalDate getDate()
	{
		return LocalDate.now().plusDays(midnightOffset);
	}
	
	/**
	 * Simulates midnight, allowing DateUpdater to be tested.
	 */
	public void simulateMidnight()
	{
		midnightOffset++;
	}
	
	/**
	 * Saves the last update to a file
	 */
	public void saveUpdater()
	{
		try
		{
			FileOutputStream bytesToDisk = new FileOutputStream("update");
			ObjectOutputStream outFile = new ObjectOutputStream(bytesToDisk);
			// outFile understands the writeObject(Object o) message.
			outFile.writeObject(lastUpdate);
			outFile.close(); // Always close the output file!
			bytesToDisk.close();
		}
		catch (IOException ioe)
		{
			System.err.println("Writing DateUpdater objects failed");
			ioe.printStackTrace();
		}
	}
	
	/**
	 * Loads the last update from a file
	 */
	public void loadUpdater()
	{
		try
		{
			FileInputStream rawBytes = new FileInputStream("update");
			ObjectInputStream inFile = new ObjectInputStream(rawBytes);
			// Need to cast Objects to the class they are known to be
			this.lastUpdate = (LocalDate) inFile.readObject();
			inFile.close();
			rawBytes.close();
		}
		catch (Exception e)
		{
			System.err.println("Reading DateUpdater objects failed");
			e.printStackTrace();
		}
	}
}
