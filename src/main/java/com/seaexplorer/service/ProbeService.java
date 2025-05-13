// ProbeService.java
package com.seaexplorer.service;

import com.seaexplorer.model.Direction;
import com.seaexplorer.model.Grid;
import com.seaexplorer.model.Position;

import java.util.ArrayList;
import java.util.List;

public class ProbeService {
    private Position position;
    private Grid grid;
    private List<Position> history = new ArrayList<>();

    public void initialize(int x, int y, Direction direction) {
        this.position = new Position(x, y, direction);
        history.clear();
        history.add(new Position(x, y, direction));
    }

    public ProbeService(int maxX, int maxY) {
        this.grid = new Grid(maxX, maxY);
    }

    public void addObstacle(int x, int y) {
        grid.addObstacle(x, y);
    }

    public void executeCommands(String commands) {
        for (char cmd : commands.toCharArray()) {
            int x = position.getX();
            int y = position.getY();
            switch (cmd) {
                case 'F' -> {
                    Position temp = new Position(x, y, position.getDirection());
                    temp.moveForward();
                    if (grid.isWithinBounds(temp.getX(), temp.getY()) && !grid.isObstacle(temp.getX(), temp.getY())) {
                        position.moveForward();
                    }
                }
                case 'B' -> {
                    Position temp = new Position(x, y, position.getDirection());
                    temp.moveBackward();
                    if (grid.isWithinBounds(temp.getX(), temp.getY()) && !grid.isObstacle(temp.getX(), temp.getY())) {
                        position.moveBackward();
                    }
                }
                case 'L' -> position.turnLeft();
                case 'R' -> position.turnRight();
            }
        }
    }


    public Position getCurrentPosition() {
        return position;
    }




    private void track() {
        history.add(new Position(position.getX(), position.getY(), position.getDirection()));
    }

    public List<Position> getVisitedPositions() {
        return history;
    }

}
