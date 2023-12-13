package fr.insalyon.controller.command;

import fr.insalyon.algorithm.AStar;
import fr.insalyon.model.DataModel;
import fr.insalyon.model.Delivery;
import fr.insalyon.model.Tour;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

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
            // Opens another window that displays the current application version
            Stage window = new Stage();
            window.setTitle("Error");
            window.setResizable(false);
            window.initModality(javafx.stage.Modality.APPLICATION_MODAL);

            // Add a label to the window containing the version
            Label label = new Label("The delivery could not be added\nto the tour because your courier\nwould be trapped.");
            label.setAlignment(Pos.BASELINE_CENTER);
            label.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
            label.setPrefHeight(100);
            label.setPrefWidth(200);

            // add the label to a pane
            Pane pane = new Pane();
            pane.getChildren().add(label);

            // add the pane to the window
            Scene scene = new Scene(pane);
            window.setScene(scene);

            window.show();
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
