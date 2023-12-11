package fr.insalyon.agile;

import fr.insalyon.controller.command.AddTourCommand;
import fr.insalyon.model.Courier;
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
        this.dataModel = new DataModel();
        Courier courier = new Courier();
        this.tour = new Tour(courier);
        this.command = new AddTourCommand(this.dataModel, this.tour);
    }

    @Test
    void testAddTourSuccess(){
        // Normal usage
        assertFalse(this.dataModel.getTours().contains(this.tour));
        this.command.doCommand();
        assertTrue(this.dataModel.getTours().contains(this.tour));
    }

    @Test
    void testAddTourAlreadyAdded() {
        // Add courier shall not be added if it is already present
        this.dataModel.getTours().add(this.tour);
        int lengthBefore = this.dataModel.getTours().size();

        this.command.doCommand();

        assertEquals(lengthBefore, this.dataModel.getTours().size());
    }

    @Test
    void testUndoAddTourSuccess() {
        this.command.doCommand();
        assertTrue(this.dataModel.getTours().contains(tour));

        this.command.undoCommand();
        assertFalse(this.dataModel.getTours().contains(this.tour));
    }

    @Test
    void testUndoAddTourAlreadyAdded() {
        // If the courier to remove is not present then nothing should happen
        int lengthBefore = this.dataModel.getTours().size();

        this.command.undoCommand();

        assertEquals(lengthBefore, this.dataModel.getTours().size());
    }
}
