package fr.insalyon.agile;

import fr.insalyon.controller.command.CommandList;
import fr.insalyon.controller.command.AddCourierCommand;
import fr.insalyon.controller.command.RemoveCourierCommand;
import fr.insalyon.model.Courier;
import fr.insalyon.model.DataModel;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class CommandListTest {

    @Test
    public void testHistory() {
        CommandList commandList = new CommandList();
        DataModel dataModel = new DataModel();
        Courier courier = new Courier();

        commandList.undo();
        Assertions.assertEquals(-1, commandList.getLastCommand());

        commandList.redo();
        Assertions.assertEquals(-1, commandList.getLastCommand());

        commandList.execute(new AddCourierCommand(dataModel, courier));
        Assertions.assertEquals(0, commandList.getLastCommand());
        Assertions.assertEquals(1, commandList.getHistory().size());

        commandList.execute(new RemoveCourierCommand(dataModel));
        Assertions.assertEquals(1, commandList.getLastCommand());
        Assertions.assertEquals(2, commandList.getHistory().size());

        commandList.undo();
        Assertions.assertEquals(0, commandList.getLastCommand());

        commandList.undo();
        Assertions.assertEquals(-1, commandList.getLastCommand());
        Assertions.assertEquals(2, commandList.getHistory().size());

        commandList.execute(new AddCourierCommand(dataModel, courier));
        Assertions.assertEquals(0, commandList.getLastCommand());
        Assertions.assertEquals(1, commandList.getHistory().size());

        commandList.undo();
        Assertions.assertEquals(-1, commandList.getLastCommand());

        commandList.redo();
        Assertions.assertEquals(0, commandList.getLastCommand());

    }
}
