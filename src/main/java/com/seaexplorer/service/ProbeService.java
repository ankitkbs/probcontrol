package com.seaexplorer.service;

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
        this.position = new Position(x, y, direction);
        history.clear();
        track(); // Log the initial position
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
