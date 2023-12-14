package fr.insalyon.agile;

import fr.insalyon.controller.command.AddDeliveryCommand;
import fr.insalyon.geometry.GeoCoordinates;
import fr.insalyon.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddDeliveryCommandTest {

    DataModel dataModel;
    Delivery delivery;
    AddDeliveryCommand command;

    @BeforeEach
    void setUp() {
        this.dataModel = new DataModel();
        Courier courier = new Courier();
        Tour tour = new Tour(courier);
        this.dataModel.addTour(tour);
        this.dataModel.setSelectedTour(tour);
        Intersection intersection = new Intersection(1L, new GeoCoordinates(1F, 1F), 0);
        Intersection intersection2 = new Intersection(2L, new GeoCoordinates(2F, 2F), 1);
        Segment segment = new Segment(intersection, intersection2, "rue 1", 10F);
        Segment segment2 = new Segment(intersection2, intersection, "rue 2", 10F);
        intersection.addOutwardSegment(segment);
        intersection2.addOutwardSegment(segment2);
        CityMap map = new CityMap();
        map.addIntersection(intersection);
        map.addIntersection(intersection2);
        map.setWarehouse(intersection2);
        this.dataModel.setMap(map);
        this.delivery = new Delivery(intersection, TimeWindow.getTimeWindow(8));
        this.command = new AddDeliveryCommand(this.dataModel, this.delivery);
    }

    @Test
    void testAddDeliverySuccess(){
        // Normal usage
        assertFalse(this.dataModel.getSelectedTour().getDeliveriesList().contains(this.delivery));
        this.command.doCommand();
        assertTrue(this.dataModel.getSelectedTour().getDeliveriesList().contains(this.delivery));
    }

    @Test
    void testUndoAddDeliverySuccess() {
        assertFalse(this.dataModel.getSelectedTour().getDeliveriesList().contains(this.delivery));

        this.command.doCommand();
        assertTrue(this.dataModel.getSelectedTour().getDeliveriesList().contains(this.delivery));

        this.command.undoCommand();
        assertFalse(this.dataModel.getSelectedTour().getDeliveriesList().contains(this.delivery));
    }

    @Test
    void testUndoAddDeliveryAlreadyDone() {
        // If the courier to remove is not present then nothing should happen
        int lengthBefore = this.dataModel.getSelectedTour().getDeliveriesList().size();

        this.command.undoCommand();

        assertEquals(lengthBefore, this.dataModel.getSelectedTour().getDeliveriesList().size());
    }
}
