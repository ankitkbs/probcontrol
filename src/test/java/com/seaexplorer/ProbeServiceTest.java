package com.seaexplorer;

import com.seaexplorer.model.Direction;
import com.seaexplorer.model.Position;
import com.seaexplorer.service.ProbeService;
import org.junit.jupiter.api.Test;
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




}
