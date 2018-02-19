package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import model.DateUpdater;
import model.Jukebox;
import model.JukeboxAccount;
import model.Song;

/**
 * A unit test for all parts of the model relating to JukeboxAccounts.
 * 
 * @author Sean Gallagher
 */
public class JukeboxAccountTest
{
	@Test
	public void testLogin()
	{
		Jukebox juke = new Jukebox();
		char[] pass = {'1'};
		juke.login("Chris", pass);
		assertEquals(juke.getCurrentUser().getUser(), "Chris");
	}
	
	@Test
	public void testLogout()
	{
		Jukebox juke = new Jukebox();
		char[] pass = {'1'};
		juke.login("Chris", pass);
		assertEquals(juke.getCurrentUser().getUser(), "Chris");
		juke.logout();
		assertEquals(juke.getCurrentUser(), null);
	}
	
	@Test
	public void testInvalidUser()
	{
		Jukebox juke = new Jukebox();
		char[] pass = {'1'};
		juke.login("chris", pass);
		assertEquals(juke.getCurrentUser(), null);
	}
	
	@Test
	public void testInvalidPass()
	{
		Jukebox juke = new Jukebox();
		char[] pass = {'2'};
		juke.login("Chris", pass);
		assertEquals(juke.getCurrentUser(), null);
	}
	
	@Test
	public void testAccountPlaySongTime()
	{
		Jukebox juke = new Jukebox();
		char[] pass = {'1'};
		juke.login("Chris", pass);
		assertEquals(juke.getCurrentUser().getUser(), "Chris");
		assertEquals(juke.getCurrentUser().getTime(), 90000);
		juke.getCurrentUser().playSong(50);
		assertEquals(juke.getCurrentUser().getTime(), 89950);
	}
	
	@Test
	public void testAccountPlaySongCount()
	{
		Jukebox juke = new Jukebox();
		char[] pass = {'1'};
		juke.login("Chris", pass);
		assertEquals(juke.getCurrentUser().getUser(), "Chris");
		assertEquals(juke.getCurrentUser().getTimesPlayed(), 0);
		juke.getCurrentUser().playSong(50);
		assertEquals(juke.getCurrentUser().getTimesPlayed(), 1);
	}
	
	@Test
	public void testAccountPlaySongCanPlayFalse()
	{
		Jukebox juke = new Jukebox();
		char[] pass = {'1'};
		juke.login("Chris", pass);
		assertEquals(juke.getCurrentUser().getUser(), "Chris");
		assertEquals(juke.getCurrentUser().getTimesPlayed(), 0);
		juke.getCurrentUser().playSong(50);
		juke.getCurrentUser().playSong(50);
		juke.getCurrentUser().playSong(50);
		assertEquals(juke.getCurrentUser().getTimesPlayed(), 3);
		assertEquals(juke.getCurrentUser().getCanPlay(), false);
	}
	
	@Test
	public void testAccountPlaySongCanPlayTrue()
	{
		Jukebox juke = new Jukebox();
		char[] pass = {'1'};
		juke.login("Chris", pass);
		assertEquals(juke.getCurrentUser().getUser(), "Chris");
		assertEquals(juke.getCurrentUser().getTimesPlayed(), 0);
		juke.getCurrentUser().playSong(50);
		juke.getCurrentUser().playSong(50);
		assertEquals(juke.getCurrentUser().getTimesPlayed(), 2);
		assertEquals(juke.getCurrentUser().getCanPlay(), true);
	}
	
	@Test
	public void testAllLogins()
	{
		Jukebox juke = new Jukebox();
		juke.login("Chris", "1".toCharArray());
		assertEquals(juke.getCurrentUser().getUser(), "Chris");
		juke.login("Devon", "22".toCharArray());
		assertEquals(juke.getCurrentUser().getUser(), "Devon");
		juke.login("River", "333".toCharArray());
		assertEquals(juke.getCurrentUser().getUser(), "River");
		juke.login("Ryan", "4444".toCharArray());
		assertEquals(juke.getCurrentUser().getUser(), "Ryan");
	}
	
	@Test
	public void testUpdates()
	{
		Jukebox juke = new Jukebox();
		juke.login("Chris", "1".toCharArray());
		juke.getCurrentUser().playSong(50);
		juke.getCurrentUser().playSong(50);
		juke.getCurrentUser().playSong(50);
		assertEquals(3, juke.getCurrentUser().getTimesPlayed());
		
		DateUpdater.getInstance().updateEvent();
		assertEquals(3, juke.getCurrentUser().getTimesPlayed());
		
		DateUpdater.getInstance().simulateMidnight();
		DateUpdater.getInstance().updateEvent();
		assertEquals(0, juke.getCurrentUser().getTimesPlayed());
		
		juke.getCurrentUser().playSong(50);
		assertEquals(1, juke.getCurrentUser().getTimesPlayed());
		DateUpdater.getInstance().simulateMidnight();
		DateUpdater.getInstance().updateEvent();
		assertEquals(0, juke.getCurrentUser().getTimesPlayed());
	}
	
	@Test
	public void testCanPlaySong()
	{
		Jukebox juke = new Jukebox();
		juke.login("Chris", "1".toCharArray());
		JukeboxAccount chris = juke.getCurrentUser();
		chris.playSong(90000 - 30);
		Song longSong = juke.getSongLibrary().getSong("Danse Macabre");
		Song shortSong = juke.getSongLibrary().getSong("Loping Sting");
		assertTrue(chris.getCanPlay());
		assertFalse(chris.getCanPlay(longSong));
		assertTrue(chris.getCanPlay(shortSong));
		chris.playSong(0);
		chris.playSong(0);
		chris.playSong(0);
		assertFalse(chris.getCanPlay());
		assertFalse(chris.getCanPlay(longSong));
		assertFalse(chris.getCanPlay(shortSong));
		assertTrue(chris.getTime() >= shortSong.getLength());
	}
	
	@Test
	public void testCanPlaySongEdge()
	{
		Jukebox juke = new Jukebox();
		juke.login("Chris", "1".toCharArray());
		JukeboxAccount chris = juke.getCurrentUser();
		chris.playSong(90000 - 283);
		Song song = juke.getSongLibrary().getSong("Untameable Fire");
		assertTrue(chris.getCanPlay(song));
		chris.playSong(1);
		assertTrue(chris.getCanPlay(song));
		chris.playSong(1);
		assertFalse(chris.getCanPlay(song));
	}
}
