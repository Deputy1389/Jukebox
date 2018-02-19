package src.controller;

import model.ReadableSongQueue;
import model.Song;
import songplayer.EndOfSongListener;
import songplayer.SongPlayer;

/**
 * Plays songs from a given Queue with the appropriate timing so that Songs will
 * never overlap.
 * 
 * @author Taylor Heimbichner
 */
public class SongController
{
	// Listener that notifies this when the song is finished playing
	private final EndOfSongListener eosListener = event -> onSongFinished();
	
	// The queue that holds the songs we should play
	private final ReadableSongQueue songQueue;
	
	// true when a song is currently playing
	private boolean isPlaying = false;
	
	/**
	 * Constructs a SongController to play songs in the given Queue.
	 */
	public SongController(ReadableSongQueue songQueue)
	{
		this.songQueue = songQueue;
	}
	
	/**
	 * Adds a song to the Queue. Will try to play the song, meaning the song
	 * will play automatically if nothing else is playing.
	 * 
	 * @param song The Song to play.
	 */
	public void addSong(Song song)
	{
		songQueue.addToQueue(song);
		tryPlaySong();
	}
	
	/**
	 * Runs whenever a song is finished. Will play the next song, if there is one.
	 */
	private void onSongFinished()
	{
		try
		{
			Thread.sleep(1000);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		isPlaying = false;
		// remove the song we just finished from the queue
		songQueue.removeFromQueue();
		tryPlaySong();
	}
	
	/**
	 * Plays a song if the queue is not empty and there is not already a song playing.
	 */
	private void tryPlaySong()
	{
		if (isPlaying)
		{
			return;
		}
		Song toPlay = songQueue.peekAtQueue();
		if (toPlay != null)
		{
			isPlaying = true;
			SongPlayer.playFile(eosListener, toPlay.getFileName());
		}
	}
}