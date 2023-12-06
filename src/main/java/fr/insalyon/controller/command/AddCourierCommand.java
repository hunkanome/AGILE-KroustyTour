package fr.insalyon.controller.command;

import fr.insalyon.model.Courier;
import fr.insalyon.model.DataModel;

/**
 *
 */
public class AddCourierCommand implements Command {

    DataModel dataModel = null;

    Courier courierToAdd = null;

    /**
     * Used when initializing the command
     * @param dataModel the dataModel where the information is stored
     * @param courier the courier to add
     */
    public void addCourier(DataModel dataModel, Courier courier) {
        this.dataModel = dataModel;
        this.courierToAdd = courier;
    }

    @Override
    public void doCommand() {
        if (!this.dataModel.getCouriers().contains(this.courierToAdd))
            this.dataModel.getCouriers().add(this.courierToAdd);
    }

    @Override
    public void undoCommand() {
        this.dataModel.getCouriers().remove(this.courierToAdd);
    }
}
