package eu.heetch.technicaltest.entrypoint.controller;

import eu.heetch.technicaltest.entrypoint.producer.ZombieProducer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Slf4j
public class ZombieProducerController {

    private final ZombieProducer zombieProducer;

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
