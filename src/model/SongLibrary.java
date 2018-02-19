package src.model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 * Stores a hard-coded list of Songs and allows access to them.
 * 
 * @author Taylor Heimbichner, Sean Gallagher
 */
public class SongLibrary implements Serializable, TableModel 
{
	// the list of songs
	private ArrayList<Song> songs;
	
	// the single instance of this library
	private static final SongLibrary instance = new SongLibrary();
	
	/**
	 * Constructor for the library. Automatically adds every song.
	 */
	private SongLibrary()
	{
		songs = new ArrayList<Song>();
		populateSongList();
	}
	
	/**
	 * Gets a single instance of the library 
	 */
	public static synchronized SongLibrary getInstance()
	{
		return instance;
	}
	
	/**
	 * Adds songs to the library
	 */
	private void populateSongList()
	{
		addSong("Danse Macabre", "DanseMacabreViolinHook.mp3", 34, "Kevin MacLeod");
		addSong("Determined Tumbao", "DeterminedTumbao.mp3", 20, "FreePlay Music");
		addSong("Flute", "flute.aif", 5, "Sun Microsystems");
		addSong("Loping Sting", "LopingSting.mp3", 4, "Kevin MacLeod");
		addSong("Space Music", "spacemusic.au", 6, "Unknown");
		addSong("Swing Cheese", "SwingCheese.mp3", 15, "FreePlay Music");
		addSong("Tada", "tada.wav", 2, "Microsoft");
		addSong("The Curtain Rises", "TheCurtainRises.mp3", 28, "Kevin MacLeod");
		addSong("Untameable Fire", "UntameableFire.mp3", 282, "Pierre Langer");
	}
	
	/**
	 * Adds a new song with the desired info to the library.
	 * 
	 * @param name The name of the song
	 * @param fileName The name of the file to which the song corresponds
	 * @param length The length, in seconds, of the song
	 */
	private void addSong(String name, String fileName, int length, String artist)
	{
		// add a song and register it with dateUpdater
		Song song = new Song(name, fileName, length, artist);
		songs.add(song);
		DateUpdater.getInstance().addObserver(song);
	}
	
	/**
	 * Saves the song library to a file
	 */
	public void saveLibrary()
	{
		try
		{
			FileOutputStream bytesToDisk = new FileOutputStream("library");
			ObjectOutputStream outFile = new ObjectOutputStream(bytesToDisk);
			// outFile understands the writeObject(Object o) message.
			outFile.writeObject(songs);
			outFile.close(); // Always close the output file!
			bytesToDisk.close();
		}
		catch (IOException ioe)
		{
			System.err.println("Writing SongLibrary objects failed");
			ioe.printStackTrace();
		}
	}
	
	/**
	 * Loads the song library from a file
	 */
	@SuppressWarnings("unchecked")
	public void loadLibrary()
	{
		try
		{
			FileInputStream rawBytes = new FileInputStream("library");
			ObjectInputStream inFile = new ObjectInputStream(rawBytes);
			// Need to cast Objects to the class they are known to be
			this.songs = (ArrayList<Song>) inFile.readObject();
			for (Song song : songs)
			{
				DateUpdater.getInstance().addObserver(song);
			}
			inFile.close();
			rawBytes.close();
		}
		catch (Exception e)
		{
			System.err.println("Reading SongLibrary objects failed");
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns a song with the given name from this SongLibrary.
	 * 
	 * @param name The name of the desired song.
	 * @return A song named with the specified name.
	 */
	public Song getSong(String name)
	{
		for (Song s : songs)
		{
			if (s.getName().equals(name))
			{
				return s;
			}
		}
		return null;
	}
	
	// Returns the type for each column
	@Override
	public Class<?> getColumnClass(int index)
	{
		if (index <= 1)
		{
			return String.class;
		}
		return Integer.class;
	}
	
	// 3 columns: Artist, Title, Length
	@Override
	public int getColumnCount()
	{
		return 3;
	}
	
	// Get the name for each column
	@Override
	public String getColumnName(int index)
	{
		if (index == 0)
		{
			return "Artist";
		}
		else if (index == 1)
		{
			return "Song";
		}
		return "Seconds";
	}
	
	// Number of rows = number of songs
	@Override
	public int getRowCount()
	{
		return songs.size();
	}
	
	// Returns the appropriate field of the appropriate song
	@Override
	public Object getValueAt(int row, int col)
	{
		Song song = songs.get(row);
		if (col == 0)
		{
			return song.getArtist();
		}
		if (col == 1)
		{
			return song.getName();
		}
		return song.getLength();
	}
	
	// Can not edit the table
	@Override
	public boolean isCellEditable(int row, int col)
	{
		return false;
	}
	
	// No need to implement these
	
	@Override
	public void addTableModelListener(TableModelListener listener) {}
	
	@Override
	public void removeTableModelListener(TableModelListener listener) {}
	
	@Override
	public void setValueAt(Object val, int row, int col) {}

}
