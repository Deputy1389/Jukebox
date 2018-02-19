package src.controller;

import model.Jukebox;

/**
 * A singleton coordinator object for appropriate portions of the controller code.
 * Certain aspects of the controller code, such as ListenerFactory, rely on instances
 * of view objects, and are not suitable for inclusion here.
 * 
 * @author Taylor Heimbichner
 */
public class ControlCoord
{
	// single instance
	private static final ControlCoord instance = new ControlCoord();
	
	// our model
	private final Jukebox model;
	
	// controls song plays
	private final SongController songController;
	
	/**
	 * Makes a single ControlCoord
	 */
	private ControlCoord()
	{
		model = new Jukebox();
		songController = new SongController(model.getSongQueue());
	}
	
	/**
	 * Returns the single existing instance of ControlCoord
	 */
	public static ControlCoord getInstance()
	{
		return instance;
	}
	
	/**
	 * Returns the model
	 */
	public Jukebox getModel()
	{
		return model;
	}
	
	/**
	 * Returns the SongController
	 */
	public SongController getSongController()
	{
		return songController;
	}
}
