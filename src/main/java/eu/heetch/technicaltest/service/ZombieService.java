package eu.heetch.technicaltest.service;

import eu.heetch.technicaltest.model.Zombie;
import eu.heetch.technicaltest.repository.ZombieRepository;
import eu.heetch.technicaltest.valueobject.ZombieCaptured;
import eu.heetch.technicaltest.valueobject.ZombieLocation;
import eu.heetch.technicaltest.valueobject.ZombieResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class ZombieService {

    private final ZombieRepository zombieRepository;
    private static final int DEFAULT_LIMIT = 10000;

    public void upsertZombieLocations(List<ZombieLocation> zombieLocations) {
        zombieRepository.saveAll(mapZombieLocations(zombieLocations));
    }

    public void upsertZombieCaptured(ZombieCaptured zombieCaptured) {
        zombieRepository.save(mapZombieCaptured(zombieCaptured));
    }

    public List<ZombieResponse> getNotCapturedZombiesNearTo(Double longitude, Double latitude, Integer limit) {
        limit = Optional.ofNullable(limit)
                .filter(ct -> ct > 0)
                .filter(ct -> ct < DEFAULT_LIMIT)
                .orElse(DEFAULT_LIMIT);
        return mapToZombieResponse(zombieRepository.getNotCapturedZombiesNearTo(longitude, latitude, limit));
    }

    private List<Zombie> mapZombieLocations(List<ZombieLocation> zombieLocations) {
        return zombieLocations.stream().map(zombieLocation -> Zombie.builder()
                .id(zombieLocation.getId())
                .captured(false)
                .latitude(zombieLocation.getLatitude())
                .longitude(zombieLocation.getLongitude())
                .updatedAt(zombieLocation.getUpdatedAt())
                .build()).toList();
    }

    private Zombie mapZombieCaptured(ZombieCaptured zombieCaptured) {
        return Zombie.builder()
                .id(zombieCaptured.getId())
                .captured(true)
                .updatedAt(zombieCaptured.getUpdatedAt())
                .build();
    }

    private List<ZombieResponse> mapToZombieResponse(List<Zombie> zombies) {
        return zombies.stream().map(zombie -> ZombieResponse.builder()
                .zombie_id(zombie.getId())
                .latitude(zombie.getLatitude())
                .longitude(zombie.getLongitude())
                .build()).toList();
    }
}
