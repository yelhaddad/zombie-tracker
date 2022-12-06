package eu.heetch.technicaltest.entrypoint.controller;

import eu.heetch.technicaltest.service.ZombieService;
import eu.heetch.technicaltest.valueobject.ZombieResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
public class ZombieLocationController {

    private final ZombieService zombieService;

    @GetMapping(value = "/zombie", produces = MediaType.APPLICATION_JSON_VALUE)
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
}
