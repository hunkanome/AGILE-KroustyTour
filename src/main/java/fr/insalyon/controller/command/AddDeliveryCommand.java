package fr.insalyon.controller.command;

import fr.insalyon.algorithm.AStar;
import fr.insalyon.model.DataModel;
import fr.insalyon.model.Delivery;
import fr.insalyon.model.Tour;

public class AddDeliveryCommand implements Command {
    private final DataModel dataModel;

    private final Delivery delivery;

    private final Tour tour;

    public AddDeliveryCommand(DataModel dataModel, Delivery delivery) {
        this.dataModel = dataModel;
        this.tour = dataModel.getSelectedTour();
        this.delivery = delivery;
    }

    @Override
    public void doCommand() {
        this.tour.addDelivery(this.delivery, dataModel.getCityMap(), new AStar());
    }

    @Override
    public void undoCommand() {
        this.tour.removeDelivery(this.delivery, dataModel.getCityMap(), new AStar());
    }
}
