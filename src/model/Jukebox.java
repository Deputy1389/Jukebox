package src.model;

/**
 * Stores references to disparate model elements and allows access to them.
 * 
 * @author Taylor Heimbichner
 */
public class Jukebox
{
	// Holds the currently playing Songs
	private final ReadableSongQueue songQueue = new ReadableSongQueue();
	
	// Holds the set of all Songs
	private final SongLibrary library =	SongLibrary.getInstance();
	
	// Authenticates users
	private final CardReader reader = new CardReader();
	
	// Represents the current user
	private JukeboxAccount currentUser;
	
	/**
	 * Gets the Song Library
	 */
	public SongLibrary getSongLibrary()
	{
		return library;
	}
	
	/**
	 * Gets the Song Queue adapted for use with JLists.
	 */
	public ReadableSongQueue getSongQueue()
	{
		return songQueue;
	}
	
	/**
	 * Gets the current user
	 */
	public JukeboxAccount getCurrentUser()
	{
		return currentUser;
	}
	
	/**
	 * Attempts to login with the specified information. Returns true if the
	 * login was successful.
	 * 
	 * @param user The username
	 * @param pass The password
	 * @return true if the login was successful, false otherwise.
	 */
	public boolean login(String user, char[] pass)
	{
		JukeboxAccount newUser = reader.authenticate(user, pass);
		if (newUser == null)
		{
			return false;
		}
		currentUser = newUser;
		return true;
	}
	
	/**
	 * Logs the current user out.
	 */
	public void logout()
	{
		currentUser = null;
	}
	
	/**
	 * Saves the model data
	 */
	public void save()
	{
		getSongLibrary().saveLibrary();
		JukeboxAccountCollection.getInstance().saveAccounts();
		getSongQueue().saveQueue();
		DateUpdater.getInstance().saveUpdater();
	}
	
	/**
	 * Loads the model data
	 */
	public void load()
	{
		getSongLibrary().loadLibrary();
		JukeboxAccountCollection.getInstance().loadAccounts();
		getSongQueue().loadQueue();
		DateUpdater.getInstance().loadUpdater();
	}
}
