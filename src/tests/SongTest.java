package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import model.DateUpdater;
import model.Song;
import model.SongLibrary;

/**
 * A unit test for all the parts of the model relating to Songs.
 * 
 * @author Taylor Heimbichner
 */
public class SongTest
{
	@Test
	public void testGetSong()
	{
		SongLibrary lib  =	SongLibrary.getInstance();
		Song song = lib.getSong("Danse Macabre");
		assertNotNull(song);
		assertEquals("Danse Macabre", song.getName());
		song = lib.getSong("Loping Sting");
	}
	
	@Test
	public void testGetInvalidSong()
	{
		SongLibrary lib  =	SongLibrary.getInstance();
		Song song = lib.getSong("kajdflkjdslaksjdf");
		assertNull(song);
	}
	
	@Test
	public void testPlayCount()
	{
		SongLibrary lib  =	SongLibrary.getInstance();
		Song song = lib.getSong("Flute");
		assertEquals(0, song.getTimesPlayed());
		assertTrue(song.canPlay());
		
		song.playSong();
		assertEquals(1, song.getTimesPlayed());
		assertTrue(song.canPlay());
		
		song.playSong();
		assertEquals(2, song.getTimesPlayed());
		assertTrue(song.canPlay());
		
		song.playSong();
		assertEquals(3, song.getTimesPlayed());
		assertFalse(song.canPlay());
		
		song.playSong();
		assertEquals(4, song.getTimesPlayed());
		assertFalse(song.canPlay());
	}
	
	@Test
	public void testUpdate()
	{
		SongLibrary lib  =	SongLibrary.getInstance();
		Song song = lib.getSong("Flute");
		song.playSong();
		song.playSong();
		song.playSong();
		song.playSong();
		assertEquals(4, song.getTimesPlayed());
		assertFalse(song.canPlay());
		
		DateUpdater.getInstance().updateEvent();
		assertEquals(4, song.getTimesPlayed());
		
		DateUpdater.getInstance().simulateMidnight();
		DateUpdater.getInstance().updateEvent();
		assertEquals(0, song.getTimesPlayed());

		song.playSong();
		song.playSong();
		assertEquals(2, song.getTimesPlayed());
		DateUpdater.getInstance().simulateMidnight();
		DateUpdater.getInstance().updateEvent();
		assertEquals(0, song.getTimesPlayed());
	}
}
