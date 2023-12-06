package fr.insalyon.controller.command;

import fr.insalyon.controller.command.Command;

import java.util.ArrayList;
import java.util.List;

/**
 * Class used to store the history of executed commands
 * It consists in a pseudo-stack of Command object
 * @see Command
 */
public class CommandList {

    private final List<Command> history;

    private int lastCommand;

    /**
     * Construct a new list of commands with an empty history
     */
    public CommandList() {
        this.history = new ArrayList<>();
        this.lastCommand = -1;
    }

    public List<Command> getHistory() {
        return history;
    }

    public int getLastCommand() {
        return lastCommand;
    }

    /**
     * Execute a command and add it to the history
     * @param command the command to execute
     * @see Command
     */
    public void execute(Command command) {
        this.history.add(command);
        this.lastCommand++;

        // if a command was executed after an undo, remove the undone commands from history
        while (this.lastCommand <= this.history.size() - 1) {
            this.history.remove(this.history.size() - 1);
        }
    }

    /**
     * Undo the last executed command
     * @see Command
     */
    public void undo() {
        if (this.lastCommand >= -1) {
            this.history.get(this.lastCommand).undoCommand();
            lastCommand--;
        }
    }

    /**
     * Redo the last command that was undone
     * @see Command
     */
    public void redo() {
        if (this.lastCommand < this.history.size()) {
            this.lastCommand++;
            this.history.get(this.lastCommand).doCommand();
        }
    }
}
