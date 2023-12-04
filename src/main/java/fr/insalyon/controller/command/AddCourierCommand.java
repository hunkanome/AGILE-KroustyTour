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
     * @param dm the dataModel where the information is stored
     * @param c the courier to add
     */
    public void addCourier(DataModel dm, Courier c) {
        dataModel = dm;
        courierToAdd = c;
    }

    @Override
    public void doCommand() {
        if (!dataModel.getCouriers().contains(courierToAdd))
            dataModel.getCouriers().add(courierToAdd);
    }

    @Override
    public void undoCommand() {
        dataModel.getCouriers().remove(courierToAdd);
    }
}
