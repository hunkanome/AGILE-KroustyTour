package fr.insalyon.command;

/**
 * Interface for the command design pattern
 */
public interface Command {
    /**
     * Execute the command
     */
    void doCommand();

    /**
     * Undo the command
     */
    void undoCommand();
}
