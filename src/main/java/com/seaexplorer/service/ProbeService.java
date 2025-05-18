package com.seaexplorer.service;

import com.seaexplorer.exception.ProbeException;
import com.seaexplorer.model.*;
import com.seaexplorer.repository.VisitedPositionRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProbeService {

    private static final Logger logger = LogManager.getLogger(ProbeService.class);
    private static final int DEFAULT_GRID_WIDTH = 5;
    private static final int DEFAULT_GRID_HEIGHT = 5;

    private final VisitedPositionRepository visitedRepo;
    private final Grid grid;

    private Position position;
    private final List<Position> history = new ArrayList<>();

    public ProbeService(VisitedPositionRepository visitedRepo) {
        this.visitedRepo = visitedRepo;
        this.grid = new Grid(DEFAULT_GRID_WIDTH, DEFAULT_GRID_HEIGHT);
    }

    public void initialize(int x, int y, Direction direction) {
        if (!grid.isWithinBounds(x, y)) {
            throw new ProbeException("Starting position is out of bounds.");
        }
        this.position = new Position(x, y, direction);
        history.clear();
        track(); // Log the initial position
    }

    public void addObstacle(int x, int y) {
        if (!grid.isWithinBounds(x, y)) {
            throw new ProbeException("Cannot place obstacle outside grid at (" + x + "," + y + ")");
        }
        grid.addObstacle(x, y);
    }

    public void addObstaclesFromString(String obstacleString) {
        if (obstacleString == null || obstacleString.isBlank()) return;

        String[] obstacleList = obstacleString.split(";");
        for (String coord : obstacleList) {
            String[] parts = coord.split(",");
            if (parts.length == 2) {
                try {
                    int ox = Integer.parseInt(parts[0].trim());
                    int oy = Integer.parseInt(parts[1].trim());
                    addObstacle(ox, oy);
                } catch (NumberFormatException e) {
                    logger.warn("Skipping invalid obstacle: {}", coord);
                }
            } else {
                logger.warn("Invalid obstacle format: {}", coord);
            }
        }
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
                        track();
                    }
                }
                case 'B' -> {
                    Position temp = new Position(x, y, position.getDirection());
                    temp.moveBackward();
                    if (grid.isWithinBounds(temp.getX(), temp.getY()) && !grid.isObstacle(temp.getX(), temp.getY())) {
                        position.moveBackward();
                        track();
                    }
                }
                case 'L' -> {
                    position.turnLeft();
                    track();
                }
                case 'R' -> {
                    position.turnRight();
                    track();
                }
                default -> throw new ProbeException("Invalid command: '" + cmd + "'. Allowed commands are F, B, L, R.");
            }
        }
    }

    private void track() {
        Position snapshot = new Position(position.getX(), position.getY(), position.getDirection());
        history.add(snapshot);

        VisitedPosition entity = new VisitedPosition();
        entity.setX(snapshot.getX());
        entity.setY(snapshot.getY());
        entity.setDirection(snapshot.getDirection());
        visitedRepo.save(entity);
    }

    public Position getCurrentPosition() {
        return position;
    }

    public List<Position> getVisitedPositions() {
        return history;
    }
}
