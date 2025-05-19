package com.seaexplorer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ProbeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void reset() throws Exception {
        // Re-initialize to a known state before each test
        mockMvc.perform(post("/probe/init")
                .param("x", "0")
                .param("y", "0")
                .param("direction", "NORTH"));
    }

    @Test
    void testProbeInitialization() throws Exception {
        mockMvc.perform(post("/probe/init")
                        .param("x", "2")
                        .param("y", "2")
                        .param("direction", "EAST"))
                .andExpect(status().isOk())
                .andExpect(content().string("Probe initialized"));
    }

    @Test
    void testInitializationWithObstacles() throws Exception {
        mockMvc.perform(post("/probe/init")
                        .param("x", "0")
                        .param("y", "0")
                        .param("direction", "NORTH")
                        .param("obstacles", "0,1;1,1"))
                .andExpect(status().isOk());
    }

    @Test
    void testCommandExecution() throws Exception {
        mockMvc.perform(post("/probe/command")
                        .param("commands", "FFRFF"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.x").value(2))
                .andExpect(jsonPath("$.y").value(2))
                .andExpect(jsonPath("$.direction").value("EAST"));
    }

    @Test
    void testInvalidCommand() throws Exception {
        mockMvc.perform(post("/probe/command")
                        .param("commands", "FX"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Invalid command")));
    }

    @Test
    void testObstacleAvoidance() throws Exception {
        mockMvc.perform(post("/probe/init")
                        .param("x", "1")
                        .param("y", "1")
                        .param("direction", "NORTH")
                        .param("obstacles", "1,2"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/probe/command")
                        .param("commands", "F"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.x").value(1))
                .andExpect(jsonPath("$.y").value(1)); // Should not move into obstacle
    }

    @Test
    void testInvalidObstaclePosition() throws Exception {
        mockMvc.perform(post("/probe/init")
                        .param("x", "0")
                        .param("y", "0")
                        .param("direction", "SOUTH")
                        .param("obstacles", "999,999"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Cannot place obstacle outside grid")));
    }

    @Test
    void testVisitedCoordinates() throws Exception {
        mockMvc.perform(post("/probe/init")
                        .param("x", "1")
                        .param("y", "1")
                        .param("direction", "NORTH"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/probe/command")
                .param("commands", "FF"));

        mockMvc.perform(get("/probe/summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(1))))
                .andExpect(jsonPath("$[0].x").exists())
                .andExpect(jsonPath("$[0].y").exists())
                .andExpect(jsonPath("$[0].direction").exists());
    }
}
