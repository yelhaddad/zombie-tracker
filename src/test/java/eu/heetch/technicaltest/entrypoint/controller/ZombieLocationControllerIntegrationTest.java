package eu.heetch.technicaltest.entrypoint.controller;

import eu.heetch.technicaltest.configuration.PostgresContainerExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.org.apache.commons.io.FileUtils;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(PostgresContainerExtension.class)
@DirtiesContext
class ZombieLocationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Sql(value = "/scripts/insert_zombie.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/scripts/clear_table_zombie.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void shouldGetNotCapturedZombiesNearTo() throws Exception {
        ClassPathResource resource = new ClassPathResource("json/expected_response.json");
        String expectedResponse = FileUtils.readFileToString(resource.getFile(), StandardCharsets.UTF_8);

        mockMvc.perform(
                        get("/zombies")
                                .queryParam("longitude", "2.294533")
                                .queryParam("latitude", "48.85905")
                                .queryParam("limit", "5")
                ).andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    @Sql(value = "/scripts/insert_zombie.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/scripts/clear_table_zombie.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void shouldReturnBadRequestWhenLatitudeIsMissing() throws Exception {
        mockMvc.perform(
                get("/zombies")
                        .queryParam("latitude", "2.294533")
                        .queryParam("limit", "5")
        ).andExpect(status().isBadRequest());
    }

    @Test
    @Sql(value = "/scripts/insert_zombie.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/scripts/clear_table_zombie.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void shouldReturnBadRequestWhenLongitudeIsMissing() throws Exception {
        mockMvc.perform(
                get("/zombies")
                        .queryParam("longitude", "2.294533")
                        .queryParam("limit", "5")
        ).andExpect(status().isBadRequest());
    }
}