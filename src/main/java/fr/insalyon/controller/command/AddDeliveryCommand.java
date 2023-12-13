package fr.insalyon.controller.command;

import fr.insalyon.algorithm.AStar;
import fr.insalyon.model.DataModel;
import fr.insalyon.model.Delivery;
import fr.insalyon.model.Tour;
import fr.insalyon.view.PopUp;

/**
 * Command used to add a <code>Delivery</code> to a <code>Tour</code>
 * @see CommandList
 * @see Tour
 * @see Delivery
 * @see DataModel
 */
public class AddDeliveryCommand implements Command {
    private final DataModel dataModel;

    private final Delivery delivery;

    private final Tour tour;

    /**
     * Used when initializing the command
     * @param dataModel the dataModel where the information is stored
     * @param delivery the delivery to add
     */
    public AddDeliveryCommand(DataModel dataModel, Delivery delivery) {
        this.dataModel = dataModel;
        this.tour = dataModel.getSelectedTour();
        this.delivery = delivery;
    }

    /**
     * Add the delivery to the tour and recalculate the graph
     */
    @Override
    public void doCommand() {
        int sizeBefore = this.tour.getDeliveriesList().size();
        this.tour.addDelivery(this.delivery, dataModel.getCityMap(), new AStar());
        this.dataModel.setSelectedIntersection(null);
        if (sizeBefore < this.tour.getDeliveriesList().size()) {
            this.dataModel.setSelectedDelivery(delivery);
        } else { // If the delivery could not be added and was deleted
            this.dataModel.setSelectedDelivery(null);
            PopUp popUpErrorMessage = new PopUp("Error", "The delivery could not be added\nto the tour because your courier\nwould be trapped.");
            popUpErrorMessage.show();
        }

    }

    /**
     * Undoes the addition of the delivery and recalculate the graph
     */
    @Override
    public void undoCommand() {
        this.tour.removeDelivery(this.delivery, dataModel.getCityMap(), new AStar());
        this.dataModel.setSelectedDelivery(null);
    }
}
