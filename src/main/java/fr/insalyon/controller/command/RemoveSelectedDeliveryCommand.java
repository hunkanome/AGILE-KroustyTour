package fr.insalyon.controller.command;

import fr.insalyon.algorithm.AStar;
import fr.insalyon.model.DataModel;
import fr.insalyon.model.Delivery;
import fr.insalyon.model.Tour;

/**
 * Command used to remove a <code>Delivery</code> from a <code>Tour</code>
 * The <code>Delivery</code> to be deleted is the <code>SelectedDelivery</code> in the <code>DataModel</code>
 * @see CommandList
 * @see Tour
 * @see Delivery
 * @see DataModel
 */
public class RemoveSelectedDeliveryCommand implements Command {

    protected final DataModel dataModel;

    protected final Tour tour;

    protected final Delivery delivery;

    /**
     * Used when initializing the command
     * @param dataModel the dataModel where the information is stored
     */
    public RemoveSelectedDeliveryCommand(DataModel dataModel) {
        this.dataModel = dataModel;
        this.tour = dataModel.getSelectedTour();
        this.delivery = dataModel.getSelectedDelivery();
    }

    /**
     * Remove the delivery from the tour and recalculate the graph
     */
    @Override
    public void doCommand() {
        this.tour.removeDelivery(this.delivery, this.dataModel.getCityMap(), new AStar());
        this.dataModel.setSelectedDelivery(null);
    }

    /**
     * Undoes the removal of the delivery from the tour and recalculates the graph
     */
    @Override
    public void undoCommand() {
        this.tour.addDelivery(this.delivery, dataModel.getCityMap(), new AStar());
        this.dataModel.setSelectedDelivery(delivery);
    }
}
