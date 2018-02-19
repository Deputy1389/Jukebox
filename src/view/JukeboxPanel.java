package src.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import controller.ListenerFactory;
import model.JukeboxAccount;

/**
 * Defines a login panel for the Jukebox GUI.
 * 
 * @author Sean Gallagher, Taylor Heimbichner
 */
@SuppressWarnings("serial")
public class JukeboxPanel extends JPanel
{
	private static final Color BG_COLOR = Color.WHITE;
	
	// Text field for the username
	private JTextField username;
	
	// Password field
	private JPasswordField password;
	
	// Gives user information about current login
	private JLabel statusText = new JLabel("Not currently signed in");
	
	/**
	 * Constructs a JPanel that has appropriate log in/out components
	 * @param factory A factory used to create event listeners.
	 */
	public JukeboxPanel(ListenerFactory factory)
	{
		addComponents(factory);
	}
	
	/**
	 * Adds the username and password textfields and the login/logout buttons
	 * as well as the information text.
	 * 
	 * @param factory The ListenerFactory used to add listeners to the buttons
	 */
	private void addComponents(ListenerFactory factory)
	{
		JPanel componentPanel = new JPanel();
		componentPanel.setLayout(new GridLayout(0, 1));
		componentPanel.setBackground(BG_COLOR);
		
		JPanel usernamePanel = new JPanel();
		usernamePanel.setBackground(BG_COLOR);
		usernamePanel.add(new JLabel("Username"));
		username = new JTextField(8);
		usernamePanel.add(username);
		componentPanel.add(usernamePanel);
		
		JPanel passwordPanel = new JPanel();
		passwordPanel.setBackground(BG_COLOR);
		passwordPanel.add(new JLabel("Password"));
		password = new JPasswordField(8);
		passwordPanel.add(password);
		componentPanel.add(passwordPanel);

		JPanel logPanel = new JPanel();
		logPanel.setBackground(BG_COLOR);		
		JButton loginButton = new JButton("Sign in");
		loginButton.addActionListener(factory.makeLoginListener());
		logPanel.add(loginButton);
		JButton logoutButton = new JButton("Sign out");
		logoutButton.addActionListener(factory.makeLogoutListener());
		logPanel.add(logoutButton);
		componentPanel.add(logPanel);
		
		JPanel statusPanel = new JPanel();
		statusPanel.setBackground(BG_COLOR);
		statusPanel.setPreferredSize(new Dimension(400, 0));
		statusPanel.add(statusText);
		componentPanel.add(statusPanel);
		
		add(componentPanel);
	}
	
	/**
	 * Updates the view when someone logs in.
	 * 
	 * @param success If the login was successful
	 * @param user A reference to the newly logged in user, null if success == false
	 */
	public void onLogin(boolean success, JukeboxAccount user)
	{
		if (success)
		{
			updateStatus(user);
		}
		else
		{
			//No one logged in, display error
			statusText.setText("Invalid Username or Password");
		}
		// Reset user and pass text fields
		username.setText("");
		password.setText("");
	}
	
	/**
	 * Updates the view when someone logs out.
	 */
	public void onLogout()
	{
		username.setText("");
		password.setText("");
		statusText.setText("Logged out");
	}
	
	/**
	 * Returns a reference to the username text field.
	 */
	public JTextField getUsernameField()
	{
		return username;
	}
	
	/**
	 * Returns a reference to the password field.
	 */
	public JPasswordField getPasswordField()
	{
		return password;
	}
	
	/**
	 * Updates the status label with the appropriate user info.
	 * 
	 * @param user The current user.
	 */
	public void updateStatus(JukeboxAccount user)
	{
		int secs = user.getTime() % 60;
		int mins = (user.getTime() / 60) % 60;
		int hrs = user.getTime() / 3600;
		String text = "Logged in as: " + user.getUser();
		text += String.format("   Status: %d times played, ", user.getTimesPlayed());
		text += String.format("%d:%02d:%02d remaining", hrs, mins, secs);
		statusText.setText(text);
	}
}
