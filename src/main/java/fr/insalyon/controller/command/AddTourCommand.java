package fr.insalyon.controller.command;

import fr.insalyon.model.DataModel;
import fr.insalyon.model.Delivery;
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
	 * @param tour      the tour to add
	 */
	public AddTourCommand(DataModel dataModel, Tour tour) {
		this.dataModel = dataModel;
		this.tourToAdd = tour;
	}

	/*
	 * Adds the tour to the DataModel
	 */
	@Override
	public void doCommand() {
		if (!this.dataModel.getTours().contains(this.tourToAdd)) {
			this.dataModel.addTour(this.tourToAdd);
			this.dataModel.setSelectedTour(this.tourToAdd);
		}
	}

	/*
	 * Removes the added tour from the DataModel
	 */
	@Override
    public void undoCommand() {
        if (this.dataModel.getTours().contains(tourToAdd)) {
            this.dataModel.removeTour(this.tourToAdd);
            this.dataModel.setSelectedTour(null);
            Delivery selectedDelivery = this.dataModel.getSelectedDelivery();
            if (this.tourToAdd.getDeliveriesList().contains(selectedDelivery)) {
            	this.dataModel.setSelectedDelivery(null);
            }
        }
    }
}
