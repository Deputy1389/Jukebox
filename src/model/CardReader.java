package src.model;

import java.util.Arrays;

/**
 * Responsible for determining whether or not login credentials are valid.
 * 
 * @author Sean Gallagher
 */
public class CardReader 
{
	private JukeboxAccountCollection collection = JukeboxAccountCollection.getInstance();
	
	/**
	 * The authenticate method takes a string and char array as parameters and returns
	 * the account if valid or null if the information given is invalid.
	 * 
	 * @param user The username.
	 * @param pass The password, as a char array.
	 * @return The JukeboxAccount that matches the given credentials, or null if none exists.
	 */
	public JukeboxAccount authenticate(String user, char[] pass)
	{
		//gets the account with the name user
		JukeboxAccount account = collection.getAccount(user);
		
		//checks if the account exists
		if (account != null)
		{
			//gets the password for the account
			char[] password = account.getPass();
			//compares the password array with the given array
			if (Arrays.equals(pass, password))
			{
				//if they're equal return the account
				return account;
			}
		}
		//else return null
		return null;
	}
}
