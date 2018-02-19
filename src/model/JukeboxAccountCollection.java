package src.model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Stores and allows access to a list of user acconts.
 * 
 * @author Sean Gallagher
 */
public class JukeboxAccountCollection implements Serializable
{
	private ArrayList<JukeboxAccount> accounts = new ArrayList<JukeboxAccount>();
	
	private static final JukeboxAccountCollection instance = new JukeboxAccountCollection();
	
	/**
	 * Constructor for the collection
	 * The accounts are added to the arraylist
	 */
	private JukeboxAccountCollection()
	{
		char[] one = {'1'};
		char[] two = {'2','2'};
		char[] three = {'3','3','3'};
		char[] four = {'4','4','4','4'};
		
		addAccount("Chris", one);
		addAccount("Devon", two);
		addAccount("River", three);
		addAccount("Ryan", four);
	}
	
	public static synchronized JukeboxAccountCollection getInstance()
	{
		return instance;
	}
	
	/**
	 * Adds a new account with the given info to the list.
	 * 
	 * @param name The username
	 * @param pass The password
	 */
	private void addAccount(String name, char[] pass)
	{
		JukeboxAccount acc = new JukeboxAccount(name, pass);
		accounts.add(acc);
		DateUpdater.getInstance().addObserver(acc);
	}
	
	/**
	 * Searches for a user with the given username.
	 * 
	 * @param user The username of the desired account
	 * @return An account with a username of user, or null if none exists.
	 */
	public JukeboxAccount getAccount(String user)
	{
		for (int i = 0; i < accounts.size(); i++)
		{
			if (accounts.get(i).getUser().compareTo(user) == 0)
			{
				return accounts.get(i);
			}
		}
		return null;
	}
	
	/**
	 * Saves the accounts to a file
	 */
	public void saveAccounts()
	{
		try
		{
			FileOutputStream bytesToDisk = new FileOutputStream("accounts");
			ObjectOutputStream outFile = new ObjectOutputStream(bytesToDisk);
			// outFile understands the writeObject(Object o) message.
			outFile.writeObject(accounts);
			outFile.close(); // Always close the output file!
			bytesToDisk.close();
		}
		catch (IOException ioe)
		{
			System.err.println("Writing JukeboxAccountCollection objects failed");
			ioe.printStackTrace();
		}
	}
	
	/**
	 * Loads the accounts from a file
	 */
	@SuppressWarnings("unchecked")
	public void loadAccounts()
	{
		try
		{
			FileInputStream rawBytes = new FileInputStream("accounts");
			ObjectInputStream inFile = new ObjectInputStream(rawBytes);
			// Need to cast Objects to the class they are known to be
			this.accounts = (ArrayList<JukeboxAccount>) inFile.readObject();
			for (JukeboxAccount user : accounts)
			{
				DateUpdater.getInstance().addObserver(user);
			}
			inFile.close();
			rawBytes.close();
		}
		catch (Exception e)
		{
			System.err.println("Reading JukeboxAccountCollection objects failed");
		}
	}
}
