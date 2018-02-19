package src.model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * An adapter that allows a LinkedList<Song> to be read like a List while enforcing
 * that it is only ever modified as a Queue. Has been further adapted to function
 * as a ListModel.
 * 
 * @author Taylor Heimbichner, Sean Gallagher
 */
public class ReadableSongQueue implements ListModel<Song>, Serializable
{
	// All of our data, stored as a list
	private List<Song> list;
	
	// All of our data, stored as a queue
	private Queue<Song> queue;
	
	// stores listeners
	private final ArrayList<ListDataListener> listeners = new ArrayList<ListDataListener>();
	
	/**
	 * Creates an empty Queue.
	 */
	public ReadableSongQueue()
	{
		LinkedList<Song> linkedList = new LinkedList<Song>();
		list = linkedList;
		queue = linkedList;
	}
	
	/**
	 * Adds the song to the collection as a queue
	 */
	public void addToQueue(Song song)
	{
		if (song != null)
		{
			queue.add(song);
		}
		notifyListeners();
	}
	
	/**
	 * Reads a song from the collection as a queue. That is, returns the oldest
	 * element in the collection. Removes the read element.
	 */
	public Song removeFromQueue()
	{
		Song ret = queue.remove();
		notifyListeners();
		return ret;
	}
	
	/**
	 * Reads a song from the collection as a queue. That is, returns the oldest
	 * element in the collection.
	 */
	public Song peekAtQueue()
	{
		return queue.peek();
	}
	
	/**
	 * Gets the given element of the Queue.
	 */
	public Song getSong(int i)
	{
		return list.get(i);
	}
	
	/**
	 * Notifies listeners whenever a change occurs. This is a simple implementation
	 * that tells each listener that the entirety of the model has changed whenever
	 * anything has changed.
	 */
	private void notifyListeners()
	{
		int type = ListDataEvent.CONTENTS_CHANGED;
		ListDataEvent e = new ListDataEvent(this, type, 0, getSize());
		for (ListDataListener listener : listeners)
		{
			listener.contentsChanged(e);
		}
	}
	
	/**
	 * Saves the song queue to a file
	 */
	public void saveQueue()
	{
		try
		{
			FileOutputStream bytesToDisk = new FileOutputStream("queue");
			ObjectOutputStream outFile = new ObjectOutputStream(bytesToDisk);
			// outFile understands the writeObject(Object o) message.
			outFile.writeObject(list);
			outFile.close(); // Always close the output file!
			bytesToDisk.close();
		}
		catch (IOException ioe)
		{
			System.err.println("Writing ReadableSongQueue objects failed");
			ioe.printStackTrace();
		}
	}
	
	/**
	 * Loads the song queue from a file
	 */
	@SuppressWarnings("unchecked")
	public void loadQueue()
	{
		try
		{
			FileInputStream rawBytes = new FileInputStream("queue");
			ObjectInputStream inFile = new ObjectInputStream(rawBytes);
			// Need to cast Objects to the class they are known to be
			LinkedList<Song> llist = (LinkedList<Song>) inFile.readObject();
			list = llist;
			queue = llist;
			inFile.close();
			rawBytes.close();
		}
		catch (Exception e)
		{
			System.err.println("Reading ReadableSongQueue objects failed");
			e.printStackTrace();
		}
	}

	// Returns the size of the list.
	@Override
	public int getSize()
	{
		return list.size();
	}

	// Returns values in the appropriate order
	@Override
	public Song getElementAt(int i)
	{
		return getSong(i);
	}

	// Adds the given listener to listeners
	@Override
	public void addListDataListener(ListDataListener listener)
	{
		listeners.add(listener);
	}

	// Removes the given listener from listeners
	@Override
	public void removeListDataListener(ListDataListener listener)
	{
		listeners.remove(listener);
	}
}
