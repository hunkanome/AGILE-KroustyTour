package fr.insalyon.controller;

import fr.insalyon.model.Courier;
import fr.insalyon.model.DataModel;

public class ActionController {
    private DataModel dataModel;

    private MainController parentController;

    public void initialize(DataModel dataModel, MainController mainController) {
        this.parentController = mainController;
        this.dataModel = dataModel;
    }

    /**
     * Add couriers to the list
     * @param nbCouriers the number of couriers to add
     */
    public void addCouriers(int nbCouriers) {
        for (int i=0; i<nbCouriers; i++) {
            dataModel.getCouriers().add(new Courier());
        }
    }

    /**
     * Remove an available courier from the list if possible
     * @return true if a courier was removed, false otherwise
     */
    public boolean removeACourier() {
        for(Courier courier : dataModel.getCouriers()) {
            if (courier.isAvailable()) {
                dataModel.getCouriers().remove(courier);
                return true;
            }
        }
        return false;
    }
}
