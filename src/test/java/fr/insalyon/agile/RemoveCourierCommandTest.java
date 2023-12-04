package fr.insalyon.agile;

import fr.insalyon.controller.command.RemoveCourierCommand;
import fr.insalyon.model.Courier;
import fr.insalyon.model.DataModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RemoveCourierCommandTest {

    @Test
    void testRemoveCourierSuccess() {
        // Normal usage
        DataModel dataModel = new DataModel();
        Courier courier = new Courier();
        dataModel.getCouriers().add(courier);
        RemoveCourierCommand command = new RemoveCourierCommand();
        command.removeCourier(dataModel);

        command.doCommand();

        assertFalse(dataModel.getCouriers().contains(courier));
    }

    @Test
    void testRemoveCourierNothingToRemove() {
        // When there is nothing to remove it doesn't do anything
        DataModel dataModel = new DataModel();
        dataModel.getCouriers().getFirst().setAvailable(false);
        RemoveCourierCommand command = new RemoveCourierCommand();
        command.removeCourier(dataModel);
        int lengthBefore = dataModel.getCouriers().size();

        command.doCommand();

        assertEquals(lengthBefore, dataModel.getCouriers().size());
    }

    @Test
    void testUndoRemoveCourierSuccess() {
        // Normal usage when undoing
        DataModel dataModel = new DataModel();
        Courier courier = new Courier();
        dataModel.getCouriers().add(courier);
        RemoveCourierCommand command = new RemoveCourierCommand();
        command.removeCourier(dataModel);

        command.doCommand();
        command.undoCommand();

        assertTrue(dataModel.getCouriers().contains(courier));
    }

    @Test
    void testUndoRemoveCourierNothingToRemove() {
        // Undoing a removal that did nothing should do nothing
        DataModel dataModel = new DataModel();
        dataModel.getCouriers().getFirst().setAvailable(false);
        RemoveCourierCommand command = new RemoveCourierCommand();
        command.removeCourier(dataModel);
        int lengthBefore = dataModel.getCouriers().size();

        command.doCommand();
        command.undoCommand();

        assertEquals(lengthBefore, dataModel.getCouriers().size());
    }
}
