package eu.yelhaddad.zombietracker.entrypoint.controller;

import eu.yelhaddad.zombietracker.entrypoint.producer.ZombieProducer;
import eu.yelhaddad.zombietracker.service.ZombieService;
import eu.yelhaddad.zombietracker.valueobject.ZombieResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
public class ZombieLocationController {

    private final ZombieService zombieService;
    private final ZombieProducer zombieProducer;

    @GetMapping(value = "/zombies", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ZombieResponse>> getNotCapturedZombiesNearTo(
            @RequestParam Double longitude,
            @RequestParam Double latitude,
            @RequestParam(required = false) Integer limit
    ) {
        if (longitude == null || latitude == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "longitude and latitude are required");
        }

        return ResponseEntity.ok(zombieService.getNotCapturedZombiesNearTo(longitude, latitude, limit));
    }

    // Generate Dummy data for tests
    @PostMapping(value = "/zombie/locations")
    public ResponseEntity<Void> publishZombiesLocations() {
        try {
            zombieProducer.publishZombiesLocations();
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.error("Unable to publish zombie locations", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
