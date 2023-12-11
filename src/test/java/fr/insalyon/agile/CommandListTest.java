package fr.insalyon.agile;

import fr.insalyon.controller.command.AddTourCommand;
import fr.insalyon.controller.command.CommandList;
import fr.insalyon.controller.command.RemoveSelectedTourCommand;
import fr.insalyon.model.Courier;
import fr.insalyon.model.DataModel;
import fr.insalyon.model.Tour;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class CommandListTest {

    @Test
    public void testHistory() {
        CommandList commandList = new CommandList();
        DataModel dataModel = new DataModel();
        Courier courier = new Courier();
        Tour tour = new Tour(courier);

        commandList.undo();
        Assertions.assertEquals(-1, commandList.getLastCommand());

        commandList.redo();
        Assertions.assertEquals(-1, commandList.getLastCommand());

        commandList.execute(new AddTourCommand(dataModel, tour));
        Assertions.assertEquals(0, commandList.getLastCommand());
        Assertions.assertEquals(1, commandList.getHistory().size());

        dataModel.setSelectedTour(tour);
        commandList.execute(new RemoveSelectedTourCommand(dataModel, dataModel.getSelectedTour()));
        Assertions.assertEquals(1, commandList.getLastCommand());
        Assertions.assertEquals(2, commandList.getHistory().size());

        commandList.undo();
        Assertions.assertEquals(0, commandList.getLastCommand());

        commandList.undo();
        Assertions.assertEquals(-1, commandList.getLastCommand());
        Assertions.assertEquals(2, commandList.getHistory().size());

        commandList.execute(new AddTourCommand(dataModel, tour));
        Assertions.assertEquals(0, commandList.getLastCommand());
        Assertions.assertEquals(1, commandList.getHistory().size());

        commandList.undo();
        Assertions.assertEquals(-1, commandList.getLastCommand());

        commandList.redo();
        Assertions.assertEquals(0, commandList.getLastCommand());

    }
}
