package src.model;

import java.io.Serializable;
import java.util.Observable;
import java.util.Observer;

/**
 * Stores all data pertaining to a single song. Implements observer in order to
 * allow DateUpdater to notify this song at midnight and reset timesPlayed.
 * 
 * @author Taylor Heimbichner
 */
public class Song implements Observer, Serializable
{
	// max number of times a song can be played in a day
	private static final int MAX_PLAYS = 3;
	
	// a string representing the directory in which the songfiles are contained
	private static final String BASE_DIR = System.getProperty("user.dir") +
			System.getProperty("file.separator") + "songfiles" +
			System.getProperty("file.separator");
	
	// the song's name
	private final String name;
	
	// the song's artist
	private final String artist;
		
	// the name of the song file
	private final String fileName;
	
	// song length in seconds
	private final int length;
	
	// number of times played today
	private int timesPlayed;
	
	/**
	 * Constructs a song with the given information
	 */
	public Song(String name, String fileName, int length, String artist)
	{
		this.name = name;
		this.fileName = fileName;
		this.length = length;
		this.artist = artist;
		timesPlayed = 0;
	}
	
	/**
	 * Returns the name of the song
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Returns the Artist
	 */
	public String getArtist()
	{
		return artist;
	}
	
	/**
	 * Returns the fully qualified filename corresponding to this song
	 */
	public String getFileName()
	{
		return BASE_DIR + fileName;
	}
	
	/**
	 * Returns the length of this song
	 */
	public int getLength()
	{
		return length;
	}
	
	/**
	 * Returns the number of times played today. Resets every midnight, assuming
	 * the GUI sends the appropriate DateUpdater events.
	 * 
	 * @return The number of times this song has been played today.
	 */
	public int getTimesPlayed()
	{
		return timesPlayed;
	}
	
	/**
	 * Returns true if the song can be played. This only considers the number
	 * of times the song has been played today, not anything pertaining to the
	 * user.
	 * 
	 * @return true if the song can be played, false otherwise.
	 */
	public boolean canPlay()
	{
		return timesPlayed < MAX_PLAYS;
	}
	
	/**
	 * Should be run when a song is slated to be played.
	 */
	public void playSong()
	{
		timesPlayed++;
	}

	// Update the timesPlayed variable, resetting it to 0 every midnight.
	@Override
	public void update(Observable o, Object arg)
	{
		timesPlayed = 0;
	}
	
	// Gives the length, title, and artist
	@Override
	public String toString()
	{
		int mins = getLength() / 60;
		int secs = getLength() % 60;
		return String.format("%d:%02d %s by %s", mins, secs, name, artist);
	}
}
