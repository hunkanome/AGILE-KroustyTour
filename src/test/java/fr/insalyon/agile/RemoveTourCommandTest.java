package fr.insalyon.agile;

import fr.insalyon.controller.command.RemoveTourCommand;
import fr.insalyon.model.DataModel;
import fr.insalyon.model.Tour;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RemoveTourCommandTest {
    DataModel dataModel;
    Tour tour;
    RemoveTourCommand command;
    
    @BeforeEach
    void setUp() {
        dataModel = new DataModel();
        tour = new Tour();
        dataModel.addTour(tour);
        command = new RemoveTourCommand(dataModel, tour);
    }

    @Test
    void testRemoveTourSuccess() {
        // Normal usage
        command.doCommand();
        assertFalse(dataModel.getTours().contains(tour));
    }

    @Test
    void testRemoveTourNothingToRemove() {
        // When there is nothing to remove it doesn't do anything
        dataModel.removeTour(tour);
        int lengthBefore = dataModel.getTours().size();

        command.doCommand();

        assertEquals(lengthBefore, dataModel.getTours().size());
    }

    @Test
    void testUndoRemoveTourSuccess() {
        // Normal usage when undoing
        command.doCommand();
        command.undoCommand();

        assertTrue(dataModel.getTours().contains(tour));
    }

    @Test
    void testUndoRemoveTourNothingToRemove() {
        // Undoing a removal that did nothing should do nothing
        int lengthBefore = dataModel.getTours().size();
        dataModel.removeTour(tour);

        command.doCommand();
        command.undoCommand();

        assertEquals(lengthBefore, dataModel.getTours().size());
    }
}
