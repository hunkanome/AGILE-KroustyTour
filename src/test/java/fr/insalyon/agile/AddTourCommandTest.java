package fr.insalyon.agile;

import fr.insalyon.algorithm.CityMapMatrix;
import fr.insalyon.controller.command.AddTourCommand;
import fr.insalyon.model.DataModel;
import fr.insalyon.model.Tour;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddTourCommandTest {
    DataModel dataModel;
    Tour tour;
    AddTourCommand command;

    @BeforeEach
    void setUp() {
        dataModel = new DataModel();
        tour = new Tour(new CityMapMatrix(dataModel.getCityMap()));
        command = new AddTourCommand(dataModel, tour);
    }

    @Test
    void testAddTourSuccess(){
        // Normal usage
        assertFalse(dataModel.getTours().contains(tour));
        command.doCommand();
        assertTrue(dataModel.getTours().contains(tour));
    }

    @Test
    void testAddTourAlreadyAdded() {
        // Add courier shall not be added if it is already present
        dataModel.getTours().add(tour);
        int lengthBefore = dataModel.getTours().size();

        command.doCommand();

        assertEquals(lengthBefore, dataModel.getTours().size());
    }

    @Test
    void testUndoAddTourSuccess() {
        command.doCommand();
        assertTrue(dataModel.getTours().contains(tour));

        command.undoCommand();
        assertFalse(dataModel.getTours().contains(tour));
    }

    @Test
    void testUndoAddTourAlreadyAdded() {
        // If the courier to remove is not present then nothing should happen
        int lengthBefore = dataModel.getTours().size();

        command.undoCommand();

        assertEquals(lengthBefore, dataModel.getTours().size());
    }
}
