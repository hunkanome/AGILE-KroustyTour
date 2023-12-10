package fr.insalyon.controller.command;

import fr.insalyon.algorithm.AStar;
import fr.insalyon.model.DataModel;
import fr.insalyon.model.Delivery;
import fr.insalyon.model.Tour;

public class RemoveSelectedDeliveryCommand implements Command {

    protected DataModel dataModel;

    protected Tour tour;

    protected Delivery delivery;

    public RemoveSelectedDeliveryCommand(DataModel dataModel) {
        this.dataModel = dataModel;
        this.tour = dataModel.getSelectedTour();
        this.delivery = dataModel.getSelectedDelivery();
    }

    @Override
    public void doCommand() {
        this.tour.removeDelivery(this.delivery, this.dataModel.getCityMap(), new AStar());
        this.dataModel.setSelectedDelivery(null);
    }

    @Override
    public void undoCommand() {
        this.tour.addDelivery(this.delivery, dataModel.getCityMap(), new AStar());
        this.dataModel.setSelectedDelivery(delivery);
    }
}
