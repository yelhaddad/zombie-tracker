package eu.heetch.technicaltest.repository;

import eu.heetch.technicaltest.configuration.PostgresContainerExtension;
import eu.heetch.technicaltest.model.Zombie;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(PostgresContainerExtension.class)
@DirtiesContext
class ZombieRepositoryIntegrationTest {

    @Autowired
    private ZombieRepository zombieRepository;

    @Test
    @Sql(value = "/scripts/insert_zombie.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/scripts/clear_table_zombie.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void shouldUpsertZombieLocations() {
        // Given
        List<Zombie> zombieLocations = List.of(
                Zombie.builder()
                        .id(UUID.fromString("fecc7702-01c0-4758-bc60-e2ca56f51005"))
                        .longitude(-34.65463)
                        .latitude(48.89852)
                        .updatedAt(LocalDateTime.now())
                        .build(),
                Zombie.builder()
                        .id(UUID.fromString("8a98c7df-8869-4ac6-bf46-6188c347cc4f"))
                        .longitude(-65.82759)
                        .latitude(-12.21462)
                        .updatedAt(LocalDateTime.now())
                        .build()
        );

        // When
        zombieRepository.saveAll(zombieLocations);

        // Then
        var result = zombieRepository.findAll();

        assertThat(result).hasSize(5)
                .extracting(
                        Zombie::getId,
                        Zombie::getLatitude,
                        Zombie::getLongitude,
                        Zombie::isCaptured
                )
                .contains(
                        new Tuple(UUID.fromString("3adefa3f-456f-43a6-a465-9be413122806"), -105.72408, -21.89278, false),
                        new Tuple(UUID.fromString("999d730f-793e-49e7-bc80-6aeadcf6278b"), 105.52744, 79.19932, false),
                        new Tuple(UUID.fromString("6b06e416-195b-42a7-8350-52888b31e44b"), -90.14157, 8.30389, false),
                        new Tuple(UUID.fromString("fecc7702-01c0-4758-bc60-e2ca56f51005"), 48.89852, -34.65463, false),
                        new Tuple(UUID.fromString("8a98c7df-8869-4ac6-bf46-6188c347cc4f"), -12.21462, -65.82759, false)
                );
    }

    @Test
    @Sql(value = "/scripts/insert_zombie.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/scripts/clear_table_zombie.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void shouldGetNotCapturedZombiesNearTo() {
        // When
        var result = zombieRepository.getNotCapturedZombiesNearTo(2.294533, 48.85905, 2);

        // Then
        assertThat(result).hasSize(2)
                .extracting(
                        Zombie::getId,
                        Zombie::getLatitude,
                        Zombie::getLongitude
                )
                .contains(
                        new Tuple(UUID.fromString("3adefa3f-456f-43a6-a465-9be413122806"), -105.72408, -21.89278),
                        new Tuple(UUID.fromString("999d730f-793e-49e7-bc80-6aeadcf6278b"), 105.52744, 79.19932)
                );
    }
}