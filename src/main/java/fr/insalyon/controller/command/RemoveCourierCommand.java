package fr.insalyon.controller.command;

import fr.insalyon.model.Courier;
import fr.insalyon.model.DataModel;

public class RemoveCourierCommand implements Command {
    private DataModel dataModel = null;
    private Courier courierToRemove = null;

    /**
     * Used when initializing the command
     * @param dm the dataModel where the information is stored
     */
    public void removeCourier(DataModel dm) {
        dataModel = dm;
    }

    @Override
    public void doCommand() {
        for(Courier courier : dataModel.getCouriers().reversed()) {
            if (courier.isAvailable()) {
                courierToRemove = courier;
                dataModel.getCouriers().remove(courier);
                break;
            }
        }
    }

    @Override
    public void undoCommand() {
        if (courierToRemove != null && !dataModel.getCouriers().contains(courierToRemove)){
            dataModel.getCouriers().add(courierToRemove);
        }
    }
}
