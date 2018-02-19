package src.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JOptionPane;

import model.DateUpdater;
import model.Jukebox;
import model.JukeboxAccount;
import model.ReadableSongQueue;
import model.Song;

/**
 * Defines a factory for making event listeners that the GUI should add to its
 * components.
 * 
 * @author Taylor Heimbichner, Sean Gallagher
 */
public class ListenerFactory
{
	// the window whose view we are making listeners for.
	private final JukeboxStartGUI window;
	
	/**
	 * Constructs a factory for the given window's view.
	 * 
	 * @param window The window whose view we will be using.
	 */
	public ListenerFactory(JukeboxStartGUI window)
	{
		this.window = window;
	}
	
	/**
	 * Returns a new ActionListener that plays a Song gotten by SongGetter.
	 */
	public ActionListener makeSongListener(SongGetter songGetter)
	{
		return new SongListener(songGetter);
	}
	
	/**
	 * Returns a new WindowListener that controls saving and loading on exit/startup.
	 */
	public WindowListener makeWindowSaveListener()
	{
		return new WindowSaveListener();
	}
	
	/**
	 * Returns a new ActionListener that logs the user in when activated.
	 */
	public ActionListener makeLoginListener()
	{
		ActionListener listener = event ->
		{
			Jukebox model = ControlCoord.getInstance().getModel();
			String username = window.getLoginView().getUsernameField().getText();
			char[] password = window.getLoginView().getPasswordField().getPassword();
			boolean success = model.login(username, password);
			window.getLoginView().onLogin(success, model.getCurrentUser());
			for (int i = 0; i < password.length; i++)
			{
				password[i] = '\0';
			}
		};
		return listener;
	}
	
	/**
	 * Returns a new ActionListener that logs the user out when activated.
	 */
	public ActionListener makeLogoutListener()
	{
		ActionListener listener = event ->
		{
			ControlCoord.getInstance().getModel().logout();
			window.getLoginView().onLogout();
		};
		return listener;
	}
	
	/**
	 * Interface to define a strategy for getting a song.
	 * 
	 * @author Taylor Heimbichner
	 */
	public static interface SongGetter
	{
		public Song getSong();
	}
	
	/**
	 * An ActionListener that will play a given song when activated.
	 * 
	 * @author Sean Gallagher, Taylor Heimbichner
	 */
	private class SongListener implements ActionListener
	{
		private final SongGetter songGetter;
		
		/**
		 * Creates a SongListener that will attempt to play the song gotten
		 * by songGetter.getSong().
		 */
		public SongListener(SongGetter songGetter)
		{
			this.songGetter = songGetter;
		}
		
		/*
		 * Sends an update message to DateUpdater and attempts to play the song,
		 * giving error messages as appropriate.
		 */
		@Override
		public void actionPerformed(ActionEvent e)
		{
			DateUpdater.getInstance().updateEvent();
			ControlCoord coord = ControlCoord.getInstance();
			JukeboxAccount user = coord.getModel().getCurrentUser();
			Song song = songGetter.getSong();
			
			// error if we are not logged in
			if (user == null)
			{
				String msg = "Error: No user is signed in.";
				JOptionPane.showMessageDialog(window, msg);
				return;
			}
			
			// error if we can't find the song
			if (song == null)
			{
				String msg = "Error: No song selected.";
				JOptionPane.showMessageDialog(window, msg);
				return;
			}
			
			int length = song.getLength();
			
			// checks if the user can play this song and if the song can be played more
			if (user.getCanPlay(song) && song.canPlay())
			{
				user.playSong(length);
				song.playSong();
				coord.getSongController().addSong(song);
			}
			// error because user cannot play anymore songs
			else if (!user.getCanPlay())
			{
				String msg = "Error: User is out of songs for today.";
				JOptionPane.showMessageDialog(window, msg);
			}
			// error because the song can't be played
			else if (!song.canPlay())
			{
				String msg = "Error: Song has reached its daily limit.";
				JOptionPane.showMessageDialog(window, msg);
			}
			// error because the user doesn't have enough time
			else
			{
				String msg = "Error: User has insufficient time remaining.";
				JOptionPane.showMessageDialog(window, msg);
			}
			window.getLoginView().updateStatus(user);
		}
	}
	
	/**
	 * A WindowListener that will ask the user if they would like to save data
	 * on exit/startup.
	 * 
	 * @author Sean Gallagher
	 */
	private class WindowSaveListener extends WindowAdapter
	{
		// Ask if we should save data
		@Override
		public void windowClosing(WindowEvent e)
		{
			String msg = "Would you like to save before closing?";
			int result = confirmDialog(msg);
			if (result == JOptionPane.YES_OPTION)
			{
				ControlCoord.getInstance().getModel().save();
				System.exit(0);
			}
			if (result == JOptionPane.NO_OPTION)
			{
				System.exit(0);
			}
		}
		
		// Ask if we should load data
		@Override
		public void windowOpened(WindowEvent e)
		{
			String msg = "Would you like to use the previous save?";
			int result = confirmDialog(msg);
			if (result == JOptionPane.YES_OPTION)
			{
				ControlCoord coord = ControlCoord.getInstance();
				coord.getModel().load();
				coord.getSongController().addSong(null);
			}
			if (result == JOptionPane.CANCEL_OPTION)
			{
				System.exit(0);
			}
		}
		
		/**
		 * Shows a dialog box with the given message.
		 * 
		 * @param msg The message to ask the user.
		 * @return An int representing the user's choice.
		 */
		private int confirmDialog(String msg)
		{
			int type = JOptionPane.YES_NO_CANCEL_OPTION;
			int msgType = JOptionPane.QUESTION_MESSAGE;
			return JOptionPane.showConfirmDialog(window, msg, "Jukebox", type, msgType);
		}
	}
}
