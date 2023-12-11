package fr.insalyon.controller.command;

import fr.insalyon.algorithm.AStar;
import fr.insalyon.model.DataModel;
import fr.insalyon.model.Delivery;
import fr.insalyon.model.Tour;

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
        this.tour.addDelivery(this.delivery, dataModel.getCityMap(), new AStar());
        this.dataModel.setSelectedIntersection(null);
        this.dataModel.setSelectedDelivery(delivery);
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
