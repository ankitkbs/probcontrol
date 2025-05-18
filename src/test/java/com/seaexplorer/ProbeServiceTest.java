package com.seaexplorer;

import com.seaexplorer.model.Direction;
import com.seaexplorer.model.Position;
import com.seaexplorer.repository.VisitedPositionRepository;
import com.seaexplorer.service.ProbeService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProbeServiceTest {

    private final VisitedPositionRepository mockRepo = Mockito.mock(VisitedPositionRepository.class);

    @Test
    void shouldMoveForwardFromInitialPosition() {
        ProbeService probeService = new ProbeService(mockRepo);
        probeService.initialize(1, 2, Direction.NORTH);
        probeService.executeCommands("F");
        Position pos = probeService.getCurrentPosition();
        assertEquals(1, pos.getX());
        assertEquals(3, pos.getY());
        assertEquals(Direction.NORTH, pos.getDirection());
    }

    @Test
    void shouldRotateLeftAndRight() {
        ProbeService probeService = new ProbeService(mockRepo);
        probeService.initialize(1, 2, Direction.NORTH);
        probeService.executeCommands("R");
        assertEquals(Direction.EAST, probeService.getCurrentPosition().getDirection());

        probeService.executeCommands("L");
        assertEquals(Direction.NORTH, probeService.getCurrentPosition().getDirection());
    }

    @Test
    void shouldMoveBackwardFromInitialPosition() {
        ProbeService probeService = new ProbeService(mockRepo);
        probeService.initialize(1, 2, Direction.NORTH);
        probeService.executeCommands("B");
        Position pos = probeService.getCurrentPosition();
        assertEquals(1, pos.getX());
        assertEquals(1, pos.getY());
    }

    @Test
    void shouldNotMoveIntoObstacle() {
        ProbeService probeService = new ProbeService(mockRepo);
        probeService.addObstacle(1, 3); // Set obstacle one step ahead
        probeService.initialize(1, 2, Direction.NORTH);
        probeService.executeCommands("F"); // Should try to move to (1,3)
        Position pos = probeService.getCurrentPosition();
        assertEquals(1, pos.getX());
        assertEquals(2, pos.getY()); // Should stay in place
    }

    @Test
    void shouldTrackVisitedCoordinates() {
        ProbeService probeService = new ProbeService(mockRepo);
        probeService.initialize(1, 2, Direction.NORTH);
        probeService.executeCommands("FFRFF");

        List<Position> history = probeService.getVisitedPositions();
        assertTrue(history.size() > 1); // Initial + movements
    }

    @Test
    void shouldParseValidObstacleList() {
        ProbeService probeService = new ProbeService(mockRepo);
        probeService.addObstaclesFromString("1,1;2,2;3,3");
        // Now try placing the probe and moving
        probeService.initialize(1, 0, Direction.NORTH);
        probeService.executeCommands("F"); // Hits (1,1), should stop
        Position pos = probeService.getCurrentPosition();
        assertEquals(1, pos.getX());
        assertEquals(0, pos.getY());
    }

    @Test
    void shouldHandleMalformedObstacleInputGracefully() {
        ProbeService probeService = new ProbeService(mockRepo);
        // Should skip all silently
        probeService.addObstaclesFromString("abc;2,x;3,");
        probeService.initialize(1, 2, Direction.NORTH);
        probeService.executeCommands("F");
        Position pos = probeService.getCurrentPosition();
        assertEquals(1, pos.getX());
        assertEquals(3, pos.getY()); // Moved forward despite garbage input
    }

    @Test
    void shouldThrowExceptionForInvalidStartPosition() {
        ProbeService probeService = new ProbeService(mockRepo);
        Exception ex = assertThrows(RuntimeException.class, () -> {
            probeService.initialize(99, 99, Direction.NORTH);
        });
        assertTrue(ex.getMessage().contains("out of bounds"));
    }

}
