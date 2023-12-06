package fr.insalyon.controller.command;

import fr.insalyon.model.Courier;
import fr.insalyon.model.DataModel;

public class RemoveCourierCommand implements Command {
    private DataModel dataModel = null;
    private Courier courierToRemove = null;

    /**
     * Used when initializing the command
     * @param dataModel the dataModel where the information is stored
     */
    public void removeCourier(DataModel dataModel) {
        this.dataModel = dataModel;
    }

    @Override
    public void doCommand() {
        for(Courier courier : this.dataModel.getCouriers().reversed()) {
            if (courier.isAvailable()) {
                this.courierToRemove = courier;
                this.dataModel.getCouriers().remove(courier);
                break;
            }
        }
    }

    @Override
    public void undoCommand() {
        if (this.courierToRemove != null && !this.dataModel.getCouriers().contains(this.courierToRemove)){
            this.dataModel.getCouriers().add(this.courierToRemove);
        }
    }
}
