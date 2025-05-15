// ProbeService.java
package com.seaexplorer.service;

import com.seaexplorer.model.Direction;
import com.seaexplorer.model.Grid;
import com.seaexplorer.model.Position;
import com.seaexplorer.model.VisitedPosition;
import com.seaexplorer.repository.VisitedPositionRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class ProbeService {
    private static final Logger logger = LogManager.getLogger(ProbeService.class);
    private Position position;
    private Grid grid;
    private List<Position> history = new ArrayList<>();
    @Autowired
    private VisitedPositionRepository visitedRepo;

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
        logger.info("Executing command sequence: {}", commands);
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

        VisitedPosition entity = new VisitedPosition();
        entity.setX(position.getX());
        entity.setY(position.getY());
        entity.setDirection(position.getDirection());
        visitedRepo.save(entity);
    }

    public List<Position> getVisitedPositions() {
        return history;
    }

}
