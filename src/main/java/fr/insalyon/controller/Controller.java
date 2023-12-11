package fr.insalyon.controller;

import fr.insalyon.controller.command.CommandList;
import fr.insalyon.model.DataModel;

/**
 * A controller for the MVC pattern with JavaFX.
 * <p>
 * 
 * The initialize method allows for sharing the global context of the
 * application, with the data model, the main controller (if so) and the
 * command list.
 * <p>
 * 
 * The overriding method of the initialize method must call the super method.
 * The implementation can fill the view with the data model.
 */
public interface Controller {
	/**
	 * Initializes the controller with the global context of the application.
	 * 
	 * @param dataModel      - the data model
	 * @param mainController - the main controller (if so)
	 * @param commandList    - the command list
	 */
	void initialize(DataModel dataModel, MainController mainController, CommandList commandList);
}
