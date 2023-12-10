package fr.insalyon.controller.command;

import fr.insalyon.model.DataModel;
import fr.insalyon.model.Tour;

/**
 *
 */
public class AddTourCommand implements Command {

    private final DataModel dataModel;

    private final Tour tourToAdd;

    /**
     * Used when initializing the command
     * @param dataModel the dataModel where the information is stored
     * @param tour the tour to add
     */
    public AddTourCommand(DataModel dataModel, Tour tour) {
        this.dataModel = dataModel;
        this.tourToAdd = tour;
    }

    @Override
    public void doCommand() {
        if (!this.dataModel.getTours().contains(this.tourToAdd)){
            this.dataModel.addTour(this.tourToAdd);
        }
    }

    @Override
    public void undoCommand() {
        if (this.dataModel.getTours().contains(tourToAdd)) {
            this.dataModel.removeTour(this.tourToAdd);
        }
    }
}
