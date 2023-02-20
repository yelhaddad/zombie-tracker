package eu.yelhaddad.zombietracker.service;

import eu.yelhaddad.zombietracker.model.Zombie;
import eu.yelhaddad.zombietracker.repository.ZombieRepository;
import eu.yelhaddad.zombietracker.valueobject.ZombieCaptured;
import eu.yelhaddad.zombietracker.valueobject.ZombieLocation;
import eu.yelhaddad.zombietracker.valueobject.ZombieResponse;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ZombieServiceTest {

    @Mock
    private ZombieRepository zombieRepository;

    @InjectMocks
    private ZombieService zombieService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void shouldInsertZombieLocations() {
        // Given
        List<ZombieLocation> zombieLocations = List.of(
                ZombieLocation.builder()
                        .id(UUID.fromString("fecc7702-01c0-4758-bc60-e2ca56f51005"))
                        .latitude(123.89392)
                        .longitude(89.78903)
                        .updatedAt(LocalDateTime.of(2022, 12, 12, 13, 14, 15))
                        .build()
                );

        List<Zombie> zombies = List.of(
                Zombie.builder()
                        .id(UUID.fromString("fecc7702-01c0-4758-bc60-e2ca56f51005"))
                        .latitude(123.89392)
                        .longitude(89.78903)
                        .captured(false)
                        .updatedAt(LocalDateTime.of(2022, 12, 12, 13, 14, 15))
                        .build()
        );

        when(zombieRepository.saveAll(zombies)).thenReturn(new ArrayList<>());

        // When
        zombieService.insertZombieLocations(zombieLocations);

        // Then
        verify(zombieRepository, times(1)).saveAll(zombies);
    }

    @Test
    void shouldInsertZombieCaptured() {
        // Given
        UUID zombieId = UUID.fromString("fecc7702-01c0-4758-bc60-e2ca56f51005");
        ZombieCaptured zombieCaptured = ZombieCaptured.builder()
                .id(zombieId)
                .updatedAt(LocalDateTime.of(2022, 12, 12, 13, 14, 15))
                .build();

        Zombie zombie = Zombie.builder()
                .id(zombieId)
                .captured(true)
                .updatedAt(LocalDateTime.of(2022, 12, 12, 13, 14, 15))
                .build();

        when(zombieRepository.save(zombie)).thenReturn(new Zombie());
        when(zombieRepository.findById(zombieId)).thenReturn(Optional.empty());

        // When
        zombieService.upsertZombieCaptured(zombieCaptured);

        // Then
        verify(zombieRepository, times(1)).save(zombie);
        verify(zombieRepository, times(1)).findById(zombieId);
    }

    @Test
    void shouldUpdateZombieCaptured() {
        // Given
        UUID zombieId = UUID.fromString("fecc7702-01c0-4758-bc60-e2ca56f51005");
        ZombieCaptured zombieCaptured = ZombieCaptured.builder()
                .id(zombieId)
                .updatedAt(LocalDateTime.of(2023, 1, 1, 12, 13, 14))
                .build();

        Zombie zombie = Zombie.builder()
                .id(zombieId)
                .captured(false)
                .longitude(123.456)
                .latitude(889.900)
                .updatedAt(LocalDateTime.of(2022, 12, 12, 13, 14, 15))
                .build();

        Zombie zombieToSave = Zombie.builder()
                .id(zombieId)
                .captured(true)
                .longitude(123.456)
                .latitude(889.900)
                .updatedAt(LocalDateTime.of(2023, 1, 1, 12, 13, 14))
                .build();

        when(zombieRepository.save(zombieToSave)).thenReturn(new Zombie());
        when(zombieRepository.findById(zombieId)).thenReturn(Optional.of(zombie));

        // When
        zombieService.upsertZombieCaptured(zombieCaptured);

        // Then
        verify(zombieRepository, times(1)).save(zombieToSave);
        verify(zombieRepository, times(1)).findById(zombieId);
    }

    @Test
    void shouldGetNotCapturedZombiesNearTo() {
        // Given
        var longitude = 123.89392;
        var latitude = 89.78903;
        var limit = 5;
        var zombieId = UUID.fromString("fecc7702-01c0-4758-bc60-e2ca56f51005");

        List<Zombie> zombies = List.of(
                Zombie.builder()
                        .id(zombieId)
                        .latitude(latitude)
                        .longitude(longitude)
                        .captured(false)
                        .updatedAt(LocalDateTime.of(2022, 12, 12, 13, 14, 15))
                        .build()
        );

        when(zombieRepository.getNotCapturedZombiesNearTo(longitude, latitude, limit)).thenReturn(zombies);

        // When
        var result = zombieService.getNotCapturedZombiesNearTo(longitude, latitude, limit);

        // Then
        verify(zombieRepository, times(1)).getNotCapturedZombiesNearTo(longitude, latitude, limit);
        assertThat(result).hasSize(1)
                .extracting(
                        ZombieResponse::getZombie_id,
                        ZombieResponse::getLongitude,
                        ZombieResponse::getLatitude
                )
                .contains(
                        new Tuple(zombieId, longitude, latitude)
                );
    }
}