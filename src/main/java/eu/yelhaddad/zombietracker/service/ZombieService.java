package eu.yelhaddad.zombietracker.service;

import eu.yelhaddad.zombietracker.model.Zombie;
import eu.yelhaddad.zombietracker.repository.ZombieRepository;
import eu.yelhaddad.zombietracker.valueobject.ZombieCaptured;
import eu.yelhaddad.zombietracker.valueobject.ZombieLocation;
import eu.yelhaddad.zombietracker.valueobject.ZombieResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Optional.ofNullable;

@Service
@AllArgsConstructor
@Slf4j
public class ZombieService {

    private final ZombieRepository zombieRepository;
    private static final int DEFAULT_LIMIT = 10000;

    public void insertZombieLocations(List<ZombieLocation> zombieLocations) {
        zombieRepository.saveAll(mapZombieLocations(zombieLocations));
    }

    public void upsertZombieCaptured(ZombieCaptured zombieCaptured) {
        var optionalZombie = zombieRepository.findById(zombieCaptured.getId());
        var zombieToSave = optionalZombie.map(zombie -> {
            zombie.setCaptured(true);
            zombie.setUpdatedAt(zombieCaptured.getUpdatedAt());
            return zombie;
        }).orElse(mapZombieCaptured(zombieCaptured));
        zombieRepository.save(zombieToSave);

    }

    public List<ZombieResponse> getNotCapturedZombiesNearTo(Double longitude, Double latitude, Integer limit) {
        limit = ofNullable(limit)
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
