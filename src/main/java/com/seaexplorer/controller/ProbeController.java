package com.seaexplorer.controller;

import com.seaexplorer.model.Direction;
import com.seaexplorer.model.Position;
import com.seaexplorer.service.ProbeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/probe")
public class ProbeController {

    private final ProbeService probeService = new ProbeService(5, 5); // Default 5x5 grid

    @PostMapping("/init")
    public ResponseEntity<String> initialize(
            @RequestParam int x,
            @RequestParam int y,
            @RequestParam Direction direction) {
        probeService.initialize(x, y, direction);
        return ResponseEntity.ok("Probe initialized");
    }

    @PostMapping("/command")
    public ResponseEntity<Position> sendCommands(@RequestParam String commands) {
        probeService.executeCommands(commands);
        return ResponseEntity.ok(probeService.getCurrentPosition());
    }

    @GetMapping("/summary")
    public ResponseEntity<List<Position>> getVisitedCoordinates() {
        return ResponseEntity.ok(probeService.getVisitedPositions());
    }
}
