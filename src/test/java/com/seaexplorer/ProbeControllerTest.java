package com.seaexplorer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ProbeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void resetState() throws Exception {
        mockMvc.perform(post("/probe/init")
                .param("x", "0")
                .param("y", "0")
                .param("direction", "NORTH"));
    }

    @Test
    void shouldInitAndMoveProbe() throws Exception {
        // Initialize probe
        mockMvc.perform(post("/probe/init")
                        .param("x", "1")
                        .param("y", "2")
                        .param("direction", "NORTH"))
                .andExpect(status().isOk())
                .andExpect(content().string("Probe initialized"));

        // Send movement commands
        mockMvc.perform(post("/probe/command")
                        .param("commands", "FFRFF"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.x").value(1))
                .andExpect(jsonPath("$.y").value(2))
                .andExpect(jsonPath("$.direction").value("EAST"));
    }

    @Test
    void shouldReturnVisitedPositions() throws Exception {
        mockMvc.perform(get("/probe/summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void shouldInitializeWithObstaclesAndAvoidThem() throws Exception {
        mockMvc.perform(post("/probe/init")
                        .param("x", "1")
                        .param("y", "2")
                        .param("direction", "NORTH")
                        .param("obstacles", "1,3;2,2"))
                .andExpect(status().isOk())
                .andExpect(content().string("Probe initialized"));

        // move forward should hit obstacle at (1,3) and stay in place
        mockMvc.perform(post("/probe/command")
                        .param("commands", "F"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.x").value(1))
                .andExpect(jsonPath("$.y").value(2));
    }

    @Test
    void shouldFailOnInvalidCommand() throws Exception {
        mockMvc.perform(post("/probe/init")
                        .param("x", "1")
                        .param("y", "2")
                        .param("direction", "NORTH"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/probe/command")
                        .param("commands", "X")) // Invalid
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Invalid command")));
    }

    @Test
    void shouldFailOnObstacleOutsideGrid() throws Exception {
        mockMvc.perform(post("/probe/init")
                        .param("x", "1")
                        .param("y", "2")
                        .param("direction", "NORTH")
                        .param("obstacles", "99,99"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Cannot place obstacle outside grid")));
    }

}
