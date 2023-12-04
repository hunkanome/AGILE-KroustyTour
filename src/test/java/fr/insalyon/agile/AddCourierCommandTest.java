package fr.insalyon.agile;

import fr.insalyon.model.Courier;
import fr.insalyon.model.DataModel;

import static org.junit.jupiter.api.Assertions.*;
import fr.insalyon.controller.command.AddCourierCommand;
import org.junit.jupiter.api.Test;

class AddCourierCommandTest {

    @Test
    void testAddCourierSuccess(){
        // Normal usage
        DataModel dataModel = new DataModel();
        Courier courier = new Courier();
        AddCourierCommand command = new AddCourierCommand();

        command.addCourier(dataModel, courier);
        assertFalse(dataModel.getCouriers().contains(courier));
        command.doCommand();
        assertTrue(dataModel.getCouriers().contains(courier));
    }

    @Test
    void testAddCourierAlreadyAdded() {
        // Add courier shall not be added if it is already present
        DataModel dataModel = new DataModel();
        Courier courier = new Courier();
        AddCourierCommand command = new AddCourierCommand();

        dataModel.getCouriers().add(courier);
        int lengthBefore = dataModel.getCouriers().size();

        command.addCourier(dataModel, courier);
        command.doCommand();

        assertEquals(lengthBefore, dataModel.getCouriers().size());
    }

    @Test
    void testUndoAddCourierSuccess() {
        DataModel dataModel = new DataModel();
        Courier courier = new Courier();
        AddCourierCommand command = new AddCourierCommand();

        command.addCourier(dataModel, courier);
        command.doCommand();
        assertTrue(dataModel.getCouriers().contains(courier));

        command.undoCommand();
        assertFalse(dataModel.getCouriers().contains(courier));
    }

    @Test
    void testUndoAddCourierAlreadyAdded() {
        // If the courier to remove is not present then nothing should happen
        DataModel dataModel = new DataModel();
        Courier courier = new Courier();
        AddCourierCommand command = new AddCourierCommand();

        int lengthBefore = dataModel.getCouriers().size();

        command.addCourier(dataModel, courier);
        command.undoCommand();

        assertEquals(lengthBefore, dataModel.getCouriers().size());
    }
}
