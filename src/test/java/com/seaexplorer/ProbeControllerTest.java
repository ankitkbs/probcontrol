package com.seaexplorer;

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
                .andExpect(jsonPath("$.x").value(3))
                .andExpect(jsonPath("$.y").value(4))
                .andExpect(jsonPath("$.direction").value("EAST"));
    }

    @Test
    void shouldReturnVisitedPositions() throws Exception {
        mockMvc.perform(get("/probe/summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
