// ProbeService.java
package com.seaexplorer.service;

import com.seaexplorer.model.Direction;
import com.seaexplorer.model.Position;

public class ProbeService {
    private final int maxX;
    private final int maxY;
    private Position position;

    public ProbeService(int maxX, int maxY) {
        this.maxX = maxX;
        this.maxY = maxY;
    }

    public void initialize(int x, int y, Direction direction) {
        this.position = new Position(x, y, direction);
    }

    public void executeCommands(String commands) {
        for (char cmd : commands.toCharArray()) {
            switch (cmd) {
                case 'F' -> position.moveForward();
                case 'L' -> position.turnLeft();
                case 'R' -> position.turnRight();
            }
        }
    }

    public Position getCurrentPosition() {
        return position;
    }

}
