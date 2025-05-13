package com.seaexplorer;

import com.seaexplorer.model.Direction;
import com.seaexplorer.model.Position;
import com.seaexplorer.service.ProbeService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProbeServiceTest {

    @Test
    void shouldMoveForwardFromInitialPosition() {
        ProbeService probeService = new ProbeService(5, 5);
        probeService.initialize(1, 2, Direction.NORTH);
        probeService.executeCommands("F");
        Position pos = probeService.getCurrentPosition();
        assertEquals(1, pos.getX());
        assertEquals(3, pos.getY());
        assertEquals(Direction.NORTH, pos.getDirection());
    }

    @Test
    void shouldRotateLeftAndRight() {
        ProbeService probeService = new ProbeService(5, 5);
        probeService.initialize(1, 2, Direction.NORTH);
        probeService.executeCommands("R");
        assertEquals(Direction.EAST, probeService.getCurrentPosition().getDirection());

        probeService.executeCommands("L");
        assertEquals(Direction.NORTH, probeService.getCurrentPosition().getDirection());
    }

    @Test
    void shouldMoveBackwardFromInitialPosition() {
        ProbeService probeService = new ProbeService(5, 5);
        probeService.initialize(1, 2, Direction.NORTH);
        probeService.executeCommands("B");
        Position pos = probeService.getCurrentPosition();
        assertEquals(1, pos.getX());
        assertEquals(1, pos.getY()); // Moving backward from NORTH
    }

    @Test
    void shouldNotMoveIntoObstacle() {
        ProbeService probeService = new ProbeService(5, 5);
        probeService.addObstacle(1, 3); // Set obstacle one step ahead
        probeService.initialize(1, 2, Direction.NORTH);
        probeService.executeCommands("F"); // Should try to move to (1,3)
        Position pos = probeService.getCurrentPosition();
        assertEquals(1, pos.getX());
        assertEquals(2, pos.getY()); // Should stay in place
    }

    @Test
    void shouldTrackVisitedCoordinates() {
        ProbeService probeService = new ProbeService(5, 5);
        probeService.initialize(1, 2, Direction.NORTH);
        probeService.executeCommands("FFRFF");

        List<Position> history = probeService.getVisitedPositions();
        assertEquals(5, history.size()); // Includes initial position + 4 moves
    }

}
