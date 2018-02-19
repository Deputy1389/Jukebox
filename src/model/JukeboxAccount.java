package src.model;

import java.io.Serializable;
import java.util.Observable;
import java.util.Observer;

/**
 * Stores all of a user's information, including the username and password, time
 * remaining, and number of songs played.
 * 
 * @author Sean Gallagher
 */
public class JukeboxAccount implements Observer, Serializable
{
	private static final int MAX_PLAYS = 3;
	private String username;
	private char[] password;
	private int time;
	private int timesPlayed;
	
	/**
	 * Constructor for the JukeboxAccount
	 * takes the username as a string and password as a char array as parameters
	 */
	public JukeboxAccount(String user, char[] pass)
	{
		username = user;
		password = pass;
		time = 90000;	//this is 1500 minutes in seconds, seconds just seems easier to work with
		timesPlayed = 0;
	}
	
	/**
	 * Returns the accounts username
	 */
	public String getUser()
	{
		return username;
	}
	
	/**
	 * Returns the accounts password
	 */
	public char[] getPass()
	{
		return password;
	}
	
	/**
	 * Returns the time remaining on the account
	 */
	public int getTime()
	{
		return time;
	}
	
	/**
	 * Returns whether or not the user can play more songs
	 */
	public boolean getCanPlay()
	{
		return timesPlayed < MAX_PLAYS;
	}
	
	/**
	 * Returns whether or not the user can play this song
	 */
	public boolean getCanPlay(Song song)
	{
		return getCanPlay() && getTime() >= song.getLength();
	}
	
	/**
	 * Returns the number of songs played today by the user
	 */
	public int getTimesPlayed()
	{
		return timesPlayed;
	}
	
	/**
	 * When a song plays increment song played and remove time
	 */
	public void playSong(int length)
	{
		timesPlayed++;
		time -= length;
	}
	
	// Updates the timesPlayed variable, resetting it to 0 every midnight.
	@Override
	public void update(Observable o, Object arg)
	{
		timesPlayed = 0;
	}
}
