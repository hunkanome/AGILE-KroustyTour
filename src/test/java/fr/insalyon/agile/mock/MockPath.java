package fr.insalyon.agile.mock;

import fr.insalyon.model.Path;

/**
 * Mock for the Path to easily set the length without add segments
 */
public class MockPath extends Path {
    public void setLength(float length) {
        this.length = length;
    }
}
