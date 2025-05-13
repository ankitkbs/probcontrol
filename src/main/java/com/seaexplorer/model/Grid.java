package com.seaexplorer.model;

import java.util.HashSet;
import java.util.Set;

public class Grid {
    private final int width;
    private final int height;
    private final Set<String> obstacles = new HashSet<>();

    public Grid(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void addObstacle(int x, int y) {
        obstacles.add(x + "," + y);
    }

    public boolean isObstacle(int x, int y) {
        return obstacles.contains(x + "," + y);
    }

    public boolean isWithinBounds(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }
}
