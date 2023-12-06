package fr.insalyon.controller;

import fr.insalyon.model.Courier;
import fr.insalyon.model.DataModel;

public class ActionController {
    private DataModel dataModel;

    private MainController parentController;

    private CommandList commandList;

    /**
     * Initialize the controller variables
     * @param dataModel the controller data
     * @param mainController the parent controller
     * @param commandList the command history
     */
    public void initialize(DataModel dataModel, MainController mainController, CommandList commandList) {
        this.parentController = mainController;
        this.dataModel = dataModel;
        this.commandList = commandList;
    }
}
