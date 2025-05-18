package com.seaexplorer.controller;

import com.seaexplorer.model.Direction;
import com.seaexplorer.model.Position;
import com.seaexplorer.service.ProbeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/probe")
public class ProbeController {

    private static final Logger logger = LogManager.getLogger(ProbeController.class);
    @Autowired
    private ProbeService probeService;
    @PostMapping("/init")
    public ResponseEntity<String> initialize(
            @RequestParam int x,
            @RequestParam int y,
            @RequestParam Direction direction) {
        logger.info("Initializing probe at {},{} facing {}", x, y, direction);
        probeService.initialize(x, y, direction);
        return ResponseEntity.ok("Probe initialized");
    }

    @PostMapping("/command")
    public ResponseEntity<Position> sendCommands(@RequestParam String commands) {
        probeService.executeCommands(commands);
        return ResponseEntity.ok(probeService.getCurrentPosition());
    }

    @GetMapping("/summary")
    public ResponseEntity<List<Position>> getVisitdCeoordinates() {
        return ResponseEntity.ok(probeService.getVisitedPositions());
    }
}
