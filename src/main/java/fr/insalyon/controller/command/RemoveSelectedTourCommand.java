package fr.insalyon.controller.command;

import fr.insalyon.model.DataModel;
import fr.insalyon.model.Delivery;
import fr.insalyon.model.Tour;

public class RemoveSelectedTourCommand implements Command {

    private final DataModel dataModel;

    private final Tour tourToRemove;

    /**
     * Used when initializing the command
     * @param dataModel the dataModel where the information is stored
     */
    public RemoveSelectedTourCommand(DataModel dataModel, Tour tour) {
        this.dataModel = dataModel;
        this.tourToRemove = tour;
    }

    @Override
    public void doCommand() {
        if (this.tourToRemove != null && this.dataModel.getTours().contains(this.tourToRemove)) {
            this.dataModel.removeTour(this.tourToRemove);
            this.dataModel.setSelectedTour(null);
            Delivery selectedDelivery = this.dataModel.getSelectedDelivery();
            if (this.tourToRemove.getDeliveriesList().contains(selectedDelivery)) {
            	this.dataModel.setSelectedDelivery(null);
            }
        }
    }

    @Override
    public void undoCommand() {
        if (this.tourToRemove != null && !this.dataModel.getTours().contains(this.tourToRemove)){
            this.dataModel.addTour(this.tourToRemove);
            this.dataModel.setSelectedTour(this.tourToRemove);
        }
    }
}